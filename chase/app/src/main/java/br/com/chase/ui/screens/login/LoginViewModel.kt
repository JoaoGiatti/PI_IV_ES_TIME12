package br.com.chase.ui.screens.login

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.FirebaseAuthRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.data.api.toUserRequest
import br.com.chase.utils.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val authRepository = FirebaseAuthRepository(application)
    private val chaseSpringRepository = ChaseSpringRepository(RetrofitModule.api)

    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    init {
        // 👇 Começa a observar a conexão assim que o ViewModel é criado
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }
    }

    fun getSignInIntent() = authRepository.getSignInIntent()

    fun handleSignInResult(data: Intent?) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)

        if (!_state.value.isConnected) {
            _state.value = _state.value.copy(
                isLoading = false,
                errorMessage = "Sem conexão com a internet."
            )
            return
        }

        viewModelScope.launch {
            try {
                val user = authRepository.signInWithGoogle(data)
                val result = chaseSpringRepository.createUser(user!!.toUserRequest())

                result.onSuccess {

                    Log.e("TAG", "handleSignInResult: $it")

                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = user
                    )
                }.onFailure { exception ->
                    Log.e("TAG", "handleSignInResult: $exception")
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Erro inesperado ao fazer login"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erro inesperado ao fazer login"
                )
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _state.value = LoginState(isConnected = _state.value.isConnected)
    }
}
