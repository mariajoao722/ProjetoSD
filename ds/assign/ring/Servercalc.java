
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Servercalc {
    private ServerSocket server;
	

    public Servercalc(String ipAddress, int port) throws Exception {
        this.server = new ServerSocket(port, 1, InetAddress.getByName(ipAddress));
    }

    private void listen() throws Exception {
	while(true) {
	    Socket client = this.server.accept();
	    String clientAddress = client.getInetAddress().getHostAddress();
	    System.out.printf("\r\nnew connection from %s\n", clientAddress);
	    new Thread(new ConnectionHandler(clientAddress, client)).start();
	}
    }
    
    public InetAddress getSocketAddress() {
	return this.server.getInetAddress();
    }
        
    public int getPort() {
	return this.server.getLocalPort();
    }
    
    public static void main(String[] args) throws Exception {
	Servercalc app = new Servercalc("localhost",12333);
	System.out.printf("\r\nrunning server: host=%s @ port=%d\n",
	    app.getSocketAddress().getHostAddress(), app.getPort());
	app.listen();
    }
}



class ConnectionHandler implements Runnable {
    String clientAddress;
    Socket clientSocket;    

    public ConnectionHandler(String clientAddress, Socket clientSocket) {
	this.clientAddress = clientAddress;
	this.clientSocket  = clientSocket;    
    }

    @Override
    public void run() {
	/*
	 * prepare socket I/O channels
	 */
	try {
	    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));    
	    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
	
	    while(true) {
			/* 
			* receive command 
			*/
			try{
				String command;
				if( (command = in.readLine()) == null)
					break;
				else
					System.out.printf("message from %s : %s\n", clientAddress, command);		      	                   
					/*
					* process command 
					*/
					Scanner sc = new Scanner(command).useDelimiter(":");
					String  op = sc.next();
					double  x  = Double.parseDouble(sc.next());
					double  y  = Double.parseDouble(sc.next());
					double  result = 0.0; 
					switch(op) {
					case "add": result = x + y; break;
					case "sub": result = x - y; break;
					case "mul": result = x * y; break;
					case "div": result = x / y; break;
				}  
				/*
				* send result
				*/
				System.out.println(String.valueOf(result));
				out.println(result);
				out.flush();
			}catch (SocketException se) {
				// Handle the SocketException (connection closed) as needed
        		se.printStackTrace(); // or log the exception

					break;
			}
		}
	}catch(Exception e) {
	    e.printStackTrace();
	}
    }
}
