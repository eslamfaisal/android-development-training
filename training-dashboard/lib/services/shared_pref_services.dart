import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SharedPrefServices {
  SharedPreferences? _prefs = null;

  SharedPrefServices() {
    init();
  }

  void init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  saveString(String key, String value) async {
    _prefs!.setString(key, value);
  }

  saveBoolean(String key, bool value) async {
    SharedPreferences _prefs = await SharedPreferences.getInstance();
    _prefs.setBool(key, value);
  }

  saveInLocalStorageAsInt(String key, int value) async {
    SharedPreferences _prefs = await SharedPreferences.getInstance();
    _prefs.setInt(key, value);
  }

  Future<bool> getBoolean(String? key) async {
    bool data = await Future.value(_prefs!.getBool(key!) ?? false);
    return data;
  }

  Future<String> getString(String? key) async {
    String data = await Future.value(_prefs!.getString(key!));
    return data;
  }

  removeFromLocalStorage({@required String? key}) async {
    var data = _prefs!.remove(key!);
    return data;
  }


  clear() async {
    _prefs!.clear();
  }

}
