
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

        Socket socket1 = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[1]));
        System.out.printf("\r\nconnected to server: %s\n", socket1.getInetAddress());
        PrintWriter out1 = new PrintWriter(socket1.getOutputStream(), true);
        out1.println("token");
        out1.flush();

        Socket socket2 = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[2]));
        System.out.printf("\r\nconnected to server: %s\n", socket2.getInetAddress());
        PrintWriter out2 = new PrintWriter(socket2.getOutputStream(), true);
        out2.println("token");
        out2.flush();
        
        Socket socket3 = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[3]));
        System.out.printf("\r\nconnected to server: %s\n", socket3.getInetAddress());
        PrintWriter out3 = new PrintWriter(socket3.getOutputStream(), true);
        out3.println("token");
        out3.flush();

        Socket socket4 = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[4]));
        System.out.printf("\r\nconnected to server: %s\n", socket4.getInetAddress());
        PrintWriter out4 = new PrintWriter(socket4.getOutputStream(), true);
        out4.println("token");
        out4.flush();

        Socket socket5 = new Socket(InetAddress.getByName("localhost"), Integer.parseInt(args[5]));
        System.out.printf("\r\nconnected to server: %s\n", socket5.getInetAddress());
        PrintWriter out5 = new PrintWriter(socket5.getOutputStream(), true);
        out5.println("token");
        out5.flush();
        
        socket.close(); 
        socket1.close(); 
        socket2.close();
        socket3.close();
        socket4.close();
        socket5.close();         
    }
}

