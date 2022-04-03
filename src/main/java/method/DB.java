package method;

import java.sql.*;

public class DB {
    private static Connection connection;

    private DB() {
    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/my_db", "root", "springcourse");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static synchronized void transferMoney(int idFromUser, int idToUser, int amount) {
        try {
            getConnection().setAutoCommit(false);
            System.out.println("Starting transaction from " + idFromUser + " to " + idToUser + " amount " + amount);
            //current balance user
            int fromMoney = getMoneyById(idFromUser);

            System.out.println("from money = " + fromMoney);
            if (fromMoney < amount) {
                throw new RuntimeException("not enough money!");
            }
            int toMoney = getMoneyById(idToUser);
            System.out.println("to money = " + toMoney);
            updateUserBalance(idFromUser, fromMoney - amount);
            updateUserBalance(idToUser, toMoney + amount);
            System.out.println("Balances updated!");
            connection.commit();
            connection.setAutoCommit(true);
            System.out.println(idFromUser + " = " + getMoneyById(idFromUser) + "\n" + idToUser + " = " + getMoneyById(idToUser));
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static int getMoneyById(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("Select `value` from `transactional` where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("invalid user id");
    }

    private static void updateUserBalance(int userId, int value) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE `transactional` SET `VALUE` = ? WHERE id = ?");
            ps.setInt(1, value);
            ps.setInt(2, userId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("invalid user id");
        }
    }
}
