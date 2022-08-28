package com.company;

import java.util.Scanner;

public class ClientProgram {

    public static void main(String[] args) {
        try (Client client = new Client(Settings.HOST, Settings.PORT, message -> {
            System.out.println(message);
        })) {
            System.out.println("Client connected. Type 'exit' to close");
            try (Scanner scanner = new Scanner(System.in)){
                while (true) {
                    String command = scanner.nextLine();
                    if (command == null || "exit".equals(command)) {
                        client.close();
                        break;
                    } else {
                        client.send(command);
                    }
                }
            }
        } catch (Exception ignored) {

        } finally {
            System.exit(0);
        }
    }
}