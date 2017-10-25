package ua.kharkiv.yeremenko.ex_JDBC.db.entity;

import java.util.HashSet;
import java.util.Set;

public class User {
    private int user_id;
    private String login;
    private Set<Group> userGroups = new HashSet<>();

    public User() {
    }

    public User(String login) {
        this.login = login;
    }

    public User(int id, String login) {
        this.user_id = id;
        this.login = login;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Set<Group> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(Set<Group> userGroups) {
        this.userGroups = userGroups;
    }

    @Override
    public String toString() {
        return "User [id=" + user_id + ", login=" + login + "]";
    }

    public static User createUser(String login){
        User user = new User(login);
        return user;
    }
}

