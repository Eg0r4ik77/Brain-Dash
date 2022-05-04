package com.sumsung.minigames.models;

public class User {
    private String name = "";
    private String email;
    private String password;

    private int record1;
    private int record2;
    private int record3;
    private int rating;

    public User(){};

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getRecord1() {
        return record1;
    }

    public int getRecord2() {
        return record2;
    }

    public int getRecord3() {
        return record3;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

}
