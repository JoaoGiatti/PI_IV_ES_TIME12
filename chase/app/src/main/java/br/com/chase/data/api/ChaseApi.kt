package br.com.chase.data.api

import com.google.firebase.auth.FirebaseUser
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChaseApi {

    @POST("users")
    suspend fun createUser(@Body user: FirebaseUser): Response<FirebaseUser>

    @GET("routes")
    suspend fun getAllRoutes(): RouteListResponse

    @GET("routes/user/{userId}")
    suspend fun getRoutesByUser(@Path("userId") userId: String): RouteListResponse
}
