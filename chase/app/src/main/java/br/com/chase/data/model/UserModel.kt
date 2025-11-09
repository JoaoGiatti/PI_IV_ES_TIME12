package br.com.chase.data.model

import com.google.firebase.auth.FirebaseUser

data class UserRequest(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String? = ""
)

data class UserResponse(
    val uid: String,
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val bio: String,
    val createdAt: String,
    val medals: List<String>,
    val totalCalories: Double,
    val totalDistance: Double,
    val totalTime: Double
)

fun FirebaseUser.toUserRequest() = UserRequest(
    uid = uid,
    email = email?.cleanUtf8(),
    displayName = displayName?.cleanUtf8(),
    photoUrl = photoUrl?.toString()?.cleanUtf8()
)

fun String.cleanUtf8() = this.replace("\u00A0", " ")

