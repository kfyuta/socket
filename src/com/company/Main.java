package com.company;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        Client client = Client.getInstance();
        client.execute("Hello, World.".getBytes(StandardCharsets.UTF_8));
    }
}
