import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Cliente de chat: un hilo dedicado escucha lo que llega del servidor e
 * imprime en pantalla, mientras el hilo principal lee de consola y envia.
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        int puerto = args.length > 1 ? Integer.parseInt(args[1]) : 5000;

        try (Socket socket = new Socket(host, puerto)) {
            BufferedReader entradaServidor = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader entradaConsola = new BufferedReader(
                    new InputStreamReader(System.in, StandardCharsets.UTF_8));

            Thread lector = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = entradaServidor.readLine()) != null) {
                        System.out.println(linea);
                    }
                } catch (IOException e) {
                    System.out.println("Se cerro la conexion con el servidor.");
                }
            });
            lector.setDaemon(true);
            lector.start();

            String linea;
            while ((linea = entradaConsola.readLine()) != null) {
                salida.println(linea);
                if (linea.equalsIgnoreCase("/salir")) break;
            }
        }
    }
}