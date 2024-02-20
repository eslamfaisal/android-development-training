import 'package:flutter/material.dart';

class NotFoundScreen extends StatelessWidget {
  String queryParameters;
  NotFoundScreen(this.queryParameters, {Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        child: Center(
          child: Text("no_page = $queryParameters"),
        ),
      ),
    );
  }
}
