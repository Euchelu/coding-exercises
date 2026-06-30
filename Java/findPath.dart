public static void findPath(String origin, String destination, Graph<String, Integer> graph) {
    // Obtener los vértices de origen y destino
    Vertex<String> originV = getVertex(origin, graph);
    Vertex<String> destinationV = getVertex(destination, graph);

    // Listas para almacenar visitados y el camino
    List<Vertex<String>> visitedVertices = new ArrayList<>();
    List<Edge<Integer>> visitedEdges = new ArrayList<>();
    List<String> path = new ArrayList<>();

    // Iniciar búsqueda en profundidad
    if (dfs(graph, originV, destinationV, visitedVertices, visitedEdges, path)) {
        System.out.println("Camino encontrado: " + path);
    } else {
        System.out.println("No existe un camino entre " + origin + " y " + destination);
    }
}

/**
 * Búsqueda en profundidad para encontrar un camino entre dos vértices.
 */
private static boolean dfs(Graph<String, Integer> graph, Vertex<String> current,
                           Vertex<String> destination, List<Vertex<String>> visitedVertices,
                           List<Edge<Integer>> visitedEdges, List<String> path) {
    // Si ya hemos visitado este vértice, regresar
    if (visitedVertices.contains(current)) return false;

    // Marcar el vértice como visitado y agregarlo al camino
    visitedVertices.add(current);
    path.add(current.getElement());

    // Si llegamos al destino, devolver true
    if (current.equals(destination)) return true;

    // Explorar las aristas salientes del vértice actual
    for (Edge<Integer> edge : graph.outgoingEdges(current)) {
        if (!visitedEdges.contains(edge)) {
            // Marcar la arista como visitada
            visitedEdges.add(edge);

            // Obtener el vértice opuesto en la arista
            Vertex<String> neighbor = graph.opposite(current, edge);

            // Realizar DFS recursivamente
            if (dfs(graph, neighbor, destination, visitedVertices, visitedEdges, path)) {
                return true;
            }
        }
    }

    // Si no se encuentra camino, retroceder
    path.remove(path.size() - 1);
    return false;
}
