import 'package:flutter/material.dart';
import 'package:training_questions_form/routs/routing_data.dart';

extension WidgetExtension on Widget? {
  /// add tap to parent widget
  Widget onTap(
    Function? function, {
    BorderRadius borderRadius = const BorderRadius.all(Radius.circular(0)),
    Color? splashColor,
  }) {
    return InkWell(
      onTap: function as void Function()?,
      borderRadius: borderRadius,
      child: this,
      splashColor: splashColor,
    );
  }

  Widget makeMaxWidth(double width) {
    return ConstrainedBox(
      constraints: BoxConstraints(
        maxWidth: width,
      ),
      child: this,
    );
  }
}

extension StringExtension on String {
  RoutingData get getRoutingData {
    var uriData = Uri.parse(this);
    return RoutingData(
      queryParameters: uriData.queryParameters,
      route: uriData.path,
    );
  }
}
