public class Revista extends ItemBiblioteca {
    public Revista(String id, String titulo) {
        super(id, titulo, dias -> Math.min(dias * 0.20, 2.00));
    }

    @Override
    public String getTipo() { return "REVISTA"; }
}