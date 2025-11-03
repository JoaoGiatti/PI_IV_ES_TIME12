package br.com.chase.data

import br.com.chase.data.api.ChaseApi
import br.com.chase.data.api.RetrofitModule
import com.google.firebase.auth.FirebaseUser
import retrofit2.Response

class ChaseSpringRepository(
    private val api: ChaseApi
) {
    suspend fun createUser(user: FirebaseUser): Response<FirebaseUser> {
        return RetrofitModule.api.createUser(user)
    }
}