package br.com.chase.exceptions;

public class RotaNotFoundException extends RuntimeException {
    public RotaNotFoundException(String message) {
        super(message);
    }
}

