import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import '../locator.dart';
import 'base_view_model.dart';

class BaseScreen<T extends BaseViewModel> extends StatefulWidget {
  final Widget Function(BuildContext context, T model, Widget? child)? builder;
  final Function(T)? onModelReady;
  final Function(T)? onFinish;

  BaseScreen({this.builder, this.onModelReady, this.onFinish});

  @override
  _BaseScreenState<T> createState() => _BaseScreenState<T>();
}

class _BaseScreenState<T extends BaseViewModel> extends State<BaseScreen<T>> {
  T model = locator<T>();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (widget.onModelReady != null) {
        widget.onModelReady!(model);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<T>.value(
        value: model,
        child: Consumer<T>(builder: widget.builder!));
  }

  @override
  void dispose() {
    if (widget.onFinish != null) {
      widget.onFinish!(model);
    }
    super.dispose();
  }
}
