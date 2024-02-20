import 'package:flutter/material.dart';

import 'package:shared_preferences/shared_preferences.dart';

class SharedPrefServices {
  SharedPreferences? _prefs;

  SharedPrefServices._privateConstructor();

  static final SharedPrefServices _instance = SharedPrefServices._privateConstructor();

  static SharedPrefServices get instance => _instance;

  static Future<void> init() async {
    _instance._prefs = await SharedPreferences.getInstance();
  }

  Future<void> saveString(String key, String value) async {
    await _prefs!.setString(key, value);
  }

  Future<void> saveBoolean(String key, bool value) async {
    await _prefs!.setBool(key, value);
  }

  Future<void> saveInLocalStorageAsInt(String key, int value) async {
    await _prefs!.setInt(key, value);
  }

  Future<bool> getBoolean(String key) async {
    return _prefs!.getBool(key) ?? false;
  }

  Future<String?> getString(String key) async {
    return _prefs!.getString(key);
  }

  Future<void> removeFromLocalStorage({required String key}) async {
    await _prefs!.remove(key);
  }

  Future<void> clear() async {
    await _prefs!.clear();
  }
}
