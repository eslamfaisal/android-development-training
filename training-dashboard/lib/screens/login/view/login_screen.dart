import 'dart:html' as html;

import 'package:flutter/material.dart';
import 'package:training_questions_form/enums/screen_state.dart';
import 'package:training_questions_form/models/resources.dart';
import 'package:training_questions_form/models/status.dart';
import 'package:training_questions_form/screens/login/viewmodel/login_view_model.dart';
import 'package:training_questions_form/screens/widgets/StyledButton.dart';
import 'package:training_questions_form/screens/widgets/socila_button.dart';
import 'package:training_questions_form/screens/widgets/styled_text_field.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';
import 'package:training_questions_form/utils/texts.dart';

import '../../base_screen.dart';

class LoginScreen extends StatelessWidget {
  const LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return BaseScreen<LoginViewModel>(
      builder: (context, loginViewModel, child) {
        return Scaffold(
          body: SafeArea(
            child: Center(
              child: Container(
                width: 400,
                padding: const EdgeInsets.only(top: 16, left: 16, right: 16),
                child: SingleChildScrollView(
                  child: Center(
                    child: Form(
                      key: loginViewModel.formKey,
                      child: Column(
                        children: [
                          SizedBox(
                            width: 100,
                            height: 100,
                            child: Image.asset('assets/images/ic_logo.png'),
                          ),
                          headerText(
                            ('login'),
                          ),
                          heightSpace(16),
                          StyledTextField(
                            controller: loginViewModel.emailController,
                            hint: ('email'),
                            validator: loginViewModel.emailValidator(),
                          ),
                          heightSpace(8),
                          StyledTextField(
                            controller: loginViewModel.passwordController,
                            hint: ('password'),
                            validator: loginViewModel.passwordValidator(),
                            isPassword: true,
                          ),
                          heightSpace(24),
                          loginViewModel.state == ViewState.Busy
                              ? const Center(child: CircularProgressIndicator())
                              : Column(
                                  children: [
                                    StyledButton(("login")).onTap(() async {
                                      Resource<String> response =
                                          await loginViewModel.login();

                                      if (response.status == Status.ERROR) {
                                        final snackBar = SnackBar(
                                            content:
                                                Text((response.errorMessage!)));
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(snackBar);
                                      } else {
                                        html.window.location.reload();
                                      }
                                    }),
                                    heightSpace(16),
                                    SocialButton(
                                            'Login with Google',
                                            Colors.white,
                                            blackColor,
                                            'google.png',
                                            primaryColor)
                                        .onTap(() async {
                                      Resource<String> response =
                                          await loginViewModel
                                              .loginWithGoogle();

                                      if (response.status == Status.ERROR) {
                                        final snackBar = SnackBar(
                                            content:
                                                Text((response.errorMessage!)));
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(snackBar);
                                      } else {
                                        html.window.location.reload();
                                      }
                                    }),
                                  ],
                                ),
                          heightSpace(16),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
        );
      },
    );
  }
}
