
import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/locator.dart';
import 'package:training_questions_form/models/status.dart';
import 'package:training_questions_form/screens/base_view_model.dart';
import 'package:training_questions_form/services/firebase_services.dart';

class AddSessionViewModel extends BaseViewModel {
  TextEditingController nameController = TextEditingController();
  TextEditingController referenceController = TextEditingController();

  final formKey = GlobalKey<FormState>();

  final _firebaseServices = locator<FirebaseServices>();

  Future<bool> addSession()async {
    setState(ViewState.Busy);
    final result = await _firebaseServices.addSessions(
       sessionName: nameController.text,
      reference: referenceController.text,
    );
    setState(ViewState.Idle);
    return result.status?.isSuccessful==true;

  }

  FormFieldValidator<String>? nameValidator() {
    validator(value) {
      if (value == null || value.isEmpty) {
        return ('please enter session name');
      }
      return null;
    }
    return validator;
  }
}
