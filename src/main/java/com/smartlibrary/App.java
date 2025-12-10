package com.smartlibrary;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.Member;
import com.smartlibrary.service.LibraryService;

import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LibraryService lib = LibraryService.getInstance();

    public static void main(String[] args) {
        // try load previous data (if exists)
        lib.loadFromFile();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": addBook(); break;
                case "2": listBooks(); break;
                case "3": addMember(); break;
                case "4": listMembers(); break;
                case "5": issueBook(); break;
                case "6": returnBook(); break;
                case "7": lib.saveToFile(); System.out.println("Data saved."); break;
                case "0": 
                    lib.saveToFile();
                    System.out.println("Exiting. Data saved.");
                    running = false; break;
                default: System.out.println("Invalid choice."); break;
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Smart Library Management ===");
        System.out.println("1. Add Book");
        System.out.println("2. List Books");
        System.out.println("3. Add Member");
        System.out.println("4. List Members");
        System.out.println("5. Issue Book to Member");
        System.out.println("6. Return Book from Member");
        System.out.println("7. Save Now");
        System.out.println("0. Exit");
        System.out.print("Choose: ");
    }

    private static void addBook() {
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine().trim();
        System.out.print("Enter Quantity (number): ");
        int qty = Integer.parseInt(scanner.nextLine().trim());
        Book b = new Book(id, title, author, qty);
        if (lib.addBook(b)) System.out.println("Book added.");
        else System.out.println("Book with same ID exists.");
    }

    private static void listBooks() {
        System.out.println("--- Books ---");
        lib.getAllBooks().forEach(System.out::println);
    }

    private static void addMember() {
        System.out.print("Enter Member ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        Member m = new Member(id, name);
        if (lib.addMember(m)) System.out.println("Member added.");
        else System.out.println("Member with same ID exists.");
    }

    private static void listMembers() {
        System.out.println("--- Members ---");
        lib.getAllMembers().forEach(System.out::println);
    }

    private static void issueBook() {
        System.out.print("Enter Member ID: ");
        String mid = scanner.nextLine().trim();
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine().trim();
        boolean ok = lib.issueBook(bid, mid);
        System.out.println(ok ? "Book issued." : "Issue failed (check IDs or availability).");
    }

    private static void returnBook() {
        System.out.print("Enter Member ID: ");
        String mid = scanner.nextLine().trim();
        System.out.print("Enter Book ID: ");
        String bid = scanner.nextLine().trim();
        boolean ok = lib.returnBook(bid, mid);
        System.out.println(ok ? "Book returned." : "Return failed (check IDs).");
    }
}
