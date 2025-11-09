package br.com.chase.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.utils.NetworkObserver
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val chaseSpringRepository = ChaseSpringRepository(RetrofitModule.api)
    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        // 👇 Começa a observar a conexão assim que o ViewModel é criado
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }

        loadUserRoutes()
    }

    fun loadUserRoutes() = viewModelScope.launch {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        chaseSpringRepository.getRoutesByUser(uid)
            .onSuccess { routes ->
                _state.update { it.copy(routes = routes, isLoading = false) }
            }
            .onFailure { e ->
                _state.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
    }


    fun currentUser() = viewModelScope.launch {
        _state.value = _state.value.copy(user = FirebaseAuth.getInstance().currentUser)
    }

    fun onAdicionarBiografiaClick() {
        _state.update { it.copy(editing = true) }
    }

    fun onBiografiaChange(novoTexto: String) {
        _state.update { it.copy(bio = novoTexto) }
    }

    fun onSalvarBiografiaClick() {
        _state.update { it.copy(editing = false) }
    }

    fun onEditarBiografiaClick() {
        _state.update { it.copy(editing = true) }
    }

    fun onCancelarClick() {
        _state.update { it.copy(editing = false) }
    }

    fun onDismissDialog() {
        _state.update { it.copy(showDialog = false) }
    }

    fun onShowDialog() {
        _state.update { it.copy(showDialog = true) }
    }
}