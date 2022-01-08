package com.example.discord2server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientConnectionHandler extends Thread
{

    private final Socket socket;
    private final IncomingConnectionsHandler parent;

    //create an object purely to act as an intrinsic lock for the purposes of the out writers statement
    private Object lock;

    public Scanner in;
    public PrintWriter out;

    public ClientConnectionHandler(Socket socket, IncomingConnectionsHandler parent) throws IOException {
        this.socket = socket;
        this.parent = parent;

        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

        //this is a synchronised method so it should prevent concurrency errors.
        parent.AddOutWriter(this);
    }

    @Override
    public void run() {
        System.out.println("Connection opened with " + socket);
        out.println("hello telnet!");
        while (in.hasNextLine() && !currentThread().isInterrupted()) {
            String incoming = in.nextLine();
            if (incoming.equals("say hello to the clients")) {
                parent.Broadcast("hello client!");
            }
        }
        System.out.println("Connection closed");
    }

    //make sure we close the socket before we kill this thread.
    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored){}
        finally {
            super.interrupt();
        }

    }
}
