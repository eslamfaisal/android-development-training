import 'package:flutter/material.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/extensions.dart';

class DrawerItemWidget extends StatelessWidget {
  final String title;
  final bool selectedItem;
  final VoidCallback function;

  DrawerItemWidget(
    this.title,
    this.selectedItem,
    this.function,
  );

  @override
  Widget build(BuildContext context) {
    return Container(
      color: selectedItem ? primaryColor : Colors.white,
      padding: const EdgeInsets.all(16),
      child: Row(
        children: [
          Text(
            title,
            style: TextStyle(
              fontSize: 18,
              fontWeight: FontWeight.bold,
              color: selectedItem ? Colors.white : Colors.black,
            ),
          ),
        ],
      ),
    ).onTap(function);
  }
}
