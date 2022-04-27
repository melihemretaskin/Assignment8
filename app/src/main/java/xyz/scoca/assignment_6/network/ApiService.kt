package xyz.scoca.assignment_6.network

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import xyz.scoca.assignment_6.models.User

interface ApiService {

    @FormUrlEncoded
    @POST("/login.php")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ) : Call<User>

    @FormUrlEncoded
    @POST("/register.php")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("gender") gender: String
    ) : Call<User>

    @FormUrlEncoded
    @POST("update.php")
    fun updateUser(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("gender") gender: String
    ) : Call<xyz.scoca.mobileassignment6.model.crud.User>

    @FormUrlEncoded
    @POST("delete.php")
    fun deleteUser(
        @Field("email") email: String,
    ) : Call<xyz.scoca.mobileassignment6.model.crud.User>

    @FormUrlEncoded
    @POST("fetch_user.php")
    fun fetchUser(
        @Field("email") email: String,
    ) : Call<xyz.scoca.mobileassignment6.model.crud.User>
}