package com.serezk4;

import com.serezk4.chat.Handler;
import com.serezk4.io.IOWorker;
import com.serezk4.io.console.BufferedConsoleWorker;
import com.serezk4.io.console.ConsoleWorker;
import com.serezk4.io.deque.DequeWorker;

/**
 * Main class.
 * Entry point of the program.
 */
public class Main {
    public static void main(String[] args) {
        try (ConsoleWorker console = new BufferedConsoleWorker(); IOWorker<String> script = new DequeWorker()) {
            new Handler(console, script).run();
        } catch (Exception e) {
            System.err.printf("shit happened: %s%n", e.getMessage());
        }
    }
}