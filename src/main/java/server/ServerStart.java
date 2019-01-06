package server;

import utils.Constants;

import java.util.Scanner;

public class ServerStart {

    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start(Constants.SERVER_PORT);
            System.out.println(Constants.START_SERVER_MESSAGE);
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    String command = scanner.nextLine();
                    if (command == null || "exit".equalsIgnoreCase(command.trim())) {
                        break;
                    }
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
