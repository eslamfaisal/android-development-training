import 'package:flutter/material.dart';
class CenterProgress extends StatelessWidget {
  const CenterProgress({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(child: SizedBox(width: 35,height: 35, child: CircularProgressIndicator()));
  }
}
