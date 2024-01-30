import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Myclass {

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
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            StringBuilder jsonArray = new StringBuilder("[\n");

            while (dataInputStream.available() > 0) {
                byte[] symbolBytes = new byte[4];
                dataInputStream.readFully(symbolBytes);
                String symbol = new String(symbolBytes, "ISO-8859-1");

                byte buySellIndicator = dataInputStream.readByte();
                char buySellIndicatorChar = (char) buySellIndicator;

                int quantity = dataInputStream.readInt();
                int price = dataInputStream.readInt();
                int packetSequence = dataInputStream.readInt();

                String jsonObject = String.format(
                        "  {\n    \"Symbol\": \"%s\",\n    \"BuySellIndicator\": \"%s\",\n    \"Quantity\": %d,\n    \"Price\": %d,\n    \"PacketSequence\": %d\n  }",
                        symbol, buySellIndicatorChar, quantity, price, packetSequence);

                jsonArray.append(jsonObject);

                if (dataInputStream.available() > 0) {
                    jsonArray.append(",\n");
                }
            }

            jsonArray.append("\n]");

            System.out.println(jsonArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
