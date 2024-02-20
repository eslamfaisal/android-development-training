import 'package:flutter/material.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';

class DrawerItemWidget extends StatelessWidget {
  final String title;
  final Color color;
  final VoidCallback function;
  final Icon icon;

  DrawerItemWidget(
    this.title,
    this.color,
    this.function,
    this.icon,
  );


  @override
  Widget build(BuildContext context) {
    return Container(
            color: color,
            padding: const EdgeInsets.only(top: 8, bottom: 8, left: 16, right: 16),
            child: Row(
              children: [
                icon,
                widthSpace(8),
                Text(
                  title,
                  style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
              ],
            ),
          ).onTap(function);
  }
}
