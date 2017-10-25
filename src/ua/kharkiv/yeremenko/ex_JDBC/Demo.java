package ua.kharkiv.yeremenko.ex_JDBC;

import java.sql.SQLException;
import java.util.List;

import ua.kharkiv.yeremenko.ex_JDBC.db.entity.Group;
import ua.kharkiv.yeremenko.ex_JDBC.db.entity.User;
import ua.kharkiv.yeremenko.ex_JDBC.db.DBManager;

public class Demo {
    private static <T> void printList(List<T> list) {
        for (T element : list) {
            System.out.println(element);
        }
    }
    public static void main(String[] args) throws SQLException {
        // users ==> [upsov]; groups ==> [teamD]
        DBManager dbManager = DBManager.getInstance();
        // Part 1
        //dbManager.insertUser(User.createUser("upsov"));
        //dbManager.insertUser(User.createUser("obama"));
        printList(dbManager.findAllUsers());
        // users ==> [ivanov, petrov, ..]
        System.out.println("===========================");
        // Part 2
        //dbManager.insertGroup(Group.createGroup("teamB"));
        //dbManager.insertGroup(Group.createGroup("teamC"));
        //dbManager.insertGroup(Group.createGroup("teamD"));
        //dbManager.insertGroup(Group.createGroup("teamE"));
        printList(dbManager.findAllGroups());
        // groups ==> [teamA, teamB, teamC]
        System.out.println("===========================");
        // Part 3
        User userPetrov = dbManager.getUser("petrov");
        System.out.println(userPetrov);
        User userIvanov = dbManager.getUser("ivanov");
        System.out.println(userIvanov);
        User userUpsov = dbManager.getUser("upsov");
        System.out.println(userUpsov);
        //User userObama = dbManager.getUser("obama");
        Group teamA = dbManager.getGroup("teamA");
        Group teamB = dbManager.getGroup("teamB");
        Group teamC = dbManager.getGroup("teamC");
        // method setGroupsForUser must implement transaction!
        //dbManager.setGroupsForUser(userUpsov, teamA, teamB, teamC);
        //dbManager.setGroupsForUser(userPetrov, teamA, teamB);
        //dbManager.setGroupsForUser(userObama, teamA, teamB, teamC);
        System.out.println("===========================");
        for (User user : dbManager.findAllUsers()) {
            System.out.println("user: " + user.getLogin());
            System.out.println("user groups:");
            printList(dbManager.getUserGroups(user));
            System.out.println("~~~~~");
        }
        // teamA
        // teamA teamB
        // teamA teamB teamC
        System.out.println("===========================");
    }
}