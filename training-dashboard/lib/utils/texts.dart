import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'colors.dart';

Widget headerText(String header) {
  return Text(
    header,
    style: TextStyle(
        fontSize: 24, color: darkBlueColor, fontWeight: FontWeight.w500),
  );
}

Widget stateText(String header, {Color? color}) {
  return Text(
    header,
    textAlign: TextAlign.center,
    style: TextStyle(
      fontSize: 22,
      color: color == null ? blackColor : color,
    ),
  );
}

Widget normal18Text(String header, {Color? color}) {
  return Text(
    header,
    style: TextStyle(
      fontStyle: FontStyle.normal,
      fontSize: 18,
      color: color == null ? blackColor : color,
    ),
  );
}

Widget normal16Text(String header, {Color? color, bool? isCenterd = false}) {
  return Text(
    header,
    style: TextStyle(
      fontStyle: FontStyle.normal,
      fontSize: 16,
      color: color == null ? darkBlueColor : color,
    ),
    textAlign: isCenterd! ? TextAlign.center : TextAlign.justify,
    softWrap: true,
  );
}

Widget constrained160Normal16Text(String header, BuildContext context,
    {Color? color, bool? isCenterd = false}) {
  return Container(
    constraints: new BoxConstraints(maxWidth: 160),
    child: Text(
      header,
      style: TextStyle(
        fontStyle: FontStyle.normal,
        fontSize: 16,
        color: color == null ? darkBlueColor : color,
      ),
      maxLines: 2,
    ),
  );
}

Widget normal14Text(String header, {Color? color}) {
  return Text(
    header,
    style: TextStyle(
      fontSize: 14,
      color: color == null ? blackColor : color,
    ),
  );
}

Widget bold14Text(String header, {Color? color, double size = 0}) {
  return Text(
    header,
    style: TextStyle(
        fontSize: size == 0 ? 14 : size,
        color: color == null ? blackColor : color,
        fontWeight: FontWeight.bold),
  );
}

Widget normal10Text(String header, {Color? color}) {
  return Text(
    header,
    style: TextStyle(fontSize: 10, color: color == null ? blackColor : color),
  );
}

Widget buttonText(String header) {
  return Text(
    header,
    style: TextStyle(
      fontSize: 18,
      color: Colors.white,
    ),
  );
}

Widget socialButtonText(String header, Color textcolor) {
  return Text(
    header,
    style: TextStyle(
      fontSize: 14,
      color: textcolor,
    ),
  );
}

Widget textFormFieldText(String title) {
  return Text(
    title,
    style: TextStyle(
      fontSize: 16,
      color: darkBlueColor,
    ),
  );
}

Widget textWithDivider(String header, index) => Text(
      header,
      style: TextStyle(
        shadows: [
          Shadow(
            color: lightGrey,
            offset: Offset(0, -7),
          )
        ],
        fontWeight: FontWeight.w500,
        color: Colors.transparent,
        fontSize: 16,
        // color: Theme.of(context).primaryColor,
        decorationColor: index ? primaryColor : Colors.transparent,
        decoration: TextDecoration.underline,
        decorationStyle: TextDecorationStyle.solid,
        decorationThickness: 2,
      ),
    );

Widget bold16Text(String header, {Color? color, double? size = 16}) {
  return Text(
    header,
    style: TextStyle(
        fontSize: size == null ? 16 : size,
        color: color == null ? blackColor : color,
        fontWeight: FontWeight.bold),
  );
}

Widget bold10Text(String header, {Color? color, TextAlign? align}) {
  return Text(
    header,
    textAlign: align,
    style: TextStyle(
        fontSize: 10,
        color: color == null ? blackColor : color,
        fontWeight: FontWeight.bold),
  );
}
