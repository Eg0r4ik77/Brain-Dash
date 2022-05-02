package com.sumsung.minigames.models;

public class User {
    private String name = "";
    private String email;
    private String password;

    private int[] scores = new int[4];

    public User(){};

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getScores(){
        return scores;
    }
}
