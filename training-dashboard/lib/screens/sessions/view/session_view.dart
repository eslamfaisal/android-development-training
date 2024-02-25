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
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Expanded(
          child:
              Column(crossAxisAlignment: CrossAxisAlignment.start, children: [
            normal16Text(
              session.name ?? '',
            ),
            heightSpace(8),
            linkText(
              session.reference ?? '',
            ).onTap(() {
              js.context.callMethod('open', [session.reference]);
            })
          ]),
        ),
        widthSpace(8),
        PopupMenuButton(
          color: whiteColor,
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
    );
  }
}
