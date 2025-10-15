package br.com.chase.ui.screens.route

class RouteData {
    data class Runner(
        val name: String,
        val time: String,
        val speed: String
    )

    data class Route(
        val location: String,
        val distance: String,
        val recordTime: String,
        val competitors: Int,
        val topRunners: List<Runner>
    )
}
