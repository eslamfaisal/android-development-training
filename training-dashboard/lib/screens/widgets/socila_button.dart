import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/texts.dart';
import 'package:flutter/material.dart';

class SocialButton extends StatelessWidget {
  final String text;
  final Color bgcolor;
  final Color textcolor;
  final String imageName;
  final Color borderColor;

  SocialButton(this.text, this.bgcolor, this.textcolor, this.imageName,
      this.borderColor);

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
          border: Border.all(
            color: borderColor,
          ),
          color: bgcolor,
          borderRadius: BorderRadius.all(Radius.circular(10))),
      child: Padding(
        padding: const EdgeInsets.all(15.0),
        child: Center(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Image.asset('assets/icons/$imageName', width: 24, height: 24),
              widthSpace(10),
              socialButtonText(text, textcolor),
            ],
          ),
        ),
      ),
    );
  }
}
