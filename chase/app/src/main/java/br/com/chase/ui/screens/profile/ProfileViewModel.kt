package br.com.chase.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

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