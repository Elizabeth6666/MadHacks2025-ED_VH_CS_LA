/**
 * This class is designed to bring the recipies in recipe class to the db
 * It will be very convoluted and inefficient, but it will work
 */

/* Plan:
0) get recipe
1) compare recipe id with ids in db
2) add recipe into db
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RecipeToDB {
    private Recipe recipe;
    private String dBRecipe;

    public RecipeToDB(Recipe recipe) {
        this.recipe = recipe;
        checkDB();
    }

    public void checkDB() {
        
        try {
            
        } catch (Exception e) {
            
        } finally {
           
        }
        
    }
    
}
