package com.space.controller;

public class Test {
    public static void main(String[] args) {
        try {
            Long.parseLong("5.2");
        } catch (NumberFormatException e) {
            System.out.println("urrr");
        }
    }
}
