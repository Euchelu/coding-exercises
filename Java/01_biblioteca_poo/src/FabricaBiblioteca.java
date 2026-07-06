/** Factory Method: centraliza la creacion de items segun su tipo. */
public class FabricaBiblioteca {
    public static ItemBiblioteca crear(TipoItem tipo, String id, String titulo) {
        switch (tipo) {
            case LIBRO:   return new Libro(id, titulo);
            case DVD:     return new DVD(id, titulo);
            case REVISTA: return new Revista(id, titulo);
            default: throw new IllegalArgumentException("Tipo de item desconocido: " + tipo);
        }
    }
}