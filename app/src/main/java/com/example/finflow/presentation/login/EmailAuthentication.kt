package com.example.finflow.presentation.login


import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


/**
 * EmailAuthentication is a class that handles email-based sign-up, sign-in, and password reset operations.
 */
class EmailAuthentication {

    /**
     * signUpWithEmail is a function that signs up a new user with the provided email and password.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @param context The context used to access resources and services.
     * @param onSuccess A function to be invoked when the sign-up is successful.
     * @param onFailure A function to be invoked when the sign-up fails.
     */
    fun signUpWithEmail(email: String, password: String, context: Context,
                        onSuccess: () -> Unit, onFailure: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    auth.createUserWithEmailAndPassword(email, password).await()
                }
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                onSuccess()
            } catch (e: Exception) {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", e)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseAuthInvalidUserException -> {
                        Toast.makeText(context, "User already exists", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                onFailure()
            }
        }
    }

    /**
     * signInWithEmail is a function that signs in a user with the provided email and password.
     *
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @param context The context used to access resources and services.
     * @param onSuccess A function to be invoked when the sign-in is successful.
     * @param onFailure A function to be invoked when the sign-in fails.
     */
    fun signInWithEmail(email: String, password: String, context: Context,
                        onSuccess: () -> Unit, onFailure: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    auth.signInWithEmailAndPassword(email, password).await()
                }
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                onSuccess()
            } catch (e: Exception) {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", e)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                    is FirebaseAuthInvalidUserException -> {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                onFailure()
            }
        }
    }


    /**
     * forgotPassword is a function that sends a password reset email to the provided email.
     *
     * @param email The email entered by the user.
     * @param onSuccessfullySend A function to be invoked when the password reset email is sent successfully.
     * @param onFailed A function to be invoked when sending the password reset email fails.
     */
    fun forgotPassword(email: String,
                       onSuccessfullySend: () -> Unit, onFailed: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    auth.sendPasswordResetEmail(email).await()
                }
                // Email sent successfully
                Log.d(TAG, "Email sent.")
                onSuccessfullySend()
            } catch (e: Exception) {
                // If sending email fails, display a message to the user.
                Log.w(TAG, "sendPasswordResetEmail:failure", e)
                onFailed()
            }
        }
    }


    /**
     * validatePassword is a function that validates a password based on certain criteria.
     *
     * @param password The password to be validated.
     * @return A string containing the validation error message if the password is invalid, or null if the password is valid.
     */
    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "Password should be at least 8 characters long"
        }
        if (!password.any { it.isUpperCase() }) {
            return "Password should contain at least one uppercase letter"
        }
        if (!password.any { it.isLowerCase() }) {
            return "Password should contain at least one lowercase letter"
        }
        if (!password.any { it.isDigit() }) {
            return "Password should contain at least one digit"
        }
        if (!password.contains(Regex("[@#$%^&+=]"))) {
            return "Password should contain at least one special character (@, #, $, %, ^, &, +, =)"
        }
        return null
    }


    /**
     * isValidEmail is a function that checks if an email is valid.
     *
     * @param email The email to be checked.
     * @return A boolean indicating whether the email is valid.
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

}