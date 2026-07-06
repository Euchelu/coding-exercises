/** Observer: se notifica cuando un item por el que alguien esperaba vuelve a estar disponible. */
public interface ObservadorDisponibilidad {
    void onDisponible(ItemBiblioteca item);
}