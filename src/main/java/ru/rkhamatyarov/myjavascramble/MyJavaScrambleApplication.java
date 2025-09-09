package ru.rkhamatyarov.myjavascramble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application for MyJavaScramble.
 */
@SpringBootApplication
public class MyJavaScrambleApplication {

    // Private constructor to prevent instantiation
    protected MyJavaScrambleApplication() {
        // This constructor is intentionally empty
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(final String[] args) {
        SpringApplication.run(MyJavaScrambleApplication.class, args);
    }
}
