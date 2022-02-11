package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private EchoServer(int port){
        this.port = port;
    }
    static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }
    public void run(){
        try(var server = new ServerSocket(port)){
            while (!server.isClosed()){
                Socket clientSocket = server.accept();
                pool.submit(() -> handle(clientSocket));
            }
        }catch (IOException ex){
            var msg = "port is busy";
            System.out.println(msg);
            ex.printStackTrace();
        }
    }

    private void handle(Socket socket){
        System.out.printf("Подключен клиент: %s%n",socket);
        try(socket;
        Scanner reader = getReader(socket);
            PrintWriter writer = getWriter(socket)){
            sendResponse("Привет " + socket,writer);
            while(true){
                String message = reader.nextLine();
                if(isEmptyMsg(message) || isQuitMsg(message)){
                    break;
                }
                sendResponse(message.toUpperCase(), writer);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        System.out.printf("Клиент отключен: %s%n", socket);
    }
    private static PrintWriter getWriter(Socket socket) throws IOException{
        OutputStream stream = socket.getOutputStream();
        return new PrintWriter(stream);
    }
    private static Scanner getReader(Socket socket) throws IOException{
        InputStream stream = socket.getInputStream();
        InputStreamReader input = new InputStreamReader(stream,"UTF-8");
        return new Scanner(input);
    }
    private static boolean isQuitMsg(String message){
        return "bye".equals(message.isBlank());
    }
    private static boolean isEmptyMsg(String message){
        return message == null || message.isBlank();
    }
    private static void sendResponse(String response,Writer writer) throws IOException{
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }
}
