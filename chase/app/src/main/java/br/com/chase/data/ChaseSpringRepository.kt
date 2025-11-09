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
            Result.failure(Exception("Error ${response.code()} while fetching routes data"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUser(uid: String): Result<UserResponse> = try {
        val response = api.getUser(uid)
        if (response.isSuccessful) {
            response.body()?.let { Result.success(it) }
                ?: Result.failure(Exception("Empty user response"))
        } else {
            Result.failure(Exception("Error ${response.code()} while fetching user data"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUser(uid: String, userResponse: UserResponse): Result<UserResponse> = try {
        val response = api.updateUser(uid, userResponse)
        if (response.isSuccessful) {
            response.body()?.let { Result.success(it) }
                ?: Result.failure(Exception("Empty user response"))
        } else {
            Result.failure(Exception("Error ${response.code()} while updating user"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}