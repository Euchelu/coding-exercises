import java.util.*;

public class AdjacencyListGraph<V, E> {

    // --- Clases internas para vértices y aristas ---
    public class Vertex {
        V element;
        List<Edge> incidentEdges = new LinkedList<>();
        Vertex(V elem) { this.element = elem; }
    }

    public class Edge {
        E element;
        Vertex v, w;
        Edge(Vertex v, Vertex w, E elem) {
            this.v = v; this.w = w; this.element = elem;
        }
    }

    // --- Estructuras de almacenamiento ---
    private List<Vertex> vertices = new ArrayList<>();
    private List<Edge> edges    = new ArrayList<>();

    // endVertices: devuelve los dos extremos de la arista
    public Vertex[] endVertices(Edge e) {
        return new Vertex[]{ e.v, e.w };
    }

    // opposite: dado un vértice y una arista, devuelve el otro extremo
    public Vertex opposite(Vertex u, Edge e) {
        if (e.v == u) return e.w;
        if (e.w == u) return e.v;
        throw new IllegalArgumentException("El vértice no está en la arista.");
    }

    // areAdjacent: true si existe arista entre u y v
    public boolean areAdjacent(Vertex u, Vertex v) {
        for (Edge e : u.incidentEdges) {
            if (opposite(u, e) == v) return true;
        }
        return false;
    }

    // replace en vértice: cambia su elemento
    public boolean replace(Vertex u, V x) {
        if (!vertices.contains(u)) return false;
        u.element = x;
        return true;
    }

    // replace en arista: cambia su elemento
    public boolean replace(Edge e, E x) {
        if (!edges.contains(e)) return false;
        e.element = x;
        return true;
    }

    // insertVertex: crea y añade un nuevo vértice con elemento o
    public Vertex insertVertex(V o) {
        Vertex u = new Vertex(o);
        vertices.add(u);
        return u;
    }

    // insertEdge: crea y añade una nueva arista entre v y w con elemento o
    public Edge insertEdge(Vertex v, Vertex w, E o) {
        if (!vertices.contains(v) || !vertices.contains(w))
            throw new IllegalArgumentException("Vértice no pertenece al grafo.");
        Edge e = new Edge(v, w, o);
        v.incidentEdges.add(e);
        w.incidentEdges.add(e);
        edges.add(e);
        return e;
    }

    // removeVertex: elimina u y todas sus aristas incidentes
    public boolean removeVertex(Vertex u) {
        if (!vertices.remove(u)) return false;
        // copiamos la lista para evitar ConcurrentModification
        List<Edge> toRemove = new ArrayList<>(u.incidentEdges);
        for (Edge e : toRemove) removeEdge(e);
        return true;
    }

    // removeEdge: elimina la arista e de ambos extremos
    public boolean removeEdge(Edge e) {
        if (!edges.remove(e)) return false;
        e.v.incidentEdges.remove(e);
        e.w.incidentEdges.remove(e);
        return true;
    }

    // vertices: iterable de todos los vértices
    public Iterable<Vertex> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    // edges: iterable de todas las aristas
    public Iterable<Edge> edges() {
        return Collections.unmodifiableList(edges);
    }
}
