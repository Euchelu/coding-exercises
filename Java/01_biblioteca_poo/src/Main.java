/**
 * Demo: Factory Method crea los items, Strategy calcula la multa segun el
 * tipo, y Observer notifica automaticamente cuando un item deseado vuelve
 * a estar disponible.
 */
public class Main {
    public static void main(String[] args) throws ItemNoEncontradoException {
        Biblioteca biblioteca = new Biblioteca();

        biblioteca.agregarItem(FabricaBiblioteca.crear(TipoItem.LIBRO, "L1", "El Quijote"));
        biblioteca.agregarItem(FabricaBiblioteca.crear(TipoItem.DVD, "D1", "Inception"));
        biblioteca.agregarItem(FabricaBiblioteca.crear(TipoItem.REVISTA, "R1", "National Geographic"));

        biblioteca.listarCatalogo();

        Usuario ana = new Usuario("Ana");
        Usuario luis = new Usuario("Luis");

        System.out.println("\n--- Prestamos ---");
        try {
            biblioteca.prestarItem("L1", ana);
        } catch (ItemNoDisponibleException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Luis quiere el mismo libro (no disponible) ---");
        try {
            biblioteca.prestarItem("L1", luis);
        } catch (ItemNoDisponibleException e) {
            System.out.println("Error: " + e.getMessage());
            biblioteca.solicitarAvisoDisponibilidad("L1", luis); // Observer: Luis se suscribe
        }

        System.out.println("\n--- Ana devuelve el libro (dispara la notificacion a Luis) ---");
        biblioteca.devolverItem("L1");

        System.out.println("\n--- Multas por retraso (Strategy segun tipo de item) ---");
        long diasRetraso = 5;
        System.out.printf("Multa libro (%d dias): %.2f eur%n", diasRetraso, biblioteca.calcularMulta("L1", diasRetraso));
        System.out.printf("Multa DVD (%d dias): %.2f eur%n", diasRetraso, biblioteca.calcularMulta("D1", diasRetraso));
        System.out.printf("Multa revista (%d dias, con tope): %.2f eur%n", diasRetraso, biblioteca.calcularMulta("R1", diasRetraso));

        System.out.println("\n--- Manejo de excepciones: id inexistente ---");
        try {
            biblioteca.prestarItem("X99", ana);
        } catch (ItemNoEncontradoException | ItemNoDisponibleException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n--- Estado final ---");
        biblioteca.listarCatalogo();
    }
}