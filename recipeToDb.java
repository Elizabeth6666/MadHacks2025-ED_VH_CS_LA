/**
 * This class is designed to bring the recipies in recipe class to the db
 * It will be very convoluted and inefficient, but it will work
 */

/* Plan:
0) get recipe
1) compare recipe id with ids in db
2) add recipe into db
 */
import recipe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeToDB {
    private Recipe recipe;
    private String dBRecipe;

    public RecipeToDB(Recipe recipe) {
        this.recipe = recipe;
        checkDB();
    }

    public void checkDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:recipe.db");
            System.out.println("Connection created");
        } catch (Exception e) {
            ;
        } finally {
            if (conn !=  null) {
                try {
                conn.close(); // Close the connection
                } catch (SQLException e) {
                    // Log or handle any exception during connection closing
                }
            }
        }
    }
    
}
