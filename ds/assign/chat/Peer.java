import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import poisson.*;

public class Peer {
    String host;
    Logger logger;

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
			System.out.println("Usage: java NomeDoSeuPrograma <port> [nextport1] [nextport2] [nextport3] [nextport4]");
			System.exit(1);
		}
	
		// Definindo valores padrão para nextport1, nextport2, nextport3 e nextport4
		int nextport1 = 0;
		int nextport2 = 0;
		int nextport3 = 0;
		int nextport4 = 0;
		int nextport5 = 0;
	
		// Verificando e atribuindo valores se fornecidos
		if (args.length >= 2) {
			nextport1 = Integer.parseInt(args[1]);
		}
		if (args.length >= 3) {
			nextport2 = Integer.parseInt(args[2]);
		}
		if (args.length >= 4) {
			nextport3 = Integer.parseInt(args[3]);
		}
		if (args.length >= 5) {
			nextport4 = Integer.parseInt(args[4]);
		}
		if (args.length >= 6) {
			nextport5 = Integer.parseInt(args[5]);
		}
	
		System.out.println("1:" + args[0]);
		System.out.println("2:" + nextport1);
		System.out.println("3:" + nextport2);
		System.out.println("4:" + nextport3);
		System.out.println("5:" + nextport4);
		System.out.println("6:" + nextport5);

		Peer peer = new Peer("localhost");
		System.out.printf("new peer @ host=%s\n", "localhost");
		// args[0] port, peer está ligado a 5 peers
		new Thread(new Server("localhost", Integer.parseInt(args[0]), peer.logger, nextport1, nextport2, nextport3, nextport4, nextport5)).start();
		new Thread(new Client("localhost", peer.logger, Integer.parseInt(args[0]), nextport1, nextport2, nextport3, nextport4, nextport5)).start();
    }
}


class Server implements Runnable {
    String       host;
    int          port;
    ServerSocket server;
    Logger       logger;
	int          nextport;
	int          nextport2;
	int          nextport3;
	int          nextport4;
	int 		 nextport5;
    
    public Server(String host, int port, Logger logger, int nextport, int nextport2, int nextport3, int nextport4, int nextport5) throws Exception {
	this.host   = host;
	this.port   = port;
	this.logger = logger;
	this.nextport = nextport;
	this.nextport2 = nextport2;
	this.nextport3 = nextport3;
	this.nextport4 = nextport4;
	this.nextport5 = nextport5;
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
				new Thread(new Connection(clientAddress, client, logger, nextport, nextport2, nextport3, nextport4, nextport5)).start();
			}catch(Exception e) {
				e.printStackTrace();
			}    
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}

// Recebe as mensagens dos peers mete-as numa queue e depois trata-as metendo-as numa lista final
class Connection implements Runnable {
    String clientAddress;
    Socket clientSocket;
    Logger logger;
	int    nextport;
	int    nextport2;
	int    nextport3;
	int    nextport4;
	int    nextport5;
	static final int SAMPLES = 10;
	static Queue<String> queue = new LinkedList<>();
	

    public Connection(String clientAddress, Socket clientSocket, Logger logger, int nextport, int nextport2, int nextport3, int nextport4, int nextport5) {
	this.clientAddress = clientAddress;
	this.clientSocket  = clientSocket;
	this.logger        = logger;
	this.nextport      = nextport;
	this.nextport2     = nextport2;
	this.nextport3     = nextport3;
	this.nextport4     = nextport4;
	this.nextport5     = nextport5;
    }

    @Override
    public void run() {
		/*
		* prepare socket I/O channels
		*/
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);   
		
			while (true) {
				String command;
				command = in.readLine();
				String lc = in.readLine();

				logger.info("server: message from host " + clientAddress + "[command = " + command+"]");
				System.out.println(command);

				// Adicionar à queue
				queue.add(command);
				Client.LamportClock = Math.max(Client.LamportClock,Integer.parseInt(lc)); 
				
				System.out.println(queue);

				// ver se na queue tem pelo menos 1 mensagem de cada peer, se sim fazer a análise dele pelo timestamp, caso timestamp seja igual ver pelo port
				int count1 = 0;
				int count2 = 0;
				int count3 = 0;
				int count4 = 0;
				int count5 = 0;
				
				for (String element : queue) {
					if (element.contains(Integer.toString(nextport))) {
						count1++;
					}
					if (element.contains(Integer.toString(nextport2))){
						count2++;
					}
					if (element.contains(Integer.toString(nextport3))){
						count3++;
					}
					if (element.contains(Integer.toString(nextport4))){
						count4++;
					}
					if (element.contains(Integer.toString(nextport5))){
						count5++;
					}
				}

				// se a queue tiver pelo menos 1 mensagem de cada peer
				if(count1 >= 1 && count2 >= 1 && count3 >= 1 && count4 >= 1 && count5 >= 1 ){
					// Guardar mensagens
        			List<String> mensagens = new ArrayList<>();
					List<String> copyOfQueue = new CopyOnWriteArrayList<>(queue); // CopyOnWriteArrayList para evitar ConcurrentModificationException
					for (String input : copyOfQueue) { 
						String[] parts = input.split(":");
						mensagens.add(parts[1]);
						Client.LamportClock++;
						//queue.remove(input);	
					}
					System.out.println(mensagens);
					
				}
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}

// Cliente envia de 1 em 1 segundo uma mensagem para todos os peers
class Client implements Runnable {
    String  host;
    Logger  logger;
	int port;
	int    nextport;
	int    nextport2;
	int    nextport3;
	int    nextport4;
	int    nextport5;
	static final int SAMPLES = 100;
	static List<String> palavrasAleatorias =new ArrayList<String>();
	static int LamportClock = 0;
    
