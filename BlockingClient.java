import java.io.*;
import java.net.*;

public class BlockingClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server");

            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println("Client: " + userInput);
                System.out.println("Server response: " + in.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}