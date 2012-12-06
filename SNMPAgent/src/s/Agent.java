package s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Agent {
	private String SERVERIP = "localhost";
	private int SERVERPORTRECEIVE = 54654;
	private int SERVERPORTSEND = 54655;
	private int TOTAL_PACKETS = 1;
	private ArrayList<GeladeiraModel> listaGeladeiras;

	public Agent() {
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

	private void init() {
		listaGeladeiras = new ArrayList<>();
		listaGeladeiras = FirstPopulator.populate();
	}

	private void receiveData(InetAddress serverAddr) throws SocketException, IOException {
		// aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTRECEIVE, serverAddr);
		/* Define maximum length of UDP packets */
		String buf = "";
		/* Prepare the UDP packet for received data */
		DatagramPacket packet = new DatagramPacket(buf.getBytes(), buf.length());
		do {
			long ini_time = 0, fin_time;
			socket.setSoTimeout(0);
			System.out.println("");
			System.out.println(String.format("Agent: Entering loop to receive packets..."));
			String data = null;
			String dataFull = null;
			int i = 0;
			try {
				socket.receive(packet); /* Receive the first packet */
				System.out.println("Agent: [TRANSMISSION]: Starting Transmission");
				socket.setSoTimeout(10000); /* Set a timeout for socket */
				data = packet.getData().toString(); /*
										 * Get the packet data, to analyze first
										 * 4 bytes
										 */
				dataFull = dataFull + data;

				// analyzeDataFromManager(data);
				ini_time = System.nanoTime();
				i++;
				System.out.println("Agent: [TRANSMISSION]: Packet [" + i + "] Data:" + data);
				do {

					/* Receive next packets */
					socket.receive(packet);
					i++;
					data = packet.getData().toString();
					System.out.println("Agent: [TRANSMISSION]: Packet [" + i + "] Data:" + data);
					dataFull = dataFull + data;
				} while (true);

			} catch (SocketTimeoutException e) {
				fin_time = System.nanoTime();
				System.out.println(String.format("Agent: [TRANSMISSION]: Total elapsed time %d ns", fin_time - ini_time));
				System.out.println("Agent: [TRANSMISSION]: Datagram info: " + dataFull);
				System.out.println("Agent: [TRANSMISSION]: ENDED");
			}
		} while (true);
	}

	private void analyzeDataFromManager(byte[] data) {
		// TODO aqui deve-se analisar o que veio do datagrama snmp do gerente
		SNMPPDU pduRecebida = new SNMPPDU();
		// esta linha popula a classe com todas as informações que vieram do
		// Manager
		pduRecebida.parse(data);

		for (CampoValor campoValor : pduRecebida.getCamposRequisidatos()) {

			switch (pduRecebida.getPduType()) {
				case GETREQUEST:
					getRequest(campoValor);
					break;

				case GETNEXTREQUEST:
					// getNextRequest(campoValor);
					break;

				case GETRESPONSE:
					// getResponse(campoValor);
					break;

				case SETREQUEST:
					setRequest(campoValor);
					break;

				case TRAP:
					trap(campoValor);
					break;
			}
		}
	}

	private void getRequest(CampoValor campoValor) {
		String field = clearOID(campoValor.getField());
		GeladeiraModel geladeira = listaGeladeiras.get(0);
		// aqui testamos se nao é gaveta
		if (field.contains(".7")) {
			int numeroGaveta = pegaNumeroGaveta(field);
			String itemGaveta = field.substring(4, 5);
			if (geladeira.getGaveta().get(numeroGaveta) != null) {
				switch (itemGaveta) {
					case ".1":
						if (geladeira.getGaveta().get(numeroGaveta) != null)
							campoValor.setValue(geladeira.getGaveta().get(numeroGaveta).getCervejas() + "");
						else
							campoValor.setValue("");
						break;
					case ".2":
						if (geladeira.getGaveta().get(numeroGaveta) != null)
							campoValor.setValue(geladeira.getGaveta().get(numeroGaveta).getLeite() + "");
						else
							campoValor.setValue("");
						break;
					case ".3":
						if (geladeira.getGaveta().get(numeroGaveta) != null)
							campoValor.setValue(geladeira.getGaveta().get(numeroGaveta).getOvos() + "");
						else
							campoValor.setValue("");
						break;
					case ".4":
						if (geladeira.getGaveta().get(numeroGaveta) != null)
							campoValor.setValue(geladeira.getGaveta().get(numeroGaveta).getIndexGaveta() + "");
						else
							campoValor.setValue("");
						break;
					default:
						campoValor.setValue("NoSuchIndexFromTable");
						break;
				}
			}

		} else {
			String objeto = field.substring(0, 2);
			switch (objeto) {
				case ".1":
					if (geladeira.isTurnedOn())
						campoValor.setValue("1");
					else
						campoValor.setValue("0");
					break;
				case ".2":
					if (geladeira.isLampadaOn())
						campoValor.setValue("1");
					else
						campoValor.setValue("0");
					break;
				case ".3":
					campoValor.setValue(geladeira.getTermostato().ordinal() + "");
					break;
				case ".4":
					if (geladeira.isPortaAberta())
						campoValor.setValue("1");
					else
						campoValor.setValue("0");
					break;
				case ".5":
					campoValor.setValue(geladeira.getDefrostDays() + "");
					break;
				case ".6":
					if (geladeira.isFastFreezing())
						campoValor.setValue("1");
					else
						campoValor.setValue("0");
					break;
				default:
					campoValor.setValue("noSuchName");
					break;
			}
		}

		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);
	}

	// private void getNextRequest(CampoValor campoValor) {
	// sendToManager(SNMPPDUType.GETRESPONSE, campoValor);
	//
	// }

	// private void getResponse(CampoValor campoValor) {
	// sendToManager(SNMPPDUType.GETRESPONSE, campoValor);
	//
	// }

	private void setRequest(CampoValor campoValor) {
		sendToManager(SNMPPDUType.GETRESPONSE, campoValor);
		String field = clearOID(campoValor.getField());
		GeladeiraModel geladeira = listaGeladeiras.get(0);
		// aqui testamos se nao é gaveta
		if (field.contains(".7")) {
			int numeroGaveta = pegaNumeroGaveta(field);
			String itemGaveta = field.substring(4, 5);
			if (geladeira.getGaveta().get(numeroGaveta) != null) {
				switch (itemGaveta) {
					case ".1":
						geladeira.getGaveta().get(numeroGaveta).setCervejas(Integer.parseInt(campoValor.getValue()));
						break;
					case ".2":
						geladeira.getGaveta().get(numeroGaveta).setLeite(Integer.parseInt(campoValor.getValue()));
						break;
					case ".3":
						geladeira.getGaveta().get(numeroGaveta).setOvos(Integer.parseInt(campoValor.getValue()));
					case ".4":
						geladeira.getGaveta().get(numeroGaveta).setIndexGaveta(Integer.parseInt(campoValor.getValue()));
					default:
						campoValor.setValue("NoSuchIndexFromTable");
						break;
				}
			}

		} else {
			String objeto = field.substring(0, 2);
			switch (objeto) {
				case ".1":
					if (campoValor.getValue().equals(1))
						geladeira.setTurnedOn(true);
					else
						geladeira.setTurnedOn(false);
					break;
				case ".2":
					if (campoValor.getValue().equals(1))
						geladeira.setLampadaOn(true);
					else
						geladeira.setLampadaOn(false);
					break;
				case ".3":
					geladeira.setTermostato(TermostatoEnum.indexOf(campoValor.getValue()));
					break;
				case ".4":
					if (campoValor.getValue().equals(1))
						geladeira.setPortaAberta(true);
					else
						geladeira.setPortaAberta(false);
					break;
				case ".5":
					geladeira.setDefrostDays(Integer.parseInt(campoValor.getValue()));
					break;
				case ".6":
					if (campoValor.getValue().equals(1))
						geladeira.setFastFreezing(true);
					else
						geladeira.setFastFreezing(false);
					break;
				default:
					campoValor.setValue("noSuchName");
					break;
			}
		}
	}

	private void trap(CampoValor campoValor) {
		// TrapType trapType = getTrapType(campoValor);
		// sendToManager(SNMPPDUType.TRAP, trapType);

	}

	private String clearOID(String field) {
		if (field.length() != 50) {
			System.out.println("erro no processamento do field do campoValor!!!!! diferente de 50!");
			return "";
		} else {
			// tiramos o que veio de caminho da nossa MIB
			// (".1.3.6.1.4.1.1264897646")
			String campo = field.replace(ObjectsMib.getReference(ObjectsMib.DEFAULTOID), "");
			return campo;
		}
	}

	private int pegaNumeroGaveta(String field) {
		// aqui é pra vir qualquer coisa tipo .7.X.1", logo, o 7 temos que
		// tirar, o X é o índice da gaveta,
		// e pegamos o campo da gaveta no último
		String campo = field.substring(1);// tira primeiro .
		int indexGaveta = 0;
		if (campo.lastIndexOf(".") != campo.indexOf(".", 0)) {
			if (campo.length() == 5) {
				indexGaveta = Integer.parseInt((String) campo.subSequence(2, 3));
			}
		}
		return indexGaveta;
	}

	private TrapType getTrapType(String dados) {
		// TODO aqui se pega o tipo de trap que foi enviado
		return null;
	}

	private void sendToManager(SNMPPDUType trap, TrapType trapType) {
		// TODO fazer a resposta pro manager de traps
		// sendData();

	}

	private void sendToManager(SNMPPDUType getresponse, CampoValor campoValor) {
		// TODO aqui começa a lógica de enviar dados para o gerente
		// sendData();
	}

	private void sendData(InetAddress serverAddr) throws SocketException, IOException {
		// aqui monta o socket de receber dados
		DatagramSocket socket = new DatagramSocket(SERVERPORTSEND, serverAddr);
		// establishSend(socket); //implementar o envio de dados para o Manager
	}

}
