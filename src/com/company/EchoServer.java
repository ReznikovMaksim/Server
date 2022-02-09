package com.company;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;
    private EchoServer(int port){
        this.port = port;
    }
    static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }
    public void run(){
        try(var server = new ServerSocket(port)){
            //connection
            try(var clientSocket = server.accept()){
                handle(clientSocket);
            }
        }catch (IOException ex){
            var msg = "port is busy";
            System.out.println(msg);
            ex.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException{
        var input = socket.getInputStream();
        var isr = new InputStreamReader(input,"UTF-8");
        Scanner scanner = new Scanner(isr);
        try(scanner){
            while (true){
                var message = scanner.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                if("bye".equals(message.toLowerCase())){
                    System.out.printf("Bye bye!%n");
                    return;
                }
            }
        }catch (NoSuchElementException ex){
            System.out.println("Client dropped the connection");
        }
    }
}
