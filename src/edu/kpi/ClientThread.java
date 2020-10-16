package edu.kpi;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

public class ClientThread extends Thread {
    private final Server.Client currentClient;
    private final List<Server.Client> clients;
    private String nickname;

    public ClientThread(Server.Client currentClient, List<Server.Client> clients) {
        this.currentClient = currentClient;
        this.clients = clients;
    }

    private void broadcastMessage(String message) {
        System.out.printf("Broadcasting message: %s", message);
        synchronized (clients) {
            clients.forEach(client -> client.sendMessage(message));
        }
    }

    @Override
    public void run() {
        try {
            while (!currentClient.socket.isClosed()) {
                var message = currentClient.inputStream.readUTF();
                if (message.isEmpty()) {
                    continue;
                }
                if (nickname == null) {
                    nickname = message;
                    broadcastMessage(String.format("%s has connected!%n", nickname));
                    continue;
                }
                broadcastMessage(String.format("%s> %s%n", nickname, message));
            }
        } catch (IOException e) {
            System.out.printf("Lost connection to client %s (%s)%n", nickname, currentClient);
        }

        clients.remove(currentClient);
        if (nickname != null) {
            broadcastMessage(String.format("%s has disconnected!%n", nickname));
        }
    }
}
