import 'package:flutter/material.dart';
import 'package:training_questions_form/utils/styles.dart';

class StyledTextField extends StatelessWidget {
  final keyboardType;
  final bool isPassword;
  final hint;
  final controller;
  final validator;
  final Color? fillColor;
  final double? borderRadius;
  final double? borderPadding;
  final IconData? suffixIcon;
  final Function? onIconPressed;
  final Function(String)? onChanged;
  final bool unLimitLines;
  StyledTextField({
    this.controller,
    this.keyboardType = TextInputType.text,
    this.isPassword = false,
    this.hint,
    this.validator,
    this.borderPadding,
    this.fillColor,
    this.borderRadius,
    this.suffixIcon,
    this.onIconPressed,
    this.onChanged,
    this.unLimitLines = false,
  });

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      controller: controller,
      keyboardType: keyboardType,
      obscureText: isPassword,
      style: textFieldTextStyle,
      maxLines: unLimitLines ? null : 1,
      decoration: InputDecoration(
        border: OutlineInputBorder(),
        labelText: hint,
      ),
      validator: validator,
      onChanged: onChanged,
    );
  }
}
