package com.cfloresh.mealplanner.database;

import com.cfloresh.mealplanner.Meal;
import com.cfloresh.mealplanner.Ingredient;

import java.sql.*;

public class DataBaseManager {

    private static Connection startConnection() throws SQLException {
        String dbPath = "jdbc:postgresql://localhost:5432/meals_db";
        String dbUser = "postgres";
        String dbPass = "1111";
        return DriverManager.getConnection(dbPath, dbUser, dbPass);
    }

    public static void createTables() {
        String sqlMeals = "CREATE TABLE IF NOT EXISTS meals ("
                + "category VARCHAR(100),"
                + "meal VARCHAR(100),"
                + "meal_id INTEGER UNIQUE"
                + ");";

        String sqlIngredients = "CREATE TABLE IF NOT EXISTS ingredients ("
                + "ingredient VARCHAR(100),"
                + "ingredient_id INTEGER UNIQUE,"
                + "meal_id INTEGER"
                + ");";

        String sqlLastIdVals = "CREATE TABLE IF NOT EXISTS last_id_vals ("
                + "last_id_meals INTEGER,"
                + "last_id_ingredients INTEGER"
                + ");";

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sqlMeals);
            statement.executeUpdate(sqlIngredients);
            statement.executeUpdate(sqlLastIdVals);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertMealsData (String category, String meal, int mealId) {
        String sqlMeal = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";
        String sqlId = "UPDATE last_id_vals SET last_id_meals = ?";

        try (Connection connection = startConnection()) {

            PreparedStatement statementMeals = connection.prepareStatement(sqlMeal);
            statementMeals.setString(1, category);
            statementMeals.setString(2, meal);
            statementMeals.setInt(3, mealId);
            statementMeals.executeUpdate();

            PreparedStatement statementId = connection.prepareStatement(sqlId);
            statementId.setInt(1, mealId);
            statementId.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertIngredientsData (String ingredient, int ingredient_id, int meal_id) {
        String sql = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES (?, ?, ?)";
        String sqlId = "UPDATE last_id_vals SET last_id_ingredients = ?";

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ingredient);
            statement.setInt(2, ingredient_id);
            statement.setInt(3, meal_id);
            statement.executeUpdate();

            PreparedStatement statementId = connection.prepareStatement(sqlId);
            statementId.setInt(1, ingredient_id);
            statementId.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readMealsData () {
        String sql = "SELECT category, meal, meal_id FROM meals";
        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String category = resultSet.getString("category");
                String meal = resultSet.getString("meal");
                int mealId = resultSet.getInt("meal_id");

                System.out.println();
                System.out.println("Category: " + category);
                System.out.println("name: " + meal);
                readIngredientsData(mealId, connection);
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void readIngredientsData (int meal_id, Connection connection) throws SQLException {
        String sql = "SELECT ingredient, ingredient_id FROM ingredients WHERE meal_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, meal_id);

        ResultSet resultSet = statement.executeQuery();

        System.out.println("ingredients: ");
        while (resultSet.next()) {
            String name = resultSet.getString("ingredient");
            System.out.println(name);
        }
    }

    public static void getLastIdsValues() {
        String sqlEmptyMeals = "SELECT EXISTS (SELECT 1 FROM meals);";
        String sqlEmptyIngredients = "SELECT EXISTS (SELECT 1 FROM ingredients);";

        String sqlMaxIdMeals = "SELECT MAX(meal_id) FROM meals;";
        String sqlMaxIdIngredients = "SELECT MAX(ingredient_id) FROM ingredients;";

        boolean mealsNotEmpty = false;
        boolean ingredientsNotEmpty = false;

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sqlEmptyMeals);
            if (resultSet.next()) {
                mealsNotEmpty = resultSet.getBoolean(1);
            }

            resultSet = statement.executeQuery(sqlEmptyIngredients);
            if (resultSet.next()) {
                ingredientsNotEmpty = resultSet.getBoolean(1);
            }

            if (mealsNotEmpty) {
                resultSet = statement.executeQuery(sqlMaxIdMeals);
                if (resultSet.next()) {
                    Meal.setCurrentMealID(resultSet.getInt(1));
                }
            }

            if (ingredientsNotEmpty) {
                resultSet = statement.executeQuery(sqlMaxIdIngredients);
                if (resultSet.next()) {
                    Ingredient.setCurrentIngredientID(resultSet.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}

