package br.com.chase.ui.screens.feed
import androidx.compose.ui.graphics.Color

data class FeedState(
    val isConnected: Boolean = true,
    val isLoading: Boolean = false,
    val items: List<FeedItem> = emptyList()
)
data class FeedItem(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val color: Color
)