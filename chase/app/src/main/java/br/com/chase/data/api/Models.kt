package br.com.chase.data.api

import br.com.chase.data.local.model.RouteData

data class RouteListResponse(val results: List<RouteData.Route>)
