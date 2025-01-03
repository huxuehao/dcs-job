package com.tiger.job.core.retry;

/**
 * 描述：
 *
 * @author huxuehao
 **/
public class MainTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("001");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("error now, but interrupt is " + Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                    System.out.println("after now, and interrupt is " + Thread.currentThread().isInterrupted());
                }
            }
        });

        try {
            thread.start();
            Thread.sleep(2000);
            thread.interrupt();
        } catch (InterruptedException ignored) {}
    }
}
