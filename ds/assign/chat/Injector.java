
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Injector { 
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[0]));
        System.out.printf("\r\nconnected to server: %s\n", socket.getInetAddress());
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("token");
        out.flush(); 
        socket.close();             
    }
}

