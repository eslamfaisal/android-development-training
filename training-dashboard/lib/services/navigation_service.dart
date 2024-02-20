import 'package:flutter/material.dart';

class NavigationService {
  final GlobalKey<NavigatorState> navigatorKey = GlobalKey<NavigatorState>();

  Future<dynamic> navigateTo(String routeName,
      {Map<String, dynamic>? queryParams, Object? arguments}) {
    if (queryParams != null) {
      routeName = Uri(path: routeName, queryParameters: queryParams).toString();
    }

    return navigatorKey.currentState!
        .pushNamed(routeName, arguments: arguments);
  }

  Future<dynamic> navigateToAndClearStack(String routeName,
      {Map<String, String>? queryParams, Object? arguments}) {
    if (queryParams != null) {
      routeName = Uri(path: routeName, queryParameters: queryParams).toString();
    }

    return navigatorKey.currentState!
        .pushNamedAndRemoveUntil(routeName, (r) => false, arguments: arguments);
  }

  void goBackUnTill(String routName) {
    return navigatorKey.currentState!
        .popUntil((route) => route.settings.name == routName);
  }

  void goBack() {
    return navigatorKey.currentState!.pop();
  }

  void goBackWithData(dynamic data) {
    return navigatorKey.currentState!.pop(data);
  }
}