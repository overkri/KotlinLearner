package ledger.http

data class MiniRequest(
    val method: String,
    val path: String,
)

data class RouteMatch(
    val handler: String,
    val params: Map<String, String>,
)

class MiniRouter {

    private data class Route(
        val method: String,
        val pattern: List<String>,
        val handler: String,
    )

    private val routes = mutableListOf<Route>()

    fun register(method: String, pattern: String, handler: String): MiniRouter {
        routes += Route(
            method = method.uppercase(),
            pattern = segments(pattern),
            handler = handler,
        )
        return this
    }

    fun route(request: MiniRequest): RouteMatch? {
        val path = segments(request.path)
        val method = request.method.uppercase()

        for (route in routes) {
            if (route.method != method) continue
            if (route.pattern.size != path.size) continue

            val params = mutableMapOf<String, String>()
            var matched = true
            for (index in route.pattern.indices) {
                val patternSegment = route.pattern[index]
                val pathSegment = path[index]
                if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
                    if (pathSegment.isEmpty()) {
                        matched = false
                        break
                    }
                    params[patternSegment.substring(1, patternSegment.length - 1)] = pathSegment
                } else if (patternSegment != pathSegment) {
                    matched = false
                    break
                }
            }
            if (matched) return RouteMatch(route.handler, params)
        }
        return null
    }

    private fun segments(value: String): List<String> =
        value.trim('/').split('/').filter { it.isNotEmpty() }
}
