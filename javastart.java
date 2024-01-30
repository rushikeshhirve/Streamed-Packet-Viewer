import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class javastart {

    public static void main(String[] args) {
        String serverIp = "127.0.0.1"; 
        int serverPort = 3000;

        try (Socket socket = new Socket(serverIp, serverPort)) {

            OutputStream outputStream = socket.getOutputStream();
            byte callType = 1;  
            byte[] payload = createPayload(callType);
            outputStream.write(payload);

            System.out.println("Stream All Packets request sent successfully.");

            readAndDisplayResponse(socket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] createPayload(byte callType) {
        ByteBuffer buffer = ByteBuffer.allocate(2); 
        buffer.put(callType);
        buffer.put((byte) 0); 
        return buffer.array();
    }

    private static void readAndDisplayResponse(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] responseBuffer = new byte[1024];
            int bytesRead = inputStream.read(responseBuffer);

            if (bytesRead != -1) {
                String response = new String(responseBuffer, 0, bytesRead);
                System.out.println("Server Response: " + response);
            } else {
                System.out.println("No response from the server.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
