package ru.tacos.models;

public class Client {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private int token;

    public Client(int id, String nickname, String email, String password, int token) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public Client(String nickname, String email, String password, int token) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
