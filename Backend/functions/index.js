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

// TODO create register social media user
