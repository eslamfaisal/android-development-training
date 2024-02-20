import 'package:flutter/material.dart';

class IconImage extends StatelessWidget {
  final String imageName;
  const IconImage(this.imageName);

  @override
  Widget build(BuildContext context) {
    return Image.asset(
      'assets/icons/$imageName',
      width: 24,
      height: 24,
    );
  }
}
