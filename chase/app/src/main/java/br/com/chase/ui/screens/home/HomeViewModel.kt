package br.com.chase.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.utils.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun selectTab(index: Int) {
        _state.update { it.copy(selectedTab = index) }
    }

    fun setTopBarVisible(visible: Boolean) {
        _state.update { it.copy(topBarVisible = visible) }
    }

    init {
        // 👇 Começa a observar a conexão assim que o ViewModel é criado
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }
    }
}
