package com.example.discord2server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncomingConnectionsHandler extends Thread {

    private final int port;

    public List<ClientConnectionHandler> clientThreads = new ArrayList<>();
    public List<PrintWriter> outWriters = new ArrayList<>();

    public IncomingConnectionsHandler(int port){
        this.port = port;
    }

    //please ignore the try catch gore.
    @Override
    public void run() {
        System.out.println("IncomingConnectionsHandler " + this + " started on port " + port);
        //IMPORTANT: this limits the amount of people that can join the server
        ExecutorService pool = Executors.newFixedThreadPool(500);

        try (ServerSocket listener = new ServerSocket(port)) {
            while (!(listener.isClosed() || currentThread().isInterrupted())) {
                try {
                    pool.execute(new ClientConnectionHandler(listener.accept(), this));
                } catch (IOException e) {
                    Globals.ShowErrorBoxNonBlocking("Client failed to connect.", "An error occurred while" +
                            "trying to set up a TCP connection with a client.", "This is most likely the client's" +
                            "issue.");
                }
            }
        } catch (IOException e) {
            Globals.ShowErrorBoxNonBlocking("Server error", "The program failed to set up the ServerSocket",
                    "Check your firewall and internet connection.");
        }
    }

    @Override
    public void interrupt() {
        System.out.println("Closing IncomingConnectionsHandler " + this);
        //tell all the individual client connection handler threads to shut down
        for(var thread : clientThreads ){
            thread.interrupt();
        }
        //shut ourselves down
        super.interrupt();
    }

    public synchronized void Broadcast(String message) {
        for(PrintWriter out : outWriters) {
            out.println(message);
        }
    }

    public synchronized void AddOutWriter(ClientConnectionHandler clientThread) {
        clientThreads.add(clientThread);
        outWriters.add(clientThread.out);
    }

}
