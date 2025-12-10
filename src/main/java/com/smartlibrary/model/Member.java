package com.smartlibrary.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Member implements Serializable {
    private String id;
    private String name;
    private Set<String> issuedBookIds = new HashSet<>();

    public Member() {}

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<String> getIssuedBookIds() { return issuedBookIds; }

    @Override
    public String toString() {
        return String.format("[%s] %s — issued: %d", id, name, issuedBookIds.size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member m = (Member) o;
        return Objects.equals(id, m.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
