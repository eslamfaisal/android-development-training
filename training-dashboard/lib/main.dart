import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:training_questions_form/routs/app_router.dart';
import 'package:training_questions_form/routs/routs_names.dart';
import 'package:training_questions_form/services/navigation_service.dart';

import 'locator.dart';

void main() async {
  setupLocator();
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
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Training Dashboard',
      theme: ThemeData(
        primaryColor: const Color(0xFFFD5F00), primarySwatch: Colors.deepOrange,
      ),
      initialRoute: RouteName.SPLASH,
      navigatorKey: locator<NavigationService>().navigatorKey,
      onGenerateRoute: AppRouter.generateRoute,
    );
  }
}
