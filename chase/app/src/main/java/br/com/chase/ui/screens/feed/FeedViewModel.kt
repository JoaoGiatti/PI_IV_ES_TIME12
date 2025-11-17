package br.com.chase.ui.screens.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.data.ChaseSpringRepository
import br.com.chase.data.api.RetrofitModule
import br.com.chase.utils.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val chaseSpringRepository = ChaseSpringRepository(RetrofitModule.api)
    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(FeedState(isLoading = true))
    val state: StateFlow<FeedState> = _state

    init {
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.value = _state.value.copy(isConnected = connected)
            }
        }

        loadFeedItems()
    }

    private fun loadFeedItems() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, errorMessage = null) }

        chaseSpringRepository.getPublicRoutes()
            .onSuccess { routes ->
                _state.update { it.copy(routes = routes, isLoading = false) }
            }
            .onFailure { e ->
                _state.update { it.copy(errorMessage = e.message, isLoading = false) }
            }
    }
}