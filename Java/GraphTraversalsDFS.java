import java.util.*;

public class GraphTraversalsDFS {

    private static Set<Vertex<?>> visitados = new HashSet<>();
    private static Set<Edge<?>> aristasExploradas = new HashSet<>();

    // Método conductor (Driver)
    public static <V, E> void dfsDriver(Graph<V, E> grafo, Vertex<V> inicio) {
        visitados.clear();
        aristasExploradas.clear();
        dfs(grafo, inicio);
    }

    // Método recursivo
    private static <V, E> void dfs(Graph<V, E> grafo, Vertex<V> v) {
        visitados.add(v);
        System.out.println("Visitado: " + v.getElement());

        for (Edge<E> e : grafo.outgoingEdges(v)) {
            if (!aristasExploradas.contains(e)) {
                Vertex<V> w = grafo.opposite(v, e);
                aristasExploradas.add(e);

                if (!visitados.contains(w)) {
                    // Arista de descubrimiento (Discovery Edge)
                    dfs(grafo, w);
                } else {
                    // Arista de retroceso (Back Edge) - Ciclo detectado
                    System.out.println("Ciclo detectado hacia: " + w.getElement());
                }
            }
        }
    }
}