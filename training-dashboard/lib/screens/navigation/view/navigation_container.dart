import 'package:training_questions_form/routs/routs_names.dart';
import 'package:training_questions_form/screens/navigation/widgets/drawer_item_widget.dart';
import 'package:training_questions_form/screens/navigation/widgets/pop_up_item.dart';
import 'package:training_questions_form/services/navigation_service.dart';
import 'package:training_questions_form/services/shared_pref_services.dart';
import 'package:training_questions_form/utils/colors.dart'; 
import 'package:flutter/material.dart';

import '../../../locator.dart';
import '../navigation_index.dart';

class NavigationContainer extends StatefulWidget {
  final Widget child;
  final int initialIndex;

  NavigationContainer(this.child, this.initialIndex);

  @override
  _NavigationContainerState createState() => _NavigationContainerState();
}

class _NavigationContainerState extends State<NavigationContainer>
    with SingleTickerProviderStateMixin {
  TabController? tabController;

  @override
  void initState() {
    tabController = TabController(
        vsync: this, length: 11, initialIndex: widget.initialIndex)
      ..addListener(() {});
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        // iconTheme: IconThemeData(color: Colors.green),
        automaticallyImplyLeading:
            MediaQuery.of(context).size.width < 1300 ? true : false,
        title: Image.asset('assets/images/ic_logo.png'),
        // automaticallyImplyLeading: false,
      ),
      body: Row(
        children: <Widget>[
          MediaQuery.of(context).size.width < 1300
              ? Container()
              : Card(
                  elevation: 2.0,
                  child: Container(
                    margin: const EdgeInsets.all(0),
                    height: MediaQuery.of(context).size.height,
                    width: 300,
                    color: Colors.white,
                    child: getMenuItemsWidget(),
                  ),
                ),
          Container(
            width: MediaQuery.of(context).size.width < 1300
                ? MediaQuery.of(context).size.width
                : MediaQuery.of(context).size.width - 310,
            child: widget.child,
          )
        ],
      ),
      drawer: Padding(
        padding: const EdgeInsets.only(top: 56),
        child: Drawer(
          child: getMenuItemsWidget(),
        ),
      ),
    );
  }

  Widget getMenuItemsWidget() {
    return Stack(
      alignment: Alignment.bottomCenter,
      children: [
        listDrawerItems(),
      ],
    );
  }

  Widget listDrawerItems() {
    return ListView(
      physics: const BouncingScrollPhysics(), // shrinkWrap: true,
      children: <Widget>[
        DrawerItemWidget(
          ("countries"),
          tabController!.index == HOME_INDEX
              ? Colors.deepOrange[200]!
              : Colors.white,
          () {
            locator<NavigationService>().navigateTo(RouteName.HOME);
          },
          Icon(
            Icons.category,
            color: primaryColor,
            size: 26,
          ),
        ),
      ],
    );
  }

  @override
  void dispose() {
    tabController!.dispose();
    super.dispose();
  }
}
