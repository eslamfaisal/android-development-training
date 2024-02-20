import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/models/resources.dart';
import 'package:training_questions_form/models/status.dart';
import 'package:training_questions_form/screens/base_view_model.dart';
import 'package:training_questions_form/screens/login/model/system_user_model.dart';
import 'package:training_questions_form/services/firebase_services.dart';
import 'package:training_questions_form/utils/constants.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';

import '../../../locator.dart';

class LoginViewModel extends BaseViewModel {
  TextEditingController emailController =
      TextEditingController(text: "affmohamedali@gmail.com");
  TextEditingController passwordController =
      TextEditingController(text: "123456789000");

  final formKey = GlobalKey<FormState>();
  final _firebaseServices = locator<FirebaseServices>();

  Future<Resource<String>> login() async {
    try {
      setState(ViewState.Busy);
      Resource<UserCredential>? response = await _firebaseServices.login(
          emailController.value.text.trim(), passwordController.value.text);
      if (response.status == Status.ERROR) {
        setState(ViewState.Idle);
        return Resource(Status.ERROR, errorMessage: response.errorMessage);
      } else {
        print('Login Successful = ${response.data!.user!.uid}');
        Resource<SystemUserModel> userDataResponse = await _firebaseServices
            .getSystemUserProfile(response.data!.user!.uid);

        if (response.status == Status.ERROR) {
          setState(ViewState.Idle);
          return Resource(Status.ERROR, errorMessage: response.errorMessage);
        } else {
        var  currentLoggedInUserData = userDataResponse.data!;
          return Resource(Status.SUCCESS, errorMessage: response.errorMessage);
        }
      }
    } catch (e) {
      setState(ViewState.Idle);
      return Resource(Status.ERROR, errorMessage: e.toString());
    }
  }

  FormFieldValidator<String>? emailValidator() {
    FormFieldValidator<String>? validator = (value) {
      if (value == null || value.isEmpty) {
        return ('please_enter_your_email');
      }
      if (!isValidEmail(value.trim())) {
        return ('please_enter_your_valid_email');
      }

      return null;
    };

    return validator;
  }

  bool isValidEmail(String email) {
    bool emailValid = RegExp(
            r'^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$')
        .hasMatch(email);
    return emailValid;
  }

  FormFieldValidator<String>? passwordValidator() {
    FormFieldValidator<String>? validator = (value) {
      if (value == null || value.isEmpty) {
        return ('please_enter_your_password');
      }
      if (value.length < 6) {
        return ('password_more_6_chars');
      }
      return null;
    };
    return validator;
  }
}
