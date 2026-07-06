import java.util.ArrayList;
import java.util.List;

/**
 * Clase base de cualquier material prestable. Encapsula el estado de
 * disponibilidad y delega el calculo de la multa (Strategy) y la
 * notificacion de disponibilidad (Observer) en colaboradores.
 */
public abstract class ItemBiblioteca {
    private final String id;
    private final String titulo;
    private boolean disponible = true;
    private final PoliticaMulta politicaMulta;
    private final List<ObservadorDisponibilidad> listaEspera = new ArrayList<>();

    protected ItemBiblioteca(String id, String titulo, PoliticaMulta politicaMulta) {
        this.id = id;
        this.titulo = titulo;
        this.politicaMulta = politicaMulta;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isDisponible() { return disponible; }

    public abstract String getTipo();

    public void prestar() throws ItemNoDisponibleException {
        if (!disponible) throw new ItemNoDisponibleException(titulo + " ya esta prestado");
        disponible = false;
    }

    public void devolver() {
        disponible = true;
        notificarDisponibilidad();
    }

    public double calcularMulta(long diasRetraso) {
        return politicaMulta.calcular(diasRetraso);
    }

    public void suscribirEspera(ObservadorDisponibilidad observador) {
        listaEspera.add(observador);
    }

    private void notificarDisponibilidad() {
        for (ObservadorDisponibilidad observador : listaEspera) {
            observador.onDisponible(this);
        }
        listaEspera.clear();
    }

    @Override
    public String toString() {
        return String.format("[%s] %-5s %-30s %s", id, getTipo(), titulo,
                disponible ? "disponible" : "prestado");
    }
}