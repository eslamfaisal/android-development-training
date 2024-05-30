const {onRequest} = require("firebase-functions/v2/https");
const admin = require('firebase-admin');
admin.initializeApp();

exports.hellowWorld = onRequest((req, res) => {
    console.info("hello world test log")
    res.status(401).send("Un Authorized");
});

const GenericResponse = require('./generic-response');

exports.registerUser = onRequest(async (req, res) => {
    // Check for POST request
    if (req.method !== 'POST') {
        res.status(405).send('Method Not Allowed');
        return;
    }

    // Extract user details from request body
    const {email, password, fullName} = req.body;
    if (!email || !password || !fullName) {
        res.status(400).send('Missing data');
        return;
    }

    let response = new GenericResponse();
    try {
        // Register the user with Firebase Authentication
        const userRecord = await admin.auth().createUser({
            email: email,
            password: password,
            displayName: fullName,
            disabled: false,
            emailVerified: true,
        });


        // Optionally, store additional user details in Firestore
        const userData = {
            id: userRecord.uid, email: email, name: fullName, created_at: admin.firestore.FieldValue.serverTimestamp(),
        };

        await admin.firestore().collection('users').doc(userRecord.uid).set(userData);

        // TODO send email verification to the user
        // const emailVerificationLink = await admin.auth().generateEmailVerificationLink(email);

        response.message = "User Registered successfully."
        response.code = 200;
        response.data = {
            uid: userRecord.uid, email: userRecord.email
        }; // Example data

        // Send the UID and email of the newly created user back to the client
        res.status(201).send(response);
    } catch (error) {
        console.error("Error creating new user:", error);
        response.message = "Error creating new user: " + error.message
        response.code = 400;
        res.status(400).send(response);
    }
});

exports.getFlashSaleProducts = onRequest(async (req, res) => {
    let countryID = req.query.country_id;
    let limit = parseInt(req.query.limit, 10);
    let lastDocumentId = req.query.last_document;

    if (!countryID) {
        return res.status(400).send('country_id is required');
    }

    if (isNaN(limit) || limit <= 0) {
        return res.status(400).send('limit should be a positive number');
    }

    try {
        let query = admin.firestore().collection('products').limit(limit);

        if (lastDocumentId) {
            const lastDocumentSnapshot = await admin.firestore().collection('products').doc(lastDocumentId).get();
            if (lastDocumentSnapshot.exists) {
                query = query.startAfter(lastDocumentSnapshot);
            } else {
                return res.status(400).send('Invalid last_document ID');
            }
        }

        const snapshot = await query.get();

        if (snapshot.empty) {
            return res.status(200).send({
                products: [],
                offers: []
            });
        }

        let ids = snapshot.docs.map(doc => doc.id);

        const offersSnapshot = await admin.firestore().collection('product_offers').where('countries', 'array-contains-any', [countryID]).where('product_id', 'in', ids).get();


        let products = snapshot.docs.map(doc => ({id: doc.id, ...doc.data()}));

        let offers = offersSnapshot.docs.map(doc => ({id: doc.id, ...doc.data()}));

        products.forEach(product => {
            let offer = offers.find(offer => offer.product_id === product.id);
            if (offer) {
                product.offer_percentage = offer.countries_offers.find(offer => offer.country_id === countryID).offer_percentage;
            }
        });

        res.status(200).send({
            products: products
        });

    } catch (error) {
        console.error('Error getting flash sale products:', error);
        res.status(500).send('Error getting flash sale products: ' + error.message);
    }
});
