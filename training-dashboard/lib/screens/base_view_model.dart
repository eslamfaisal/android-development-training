import 'package:flutter/widgets.dart';
import 'package:training_questions_form/enums/screen_state.dart';

class BaseViewModel extends ChangeNotifier {
  ViewState _state = ViewState.Idle;

  ViewState get state => _state;


  void setState(ViewState viewState) {
    _state = viewState;
    notifyListeners();
  }

}
