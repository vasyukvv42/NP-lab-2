package edu.kpi;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        var socket = new Socket(args[0], Integer.parseInt(args[1]));
        var inputStream = new DataInputStream(socket.getInputStream());
        var outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF(args[2]);

        Runnable loop = () -> {
            while (!socket.isClosed()) {
                try {
                    var message = inputStream.readUTF();
                    System.out.print(message);
                } catch (IOException e) {
                    System.out.println("Exiting...");;
                }
            }
        };
        new Thread(loop).start();

        var reader = new BufferedReader(new InputStreamReader(System.in));
        var message = reader.readLine();
        while (!message.equals("!exit")) {
            outputStream.writeUTF(message);
            message = reader.readLine();
        }
        socket.close();
    }
}
