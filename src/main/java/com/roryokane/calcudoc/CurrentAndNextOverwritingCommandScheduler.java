package com.roryokane.calcudoc;

public class CurrentAndNextOverwritingCommandScheduler implements Runnable {
    // I learned that this minus the overwriting is just a Executors.newSingleThreadExecutor().
    // TODO Make this just another Executor, or ExecutorService, or whatever, that is like that but with the special extra rule.
    // That would hopefully allow my to get rid of my non-working wait/notify code and use some executor utilities to help instead.
    // Though using the non-overwriting executor instead of this class is good enough for now.

    Runnable running = null;
    Runnable next = null;
    private boolean currentlyRunning = false;

    public CurrentAndNextOverwritingCommandScheduler() {
        // subscribe to CommandFinished events on itself, and run handleCommandFinished() as the handler
    }

    // this should probably be a thread of its own so it can trigger the command when the running one finishes
    // so I'll create run():
    public synchronized void run() {
        while (true) {
            try {
                while (running == null && next == null) {
                    this.wait();
                }
                if (running != null) {
                    runCommand(running);
                } else if (next != null) {
                    moveNextToRunning();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        // if possible, use java.util.concurrent classes, not wait/notify
        // look for a guide of those classes - maybe this class already exists, or can be a configured instance of one of those classes
        // see also third-party libraries like Google Guava and Apache Commons
    }

    public synchronized void schedule(Runnable command) {
        // if nothing is running, set it to running and start it
        System.out.println("scheduling command at " + System.nanoTime() % 1e10);
        if (running == null) {
            running = command;
            notify();
        } else {
            // if something is running, set it to "next" (overwriting any other command)
            next = command;
            notify();
        }
    }

    protected void runCommand(Runnable command) {
        // fire CommandStarted event
        currentlyRunning = true;
        // run the command
        command.run();
        // fire CommandFinished event
        handleCommandFinished();
    }

    protected synchronized void handleCommandFinished() {
        // TODO make this be called when a command finishes
        // do that by
        // move the next command to running and run it
        System.out.println("handling finished command at " + System.nanoTime() % 1e10);
        currentlyRunning = false;
        moveNextToRunning();
    }

    private void moveNextToRunning() {
        running = next;
        next = null;
    }
}
