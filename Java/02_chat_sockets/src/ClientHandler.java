import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/** Atiende a un unico cliente: lee su nombre y luego retransmite cada linea que envia. */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final ChatServer servidor;
    private PrintWriter salida;
    private String nombre = "anonimo";

    public ClientHandler(Socket socket, ChatServer servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);

            salida.println("Escribe tu nombre:");
            String primeraLinea = entrada.readLine();
            if (primeraLinea != null && !primeraLinea.isBlank()) nombre = primeraLinea.trim();
            servidor.broadcast("* " + nombre + " se ha unido al chat *", this);

            String linea;
            while ((linea = entrada.readLine()) != null) {
                if (linea.equalsIgnoreCase("/salir")) break;
                servidor.broadcast(nombre + ": " + linea, this);
            }
        } catch (IOException e) {
            System.out.println("Conexion perdida con " + nombre + ": " + e.getMessage());
        } finally {
            servidor.registrarSalida(this);
            servidor.broadcast("* " + nombre + " ha salido del chat *", this);
            try { socket.close(); } catch (IOException ignored) { }
        }
    }

    void enviar(String mensaje) {
        if (salida != null) salida.println(mensaje);
    }
}