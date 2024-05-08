class GenericResponse {
    constructor(msg = null) {
        this.code = null;
        this.data = null;
        this.message = msg;
    }
}

module.exports = GenericResponse;
