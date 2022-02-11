package com.company;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        EchoServer.bindToPort(8788).run();
//        ExecutorService pool = Executors.newCachedThreadPool();
//        int taskCount = 8;
//        submitTasksInto(pool,taskCount);
//        System.out.println(" ");
//        pool.shutdown();
//        measure(pool);
    }
    private static void submitTasksInto(ExecutorService pool, int taskCount){
        System.out.println("Создаем задачи");
        IntStream.rangeClosed(1,taskCount)
                .mapToObj(i -> makeTask(i))
                .forEach(pool::submit);
    }

    private static Runnable makeTask(int taskId) {
        int temp = new Random().nextInt(20000) + 10000;
        int taskTime = (int) TimeUnit.MILLISECONDS.toSeconds(temp);
        return () -> heavyTask(taskId,taskTime);
    }
    private static void heavyTask(int taskId, int taskTime){
        String msg = String.format("задача %s займет %s секунд",taskId,taskTime);
        try{
            Thread.sleep(taskTime * 1000);
            System.out.printf("Завершилась задача %s " + " выролнилась за %s секунд%n",taskId,taskTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void measure(ExecutorService pool){
        long start = System.nanoTime();
        try{
            pool.awaitTermination(600,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long delta = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.printf("выполнение заняло: %s мсек %n",delta);
    }
}
