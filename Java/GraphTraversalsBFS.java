import java.util.*;

public class GraphTraversalsBFS {

    private static final int INFINITO = Integer.MAX_VALUE;

    public static <V, E> void bfs(Graph<V, E> grafo, Vertex<V> s) {
        Map<Vertex<V>, Integer> distancia = new HashMap<>();
        Map<Vertex<V>, Vertex<V>> padre = new HashMap<>();
        Set<Vertex<V>> visitados = new HashSet<>();
        Queue<Vertex<V>> Q = new LinkedList<>();

        // Inicialización
        for (Vertex<V> u : grafo.vertices()) {
            distancia.put(u, INFINITO);
            padre.put(u, null);
        }

        // Configuración del nodo origen
        visitados.add(s);
        distancia.put(s, 0);
        padre.put(s, null);
        Q.add(s);

        // Bucle principal
        while (!Q.isEmpty()) {
            Vertex<V> u = Q.poll();

            for (Edge<E> edge : grafo.outgoingEdges(u)) {
                Vertex<V> v = grafo.opposite(u, edge);

                if (!visitados.contains(v)) {
                    visitados.add(v);
                    distancia.put(v, distancia.get(u) + 1);
                    padre.put(v, u);
                    Q.add(v);
                    
                    // Acción opcional al visitar (ej: imprimir)
                    System.out.println("Visitado: " + v.getElement() + " | Distancia: " + distancia.get(v));
                }
            }
        }
    }
}