const CountryOfferModel = require('./country-offer-model');

class OfferModel {
    constructor(product_id = null, countries_offers = []) {
        this.product_id = product_id;
        this.countries_offers = countries_offers;
    }
}

module.exports = OfferModel;
