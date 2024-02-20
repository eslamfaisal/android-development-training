import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:training_questions_form/models/resources.dart';
import 'package:training_questions_form/models/status.dart';

class FirebaseServices {
  var db = FirebaseFirestore.instance;
  var auth = FirebaseAuth.instance;

  User? getCurrentUserData() {
    return auth.currentUser;
  }

  void signOut() {
    auth.signOut();
  }

  Future<Resource<UserCredential>> login(String email, String password) async {
    try {
      UserCredential userCredential = await auth.signInWithEmailAndPassword(
          email: email, password: password);
      return Resource(Status.SUCCESS, data: userCredential);
    } catch (e) {
      return Resource(Status.ERROR, errorMessage: e.toString());
    }
  }
}
