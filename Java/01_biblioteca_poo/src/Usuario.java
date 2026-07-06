public class Usuario implements ObservadorDisponibilidad {
    private final String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    @Override
    public void onDisponible(ItemBiblioteca item) {
        System.out.printf("  >> Notificacion para %s: '%s' ya esta disponible de nuevo.%n",
                nombre, item.getTitulo());
    }
}