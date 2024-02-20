import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'package:training_questions_form/routs/app_router.dart';
import 'package:training_questions_form/routs/routs_names.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/services/shared_pref_services.dart';

import 'locator.dart';

void main() async {
  setUrlStrategy(PathUrlStrategy());
  await setupLocator();
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    // Replace with actual values
    options: const FirebaseOptions(
      apiKey: "AIzaSyDVi_fOfrtlNvMO08H0kvowwI8pI_3Cwg8",
      appId: "1:449149627104:web:ac6297ca9a8a7011c3be85",
      messagingSenderId: "449149627104",
      projectId: "android-e-commerce-training",
    ),
  );

  var isLogged = await locator<SharedPrefServices>().getBoolean('isLogged');
  runApp(MyApp(isLogged));
}

class MyApp extends StatelessWidget {
  final bool isLogged;
  const MyApp(this.isLogged, {super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Training Dashboard',
      theme: ThemeData(
        primaryColor: const Color(0xFFFD5F00),
        primarySwatch: Colors.deepOrange,
      ),
      initialRoute: RouteName.SPLASH,
      navigatorKey: locator<NavigationService>().navigatorKey,
      onGenerateRoute: (settings) {
        if (isLogged) return AppRouter.generateRoute(settings);
        return AppRouter.getLoginRoute(settings);
      },
    );
  }
}
