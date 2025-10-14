package br.com.chase.ui.screens.route

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RouteViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(RouteState())
    val state: StateFlow<RouteState> = _state
}