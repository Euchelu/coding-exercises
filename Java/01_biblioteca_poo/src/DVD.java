public class DVD extends ItemBiblioteca {
    public DVD(String id, String titulo) {
        super(id, titulo, dias -> dias * 0.50);
    }

    @Override
    public String getTipo() { return "DVD"; }
}