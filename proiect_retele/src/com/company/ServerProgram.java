package com.company;

import java.util.Scanner;

public class ServerProgram {

    public static void main(String[] args) {
        try (Server server = new Server()) {
            server.start(Settings.PORT);
            System.out.println("Server started. Type 'exit' to close");
            try (Scanner scanner = new Scanner(System.in)){
                while (true) {
                    String command = scanner.nextLine();
                    if (command == null || "exit".equals(command)) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

}