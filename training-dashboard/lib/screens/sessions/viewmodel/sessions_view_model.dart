import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/models/status.dart';
import 'package:training_questions_form/screens/base_view_model.dart';
import 'package:training_questions_form/screens/sessions/model/session_model.dart';
import 'package:training_questions_form/services/firebase_services.dart';

import '../../../locator.dart';

class SessionsViewModel extends BaseViewModel {
  final _firebaseServices = locator<FirebaseServices>();

  List<SessionModel> sessions = [];

  SessionsViewModel() {
    getSessions();
  }

  getSessions() async {
    setState(ViewState.Busy);
    _firebaseServices.getSessions().then((value) {
      if (value.status?.isSuccessful == true) {
        sessions = value.data!;
        setState(ViewState.Idle);
      } else {
        setState(ViewState.Idle);
      }
    });
  }

  deleteSession(String? id) {

  }
}
