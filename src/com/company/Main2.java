package com.company;

import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.Scanner;

public class Main2 {

    public static void main(String[] args) {
        EchoClient.connectTo(8788).run();
    }
}