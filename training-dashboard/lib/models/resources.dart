import 'status.dart';

class Resource<T> {
  T? data;
  Status? status;
  String? errorMessage;

  Resource(this.status, {this.data, this.errorMessage});
}
