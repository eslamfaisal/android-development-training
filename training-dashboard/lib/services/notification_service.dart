import 'dart:convert';

import 'package:dio/dio.dart';

class NotificationServices {
  var dio = Dio();

  void postNotificationRequest(String to, String title, String bodyMsg) async {
    var body = {
      'to': to,
      "notification": {
        "body": bodyMsg,
        "content_available": true,
        "priority": "high",
        "Title": title,
      }
    };

    var url = 'https://fcm.googleapis.com/fcm/send';
    dio.post(
      url,
      data: json.encode(body),
      options: Options(
        headers: {
          'Authorization':
              'key=AAAAI9km8ug:APA91bFELQ66Kaz0sLnaGsuOXCtb-sQOHgt92cCNO-GOcgsvx1WfzyeF1UyuBREI3zGklTppDCtjIXyaTBJqP2DhZb33lm-Cji5WvekfKj5CjplkyFrSYKKYwf8Lr6tn8xVhI0uaPCV1',
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
      ),
    );
  }
}
