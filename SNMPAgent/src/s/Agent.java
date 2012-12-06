package s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.BitSet;

public class Agent {
	public static final String SERVERIP = "127.0.0.1";
	public static final int SERVERPORTRECEIVE = 54654;
	public static final int SERVERPORTSEND = 54655;
	public static final int TOTAL_PACKETS = 1000;
	private static ArrayList<GeladeiraModel> listaGeladeiras;

	public static void main(String[] args) {
		// startLogger();
		init();
		
		try {
			System.out.println(String.format("UDP: [S]: Agente sendo iniciado"));
			/* Search server by IP address */
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			System.out.println(String.format("UDP: [S]: Got serverAddr %s. Connecting to it...", serverAddr.toString()));
			
			receiveData(serverAddr);
			sendData(serverAddr);
		} catch (Exception e) {
			System.out.println(String.format("UDP: [S]: Error", e));
		}
	}

	private static void init(){
		listaGeladeiras = new ArrayList<>();
	}
	
	private static void receiveData(InetAddress serverAddr) throws SocketException, IOException {
		//aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTRECEIVE, serverAddr);
		establishReceive(socket);
	}
	
	private static void establishReceive(DatagramSocket socket) throws SocketException, IOException {
		/* Define maximum length of UDP packets */
		byte[] buf = new byte[1472];
		/* Prepare the UDP packet for received data */
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		do {
			int receivedPacketsCounter = 1;
			BitSet receivedPackets = new BitSet(TOTAL_PACKETS);
			byte[] seqBA;
			long ini_time = 0, fin_time;
			socket.setSoTimeout(0);
			System.out.println(String.format("UDP []: [S]: Entering loop to receive packets..."));
			//aqui efetivamente recebe dados
			receiveData(socket, packet, receivedPacketsCounter, ini_time);
		} while (true);
	}

	private static void receiveData(DatagramSocket socket, DatagramPacket packet, int receivedPacketsCounter, long ini_time) throws IOException,
			SocketException {
		byte[] data;
		long fin_time;
		try {
			socket.receive(packet);		/* Receive the first packet */
			socket.setSoTimeout(10000);	/* Set a timeout for socket */
			data = packet.getData(); 	/* Get the packet data, to analyze first 4 bytes */
			analyzeDataFromManager(data);
			
			
			ini_time = System.nanoTime();
			int i = 0;
			do {
				socket.receive(packet);	/* Receive next packets */
				i++;
				receivedPacketsCounter++;/* Increment counter for received packets */
				data = packet.getData();
				if (i % 100 == 0) {
					System.out.println("[C]: Packets received: i= " + i);
			}
			} while (true);
		} catch (SocketTimeoutException e) {
			fin_time = System.nanoTime();

			System.out.println(String.format("UDP: [S]: Total elapsed time (since 1st packet): %d ns", fin_time - ini_time));

			System.out.println(String.format("UDP: [S]: Total received packets Counter: %d - %f%%", receivedPacketsCounter, 100
					* (double) receivedPacketsCounter / (double) TOTAL_PACKETS));

		}
	}

	private static void analyzeDataFromManager(byte[] data) {
		//aqui deve-se analisar o que veio do datagrama snmp do gerente
		String tipoPDU = data.toString();//ainda necessário trabalhar essa porra aqui para pegar a PDU direito
		
		switch(tipoPDU){
		case "getRequest":
			getRequest(data);
			break;

		case "getNextRequest":
			getNextRequest(data);
			break;

		case "getResponse":
			getResponse(data);
			break;

		case "setRequest":
			setRequest(data);
			break;

		case "Trap":
			trap(data);
			break;

		}
		
	}

	private static void getRequest(byte[] data) {
		String campo = data.toString(); //aqui pegar o campo que está sendo requerido qlqr coisa (1.2.3.4.56.56.89.78964.56.1)
		
	}

	private static void getNextRequest(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	private static void getResponse(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	private static void setRequest(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	private static void trap(byte[] data) {
		// TODO Auto-generated method stub
		
	}


	private static void sendData(InetAddress serverAddr) throws SocketException, IOException {
		//aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTSEND, serverAddr);
//		establishSend(socket); //implementar o envio de dados para o Manager 
	}



}
