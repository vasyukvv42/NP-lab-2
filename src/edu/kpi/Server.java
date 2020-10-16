package edu.kpi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class Server {
    public static class Client {
        public Socket socket;
        public DataOutputStream outputStream;
        public DataInputStream inputStream;

        public Client(Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
            this.socket = socket;
            this.outputStream = outputStream;
            this.inputStream = inputStream;
        }

        synchronized public void sendMessage(String message) {
            try {
                outputStream.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
	    var server = new ServerSocket(Integer.parseInt(args[0]));
	    final var clients = Collections.synchronizedList(new ArrayList<Client>());

	    while (!server.isClosed()) {
	        var socket = server.accept();
	        var client = new Client(
                socket,
                new DataOutputStream(socket.getOutputStream()),
                new DataInputStream(socket.getInputStream())
            );
	        clients.add(client);
	        var thread = new ClientThread(client, clients);
	        thread.start();
        }
    }
}
