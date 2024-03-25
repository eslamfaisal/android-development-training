class SessionModel {
  String? name;
  String? id;
  num? createdAt;
  String? reference;

  SessionModel({this.name, this.id, this.createdAt, this.reference});

  SessionModel.fromJson(Map<String, dynamic> json) {
    id = json["id"];
    name = json["Name"];
    createdAt = json["created_at"];
    reference = json["reference"];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['Name'] = this.name;
    data['id'] = this.id;
    data['created_at'] = this.createdAt;
    data['reference'] = this.reference;
    return data;
  }
}
