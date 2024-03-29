package com.hgrranzi.swingum.view;

import java.util.Scanner;

public class ScannerProvider {

    private static Scanner scanner;

    public static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
