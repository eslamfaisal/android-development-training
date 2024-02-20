import 'package:flutter/material.dart';

class EmptyScreen extends StatelessWidget {
  final String message;
  const EmptyScreen(this.message, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Text(message),
        ),
      ),
    );
  }
}
