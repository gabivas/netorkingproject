package client;

import utils.ClientRequest;
import utils.ClientResponse;
import utils.Constants;
import utils.Processor;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private Socket socket;

    public Client() {
    }

    public void connect(String host, int port) throws UnknownHostException, IOException {
        disconnect();
        socket = new Socket(host, port);
        ClientWorker worker = new ClientWorker();
        new Thread(worker).start();
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;
    }

    public void login(String name) throws IOException {
        Processor.writeServer(new ClientRequest(Constants.LOG_IN,name,""), socket);
    }

    public void tryNumber(String name,String guessedNumber) throws IOException {
        Processor.writeServer(new ClientRequest(Constants.TRY,name,guessedNumber), socket);
    }

    public void logout(String name) throws IOException {
        Processor.writeServer(new ClientRequest(Constants.LOG_OUT,name,""), socket);
    }

    private class ClientWorker implements Runnable {

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti numele:");
            String name = scanner.nextLine().trim();
            try {
                login(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (socket != null && !socket.isClosed()) {
                try {
                    if (socket.getInputStream().available() > 0) {
                        ClientResponse clientResponse = Processor.readServer(socket);
                        if (clientResponse.isSuccessfullyLoggedIn()) {
                            System.out.println(clientResponse.getMessage());
                            System.out.println("Introduceti un numar:");
                            String guessedNumber = scanner.nextLine().trim();
                            if(guessedNumber.trim().toLowerCase().equals("logout")) {
                                logout(name);
                            }
                            else {
                                tryNumber(name, guessedNumber);
                            }
                        } else if (clientResponse.isSuccessfullyLoggedOut()) {
                            System.out.println(clientResponse.getMessage());
                            System.exit(0);
                        } else if (clientResponse.isInvalidNumber()) {
                            System.out.println(clientResponse.getMessage());
                            System.out.println("Introduceti un numar:");
                            String guessedNumber = scanner.nextLine().trim();
                            if(guessedNumber.trim().toLowerCase().equals("logout")) {
                                logout(name);
                            }
                            else {
                                tryNumber(name, guessedNumber);
                            }
                        } else if (clientResponse.isTryResponse()) {
                            System.out.println(clientResponse.getMessage());
                            System.out.println("Introduceti un numar:");
                            String guessedNumber = scanner.nextLine().trim();
                            if(guessedNumber.trim().toLowerCase().equals("logout")) {
                                logout(name);
                            }
                            else {
                                tryNumber(name, guessedNumber);
                            }
                        } else if (clientResponse.isNumberFound()) {
                            System.out.println(clientResponse.getMessage());
                            System.out.println("Introduceti un numar:");
                            String guessedNumber = scanner.nextLine().trim();
                            if(guessedNumber.trim().toLowerCase().equals("logout")) {
                                logout(name);
                            }
                            else {
                                tryNumber(name, guessedNumber);
                            }
                        }
                        else if (clientResponse.isUserExits()) {
                            System.out.println(clientResponse.getMessage());
                            System.out.println("Introduceti numele:");
                            name = scanner.nextLine().trim();
                            login(name);
                        }
                    }
                } catch (IOException e) {
                    continue;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}