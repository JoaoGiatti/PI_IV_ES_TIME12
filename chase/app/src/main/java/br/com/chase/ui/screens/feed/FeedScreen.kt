package br.com.chase.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = viewModel() // Mantido o nome FeedViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Título e Logo
        Header(state.isConnected)

        // Conteúdo Principal: Loading, Vazio ou Lista
        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            // NOVO TRATAMENTO DE ERRO ABAIXO
            state.error != null -> {
                ErrorMessage(message = state.error!!)
            }
            // FIM DO NOVO TRATAMENTO
            state.items.isEmpty() -> {
                EmptyFeedMessage()
            }
            else -> {
                FeedList(state.items)
            }
        }
    }
}
@Composable
fun TopCompetitorsSection(competitors: List<Competitor>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            // Adicionado para acessibilidade: descreve o que a seção contém
            .background(Color.Transparent)
    ) {
        Text(
            text = "Top Competidores na Rota:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp),
            color = Color.DarkGray
        )
        competitors.forEach { competitor ->
            CompetitorRow(competitor = competitor)
        }
    }
}
// ...
// Adicione a nova função de Composable no final do arquivo
@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Erro",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun Header(isConnected: Boolean) {
    Column {
        if (!isConnected) {
            ConnectivityWarningBar()
        }
        // Título Principal
        Text(
            text = "Melhores Rotas de Monte Mor",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun FeedList(items: List<FeedItem>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            RouteItemCard(item = item) // Função de card renomeada internamente
        }
        item {
            // Espaço no final da lista para a barra de navegação (que não está no código)
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun RouteItemCard(item: FeedItem) { // Usa FeedItem
    Card(
        // ... (Card content remains the same) ...
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Localização
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Localização da Rota: ${item.location}", // MELHORIA A11Y
                    tint = Color.Red,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticItem(
                    label = "Distância",
                    value = item.distance)
                StatisticItem(
                    label = "Tempo recorde",
                    value = item.recordTime)
                StatisticItem(
                    label = "Competidores",
                    value = item.competitorsCount.toString())
            }

            // 3. Lista dos Top Competidores - AGORA USA O COMPONENTE EXTRAÍDO
            TopCompetitorsSection(competitors = item.topCompetitors) // CHAMADA AO NOVO COMPONENTE
        }
    }
}

@Composable
fun StatisticItem(label: String, value: String) {
    // MELHORIA A11Y: Adiciona contentDescription ao Box para leitores de tela
    Box(
        modifier = Modifier.semantics { contentDescription = "$label: $value" }
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

@Composable
fun CompetitorRow(competitor: Competitor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Rank e Nome (com simulação de imagem de perfil)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${competitor.rank}#",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp),
                color = if (competitor.rank == 1) Color.Red else Color.Black
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray) // Simulação da imagem de perfil
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = competitor.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }

        // Tempo e Velocidade
        Text(
            text = "${competitor.time} - ${competitor.speed}",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}


@Composable
fun ConnectivityWarningBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFCC00))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Aviso de conexão",
            tint = Color.Black
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = "Sem conexão! Dados podem estar desatualizados.",
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun EmptyFeedMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Seu feed de rotas está vazio por enquanto. Tente recarregar!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}