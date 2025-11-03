package br.com.chase.data

import br.com.chase.data.api.ChaseApi
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
}