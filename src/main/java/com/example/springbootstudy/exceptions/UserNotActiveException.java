package com.example.springbootstudy.exceptions;


public class UserNotActiveException extends Exception{
    public UserNotActiveException() {
        super("Your account isn't active.\n Pls confirm your account through your email");
    }
}
