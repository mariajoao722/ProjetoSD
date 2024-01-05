import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import poisson.*;

public class Peer {
    String host;
    Logger logger;
	static boolean gotToken = false;

    public Peer(String hostname) {
		host   = hostname;
		logger = Logger.getLogger("logfile");
		try {
			FileHandler handler = new FileHandler("./" + hostname + "_peer.log", true);
			logger.addHandler(handler);
			SimpleFormatter formatter = new SimpleFormatter();	
			handler.setFormatter(formatter);	
		} catch ( Exception e ) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) throws Exception {
		// Certifique-se de que há pelo menos 2 argumentos (port , nextport)
		if (args.length < 2) {
			System.out.println("Usage: java NomeDoSeuPrograma <port> [nextport]");
			System.exit(1);
		}
		Peer peer = new Peer("localhost");
		System.out.printf("new peer @ host=%s\n", "localhost");
		new Thread(new Server("localhost", Integer.parseInt(args[0]), peer.logger,Integer.parseInt(args[1]))).start(); // args[0] port, argq[1] nextport
		new Thread(new Client()).start(); // thread para gerar comandos
    }
}

class Server implements Runnable {
    String       host;
    int          port;
    ServerSocket server;
    Logger       logger;
	int          nextport;
    
    public Server(String host, int port, Logger logger, int nextport) throws Exception {
		this.host   = host;
		this.port   = port;
		this.logger = logger;
		this.nextport = nextport;
        server = new ServerSocket(port, 1, InetAddress.getByName(host));
    }

    @Override
    public void run() {
		try {
			logger.info("server: endpoint running at port " + port + " ...");
			while(true) {
			try {
				Socket client = server.accept();
				String clientAddress = client.getInetAddress().getHostAddress();
				logger.info("server: new connection from " + clientAddress);
				new Thread(new Connection(clientAddress, client, logger, nextport)).start();
			}catch(Exception e) {	
				e.printStackTrace();
			}    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}


class Connection implements Runnable {
    String clientAddress;
    Socket clientSocket;
    Logger logger;
	int nextport;

    public Connection(String clientAddress, Socket clientSocket, Logger logger, int nextport) {
		this.clientAddress = clientAddress;
		this.clientSocket  = clientSocket;
		this.logger        = logger;
		this.nextport = nextport;
    }

    @Override
    public void run() {
	/*
	 * prepare socket I/O channels
	 */
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));    
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			

			String command;
			command = in.readLine();
			logger.info("server: message from host " + clientAddress + "[command = " + command + "]");

			if(command.equals("token")){
				Peer.gotToken = true;
			}
			
			System.out.println(command);


			for (String item : Client.fila) {
            System.out.println("Elemento: " + item);
        	}

			System.out.println("op1");

			Socket socket  = new Socket(InetAddress.getByName("localhost"), 12333);
			logger.info("client: connected to server " + socket.getInetAddress() + "[port = " + socket.getPort() + "]");

			PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));


			// mandar os itens da fila para o servidor calc e eliminar a fila ciclo até a fila estar vazia
			String primeiroItem = Client.fila.poll();
			while(primeiroItem != null){
				out2.println(String.valueOf(primeiroItem));
				out2.flush();
				String result = in1.readLine();
				System.out.println(primeiroItem);
				System.out.println(result);
				primeiroItem =  Client.fila.poll();
			}
			

			if(Peer.gotToken){
				/* 
		     	* make connection
		     	*/
				Thread.sleep(10000);
				System.out.println("op");
				Socket socke  = new Socket(InetAddress.getByName("localhost"), nextport);
				logger.info("client: connected to server " + socke.getInetAddress() + "[port = " + socke.getPort() + "]");
				PrintWriter out1 = new PrintWriter(socke.getOutputStream(), true);
        		out1.println("token");
        		out1.flush();
				Peer.gotToken = false;
			
			}
			socket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}


//Class que gera comandos aleatórios e adiciona-os a uma fila
class Client implements Runnable {

	static final int SAMPLES = 10;
	static Queue<String> fila = new LinkedList<>();
    
    public Client() throws Exception {
    }

    @Override 
    public void run() {
		String command = "";
		while(!Peer.gotToken){
			
			PoissonProcess pp = new PoissonProcess(4, new Random(0) );
				for (int i = 1; i <= SAMPLES; i++) {
					double t = pp.timeForNextEvent() * 60.0 * 1000.0;
					System.out.println("next event in -> " + (int)t + " ms");

					System.out.println(Peer.gotToken);

					Random rand = new Random();
					// Gera um inteiro aleatório
					int n = rand.nextInt(4);
					// Gera dois doubles aleatórios
					Double n1 =ThreadLocalRandom.current().nextDouble(0, 10);
					Double n2 = ThreadLocalRandom.current().nextDouble(0, 10);

					switch(n){ // Adicionandar elementos à fila
						case 0: command = "add:" + n1 + ":" + n2; fila.offer(command); break;
						case 1: command = "sub:" + n1 + ":" + n2; fila.offer(command); break;
						case 2: command = "mul:" + n1 + ":" + n2; fila.offer(command); break;
						case 3: command = "div:" + n1 + ":" + n2; fila.offer(command); break;
					}
					try {
							Thread.sleep((int)t);
					}catch (InterruptedException e) {
							System.out.println("thread interrupted");
							e.printStackTrace(System.out);
					}
				}
			}
		}	
	   	    
	}

