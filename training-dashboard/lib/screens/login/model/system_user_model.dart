import 'package:cloud_firestore/cloud_firestore.dart';

class SystemUserModel {
  String? id;
  String? name;
  String? email;


  SystemUserModel({this.id,this.name, this.email});

  SystemUserModel.fromJson(Map<String, dynamic> json) {
    id = json['id'];
    name = json['name'];
    email = json['email'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['id'] = this.id;
    data['name'] = this.name;
    data['email'] = this.email;
    data['updated_at'] = DateTime.now().millisecondsSinceEpoch;
    return data;
  }

  SystemUserModel.initial() {
    this.id = "";
  }

}
