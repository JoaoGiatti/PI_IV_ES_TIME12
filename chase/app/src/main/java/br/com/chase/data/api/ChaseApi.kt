package br.com.chase.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChaseApi {

    // Cria um usuario...
    @POST("usuarios/criar")
    suspend fun createUser(@Body user: UserRequest): Response<UserResponse>

    // Mostra detalhes de um usuario pelo ID...
    @GET("users/{userId}")
    suspend fun getUser() : Response<UserResponse>

    // Lista todas as rotas de um usuario pelo ID...
    @GET("users/{userId}/routes}")
    suspend fun getAllRoutesByUser(@Path("userId") userId: String): Response<RouteListResponse>

    // Atualiza um usuario pelo ID...
    @PUT("users/{userId}")
    suspend fun updateUser(
        @Path("userId") userId: String, @Body user: UserRequest
    ): Response<UserResponse>


    // Cria uma nova rota...
    @POST("routes")
    suspend fun createRoute(@Body route: RouteRequest): Response<RouteResponse>

    // Lista todas as rotas...
    @GET("routes")
    suspend fun getAllRoutes(): Response<RouteListResponse>

    // Mostra detalhes de uma rota pelo ID...
    @GET("routes/{routeId}")
    suspend fun getRouteById(@Path("routeId") routeId: String): Response<RouteResponse>

    // Atualiza o top3 de uma rota pelo ID...
    @PUT("routes/{routeId}/attempt")
    suspend fun updateRouteAttempt(
        @Path("routeId") routeId: String, @Body attempt: RouteAttemptRequest
    ): Response<RouteResponse>
}
