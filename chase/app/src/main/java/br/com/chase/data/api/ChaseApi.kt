package br.com.chase.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChaseApi {

    @POST("usuarios/criar")
    suspend fun createUser(@Body user: UserRequest): Response<UserResponse>

    @GET("routes")
    suspend fun getAllRoutes(): RouteListResponse

    @GET("routes/user/{userId}")
    suspend fun getRoutesByUser(@Path("userId") userId: String): RouteListResponse
}
