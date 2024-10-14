import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ChatClientUDP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9876;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
            Scanner scanner = new Scanner(System.in);

            // Thread to listen for messages from the server
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (true) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        String message = new String(packet.getData(), 0, packet.getLength());
                        System.out.println("Received message: " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Sending messages to the server
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
                socket.send(packet);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
