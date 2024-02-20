import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';
import 'package:training_questions_form/routs/routs_names.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/utils/shared_preferences_constants.dart';

import '../../../locator.dart';
import '../../base_view_model.dart';

class SplashViewModel extends BaseViewModel {
  void checkLogin() async {
    try {
      SharedPreferences prefs = await SharedPreferences.getInstance();
      bool isLoggedIn = await Future.value(prefs.getBool(LOGGED_IN) ?? false);
      if (isLoggedIn) {
        String userJsonString =
            await Future.value(prefs.getString(USER_DETAILS));
        Map<String, dynamic> user = await jsonDecode(userJsonString);
        // SystemUserModel userEvent = SystemUserModel.fromJson(user);
        // currentLoggedInUserData = userEvent;
        locator<NavigationService>().navigateToAndClearStack(RouteName.HOME);
      } else {
        locator<NavigationService>().navigateToAndClearStack(RouteName.LOGIN);
      }
    } catch (e) {
      locator<NavigationService>().navigateToAndClearStack(RouteName.LOGIN);
    }
  }
}
