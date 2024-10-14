import java.io.*;
import java.net.*;

public class FileServerTCP {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                new FileHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class FileHandler extends Thread {
    private Socket socket;

    public FileHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (InputStream input = socket.getInputStream();
             DataInputStream dis = new DataInputStream(input)) {
            // Read the file name
            String fileName = dis.readUTF();
            File file = new File("server_storage/" + fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;

                // Read file from client
                while ((bytesRead = dis.read(buffer)) > 0) {
                    fos.write(buffer, 0, bytesRead);
                }

                System.out.println("File " + fileName + " received");
            }

            // Send confirmation to client
            try (OutputStream output = socket.getOutputStream();
                 DataOutputStream dos = new DataOutputStream(output)) {
                dos.writeUTF("File " + fileName + " successfully received.");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
