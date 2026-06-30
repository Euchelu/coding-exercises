import java.util.*;

public class DijkstraAlgorithm<V, E extends Number> {

    public Map<Vertex<V>, Double> dijkstra(AdjacencyListGraph<V, E> graph, Vertex<V> start) {
        Map<Vertex<V>, Double> dist = new HashMap<>();
        for (Vertex<V> v : graph.vertices()) dist.put(v, Double.MAX_VALUE);
        dist.put(start, 0.0);

        PriorityQueue<Vertex<V>> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            Vertex<V> u = pq.poll();
            if (dist.get(u) == Double.MAX_VALUE) break;

            for (Edge<E> edge : graph.outgoingEdges(u)) {
                Vertex<V> v = graph.opposite(u, edge);
                double newDist = dist.get(u) + edge.element.doubleValue();

                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }
        return dist;
    }
}