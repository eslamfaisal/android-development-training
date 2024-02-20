import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:training_questions_form/routs/routing_data.dart';
import 'package:training_questions_form/routs/routs_names.dart';

import '../screens/not_found_screen/not_found_screen.dart';
import '../screens/splash/splash_screen.dart';

class AppRouter {
  static Route<dynamic> getLoginRoute(RouteSettings settings) {
    var uriData = Uri.parse(settings.name!);

    var routingData = RoutingData(
      queryParameters: uriData.queryParameters,
      route: uriData.path,
    );

    print('RoutingName: ${routingData.route}');
    return _getPageRoute(NotFoundScreen("Login rout"), settings);
  }

  static Route<dynamic> generateRoute(RouteSettings settings) {
    var uriData = Uri.parse(settings.name!);

    var routingData = RoutingData(
      queryParameters: uriData.queryParameters,
      route: uriData.path,
    );

    print('RoutingName: ${routingData.route}');

    switch (routingData.route) {
      case RouteName.SPLASH:
        return _getPageRoute(SplashScreen(), settings);

      case RouteName.HOME:
        return _getPageRoute(
            NotFoundScreen("${uriData.queryParameters}"), settings);

      default:
        return _getPageRoute(NotFoundScreen(""), settings);
    }
  }
}

PageRoute _getPageRoute(Widget child, RouteSettings settings) {
  return _FadeRoute(child: child, routeName: settings.name);
}

class _FadeRoute extends PageRouteBuilder {
  final Widget? child;
  final String? routeName;

  _FadeRoute({this.child, this.routeName})
      : super(
          settings: RouteSettings(name: routeName),
          pageBuilder: (
            BuildContext context,
            Animation<double> animation,
            Animation<double> secondaryAnimation,
          ) =>
              child!,
          transitionsBuilder: (
            BuildContext context,
            Animation<double> animation,
            Animation<double> secondaryAnimation,
            Widget child,
          ) =>
              FadeTransition(
            opacity: animation,
            child: child,
          ),
        );
}
