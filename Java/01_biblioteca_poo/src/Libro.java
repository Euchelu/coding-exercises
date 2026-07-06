public class Libro extends ItemBiblioteca {
    public Libro(String id, String titulo) {
        super(id, titulo, dias -> dias * 0.10);
    }

    @Override
    public String getTipo() { return "LIBRO"; }
}