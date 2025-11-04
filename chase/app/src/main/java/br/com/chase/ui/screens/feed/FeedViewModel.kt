package br.com.chase.ui.screens.feed

import android.app.Application
import androidx.compose.ui.graphics.Color
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

    // Constante para o número robusto de itens de feed.
    private val ITEM_COUNT = 500

    // Contexto da aplicação, usado para o NetworkObserver.
    private val appContext = getApplication<Application>().applicationContext

    // Estado interno (MutableStateFlow) que o Compose irá observar.
    private val _state = MutableStateFlow(FeedState(isLoading = true))
    val state: StateFlow<FeedState> = _state

    init {
        // 1. Inicializa a observação do status de rede (conexão).
        observeConnectivityStatus()

        // 2. Inicia o carregamento dos itens do feed.
        loadFeedItems()
    }

    /**
     * Inicia um job de corrotina para observar o status de conectividade de rede
     * e atualizar o estado do feed.
     */
    private fun observeConnectivityStatus() {
        viewModelScope.launch {
            // A NetworkObserver é um Flow que emite o status mais recente.
            NetworkObserver.observeNetworkStatus(appContext).collectLatest { connected ->
                _state.update { it.copy(isConnected = connected) }
            }
        }
    }

    /**
     * Gerencia a lógica de carregamento dos itens do feed, incluindo simulação
     * de chamadas a serviços remotos e a atualização do estado de carregamento.
     */
    private fun loadFeedItems() {
        // Inicia o carregamento e define isLoading como true.
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                // Simula a busca de dados em um thread de I/O
                val remoteData = withContext(Dispatchers.IO) {
                    fetchRemoteData()
                }

                // Processa os dados recebidos (simulados)
                val processedItems = generateMockItems(ITEM_COUNT, remoteData)

                // Atualiza o estado com a lista de itens e define isLoading como false.
                _state.update {
                    it.copy(
                        items = processedItems,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // Em caso de falha na busca (simulada ou real)
                println("Erro ao carregar o feed: ${e.message}")
                _state.update {
                    it.copy(
                        items = emptyList(),
                        isLoading = false,
                        // Aqui poderia adicionar um campo de erro no FeedState
                    )
                }
            }
        }
    }

    /**
     * Simula uma chamada de API assíncrona para buscar dados remotos.
     * Em um ambiente real, isso faria uma chamada de rede.
     */
    private suspend fun fetchRemoteData(): String {
        // Simula um delay de 1 segundo de rede
        kotlinx.coroutines.delay(1000)
        // Retorna um dado simulado que seria usado para construir os FeedItems
        return "Dados remotos processados com sucesso!"
    }

    /**
     * Gera uma lista de FeedItem com base em uma contagem especificada
     * e dados remotos de apoio (para demonstrar o uso).
     * * @param count O número de itens a gerar (mínimo de 500).
     * @param remoteData Dados auxiliares simulados da API.
     * @return Uma lista de FeedItem.
     */
    private fun generateMockItems(count: Int, remoteData: String): List<FeedItem> {
        // Aumento da diversidade de categorias para 10
        val categories = listOf(
            "Finanças Pessoais",
            "Investimentos",
            "Tecnologia e Inovação",
            "Saúde e Bem-estar",
            "Notícias Globais",
            "Cultura Pop",
            "Mercado Imobiliário",
            "Viagens e Turismo",
            "Sustentabilidade",
            "Carreiras e Empreendedorismo"
        )

        // Aumento da paleta de cores para maior variação visual
        val colors = listOf(
            Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0),
            Color(0xFF00BCD4), Color(0xFFFF5722), Color(0xFF03A9F4), Color(0xFFE91E63),
            Color(0xFF8BC34A), Color(0xFF673AB7)
        )

        // Gerando a lista de itens robusta (500)
        return (1..count).map { i ->
            val category = categories[i % categories.size]
            val color = colors[i % colors.size]
            val indexInPalette = i % colors.size

            // Descrição mais variada e longa
            val longDescription = "Esta é a descrição detalhada para o item $i. Ele está indexado na cor $indexInPalette, e pertence à importante área de '$category'. Este conteúdo é uma simulação de dados robustos para garantir que seu LazyColumn seja testado com eficiência. A informação remota era: '$remoteData'. Mais detalhes: O uso de corrotinas garante que a UI permaneça responsiva durante a simulação de I/O."

            FeedItem(
                id = i,
                title = "Notícia de $category | ID: $i",
                description = longDescription,
                category = category,
                color = color
            )
        }
    }
}