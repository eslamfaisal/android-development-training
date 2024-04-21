import 'package:flutter/material.dart';
import 'package:training_questions_form/screens/sessions/model/session_model.dart';
import 'package:training_questions_form/utils/colors.dart';
import 'package:training_questions_form/utils/common_functions.dart';
import 'package:training_questions_form/utils/extensions.dart';
import 'dart:js' as js;

import 'package:training_questions_form/utils/texts.dart';

class SessionView extends StatelessWidget {
  final SessionModel session;
  final VoidCallback updateSession;
  final VoidCallback deleteSession;

  const SessionView({
    super.key,
    required this.session,
    required this.updateSession,
    required this.deleteSession,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: whiteColor,
        borderRadius: BorderRadius.circular(4),
        boxShadow: [
          BoxShadow(
            color: lightGrey,
            offset: const Offset(0.0, 1.0), //(x,y)
            blurRadius: 0.5,
          ),
        ]
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            child:
                Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
              normal18Text(
                session.name ?? '',
              ),
              heightSpace(8),
              if(session.reference!=null && session.reference!.isNotEmpty) linkText(session.reference!).onTap(() {
                js.context.callMethod('open', [session.reference]);
              }),
              if(session.reference==null || session.reference!.isEmpty)  normal14Text('No Reference found')
            ]),
          ),
          widthSpace(8),
          PopupMenuButton(
            color: whiteColor,
            surfaceTintColor: whiteColor,
            icon: const Icon(Icons.more_vert),
            itemBuilder: (context) => [
              PopupMenuItem(value: 1, child: normal16Text("Update")),
              PopupMenuItem(value: 2, child: normal16Text("Delete")),
            ],
            onSelected: (value) {
              if (value == 1) {
                updateSession();
              } else if (value == 2) {
                deleteSession();
              }
            },
          )
        ],
      ),
    );
  }
}
