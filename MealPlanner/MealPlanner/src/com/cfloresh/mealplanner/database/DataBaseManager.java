package com.cfloresh.mealplanner.database;

import java.sql.*;

public class DataBaseManager {

    private static Connection startConnection() throws SQLException {
        String dbPath = "jdbc:postgresql://localhost:5432/meals_db";
        String dbUser = "postgres";
        String dbPass = "1111";
        return DriverManager.getConnection(dbPath, dbUser, dbPass);
    }

    public static void createMealsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS meals ("
                + "category VARCHAR(100),"
                + "meal VARCHAR(100),"
                + "meal_id INTEGER UNIQUE"
                + ");";

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createIngredientsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ingredients ("
                + "ingredient VARCHAR(100),"
                + "ingredient_id INTEGER UNIQUE,"
                + "meal_id INTEGER"
                + ");";

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();

            statement.executeQuery(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearTable(String tableName) {
        String sql = "DELETE FROM " + tableName;

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertMealsData (String category, String meal, int mealId) {
        String sql = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category);
            statement.setString(2, meal);
            statement.setInt(3, mealId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readData() {
        String sql = "SELECT category, meal, meal_id FROM meals";
        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String meal = resultSet.getString("meal");
                int mealId = resultSet.getInt("meal_id");

                System.out.println("Category: " + category);
                System.out.println("name: " + meal);
                System.out.println("id: " + mealId);

                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}

