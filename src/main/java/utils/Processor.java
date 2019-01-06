package utils;

import java.io.*;
import java.net.Socket;

public class Processor {

    public static ClientRequest readClient(Socket socket) throws IOException, ClassNotFoundException {

        ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
        ClientRequest clientRequest = (ClientRequest) stream.readObject();
        System.out.println("S-a primit " + clientRequest);
        return clientRequest;
    }

    public static void writeClient(ClientResponse clientResponse, Socket socket) throws IOException {
        System.out.println("S-a raspuns cu " + clientResponse);
        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
        stream.writeObject(clientResponse);
        stream.flush();
    }


    public static ClientResponse getTryResponse(Integer receivedNumber, Integer generatedNumber) {
        int correctDigits=0;
        int receivedNumberTemp = receivedNumber.intValue();
        int generatedNumberTemp = generatedNumber.intValue();
        while(receivedNumberTemp!=0) {
            if(receivedNumberTemp%10==generatedNumberTemp%10) {
                correctDigits++;
            }

            receivedNumberTemp/=10;
            generatedNumberTemp/=10;
        }
        return new ClientResponse(Constants.TRY_RESPONSE,"In numarul ales, " + correctDigits + " sunt centrare si " + (4-correctDigits) + " sunt necentrate");
    }

    public static void writeServer(ClientRequest clientRequest, Socket socket) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
        stream.writeObject(clientRequest);
        stream.flush();
    }

    public static ClientResponse readServer(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
        ClientResponse clientResponse = (ClientResponse) stream.readObject();
        return clientResponse;
    }
}
