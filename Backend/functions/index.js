const {onRequest} = require("firebase-functions/v2/https");
const admin = require('firebase-admin');
admin.initializeApp();

exports.hellowWorld = onRequest((req, res) => {
    console.info("hello world test log")
    res.status(401).send("Un Authorized");
});

const GenericResponse = require('./generic-response');
const {credential} = require("firebase-admin");
const {UserRecord} = require("firebase-admin/auth");

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

admin.initializeApp({
    credential: admin.credential.applicationDefault(),
});

const firestore = admin.firestore();

exports.registerWithSocialMedia = onRequest(async (req, res) => {

        if (req.method !== "POST") {
            return res.status(405).send('Method not allowed')
        }

        const {idToken, provider} = req.body;
        if (!idToken || !provider) {
            return res.status(401).send('Missing IdToken ')
        }

        let response = new GenericResponse();
        try {
            // choose between facebook and google
            if (provider === "facebook") {
                const createCustomToken = await admin.auth().createCustomToken("user", {provider: 'facebook.com', idToken})
                const authResultWithFacebook = await admin.auth().verifyIdToken(createCustomToken);
                const uid = authResultWithFacebook.uid;

                // Check if the user already exists
                const userSnapshot = await firestore.collection('users').doc(uid).get();
                if (userSnapshot.exists) {
                    return res.status(401).send('user is already exists in firestore')
                }

                const userRecord = {
                    uid : authResultWithFacebook.uid,
                    email : authResultWithFacebook.email
                }

                await firestore.collection("users").doc(uid).set(userRecord);

                response.message = "User Registered successfully with social media"
                response.code = 200;
                response.data = {
                    uid: authResultWithFacebook.uid, email: authResultWithFacebook.email
                }; // Example data

                // Send the UID and email of the newly created user back to the client
                res.status(201).send(response);
            } else if (provider === "google") {
                const createCustomToken = await admin.auth().createCustomToken("user", {
                    provider: 'google.com',
                    idToken
                })
                const authResultWithGoogle = await admin.auth().verifyIdToken(createCustomToken);
                const uid = authResultWithGoogle.uid;

                // Check if the user already exists
                const userSnapshotFirestore = await firestore.collection('users').doc(uid).get();
                if (userSnapshotFirestore.exists) {
                    return res.status(401).send('user is already exists in firestore')
                }

                const userRecord = {
                    uid : authResultWithGoogle.uid,
                    email : authResultWithGoogle.email
                }

                await firestore.collection("users").doc(uid).set(userRecord);

                response.message = "User Registered successfully with social media"
                response.code = 200;
                response.data = {
                    uid: authResultWithGoogle.uid, email: authResultWithGoogle.email
                }; // Example data

                // Send the UID and email of the newly created user back to the client
                res.status(201).send(response);
            }
        } catch
            (error) {
            console.error("Error registration with  facebook | google :", error);
            response.message = "Error registration with  facebook | google : " + error.message
            response.code = 400;
            res.status(400).send(response);
        }

    }
)
;

