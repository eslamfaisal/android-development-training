import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/locator.dart';
import 'package:training_questions_form/screens/base_screen.dart';
import 'package:training_questions_form/screens/sessions/model/session_model.dart';
import 'package:training_questions_form/screens/sessions/viewmodel/update_session_view_model.dart';
import 'package:training_questions_form/screens/widgets/StyledButton.dart';
import 'package:training_questions_form/screens/widgets/styled_text_field.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';
import 'package:training_questions_form/utils/texts.dart';

class UpdateSession extends StatelessWidget {
  final SessionModel session;
  const UpdateSession({super.key, required this.session});

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: whiteColor,
      shadowColor: Colors.white,
      surfaceTintColor: Colors.white,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24.0)),
      child: Wrap(
        children: [
          BaseScreen<UpdateSessionViewModel>(
            builder: (context, updateSessionViewModel, child) {
              return Padding(
                padding: const EdgeInsets.all(24.0),
                child: SizedBox(
                  width: MediaQuery.sizeOf(context).width * 0.6,
                  child: Form(
                    key: updateSessionViewModel.formKey,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            headerText('Update Session'),
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
                          controller: updateSessionViewModel.nameController,
                          validator: updateSessionViewModel.nameValidator(),
                        ),
                        heightSpace(16),
                        normal18Text(
                          ('Reference'),
                        ),
                        heightSpace(8),
                        StyledTextField(
                          controller: updateSessionViewModel.referenceController,
                        ),
                        heightSpace(8),
                        const Divider(),
                        updateSessionViewModel.state == ViewState.Busy
                            ? const Center(child: CircularProgressIndicator())
                            : StyledButton('Update').onTap(
                            () => _onClickUpdate(context, updateSessionViewModel))
                      ],
                    ),
                  ),
                ),
              );
            },
            onModelReady: (updateSessionViewModel){
              updateSessionViewModel.nameController.text = session.name ?? '';
              updateSessionViewModel.referenceController.text= session.reference ?? '';
              updateSessionViewModel.sessionId = session.id ?? '';
              updateSessionViewModel.createdAt = session.createdAt ?? 0;
            },
          ),
        ],
      ),
    );
  }

  _onClickUpdate(BuildContext context, UpdateSessionViewModel viewModel) {
    if (viewModel.formKey.currentState!.validate()) {
      viewModel.updateSession().then((value) {
        if (value) {
          locator<NavigationService>().goBackWithData(true);
          ScaffoldMessenger.of(context)
              .showSnackBar(const SnackBar(content: Text('Session Updated')));
        } else {
          locator<NavigationService>().goBack();
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Session Updated Fail')));
        }
      });
    }
  }
}
