enum Status { SUCCESS, ERROR }

extension StatusExtension on Status {
  bool get isSuccessful {
    return this == Status.SUCCESS;
  }

  bool get isError {
    return this == Status.ERROR;
  }
}
