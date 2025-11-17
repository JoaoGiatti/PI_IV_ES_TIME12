package br.com.chase.ui.screens.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.ui.components.ErrorMessage
import br.com.chase.ui.components.LoadingIndicator
import br.com.chase.ui.components.RoutesCard
import br.com.chase.ui.theme.Poppins

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Título Principal
        Text(
            text = "Melhores Rotas São Paulo",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when {
            state.isLoading -> {
                LoadingIndicator()
            }
            state.routes.isEmpty() -> {
                Text(
                    text = "Não há rotas por enquanto",
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(state.routes) { route ->
                        Spacer(modifier = Modifier.height(10.dp))
                        RoutesCard(route)
                    }
                }
            }
        }
    }
}
