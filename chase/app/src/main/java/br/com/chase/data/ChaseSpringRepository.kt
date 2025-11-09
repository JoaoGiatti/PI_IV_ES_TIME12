package br.com.chase.data

import br.com.chase.data.api.ChaseApi
import br.com.chase.data.model.RouteRequest
import br.com.chase.data.model.RouteResponse
import br.com.chase.data.model.UserRequest
import br.com.chase.data.model.UserResponse

class ChaseSpringRepository(
    private val api: ChaseApi
) {
    suspend fun createUser(user: UserRequest): Result<UserResponse?> = try {
        val response = api.createUser(user)

        if(response.isSuccessful || response.code() == 409) {
            Result.success(response.body())
        }
        else  {
            Result.failure(Exception("Error ${response.code()} while creating user"))
        }
    } catch (e: Exception) {
        Result.failure(e)
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

    suspend fun createRoute(route: RouteRequest): Result<RouteResponse> = try {
        val response = api.createRoute(route)

        if (response.isSuccessful) {
            response.body()?.let { Result.success(it) }
                ?: Result.failure(Exception("Empty route response"))
        } else {
            Result.failure(Exception("Error ${response.code()} while creating route"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
