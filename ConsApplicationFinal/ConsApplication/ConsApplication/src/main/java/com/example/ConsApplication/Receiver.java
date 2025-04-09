package com.example.ConsApplication;

import org.springframework.stereotype.Component;

@Component
public class Receiver {
    public void receiveMessage(byte[] message) {
        String msg = new String(message);
        System.out.println("Mensagem recebida: " + msg);
    }
}
