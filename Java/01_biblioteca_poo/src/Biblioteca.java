import java.util.LinkedHashMap;
import java.util.Map;

/** Catalogo central: agrupa los items y coordina prestamos/devoluciones. */
public class Biblioteca {
    private final Map<String, ItemBiblioteca> catalogo = new LinkedHashMap<>();

    public void agregarItem(ItemBiblioteca item) {
        catalogo.put(item.getId(), item);
    }

    private ItemBiblioteca buscar(String id) throws ItemNoEncontradoException {
        ItemBiblioteca item = catalogo.get(id);
        if (item == null) throw new ItemNoEncontradoException("No existe ningun item con id " + id);
        return item;
    }

    public void prestarItem(String id, Usuario usuario)
            throws ItemNoEncontradoException, ItemNoDisponibleException {
        ItemBiblioteca item = buscar(id);
        item.prestar();
        System.out.printf("%s ha tomado prestado '%s'%n", usuario.getNombre(), item.getTitulo());
    }

    public void solicitarAvisoDisponibilidad(String id, Usuario usuario) throws ItemNoEncontradoException {
        ItemBiblioteca item = buscar(id);
        item.suscribirEspera(usuario);
        System.out.printf("%s quedo en lista de espera para '%s'%n", usuario.getNombre(), item.getTitulo());
    }

    public void devolverItem(String id) throws ItemNoEncontradoException {
        ItemBiblioteca item = buscar(id);
        item.devolver();
        System.out.printf("Se devolvio '%s'%n", item.getTitulo());
    }

    public double calcularMulta(String id, long diasRetraso) throws ItemNoEncontradoException {
        return buscar(id).calcularMulta(diasRetraso);
    }

    public void listarCatalogo() {
        System.out.println("--- Catalogo ---");
        for (ItemBiblioteca item : catalogo.values()) {
            System.out.println(item);
        }
    }
}