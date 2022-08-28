package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.List;
import java.util.Map;

public class Server implements AutoCloseable {

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private final List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());
    private GameTable gameTable;

    /**
     * key: socket.toString()
     * value: client's name
     */
    private Map<String, String> clientsMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * List that contains the paths to input files used for the game
     */
    private List<String> configFiles = Collections.synchronizedList(new ArrayList<>());

    /**
     * Map that holds the names of the connected users and their state of the game
     * Each user has its own gameTable state
     */
    private Map<String, GameTable> onlineClients = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void close() throws Exception {
        stop();
    }

    public void start(int port) throws IOException {
        stop();
        serverSocket = new ServerSocket(port);
        executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());

        configFiles.add("./com/company/input_files/input1.txt");
        configFiles.add("./com/company/input_files/input2.txt");
        configFiles.add("./com/company/input_files/input3.txt");

        this.gameTable = initializeGameTable();

        executorService.execute(() -> {
            while (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    Socket socket = serverSocket.accept();
                    executorService.execute(() -> {
                        try {
                            clients.add(socket);
                            ClientState state = new ClientState();
                            while (socket != null && !socket.isClosed()) {
                                String command = Transport.receive(socket);
                                String response = processCommand(command, state, socket);

                                if (response.contains("WINNER!")) {
                                    String messageForUsers = response.substring(2, response.length());
                                    System.out.println(messageForUsers);

                                    for (Socket currentSocket : clients) {
                                        if (!currentSocket.toString().equals(socket.toString())) {
                                            Transport.send(messageForUsers, currentSocket);
                                        } else {
                                            Transport.send(response, socket);
                                        }
                                    }

                                    this.gameTable = initializeGameTable();

                                    for (Socket currentSocket : clients) {
                                        String clientName = clientsMap.get(currentSocket.toString());
                                        onlineClients.put(clientName, new GameTable(this.gameTable));
                                        Transport.send("New game has been set", currentSocket);
                                    }

                                    System.out.println("New game has been set");
                                } else {
                                    Transport.send(response, socket);
                                }
                            }
                        } catch (Exception ignored) {
                            String clientName = clientsMap.get(socket.toString());
                            if (clientName == null) {
                                System.out.println("non auth client has disconnected from server");
                            } else {
                                System.out.println("client " + clientName +
                                        " has disconnected from the server\n");
                            }
                        } finally  {
                            clients.remove(socket);
                            onlineClients.remove(clientsMap.get(socket.toString()));
                            clientsMap.remove(socket.toString());
                        }
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    private String processCommand(String command, ClientState state, Socket socket) {
        String[] items = command.strip().split("\\s");
        if (state.isAuthenticated) {
            if (items.length == 2) {
                int row;
                int column;

                try {
                    row = Integer.parseInt(items[0]);
                    column = Integer.parseInt(items[1]);
                } catch (NumberFormatException nfe) {
                    return "possible values for rows and columns are from 1 to 10";
                }

                if (row < 1 || row > 10 || column < 1 || column > 10) {
                    return "possible values for rows and columns are from 1 to 10";
                }

                String clientName = clientsMap.get(socket.toString());
                GameTable clientGameTable = onlineClients.get(clientName);
                String shootingResult = clientGameTable.shoot(row, column);

                if (clientGameTable.getPlanesDestroyed() == 3) {
                    return shootingResult + "\n" + clientName + " is the WINNER! Restarting the game..";
                } else {
                    return shootingResult;
                }
            } else {
                return "type \"<line> <column>\" in order to try shooting a plane. 10x10 game table";
            }
        } else {
            if (items[0].equals("auth")) {
                if (items.length == 2) {
                    if (onlineClients.containsKey(items[1])) {
                        return items[1] + " already connected";
                    } else {
                        state.isAuthenticated = true;
                        onlineClients.put(items[1], new GameTable(this.gameTable));
                        clientsMap.put(socket.toString(), items[1]);

                        System.out.println(items[1] + " has connected to the server");
                        return "Welcome " + items[1];
                    }
                } else {
                    return "Please type \"auth <your_name>\" to connect to the server";
                }
            } else {
                return "Please type \"auth <your_name>\" to connect to the server";
            }
        }
    }

    public void stop() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private GameTable initializeGameTable() {
        BufferedReader reader;

        ArrayList<String> gameValues = new ArrayList<>();
        ArrayList<Integer> planeABodyIndexes = new ArrayList<>();
        ArrayList<Integer> planeBBodyIndexes = new ArrayList<>();
        ArrayList<Integer> planeCBodyIndexes = new ArrayList<>();

        int planeAHeadIndex = 0;
        int planeBHeadIndex = 0;
        int planeCHeadIndex = 0;

        int randomNumber = (int) (Math.random() * (configFiles.size()));

        try {
            reader = new BufferedReader(new FileReader(configFiles.get(randomNumber)));

            String line = reader.readLine();
            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    String currentValue = Character.toString(line.charAt(i));
                    gameValues.add(currentValue);

                    if (currentValue.equals("A")) {
                        planeAHeadIndex = gameValues.size() - 1;
                    } else if (currentValue.equals("B")) {
                        planeBHeadIndex = gameValues.size() - 1;
                    } else if (currentValue.equals("C")) {
                        planeCHeadIndex = gameValues.size() - 1;
                    } else if (currentValue.equals("1")) {
                        planeABodyIndexes.add(gameValues.size() - 1);
                    } else if (currentValue.equals("2")) {
                        planeBBodyIndexes.add(gameValues.size() - 1);
                    } else if (currentValue.equals("3")) {
                        planeCBodyIndexes.add(gameValues.size() - 1);
                    }
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameTable gameTable = new GameTable(
                gameValues,
                planeABodyIndexes,
                planeBBodyIndexes,
                planeCBodyIndexes,
                planeAHeadIndex,
                planeBHeadIndex,
                planeCHeadIndex
        );

        return gameTable;
    }
}