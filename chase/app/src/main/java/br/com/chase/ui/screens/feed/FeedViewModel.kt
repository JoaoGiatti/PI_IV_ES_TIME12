package br.com.chase.ui.screens.feed

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.utils.NetworkObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = getApplication<Application>().applicationContext

    private val _state = MutableStateFlow(FeedState(isLoading = true))
    val state: StateFlow<FeedState> = _state

    init {
        // 👇 Começa a observar a conexão assim que o ViewModel é criado
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.update { it.copy(isConnected = connected) }
            }
        }

        // Carrega dados simulados (o "bastante linhas")
        loadFeedItems()
    }

    private fun loadFeedItems() {
        // Simulando um carregamento assíncrono
        viewModelScope.launch {
            // kotlinx.coroutines.delay(1000) // Descomente para simular um delay de carregamento

            val mockItems = generateMockItems(50) // Gerando 50 itens para um feed robusto
            _state.update {
                it.copy(
                    items = mockItems,
                    isLoading = false
                )
            }
        }
    }

    private fun generateMockItems(count: Int): List<FeedItem> {
        val categories = listOf("Finanças", "Notícias", "Tecnologia", "Esportes", "Cultura")
        // Usando cores do Material Design para um visual agradável
        val colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0), Color(0xFF00BCD4))

        return (1..count).map { i ->
            val category = categories[i % categories.size]
            val color = colors[i % colors.size]
            FeedItem(
                id = i,
                title = "Item do Feed N° $i",
                description = "Esta é a descrição detalhada para o item $i, pertencente à categoria '$category'. O conteúdo aqui é longo o suficiente para preencher uma linha. É importante ter bastante linhas para o feed!",
                category = category,
                color = color
            )
        }
    }
}