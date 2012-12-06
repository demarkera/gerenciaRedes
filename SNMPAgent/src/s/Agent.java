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
	public static final String SERVERIP = "localhost";
	public static final int SERVERPORTRECEIVE = 54654;
	public static final int SERVERPORTSEND = 54655;
	public static final int TOTAL_PACKETS = 1;
	private static ArrayList<GeladeiraModel> listaGeladeiras;

	public static void main(String[] args) {
		// startLogger();
		init();

		try {
			System.out.println(String.format("Agent: Agente sendo iniciado"));
			/* Search server by IP address */
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			System.out.println(String.format("Agent: Got serverAddr %s. Connecting to it...", serverAddr.toString()));

			receiveData(serverAddr);
			
		} catch (Exception e) {
			System.out.println(String.format("UDP: [S]: Error", e));
		}
	}

	private static void init() {
		listaGeladeiras = new ArrayList<>();
		listaGeladeiras = FirstPopulator.populate();
	}

	private static void receiveData(InetAddress serverAddr) throws SocketException, IOException {
		// aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTRECEIVE, serverAddr);
		/* Define maximum length of UDP packets */
		byte[] buf = new byte[1472];
		/* Prepare the UDP packet for received data */
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		do {
			int receivedPacketsCounter = 1;
			BitSet receivedPackets = new BitSet(TOTAL_PACKETS);
			long ini_time = 0, fin_time;
			socket.setSoTimeout(0);
			System.out.println("");
			System.out.println(String.format("Agent: Entering loop to receive packets..."));
			byte[] data = null;
			String dataFull = null;
			int i = 0;
			try {
				socket.receive(packet); /* Receive the first packet */
				System.out.println("Agent: [TRANSMISSION]: Starting Transmission");
				socket.setSoTimeout(10000); /* Set a timeout for socket */
				data = packet.getData(); /*
										  * Get the packet data, to analyze first 4
										  * bytes
										  */
				dataFull = dataFull+data;

//				analyzeDataFromManager(data);
				ini_time = System.nanoTime();
				i++;
				System.out.println("Agent: [TRANSMISSION]: Packet ["+i+"] Data:"+ data.toString());
				do {

					/* Receive next packets */
					socket.receive(packet);
					i++;
					System.out.println("Agent: [TRANSMISSION]: Packet ["+i+"] Data:"+ data.toString());
					data = packet.getData();
					dataFull = dataFull+data;
				} while (true);
				
			} catch (SocketTimeoutException e) {
				fin_time = System.nanoTime();
				System.out.println(String.format("Agent: [TRANSMISSION]: Total elapsed time %d ns", fin_time - ini_time));
				System.out.println("Agent: [TRANSMISSION]: Datagram info: "+dataFull);
				System.out.println("Agent: [TRANSMISSION]: ENDED");
			}
		} while (true);
	}


	private static void analyzeDataFromManager(byte[] data) {
		//TODO aqui deve-se analisar o que veio do datagrama snmp do gerente
		String pdu = getPDUType(data);
		String dados = getPDUData(data);
		SNMPPDUType tipoPDU = SNMPPDUType.valueOf(pdu);
		//TODO ainda necessário trabalhar essa porra aqui para pegar a PDU direito

		switch (tipoPDU) {
		case GETREQUEST:
			getRequest(dados);
			break;

		case GETNEXTREQUEST:
			getNextRequest(dados);
			break;

		case GETRESPONSE:
			getResponse(dados);
			break;

		case SETREQUEST:
			setRequest(dados);
			break;

		case TRAP:
			trap(dados);
			break;

		}

	}

	private static String getPDUType(byte[] data) {
		String tipoPDU = null; // TODO aqui tem que analisar o dado que veio do
							   // manager, e pegar qual o tipo de PDU que
							   // estamos tratando
		return tipoPDU;
	}

	private static String getPDUData(byte[] data) {
		// TODO analisar Datagrama e buscar os dados da PDU
		return null;
	}

	private static CampoValor getFieldAndValue(String dados) {
		// TODO implementar lógica de buscar o campo analisando os
		// 1.2.4.56654.4.52421.24 da vida
		CampoValor campoValor = new CampoValor();
		if(campoValor.getField() == null)
		{
			
		}

		return null;
	}

	private static void getRequest(String dados) {
		CampoValor campoValor = getFieldAndValue(dados);
		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);
	}

	private static void getNextRequest(String dados) {
		CampoValor campoValor = getFieldAndValue(dados);
		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);

	}

	private static void getResponse(String dados) {
		CampoValor campoValor = getFieldAndValue(dados);
		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);

	}

	private static void setRequest(String dados) {
		CampoValor campoValor = getFieldAndValue(dados);
		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);

	}

	private static void trap(String dados) {
		TrapType trapType = getTrapType(dados);
		sendToManager(SNMPPDUType.TRAP, trapType);

	}

	private static String searchField(byte[] data) {
		String campo = data.toString(); // TODO aqui pegar o campo que está sendo
										// requerido qlqr coisa
										// (1.2.3.4.56.56.89.78964.56.1)
		return null;
	}
	
	private static TrapType getTrapType(String dados) {
		// TODO aqui se pega o tipo de trap que foi enviado
		return null;
	}


	private static void sendToManager(SNMPPDUType trap, TrapType trapType) {
		// TODO fazer a resposta pro manager de traps
		// sendData();
		
	}

	
	private static void sendToManager(SNMPPDUType getresponse, CampoValor campoValor) {
		// TODO aqui começa a lógica de enviar dados para o gerente
		// sendData();
	}

	private static void sendData(InetAddress serverAddr) throws SocketException, IOException {
		// aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTSEND, serverAddr);
		// establishSend(socket); //implementar o envio de dados para o Manager
	}

}
