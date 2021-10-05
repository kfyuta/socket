package com.company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public void listen() {
        try {
            ServerSocket sSocket = new ServerSocket();
            int port = 3000;
            sSocket.bind(new InetSocketAddress(port));
            Socket socket = sSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
