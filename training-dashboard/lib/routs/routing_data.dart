class RoutingData {
  final String? route;
  final Map<String, dynamic>? queryParameters;

  RoutingData({
    this.route,
    Map<String, dynamic>? queryParameters,
  }) : queryParameters = queryParameters;

  operator [](String key) => queryParameters![key];
}
