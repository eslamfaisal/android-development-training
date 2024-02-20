import 'package:get_it/get_it.dart';
import 'package:training_questions_form/screens/splash/view_model/splash_view_model.dart';
import 'package:training_questions_form/services/shared_pref_services.dart';

import 'services/navigation_service.dart';

GetIt locator = GetIt.instance;

Future setupLocator() async {
  locator.registerSingletonAsync<SharedPrefServices>(() async {
    await SharedPrefServices.init();
    return SharedPrefServices.instance;
  });
  locator.registerLazySingleton(() => NavigationService());
  locator.registerLazySingleton(() => SplashViewModel());
}
