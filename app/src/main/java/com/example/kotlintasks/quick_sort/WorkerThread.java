package com.example.kotlintasks.quick_sort;

import java.util.ArrayDeque;
import java.util.Queue;

public class WorkerThread extends Thread {
    Queue<Runnable> taskQueue = new ArrayDeque<>();

    public WorkerThread(Boolean isDaemon) {
        this.setDaemon(isDaemon);
    }

    private boolean isActive = true;

    private boolean isSleep = false;

    @Override
    synchronized public void run() {
        System.out.println("WorkerThread active");
        while (isActive || !taskQueue.isEmpty()) {
            if (taskQueue.isEmpty()) {
                try {
                    if (!isSleep) {
                        isSleep = true;
                        System.out.println("WorkerThread sleep");
                    }
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if (isSleep) {
                    System.out.println("WorkerThread run");
                    isSleep = false;
                }
                taskQueue.remove().run();
            }
        }
        System.out.println("WorkerThread dead");
    }

    public void stopThread() {
        isActive = false;
        System.out.println("WorkerThread request to stop");
    }

    synchronized public void putTask(Runnable task) {
        notify();
        if (isActive)
            taskQueue.add(task);
    }
}
