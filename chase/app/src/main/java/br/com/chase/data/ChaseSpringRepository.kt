package br.com.chase.data

import br.com.chase.data.api.ChaseApi
import br.com.chase.data.api.RouteListResponse
import br.com.chase.data.api.RouteResponse
import br.com.chase.data.api.UserRequest
import br.com.chase.data.api.UserResponse

class ChaseSpringRepository(
    private val api: ChaseApi
) {
    suspend fun createUser(user: UserRequest): Result<UserResponse?> {
        return try {
            val res = api.createUser(user)
            when {
                res.isSuccessful -> Result.success(res.body())
                res.code() == 409 -> {
                    Result.success(null)
                }
                else -> {
                    val msg = res.errorBody()?.string() ?: "Erro ao criar usuário"
                    Result.failure(IllegalStateException("HTTP ${res.code()} - $msg"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRoutesByUser(uid: String) = try {
        val response = api.getRoutesByUser(uid)
        if (response.isSuccessful) {
            Result.success(response.body()?.routes ?: emptyList())
        } else {
            Result.failure(Exception("Erro ${response.code()} ao buscar rotas"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }


}