import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/models/resources.dart';
import 'package:training_questions_form/models/status.dart';
import 'package:training_questions_form/screens/base_view_model.dart';
import 'package:training_questions_form/services/firebase_services.dart';
import 'package:training_questions_form/services/shared_pref_services.dart';

import '../../../locator.dart';
import '../../../utils/shared_preferences_constants.dart';

class LoginViewModel extends BaseViewModel {
  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  final formKey = GlobalKey<FormState>();
  final _firebaseServices = locator<FirebaseServices>();

  Future<Resource<String>> login() async {
    try {
      setState(ViewState.Busy);
      Resource<UserCredential>? response = await _firebaseServices.login(
          emailController.value.text.trim(), passwordController.value.text);
      setState(ViewState.Idle);
      if (response.status == Status.ERROR) {
        return Resource(Status.ERROR, errorMessage: response.errorMessage);
      } else {
        locator<SharedPrefServices>().saveBoolean(LOGGED_IN, true);
        locator<SharedPrefServices>()
            .saveString(USER_ID, response.data!.user!.uid);
        return Resource(Status.SUCCESS, data: response.data!.user!.uid);
      }
    } catch (e) {
      setState(ViewState.Idle);
      return Resource(Status.ERROR, errorMessage: e.toString());
    }
  }

  Future<Resource<String>> loginWithGoogle() async {
    try {
      setState(ViewState.Busy);
      Resource<UserCredential>? response =
          await _firebaseServices.loginWithGoogle();
      setState(ViewState.Idle);
      if (response.status == Status.ERROR) {
        return Resource(Status.ERROR, errorMessage: response.errorMessage);
      } else {
        locator<SharedPrefServices>().saveBoolean(LOGGED_IN, true);
        locator<SharedPrefServices>()
            .saveString(USER_ID, response.data!.user!.uid);
        return Resource(Status.SUCCESS, data: response.data!.user!.uid);
      }
    } catch (e) {
      setState(ViewState.Idle);
      return Resource(Status.ERROR, errorMessage: e.toString());
    }
  }

  FormFieldValidator<String>? emailValidator() {
    validator(value) {
      if (value == null || value.isEmpty) {
        return ('please_enter_your_email');
      }
      if (!isValidEmail(value.trim())) {
        return ('please_enter_your_valid_email');
      }

      return null;
    }

    return validator;
  }

  bool isValidEmail(String email) {
    bool emailValid = RegExp(
            r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$')
        .hasMatch(email);
    return emailValid;
  }

  FormFieldValidator<String>? passwordValidator() {
    validator(value) {
      if (value == null || value.isEmpty) {
        return ('please_enter_your_password');
      }
      if (value.length < 6) {
        return ('password_more_6_chars');
      }
      return null;
    }

    return validator;
  }
}
