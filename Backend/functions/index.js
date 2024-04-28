const {onRequest} = require("firebase-functions/v2/https");
const admin = require('firebase-admin');
admin.initializeApp();

exports.hellowWorld = onRequest((req, res) => {
    console.info("hello world test log")
    res.status(401).send("Un Authorized");
});

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

    try {
        // Register the user with Firebase Authentication
        const userRecord = await admin.auth().createUser({
            email: email, password: password
        });

        // Optionally, store additional user details in Firestore
        const userData = {
            email: email, fullName: fullName, created_at: admin.firestore.FieldValue.serverTimestamp(),
        };

        await admin.firestore().collection('users').doc(userRecord.uid).set(userData);

        // send email verification tasskkkkkk

        // Send the UID and email of the newly created user back to the client
        res.status(201).send({
            uid: userRecord.uid, email: userRecord.email
        });
    } catch (error) {
        console.error("Error creating new user:", error);
        res.status(500).send("Error creating new user: " + error.message);
    }
});
