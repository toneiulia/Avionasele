package com.company;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements AutoCloseable {

    private Socket socket;
    private String name;
    private boolean isConnected = false;

    public interface ClientCallback {
        void onTalk(String message);
    }

    public Client(String host, int port, ClientCallback callback) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        new Thread(()-> {
            while (socket != null && !socket.isClosed()) {
                try {
                    callback.onTalk(Transport.receive(socket));
                } catch (ClassNotFoundException | IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void send(String message) {
        try {
            Transport.send(message, socket);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}