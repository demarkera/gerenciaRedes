package s;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Manager {

	/* Parametros do trabalho */
	private String SERVERIP = "127.0.0.1";
	private int SERVERPORTSEND = 54654;
	private int SERVERPORTRECEIVE = 54655;
	private int TOTAL_PACKETS = 1000;

	BufferedWriter output;
	private FileWriter outputfstream;

	public Manager() {
		try {
			System.out.println("initializing Manager");
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			DatagramSocket socket = new DatagramSocket();
			
			
			//aqui é colocar a mensagem que quiser
			
			String buf = "100000001100.1.3.6.1.4.1.1264897646.1                        1    ";
			//                        [          endereço do objeto(50 caracs         ]valor  
			//nao consegui colocar a mensagem acima no envio... vai outra coisa na da a ver..
			
			DatagramPacket packet;
			/* Creates UDP packet with data and destination */
			packet = new DatagramPacket(buf.getBytes(), buf.length(), serverAddr, SERVERPORTSEND);
			System.out.println("sending data");
			sendData(socket, buf.getBytes(), packet);
			/* All messages sent, close the socket */
			socket.close();
			System.out.println(String.format("Done!"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendData(DatagramSocket socket, byte[] buf, DatagramPacket packet) throws IOException, InterruptedException {
		// aqui vem a lógica de enviar dados snmp
		for (int i = 0; i < TOTAL_PACKETS; i++) {
			System.arraycopy(ByteBuffer.allocate(4).putInt(i).array(), 0, buf, 0, 4);
			socket.send(packet);
			Thread.sleep(1, 0); // 10 milliseconds
			if (i % 100 == 0) {
				System.out.println("[C]: Packets sent: i= " + i);
			}
		}
	}

}
