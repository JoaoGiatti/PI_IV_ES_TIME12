package br.com.chase.ui.screens.feed

import androidx.compose.ui.graphics.Color

// Modelo de dados para um competidor individual em uma rota
data class Competitor(
    val rank: Int,
    val name: String,
    val time: String, // Ex: "24:32"
    val speed: String, // Ex: "31.5 km/h"
)

// O FeedItem agora representa uma Rota
data class FeedItem(
    val location: String, // Ex: "R. Tiradentes, Jd. Guanabara, Monte Mor"
    val distance: String, // Ex: "5.27 km"
    val recordTime: String, // Ex: "24:32"
    val competitorsCount: Int, // Ex: 19
    val topCompetitors: List<Competitor> // Top 3
)

// Estado da tela de Feed, que contém a lista de Rotas
data class FeedState(
    val isConnected: Boolean = true,
    val isLoading: Boolean = false,
    val items: List<FeedItem> = emptyList() // Lista de Rotas
)