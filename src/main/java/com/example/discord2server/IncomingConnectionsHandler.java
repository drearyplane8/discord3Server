package com.example.discord2server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IncomingConnectionsHandler extends Thread{

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
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() {
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
