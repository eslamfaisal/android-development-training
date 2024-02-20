import 'package:cloud_firestore/cloud_firestore.dart';
 import 'package:flutter/cupertino.dart';

Widget widthSpace(double widthSpace) {
  return SizedBox(
    width: widthSpace,
  );
}

Widget heightSpace(double heightSpace) {
  return SizedBox(
    height: heightSpace,
  );
}


bool isNumeric(String s) {
  if (s == null) {
    return false;
  }
  return double.tryParse(s) != null;
}


String notNullString(any) {
  return any == null ? '' : any.toString();
}