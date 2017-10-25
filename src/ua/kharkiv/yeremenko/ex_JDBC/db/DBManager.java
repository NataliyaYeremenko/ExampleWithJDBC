package ua.kharkiv.yeremenko.ex_JDBC.db;

import ua.kharkiv.yeremenko.ex_JDBC.db.entity.Group;
import ua.kharkiv.yeremenko.ex_JDBC.db.entity.User;

import java.sql.*;
import java.util.*;

public class DBManager {
    private static final String SQL_CREATE_USER = "INSERT INTO users (user_id, login) VALUES(DEFAULT, ?)";
    private static final String SQL_CREATE_GROUP = "INSERT INTO groups (group_id, name) VALUES(DEFAULT, ?)";
    private static final String SQL_CREATE_USERS_GROUPS = "INSERT INTO users_groups (id, user_id, group_id) VALUES(DEFAULT, ?, ?)";
    private static final String SQL_FIND_ALL_USERS = "SELECT * FROM USERS";
    private static final String SQL_FIND_ALL_GROUPS = "SELECT * FROM GROUPS";
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM USERS WHERE LOGIN=?";
    private static final String SQL_FIND_GROUP_BY_NAME = "SELECT * FROM GROUPS WHERE NAME=?";
    private static final String SQL_FIND_GROUP_BY_ID = "SELECT * FROM GROUPS WHERE GROUP_ID=?";
    //private final String CON_URL;

    private static DBManager instance;

    public synchronized static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {
        /*Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/ua/kharkiv/yeremenko/ex_JDBC/db/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CON_URL = prop.getProperty("connection.url");*/
    }

    private static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "root");
        properties.setProperty("useSSL", "false");
        properties.setProperty("autoReconnect", "true");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/ex_jdbc", properties);
        /*ResourceBundle resource = ResourceBundle.getBundle("database");
        String url = resource.getString("db.url");
        String user = resource.getString("db.user");
        String pass = resource.getString("db.password");

        return DriverManager.getConnection(url, user, pass);*/
    }

    private static void prepStatement(String sqlRequest, String param){
        Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(sqlRequest);
            preparedStatement.setString(1, param);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertUser(User user) {
        prepStatement(SQL_CREATE_USER, user.getLogin());
    }

    public void insertGroup(Group group) {
        prepStatement(SQL_CREATE_GROUP, group.getName());
    }

    public <T> List<T> execQuery(String sqlRequest) {
        List<T> lst = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = null;
            try {
                rs = st.executeQuery(sqlRequest);
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String login_or_name = rs.getString(2);
                    if (sqlRequest.equals(SQL_FIND_ALL_USERS)) lst.add((T) new User(id, login_or_name));
                    if (sqlRequest.equals(SQL_FIND_ALL_GROUPS)) lst.add((T) new Group(id, login_or_name));
                }
                if (lst.size() == 0) {
                    System.out.println("Not found");
                }
            } finally {
                if (rs != null) {
                    rs.close();
                } else {
                    System.err.println("ошибка во время чтения из БД");
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return lst;
    }

    public List<User> findAllUsers() {
        List<User> lst = execQuery(SQL_FIND_ALL_USERS);
        return lst;
    }

    public List<Group> findAllGroups() {
        List<Group> lst = execQuery(SQL_FIND_ALL_GROUPS);
        return lst;
    }

    private <T> T requestResult(String sqlReq, String param){
        User user = new User();
        Group group = new Group();
        boolean check = true;
        Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = con.prepareStatement(sqlReq);
            preparedStatement.setString(1, param);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                if(sqlReq.equals(SQL_FIND_USER_BY_LOGIN)){
                    user = extractUser(rs);
                    check = false;
                }
                if(sqlReq.equals(SQL_FIND_GROUP_BY_NAME)){
                    group = extractGroup(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (check) return (T) group;
        else return (T) user;
    }

    public User getUser(String login) {
        return requestResult(SQL_FIND_USER_BY_LOGIN, login);
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUser_id(rs.getInt("user_id"));
        user.setLogin(rs.getString("login"));
        return user;
    }

    public Group getGroup(String name) {
        return requestResult(SQL_FIND_GROUP_BY_NAME, name);
    }

    private Group extractGroup(ResultSet rs) throws SQLException {
        Group group = new Group();
        group.setGroup_Id(rs.getInt("group_id"));
        group.setName(rs.getString("name"));
        return group;
    }

    public void setGroupsForUser(User user, Group ... groups) {
        int user_id = user.getUser_id();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            for (Group gr:groups) {
                System.out.println(gr.getGroup_id());
                try {
                    preparedStatement = con.prepareStatement(SQL_CREATE_USERS_GROUPS);
                    preparedStatement.setInt(1, user_id);
                    preparedStatement.setInt(2, gr.getGroup_id());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            con.commit();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Group> getUserGroups(User user) throws SQLException {
        int user_id = user.getUser_id();
        List<Integer> gr_id = new ArrayList<>();
        List<Group> lst = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = null;
            try {
                rs = st.executeQuery("SELECT GROUP_ID FROM USERS_GROUPS WHERE USER_ID="+user_id);
                while (rs.next()) {
                    gr_id.add(rs.getInt(1));
                    //System.out.println(rs.getInt(1));
                }
                if (gr_id.size() == 0) {
                    System.out.println("Not found");
                } else {
                    for (int id : gr_id) {
                        Group group = new Group();
                        PreparedStatement preparedStatement = null;
                        rs = null;
                        try {
                            preparedStatement = con.prepareStatement(SQL_FIND_GROUP_BY_ID);
                            preparedStatement.setInt(1, id);
                            rs = preparedStatement.executeQuery();
                            if (rs.next()) {
                                lst.add(extractGroup(rs));
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } finally {
                            if (rs != null) {
                                rs.close();
                            } else {
                                System.err.println("ошибка во время чтения из БД");
                            }
                        }
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                if (st != null) {
                    try {
                        st.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        catch (SQLException e) {
        e.printStackTrace();
    }
        finally {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    return lst;
    }
}

