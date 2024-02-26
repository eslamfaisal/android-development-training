import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/locator.dart';
import 'package:training_questions_form/screens/base_screen.dart';
import 'package:training_questions_form/screens/sessions/viewmodel/add_session_view_model.dart';
import 'package:training_questions_form/screens/widgets/StyledButton.dart';
import 'package:training_questions_form/screens/widgets/styled_text_field.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';
import 'package:training_questions_form/utils/texts.dart';

class AddSession extends StatelessWidget {
  const AddSession({super.key});

  @override
  Widget build(BuildContext context) {
    return Dialog(
      backgroundColor: whiteColor,
      shadowColor: Colors.white,
      surfaceTintColor: Colors.white,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(24.0)),
      child: Wrap(
        children: [
          BaseScreen<AddSessionViewModel>(
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
                            headerText('Add Session'),
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
                            : StyledButton('Add').onTap(
                            () => _onClickAdd(context, addSessionViewModel))
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

  _onClickAdd(BuildContext context, AddSessionViewModel viewModel) {
    if (viewModel.formKey.currentState!.validate()) {
      viewModel.addSession().then((value) {
        if (value) {
          locator<NavigationService>().goBackWithData(true);
          ScaffoldMessenger.of(context)
              .showSnackBar(const SnackBar(content: Text('Session Added')));
        } else {
          locator<NavigationService>().goBack();
          ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(content: Text('Session Added Fail')));
        }
      });
    }
  }
}
