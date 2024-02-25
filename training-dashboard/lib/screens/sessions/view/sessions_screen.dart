import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/screens/base_screen.dart';
import 'package:training_questions_form/screens/sessions/dialog/add_session.dart';
import 'package:training_questions_form/screens/sessions/model/session_model.dart';
import 'package:training_questions_form/screens/sessions/view/session_view.dart';
import 'package:training_questions_form/screens/sessions/viewmodel/sessions_view_model.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/styles.dart';
import 'package:training_questions_form/utils/texts.dart';

class SessionScreen extends StatelessWidget {
  const SessionScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return BaseScreen<SessionsViewModel>(
      builder: (context, sessionsViewModel, child) {
        return Scaffold(
          appBar: AppBar(
            title: headerText(
              "Sessions",
            ),
            centerTitle: false,
            actions: [
              Padding(
                padding: const EdgeInsets.only(right: 40),
                child: OutlinedButton.icon(
                  onPressed: () => _addSession(context, sessionsViewModel),
                  icon: const Icon(Icons.add),
                  label: normal16Text('Add Session', color: primaryColor),
                  style: ButtonStyle(
                      shape: MaterialStateProperty.all(RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8))),
                      foregroundColor: MaterialStateProperty.all(primaryColor),
                      textStyle: MaterialStateProperty.all(normalStyle)),
                ),
              ),
            ],
          ),
          body: sessionsViewModel.state == ViewState.Busy
              ? const Center(child: CircularProgressIndicator())
              : sessionsViewModel.sessions.isEmpty
                  ? Center(child: stateText('There are no sessions yet'))
                  : ListView.separated(
                      padding: const EdgeInsets.symmetric(
                          horizontal: 16, vertical: 16),
                      itemCount: sessionsViewModel.sessions.length,
                      separatorBuilder: (context, index) {
                        return const Divider();
                      },
                      itemBuilder: (context, index) {
                        final session = sessionsViewModel.sessions[index];
                        return Padding(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 16, vertical: 8),
                          child: SessionView(
                            session: session,
                            updateSession: () => _updateSession(session),
                            deleteSession: () =>
                                sessionsViewModel.deleteSession(session.id),
                          ),
                        );
                      },
                    ),
        );
      },
    );
  }

  _addSession(BuildContext context, SessionsViewModel viewModel) {
    showGeneralDialog(
        context: context,
        pageBuilder: (context, _, p) {
          return const AddSession();
        }).then((value) {
      if (value == true) {
        viewModel.getSessions();
      }
    });
  }

  _updateSession(SessionModel session) {}
}
