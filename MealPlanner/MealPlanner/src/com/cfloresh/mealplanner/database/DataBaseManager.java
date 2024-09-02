package com.cfloresh.mealplanner.database;

import com.cfloresh.mealplanner.Meal;
import com.cfloresh.mealplanner.Ingredient;
import com.cfloresh.mealplanner.enumerations.WeekDay;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
                + "meal_id INTEGER UNIQUE PRIMARY KEY"
                + ");";

        String sqlIngredients = "CREATE TABLE IF NOT EXISTS ingredients ("
                + "ingredient VARCHAR(100),"
                + "ingredient_id INTEGER UNIQUE,"
                + "meal_id INTEGER,"
                + "FOREIGN KEY (meal_id) REFERENCES meals(meal_id)"
                + ");";

        String sqlPlan = "CREATE TABLE IF NOT EXISTS plan ("
                + "day_of_week VARCHAR(100),"
                + "meal_option VARCHAR(100),"
                + "meal_category VARCHAR(100),"
                + "meal_id INTEGER,"
                + "FOREIGN KEY (meal_id) REFERENCES meals(meal_id)"
                + ");";

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();

            statement.executeUpdate(sqlMeals);
            statement.executeUpdate(sqlIngredients);
            statement.executeUpdate(sqlPlan);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertMealsData (String category, String meal, int mealId) {
        String sqlMeal = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";

        try (Connection connection = startConnection()) {

            PreparedStatement statementMeals = connection.prepareStatement(sqlMeal);
            statementMeals.setString(1, category);
            statementMeals.setString(2, meal);
            statementMeals.setInt(3, mealId);
            statementMeals.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertIngredientsData (String ingredient, int ingredient_id, int meal_id) {
        String sql = "INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES (?, ?, ?)";

        try (Connection connection = startConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, ingredient);
            statement.setInt(2, ingredient_id);
            statement.setInt(3, meal_id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void readMealsData (String requestedCategory) {
        String sql = "SELECT meal, meal_id FROM meals WHERE category = ?";
        boolean notEmpty = false;
        boolean catPrinted = false;

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, requestedCategory);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                notEmpty = true;

                if(!catPrinted) {
                    System.out.println("Category: " + requestedCategory);
                    catPrinted = true;
                }

                String meal = resultSet.getString("meal");
                int mealId = resultSet.getInt("meal_id");

                System.out.println();

                System.out.println("Name: " + meal);
                readIngredientsData(mealId, connection);
            }

            if(!notEmpty) {
                System.out.println("No meals found.");
                return;
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean getMealsByCategory(String category, String weekDay) {
        String sql = "SELECT meal from Meals WHERE category = ? ORDER BY meal";
        boolean isEmpty = true;
        boolean shownDay = false;

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if (!shownDay && category.equals("breakfast")) {
                    System.out.println(weekDay);
                    shownDay = true;
                }

                isEmpty = false;
                String meal = resultSet.getString("meal");
                System.out.println(meal);
            }

            return !isEmpty;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static void readIngredientsData (int meal_id, Connection connection) throws SQLException {
        String sql = "SELECT ingredient, ingredient_id FROM ingredients WHERE meal_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, meal_id);

        ResultSet resultSet = statement.executeQuery();

        System.out.println("Ingredients: ");
        while (resultSet.next()) {
            String name = resultSet.getString("ingredient");
            System.out.println(name);
        }
    }

    public static boolean mealExistInTable(String category, String meal) {
        String sql = "SELECT meal from meals WHERE category = ? AND meal = ? LIMIT 1";
        boolean result = false;

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category);
            statement.setString(2, meal);

            ResultSet resultSet = statement.executeQuery();

            result = resultSet.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static int getMealId(String category, String meal) {
        String sql = "SELECT meal_id from meals WHERE category = ? AND meal = ? LIMIT 1";
        int retVal = 0;

        try (Connection connection = startConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category);
            statement.setString(2, meal);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            retVal = resultSet.getInt("meal_id");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return retVal;
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

    public static void insertPlanData (String day, String meal, String category, int mealId) {
        String sqlMeal = "INSERT INTO plan (day_of_week, meal_option, meal_category, meal_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = startConnection()) {

            PreparedStatement statement = connection.prepareStatement(sqlMeal);
            statement.setString(1, day);
            statement.setString(2, meal);
            statement.setString(3, category);
            statement.setInt(4, mealId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void clearPlan() {
        String sql = "DELETE FROM plan";

        try (Connection connection = startConnection()) {

            Statement statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static Map<String, String> getDayPlan(String day) {
        Map<String, String> result = new LinkedHashMap<>();

        String sql = "SELECT * from plan WHERE day_of_week = ?";

        try (Connection connection = startConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, day);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.put(resultSet.getString("meal_category"), resultSet.getString("meal_option"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static boolean planIsNotEmpty() {
        String sql = "SELECT * from plan LIMIT 1";
        boolean result = false;

        try (Connection connection = startConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            result = resultSet.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

}

