package client;

import server.Server;
import utils.Constants;

import java.util.Scanner;

public class ClientStart {

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connect("192.168.43.227",8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
