package com.training.ecommerce.utils


// create extension function for email validation
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}