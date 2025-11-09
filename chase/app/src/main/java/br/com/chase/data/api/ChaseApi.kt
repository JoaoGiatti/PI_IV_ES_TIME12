package br.com.chase.data.api

import br.com.chase.data.model.RouteAttemptRequest
import br.com.chase.data.model.RouteListResponse
import br.com.chase.data.model.RouteRequest
import br.com.chase.data.model.RouteResponse
import br.com.chase.data.model.UserRequest
import br.com.chase.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChaseApi {

    // POST - Cria um usuario...
    @POST("users/create")
    suspend fun createUser(@Body user: UserRequest): Response<UserResponse>

    // GET - Mostra detalhes de um usuario pelo UID...
    @GET("users/{uid}")
    suspend fun getUser(@Path("uid") uid: String) : Response<UserResponse>

    // PUT - Atualiza um usuario pelo UID
    @PUT("users/{uid}")
    suspend fun updateUser(
        @Path("uid") uid: String, @Body user: UserResponse
    ): Response<UserResponse>


    // POST - Cria uma rota...
    @POST("routes/create")
    suspend fun createRoute(@Body route: RouteRequest): Response<RouteResponse>

    // GET - Mostra detalhes de uma rota pelo RID => Rota ID...
    @GET("routes/{rid}")
    suspend fun getRoute(@Path("rid") rid: String): Response<RouteResponse>

    // GET - Lista todas as rotas(publicas)...
    @GET("routes/public")
    suspend fun getPublicRoutes(): Response<RouteListResponse>

    // GET - Lista todas as rotas de um usuario pelo UID...
    @GET("routes/users/{uid}")
    suspend fun getRoutesByUser(@Path("uid") uid: String): Response<List<RouteResponse>>

    // PUT - Atualiza o top3 de uma rota pelo RID => Rota ID...
    @PUT("routes/{rid}/attempt")
    suspend fun updateRoute(
        @Path("rid") rid: String, @Body attempt: RouteAttemptRequest
    ): Response<RouteResponse>
}
