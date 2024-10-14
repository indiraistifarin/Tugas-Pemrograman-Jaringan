import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServerUDP {
    private static final int PORT = 9876;
    private static List<InetSocketAddress> clientAddresses = new ArrayList<>();

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[1024];

            System.out.println("Chat server is running on port " + PORT);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                InetSocketAddress clientAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());

                if (!clientAddresses.contains(clientAddress)) {
                    clientAddresses.add(clientAddress);
                }

                System.out.println("Received message: " + message + " from " + clientAddress);

                // Broadcast message to all clients
                for (InetSocketAddress address : clientAddresses) {
                    if (!address.equals(clientAddress)) {
                        packet = new DatagramPacket(message.getBytes(), message.length(), address.getAddress(), address.getPort());
                        socket.send(packet);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
