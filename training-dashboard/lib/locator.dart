
import 'package:get_it/get_it.dart';
import 'package:training_questions_form/screens/splash/view_model/splash_view_model.dart';

import 'services/navigation_service.dart';

GetIt locator = GetIt.instance;

void setupLocator() {
  locator.registerLazySingleton(() => NavigationService());
  locator.registerLazySingleton(() => SplashViewModel());
}
