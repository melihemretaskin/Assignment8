package xyz.scoca.assignment_6.models


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("data")
    val user: List<UserData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)