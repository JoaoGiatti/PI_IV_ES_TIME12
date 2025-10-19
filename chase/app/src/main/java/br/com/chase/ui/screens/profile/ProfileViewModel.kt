package br.com.chase.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.utils.NetworkObserver
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
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
    }

    fun currentUser() = viewModelScope.launch {
        _state.value = _state.value.copy(user = FirebaseAuth.getInstance().currentUser)
    }
}