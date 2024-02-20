import 'package:training_questions_form/utils/colors.dart';
import 'package:flutter/material.dart';

class RoundedWidget extends StatelessWidget {
  final Widget child;

  const RoundedWidget({required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      // height: 45,
      margin: EdgeInsets.only(bottom: 10),
      decoration: BoxDecoration(
        shape: BoxShape.rectangle,
        color: greyColor,
        borderRadius: BorderRadius.circular(15),
        border: Border.all(color: Colors.grey, width: 1),
      ),
      child: Center(
        child: ListTile(title: child),
      ),
    );
  }
}
