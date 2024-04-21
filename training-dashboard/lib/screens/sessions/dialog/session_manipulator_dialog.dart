import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/locator.dart';
import 'package:training_questions_form/screens/base_screen.dart';
import 'package:training_questions_form/screens/sessions/model/session_model.dart';
import 'package:training_questions_form/screens/sessions/viewmodel/session_manipulator_view_model.dart';
import 'package:training_questions_form/screens/widgets/StyledButton.dart';
import 'package:training_questions_form/screens/widgets/styled_text_field.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';
import 'package:training_questions_form/utils/texts.dart';

class SessionManipulatorDialog extends StatelessWidget {
  const SessionManipulatorDialog({super.key, this.session});

  final SessionModel? session;

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: whiteColor,
      shadowColor: Colors.white,
      surfaceTintColor: Colors.white,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24.0)),
      child: Wrap(
        children: [
          BaseScreen<SessionManipulatorViewModel>(
            onModelReady: (viewModel) {
              if (session != null) {
                viewModel.nameController.text = session!.name ?? '';
                viewModel.referenceController.text = session!.reference ?? '';
                viewModel.sessionId = session!.id ?? '';
                viewModel.createdAt = session!.createdAt ?? 0;
              }
            },
            builder: (context, addSessionViewModel, child) {
              return Padding(
                padding: const EdgeInsets.all(24.0),
                child: SizedBox(
                  width: MediaQuery.sizeOf(context).width * 0.6,
                  child: Form(
                    key: addSessionViewModel.formKey,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            headerText(session == null
                                ? 'Add Session'
                                : 'Update Session'),
                            const Spacer(),
                            IconButton(
                                onPressed: () {
                                  locator<NavigationService>().goBack();
                                },
                                icon: const Icon(Icons.close))
                          ],
                        ),
                        const Divider(),
                        heightSpace(16),
                        normal18Text(
                          ('Name'),
                        ),
                        heightSpace(8),
                        StyledTextField(
                          controller: addSessionViewModel.nameController,
                          validator: addSessionViewModel.nameValidator(),
                        ),
                        heightSpace(16),
                        normal18Text(
                          ('Reference'),
                        ),
                        heightSpace(8),
                        StyledTextField(
                          controller: addSessionViewModel.referenceController,
                        ),
                        heightSpace(8),
                        const Divider(),
                        addSessionViewModel.state == ViewState.Busy
                            ? const Center(child: CircularProgressIndicator())
                            : StyledButton(session == null ? 'Add' : 'Update')
                                .onTap(() => session == null
                                    ? _onClickAdd(context, addSessionViewModel)
                                    : _onClickUpdate(
                                        context, addSessionViewModel))
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ],
      ),
    );
  }

  _onClickAdd(BuildContext context, SessionManipulatorViewModel viewModel) {
    if (viewModel.formKey.currentState!.validate()) {
      viewModel.addSession().then((value) {
        if (value) {
          locator<NavigationService>().goBackWithData(true);
          ScaffoldMessenger.of(context)
              .showSnackBar(const SnackBar(content: Text('Session Added')));
        } else {
          locator<NavigationService>().goBack();
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Session addition failed')));
        }
      });
    }
  }

  _onClickUpdate(BuildContext context, SessionManipulatorViewModel viewModel) {
    if (viewModel.formKey.currentState!.validate()) {
      viewModel.updateSession().then((value) {
        if (value) {
          locator<NavigationService>().goBackWithData(true);
          ScaffoldMessenger.of(context)
              .showSnackBar(const SnackBar(content: Text('Session Updated')));
        } else {
          locator<NavigationService>().goBack();
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Session Update Fail')));
        }
      });
    }
  }
}
