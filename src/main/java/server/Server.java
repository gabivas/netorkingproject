package server;

import utils.ClientRequest;
import utils.ClientResponse;
import utils.Constants;
import utils.Processor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Server implements AutoCloseable {

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private Map<Socket, String> logins;
    private Integer generatedNumber;

    public void start(int port) throws UnknownHostException, IOException {
        stop();
        serverSocket = new ServerSocket(port, 10, InetAddress.getLocalHost());
        executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
        generatedNumber = ThreadLocalRandom.current().nextInt(1000, 9999);
        System.out.println("First generated number " + generatedNumber);
        logins = new HashMap<Socket, String>();
        ServerWorker serverWorker = new ServerWorker();
        executorService.submit(serverWorker);
    }

    public void stop() throws IOException {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            serverSocket = null;
        }
        if (generatedNumber != null) {
            generatedNumber = null;
        }
    }

    private class ServerWorker implements Runnable {

        @Override
        public void run() {
            while (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientWorker clientWorker = new ClientWorker(clientSocket);
                    executorService.submit(clientWorker);
                } catch (IOException e) {
                }
            }

        }

    }

    private class ClientWorker implements Runnable {

        private Socket clientSocket;

        public ClientWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            while (clientSocket != null && !clientSocket.isClosed()) {
                try {
                    if (clientSocket.getInputStream().available() > 0) {
                        ClientRequest clientRequest = Processor.readClient(clientSocket);
                        if (clientRequest.isLogin()) {
                            handleLogin(clientSocket, clientRequest);
                        } else if (clientRequest.isTry()) {
                            handleTry(clientSocket, clientRequest);
                        } else if (clientRequest.isLogout()) {
                            handleLogout(clientSocket);
                        }
                    }

                } catch (Exception e) {
                    continue;
                }
            }
        }
    }



        private boolean isLogged(Socket socket) {
            return logins.containsKey(socket)
                    && logins.get(socket) != null;
        }

        private void handleLogin(Socket socket, ClientRequest clientRequest) throws IOException {
            synchronized (logins) {
                if (isLogged(socket)) {
                    return;
                }
                if (logins.values().contains(clientRequest.getName())) {
                    Processor.writeClient(new ClientResponse(Constants.USER_EXISTS,Constants.USER_EXISTS_MESSAGE),socket);
                    return;
                }
                logins.put(socket, clientRequest.getName());
                Processor.writeClient(new ClientResponse(
                        Constants.SUCCESSFULLY_LOG_IN,new StringBuilder().append(Constants.SUCCESSFULLY_LOG_IN_MESSAGE).append(clientRequest.getName()).toString()),
                        socket);

            }
        }

        private void handleTry(Socket socket, ClientRequest clientRequest) throws IOException {
            synchronized (logins) {
                if (!isLogged(socket)) {
                    return;
                }
                try {
                    Integer receivedNumber = Integer.parseInt(clientRequest.getMessage());
                    if (receivedNumber >= 1000 && receivedNumber < 10000) {
                        synchronized (generatedNumber) {
                            if (receivedNumber.equals(generatedNumber)) {
                                for (Map.Entry<Socket, String> login : logins.entrySet()) {
                                    Processor.writeClient(new ClientResponse(Constants.NUMBER_FOUND, new StringBuilder().append(Constants.NUMBER_FOUND_MESSAGE).append(clientRequest.getName()).toString()), login.getKey());
                                }
                                generatedNumber = ThreadLocalRandom.current().nextInt(1000, 9999);
                                System.out.println("Noul numar generat este " + generatedNumber);
                            }
                            else {
                                ClientResponse clientResponse = Processor.getTryResponse(receivedNumber,generatedNumber);
                                Processor.writeClient(clientResponse,socket);
                            }
                        }
                    }
                }
                catch(NumberFormatException e) {
                    Processor.writeClient(new ClientResponse(Constants.INVALID_NUMBER,Constants.INVALID_NUMBER_MESSAGE), socket);
                }

            }
        }

        private void handleLogout(Socket socket) throws IOException {
            if (!isLogged(socket)) {
                return;
            }
            logins.remove(socket);
            Processor.writeClient(new ClientResponse(Constants.SUCCESSFULLY_LOG_OUT,Constants.SUCCESSFULLY_LOG_OUT_MESSAGE), socket);
        }



    @Override
    public void close() throws Exception {
        stop();
    }
}
