package ua.kharkiv.yeremenko.ex_JDBC.db.entity;

import java.sql.SQLException;

public class Group {
    private int group_id;
    private String name;

    public Group(){}

    public Group(String name) {
        this.name = name;
    }

    public Group(int id, String name) {
        this.group_id = id;
        this.name = name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_Id(int id) {
        this.group_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Group createGroup(String name) throws SQLException {
        Group group = new Group(name);
        return group;
    }

    @Override
    public String toString() {
        return "Group [id=" + group_id + ", name=" + name + "]";
    }
}
