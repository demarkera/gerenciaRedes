package s;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Manager{

	/* Parametros do trabalho */
	static String SERVERIP = "127.0.0.1";
	static int SERVERPORTSEND = 54654;
	static int SERVERPORTRECEIVE = 54655;
	static int TOTAL_PACKETS = 1000;

	static BufferedWriter output;
	private static FileWriter outputfstream;

	public static void main(String[] args) {
		try {
			System.out.println("initializing Manager");
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);
			DatagramSocket socket = new DatagramSocket();

			byte[] buf = new byte[1472];
			DatagramPacket packet;
			/* Creates UDP packet with data and destination */
			packet = new DatagramPacket(buf, buf.length, serverAddr, SERVERPORTSEND);
			System.out.println("sending data");
			sendData(socket, buf, packet);
			/* All messages sent, close the socket */
			socket.close();
			System.out.println(String.format("Done!"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendData(DatagramSocket socket, byte[] buf, DatagramPacket packet) throws IOException, InterruptedException {
		//aqui vem a l�gica de enviar  dados snmp
		for (int i = 0; i < TOTAL_PACKETS; i++) {
			System.arraycopy(ByteBuffer.allocate(4).putInt(i).array(), 0, buf, 0, 4);
			socket.send(packet);
			Thread.sleep(1, 0); // 10 milliseconds
			if (i % 100 == 0) {
				System.out.println("[C]: Packets sent: i= "+i);
			}
		}
	}

}