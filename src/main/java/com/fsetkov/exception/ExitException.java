package com.fsetkov.exception;

/**
 * Custom unchecked exception that is used to indicate a request to exit the program.
 * <p>
 * This exception is intended to be used instead of {@code System.exit()} to allow
 * for easier testing and to prevent the JVM from terminating. When this exception
 * is thrown, it signifies that the program should terminate.
 * <p>
 * In a test, this exception can be caught and ignored to allow the test to continue
 * running even when the program under test has finished executing.
 */
public class ExitException extends RuntimeException {

    /**
     * Constructs a new ExitException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ExitException(String message) {
        super(message);
    }
}
