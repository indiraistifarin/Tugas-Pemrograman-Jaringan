import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class NonBlockingClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 5000));
        socketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(256);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Connected to the non-blocking server");

        while (true) {
            String userInput = scanner.nextLine();
            buffer.clear();
            buffer.put(("Client: " + userInput).getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            buffer.clear();
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead > 0) {
                String response = new String(buffer.array()).trim();
                System.out.println(response);
            }
        }
    }
}