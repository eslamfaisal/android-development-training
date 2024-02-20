import 'package:flutter/material.dart';
import 'package:training_questions_form/utils/colors.dart';

class PopUpItemWidget extends StatelessWidget {

  final icon;
  final title;

  PopUpItemWidget(this.title, this.icon);

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        SizedBox(
          height: 50,
          width: 50,
          child: Icon(
            icon,
            color: primaryColor,
          ),
        ),
        Text(title),
      ],
    ) ;
  }
}
