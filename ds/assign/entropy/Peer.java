import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
		// args[0] port, peer pode ter até 5 peers ligados
		new Thread(new Server("localhost", Integer.parseInt(args[0]), peer.logger, nextport1, nextport2, nextport3, nextport4, nextport5)).start();
		new Thread(new Client()).start();
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

// Thread que vai reaizar as operações de push e pull
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
	
		String command;
		command = in.readLine();

	    logger.info("server: message from host " + clientAddress + "[command = " + command+"]");
		System.out.println(command);

		// Criando uma lista para armazenar os ports
        List<Integer> ports = new ArrayList<>();

		ports.add(nextport);
	
		if (nextport2 != 0) {
			ports.add(nextport2);
		}
		if (nextport3 != 0) {
			ports.add(nextport3);
		}
		if (nextport4 != 0) {
			ports.add(nextport4);
		}
		if (nextport5 != 0) {
			ports.add(nextport5);
		}

		for(int port : ports){
			System.out.println(port);
		}

		out.println(Client.palavrasAleatorias);

		if (!command.equals("token")) {
			// comparar lista de palavras com a lista de palavras do peer que enviou a mensagem
			List<String> commandwords = new ArrayList<String>(); 
			for(String j : command.split(",")) {
				commandwords.add(j);
			}

			List<String> copyOfPalavras = new CopyOnWriteArrayList<>(Client.palavrasAleatorias); // CopyOnWriteArrayList para evitar ConcurrentModificationException, lista deste peer
			
			for(String str : commandwords) { // lista de palavras do peer que enviou a mensagem
				if (str.startsWith("[") && str.endsWith("]")) {
					// Remove "[" e "]"
					str = str.replaceAll("^\\[|\\]$", "");
				} else if (str.startsWith("[")) {
					// Remove "[" 
					str = str.substring(1);
				} else if (str.endsWith("]")) {
					// Remove "]"
					str = str.substring(0, str.length() - 1);
				}

				if (copyOfPalavras.contains(str)) {
					//System.out.println(str + " is in the list.");
				} else {
					System.out.println(str + " is not in the list.");
					Client.palavrasAleatorias.add(str);
					//System.out.println("Added to Client.palavrasAleatorias: " + str);
				}
			}
			//System.out.println("333331:"+ Client.palavrasAleatorias);
		}

		// enviar a lista de palavras para um peer aleatório e desse peer aleatorio mandar o lista de palavras para o peer que enviou a mensagem
		PoissonProcess pp = new PoissonProcess(1, new Random(0) );
		for (int i = 1; i <= SAMPLES; i++) {
			double t = pp.timeForNextEvent() * 60.0 * 1000.0;
			System.out.println("1:next event in -> " + (int)t + " ms");

			Random rand = new Random();
			// Gera um inteiro aleatório
			int f = ports.size();
			int n = rand.nextInt(f);
			int port;
			if (ports.size() == 1) {
				port = nextport;
			}else{
				port = ports.get(n);
			}
	
			Socket socket  = new Socket(InetAddress.getByName("localhost"), port);
			logger.info("client: connected to server " + socket.getInetAddress() + "[port = " + socket.getPort() + "]");
			PrintWriter out2 = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out2.println(Client.palavrasAleatorias);
			// Receive the list of words from the random peer
            String peerWords = in2.readLine();
            System.out.println("Received list from random peer: " + peerWords);
			
			// comparar lista de palavras com a lista de palavras do peer que enviou a mensagem
			if (!peerWords.equals(null)) {
			List<String> commandwords = new ArrayList<String>(); 

			for(String j : peerWords.split(",")) {
				commandwords.add(j);
			}

			List<String> copyOfPalavras = new CopyOnWriteArrayList<>(Client.palavrasAleatorias); // CopyOnWriteArrayList para evitar ConcurrentModificationException, lista deste peer

			for(String str : commandwords) { // lista de palavras do peer que enviou a mensagem
				if (str.startsWith("[") && str.endsWith("]")) {
					// Remove "[" e "]"
					str = str.replaceAll("^\\[|\\]$", "");
				} else if (str.startsWith("[")) {
					// Remove "[" 
					str = str.substring(1);
				} else if (str.endsWith("]")) {
					// Remove "]"
					str = str.substring(0, str.length() - 1);
				}

				if (copyOfPalavras.contains(str)) {
					//System.out.println(str + " is in the list.");
				} else {
					//System.out.println(str + " is not in the list.");
					Client.palavrasAleatorias.add(str);
					//System.out.println("Added to Client.palavrasAleatorias: " + str);
				}
			}
			//System.out.println("333332:"+ Client.palavrasAleatorias);
		}
			out2.flush();
			socket.close();

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
	    clientSocket.close();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }
}


// Adicionar palavras aleatórias à lista de palavras
class Client implements Runnable {
	static final int SAMPLES = 100;
	static List<String> palavrasAleatorias =new ArrayList<String>();
    
    public Client() throws Exception {
    }

    @Override 
    public void run() {

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

			PoissonProcess pp = new PoissonProcess(4, new Random(0) );
				for (int i = 1; i <= SAMPLES; i++) {
					double t = pp.timeForNextEvent() * 60.0 * 1000.0;
					System.out.println("2:next event in -> " + (int)t + " ms");

					Random rand = new Random();
				    int indiceAleatorio = rand.nextInt(lista.size());
            		palavrasAleatorias.add(lista.get(indiceAleatorio));

					try {
							Thread.sleep((int)t);
					}catch (InterruptedException e) {
							System.out.println("thread interrupted");
							e.printStackTrace(System.out);
					}
				}
		}  
	}