    public Client(String host, Logger logger, int port, int nextport, int nextport2, int nextport3, int nextport4, int nextport5) throws Exception {
	this.host       = host;
	this.logger     = logger; 
	this.port       = port;
	this.nextport   = nextport;
	this.nextport2  = nextport2;
	this.nextport3  = nextport3;
	this.nextport4  = nextport4;
	this.nextport5  = nextport5;
    }

    @Override 
    public void run() {
		try {
			logger.info("client: endpoint running ...\n");

			String filePath = "words.txt";
				// Lista para armazenar as palavras do arquivo
				List<String> lista = new ArrayList<>();
				try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
					String linha;
					while ((linha = br.readLine()) != null) {
						lista.add(linha.trim()); // Adiciona cada linha do arquivo como uma palavra
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			while (true) {			
				try {

					/* 
					* make connection
					*/
					Socket socket  = new Socket(InetAddress.getByName("localhost"), nextport);
					logger.info("client: connected to server " + socket.getInetAddress() + "[port = " + socket.getPort() + "]");
					Socket socket2  = new Socket(InetAddress.getByName("localhost"), nextport2);
					logger.info("client: connected to server " + socket2.getInetAddress() + "[port = " + socket2.getPort() + "]");
					Socket socket3  = new Socket(InetAddress.getByName("localhost"), nextport3);
					logger.info("client: connected to server " + socket3.getInetAddress() + "[port = " + socket3.getPort() + "]");
					Socket socket4  = new Socket(InetAddress.getByName("localhost"), nextport4);
					logger.info("client: connected to server " + socket4.getInetAddress() + "[port = " + socket4.getPort() + "]");
					Socket socket5  = new Socket(InetAddress.getByName("localhost"), nextport5);
					logger.info("client: connected to server " + socket5.getInetAddress() + "[port = " + socket5.getPort() + "]");
					/*
					* prepare socket I/O channels
					*/
					PrintWriter  out = new PrintWriter(socket.getOutputStream(), true);
				  
					PrintWriter  out2 = new PrintWriter(socket2.getOutputStream(), true);
					
					PrintWriter  out3 = new PrintWriter(socket3.getOutputStream(), true);
					
					PrintWriter  out4 = new PrintWriter(socket4.getOutputStream(), true);
					
					PrintWriter  out5 = new PrintWriter(socket5.getOutputStream(), true);
					
					PoissonProcess pp = new PoissonProcess(60, new Random(0));
					for (int i = 1; i <= SAMPLES; i++) {
						double t = pp.timeForNextEvent() * 60.0 * 1000.0;
						System.out.println("next event in -> " + (int)t + " ms");

						Random rand = new Random();
						int indiceAleatorio = rand.nextInt(lista.size());
						String m = lista.get(indiceAleatorio);
						//System.out.println("2: "+ m);
						/*
						* send message*/
						LamportClock++;
						m = LamportClock + ":" + m + ":" + port;
						out.println(m);
						out.println(LamportClock);
						
						out2.println(m);
						out2.println(LamportClock);

						out3.println(m);
						out3.println(LamportClock);

						out4.println(m);
						out4.println(LamportClock);

						out5.println(m);
						out5.println(LamportClock);

						out.flush();
						out2.flush();
						out3.flush();
						out4.flush();
						out5.flush();
						try {
								Thread.sleep((int)t);
						}catch (InterruptedException e) {
								System.out.println("thread interrupted");
								e.printStackTrace(System.out);
						}
					} 	    
					/*
					* close connection
					*/
					socket.close();
				} catch(Exception e) {
					e.printStackTrace();
				}   
			}
		}catch(Exception e) {
			e.printStackTrace();
		} 
	} 
}
