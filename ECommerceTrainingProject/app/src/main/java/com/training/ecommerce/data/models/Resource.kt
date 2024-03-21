package com.training.ecommerce.data.models

sealed class Resource<T>(
    val data: T? = null, val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: Exception, data: T? = null) : Resource<T>(data, message)

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Loading -> "Loading[data=$data]"
            is Error -> "Error[exception=$exception, data=$data]"
        }
    }
}
