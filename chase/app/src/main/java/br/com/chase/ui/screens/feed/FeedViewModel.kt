package br.com.chase.ui.screens.feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.chase.utils.NetworkObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = getApplication<Application>().applicationContext

    // Estado interno (MutableStateFlow) que o Compose irá observar.
    private val _state = MutableStateFlow(FeedState(isLoading = true))
    val state: StateFlow<FeedState> = _state

    init {
        // 1. Inicializa a observação do status de rede (conexão).
        observeConnectivityStatus()

        // 2. Inicia o carregamento dos itens do feed (rotas).
        loadFeedItems()
    }

    /**
     * Inicia um job de corrotina para observar o status de conectividade de rede
     * e atualizar o estado do feed.
     */
    private fun observeConnectivityStatus() {
        viewModelScope.launch {
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.update { it.copy(isConnected = connected) }
            }
        }
    }

    /**
     * Gerencia a lógica de carregamento dos itens do feed (rotas).
     */
    private fun loadFeedItems() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Simula a busca de dados em um thread de I/O
                withContext(Dispatchers.IO) {
                    kotlinx.coroutines.delay(1000) // Simula delay de rede
                }

                // Gera a lista de rotas simuladas
                val routeItems = generateMockRouteItems()

                // Atualiza o estado com a lista de itens e define isLoading como false.
                _state.update {
                    it.copy(
                        items = routeItems,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                println("Erro ao carregar o feed: ${e.message}")
                _state.update {
                    it.copy(
                        items = emptyList(),
                        isLoading = false,
                    )
                }
            }
        }
    }

    /**
     * Gera uma lista de FeedItem (Rotas) que corresponde à interface da imagem.
     */
    private fun generateMockRouteItems(): List<FeedItem> {
        return listOf(
            FeedItem(
                location = "R. Tiradentes, Jd. Guanabara, Monte Mor",
                distance = "5.27 km",
                recordTime = "24:32",
                competitorsCount = 19,
                topCompetitors = listOf(
                    Competitor(1, "Jaime Ferreira", "24:32", "31.5 km/h"),
                    Competitor(2, "Rafinha Jr", "24:21", "30.3 km/h"),
                    Competitor(3, "Emanuele Ferreira", "23:46", "29.0 km/h"),
                )
            ),
            FeedItem(
                location = "R. Olímpio Faria, Jd Alvorada, Monte Mor",
                distance = "16.78 km",
                recordTime = "1:35:07",
                competitorsCount = 17,
                topCompetitors = listOf(
                    Competitor(1, "Júlia Mattos", "1:35:07", "35.5 km/h"),
                    Competitor(2, "Felps Humild", "1:32:22", "35.1 km/h"),
                    Competitor(3, "Ferrazzz", "1:23:01", "33.2 km/h"),
                )
            ),
            // Terceiro item, repetindo o primeiro
            FeedItem(
                location = "R. Tiradentes, Jd. Guanabara, Monte Mor",
                distance = "5.27 km",
                recordTime = "24:32",
                competitorsCount = 19,
                topCompetitors = listOf(
                    Competitor(1, "Jaime Ferreira", "24:32", "31.5 km/h"),
                    Competitor(2, "Rafinha Jr", "24:21", "30.3 km/h"),
                    Competitor(3, "Emanuele Ferreira", "23:46", "29.0 km/h"),
                )
            ),
        )
    }
}