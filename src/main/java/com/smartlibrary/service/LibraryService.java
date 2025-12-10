package com.smartlibrary.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smartlibrary.model.Book;
import com.smartlibrary.model.Member;

public class LibraryService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "library_data.ser";

    // Use maps for fast lookup
    private Map<String, Book> books = new ConcurrentHashMap<>();
    private Map<String, Member> members = new ConcurrentHashMap<>();

    private static LibraryService instance = new LibraryService();

    private LibraryService() {}

    public static LibraryService getInstance() {
        return instance;
    }

    // Book operations
    public boolean addBook(Book b) {
        if (books.containsKey(b.getId())) return false;
        books.put(b.getId(), b);
        return true;
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    // Member operations
    public boolean addMember(Member m) {
        if (members.containsKey(m.getId())) return false;
        members.put(m.getId(), m);
        return true;
    }

    public Collection<Member> getAllMembers() {
        return members.values();
    }

    // Issue book: decrease quantity, add to member issued set
    public boolean issueBook(String bookId, String memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null || m == null) return false;
        if (b.getQuantity() <= 0) return false;
        // simple rule: allow multiple copies to one member
        b.setQuantity(b.getQuantity() - 1);
        m.getIssuedBookIds().add(bookId);
        return true;
    }

    // Return book: increase quantity, remove from member issued set
    public boolean returnBook(String bookId, String memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null || m == null) return false;
        if (!m.getIssuedBookIds().contains(bookId)) return false;
        b.setQuantity(b.getQuantity() + 1);
        m.getIssuedBookIds().remove(bookId);
        return true;
    }

    // Persistence (simple serialization)
    public void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            LibraryService loaded = (LibraryService) in.readObject();
            // copy loaded data into the singleton
            this.books = new ConcurrentHashMap<>(loaded.books);
            this.members = new ConcurrentHashMap<>(loaded.members);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load data: " + e.getMessage());
        }
    }
}
