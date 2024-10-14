import java.io.*;
import java.net.*;

public class FileClientTCP {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FileClientTCP <file-path>");
            return;
        }

        String filePath = args[0];

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            File file = new File(filePath);

            // Send file name to the server
            try (OutputStream output = socket.getOutputStream();
                 DataOutputStream dos = new DataOutputStream(output)) {
                dos.writeUTF(file.getName());

                // Send file content to the server
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) > 0) {
                        dos.write(buffer, 0, bytesRead);
                    }
                }

                // Receive confirmation from server
                try (InputStream input = socket.getInputStream();
                     DataInputStream dis = new DataInputStream(input)) {
                    String response = dis.readUTF();
                    System.out.println("Server response: " + response);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
