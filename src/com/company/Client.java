package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {
    private Socket socket;
    private static Client instance;
    private Client() {}
    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public byte[] execute(byte[] param) {
        connect();
        byte[] result = new byte[1024];
            try {
                send(param);
                result = recv();
            } catch(SocketTimeoutException e) {
                e.printStackTrace();
                System.out.println("タイムアウト");
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
            return result;
    }

    public void connect() {
        try {
            socket = new Socket("localhost", 3333);
            System.out.println("Connected!");
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            System.out.println("タイムアウト");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] param) throws IOException {
        var out = new DataOutputStream(socket.getOutputStream());
        int pos = 0;
        while(out.size() < param.length) {
            out.write(param, pos, param.length);
            pos = out.size();
        }
        out.flush();
        System.out.println("Send Data");
    }

    public byte[] recv() throws SocketTimeoutException, IOException {
        final int LENGTH = 1024;
        byte[] result = new byte[LENGTH];
        socket.setSoTimeout(3000);
        socket.setReceiveBufferSize(LENGTH);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int pos = 0;
        boolean end = false;
        while(!end) {
            pos = in.read(result, pos, LENGTH);
            if (pos >= LENGTH) {
                end = true;
            }
        }
        return result;
    }

    public void close() {
        try {
            socket.close();
        } catch(IOException e) {
            ;
        } finally {
            System.out.println("ソケットを切断しました");
        }
    }
}
