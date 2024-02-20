import 'package:flutter/material.dart';
import 'package:training_questions_form/utils/colors.dart';

const logoStyle =
    TextStyle(color: Colors.black, fontSize: 30.0, fontWeight: FontWeight.bold);

const smallStyle = TextStyle(
  color: Colors.grey,
  fontSize: 14.0,
);

const textFieldTextStyle = TextStyle(
  color: Colors.black,
  fontSize: 14.0,
);

const smallStyleWhite = TextStyle(
  color: Colors.white,
  fontSize: 14.0,
);

const normalStyle = TextStyle(
  color: Colors.black,
  fontSize: 16.0,
);


Widget bold22Text(String header, {Color? color, TextAlign? align}) {
  return Text(
    header,
    textAlign: align,
    style: TextStyle(
        fontSize: 22,
        color: color == null ? blackColor : color,
        fontWeight: FontWeight.bold),
  );
}
