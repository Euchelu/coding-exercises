import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Servidor de chat TCP: acepta conexiones y atiende cada cliente en su
 * propio hilo (via ExecutorService), retransmitiendo los mensajes de uno
 * a todos los demas conectados.
 */
public class ChatServer {
    private final int puerto;
    private final Set<ClientHandler> clientes = new CopyOnWriteArraySet<>();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public ChatServer(int puerto) {
        this.puerto = puerto;
    }

    public void iniciar() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor de chat escuchando en el puerto " + puerto);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clientes.add(handler);
                pool.execute(handler);
            }
        } finally {
            pool.shutdown();
        }
    }

    void registrarSalida(ClientHandler handler) {
        clientes.remove(handler);
    }

    void broadcast(String mensaje, ClientHandler remitente) {
        for (ClientHandler cliente : clientes) {
            if (cliente != remitente) cliente.enviar(mensaje);
        }
    }

    public static void main(String[] args) throws IOException {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        new ChatServer(puerto).iniciar();
    }
}