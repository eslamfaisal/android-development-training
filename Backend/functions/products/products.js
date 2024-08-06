const admin = require('firebase-admin');
const { onRequest } = require('firebase-functions/v2/https');
const ProductModel = require('./models/product-model');
const OfferModel = require('./models/offer-model');
const CountryOfferModel = require('./models/country-offer-model');

const getSaleProducts = onRequest(async (req, res) => {
    try {
        const { country_id: countryID, limit: limitStr, last_document: lastDocumentId, sale_type: saleType } = req.query;
        const limit = parseInt(limitStr, 10);

        if (!countryID) {
            return res.status(400).send('country_id is required');
        }

        if (isNaN(limit) || limit <= 0) {
            return res.status(400).send('limit should be a positive number');
        }

        const productsRef = admin.firestore().collection('products');
        const productsCollection = productsRef.where('sale_type', '==', saleType);
        let productsQuery = productsCollection.limit(limit);

        if (lastDocumentId) {
            const lastDocumentSnapshot = await productsRef.doc(lastDocumentId).get();
            if (!lastDocumentSnapshot.exists) {
                return res.status(400).send('Invalid last_document ID');
            }
            productsQuery = productsQuery.startAfter(lastDocumentSnapshot);
        }

        const productsSnapshot = await productsQuery.get();

        if (productsSnapshot.empty) {
            return res.status(200).send({ products: [] });
        }

        const productIds = productsSnapshot.docs.map(doc => doc.id);

        const offersSnapshot = await admin.firestore().collection('product_offers')
            .where('countries', 'array-contains-any', [countryID])
            .where('product_id', 'in', productIds)
            .get();

        const products = productsSnapshot.docs.map(doc => new ProductModel(doc.id, doc.data().offer_percentage));
        const offers = offersSnapshot.docs.map(doc => new OfferModel(doc.id, doc.data().countries_offers.map(
            co => new CountryOfferModel(co.country_id, co.offer_percentage))));

        products.forEach(product => {
            const offer = offers.find(offer => offer.product_id === product.id);
            if (offer) {
                const countryOffer = offer.countries_offers.find(offer => offer.country_id === countryID);
                if (countryOffer) {
                    product.offer_percentage = countryOffer.offer_percentage;
                }
            }
        });

        res.status(200).send({ products });

    } catch (error) {
        console.error('Error getting flash sale products:', error);
        res.status(500).send('Error getting flash sale products: ' + error.message);
    }
});

module.exports = {
    getSaleProducts
};
