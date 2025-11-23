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
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RecipeToDB {
    private Recipe recipe;
    private String dBRecipe = "";
    private String filePath = "recipe.csv";
    private BufferedWriter writer = null;
    private BufferedReader reader = null;

    public RecipeToDB(Recipe recipe) {
        this.recipe = recipe;
        addToDB();
    }
    
    public void addToDB() {
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            String id;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count += 1;
            }
            id = "ID" + count;
            recipe.setID(id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            } 
        }
        try {
            writer = new BufferedWriter( new FileWriter(filePath,true));
            dBRecipe += recipe.getRecipeID() + ",";
            dBRecipe += recipe.getName() + ",";
            ArrayList<String> ingredients;
            ingredients = recipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                dBRecipe += ingredients.get(i) + "_";
            }
            dBRecipe = dBRecipe.substring(0,dBRecipe.length()-1);
            dBRecipe += "," + recipe.getCookTimeInMinutes() + ",";
            dBRecipe += recipe.getCookMethod() + ",";
            ArrayList<String> allergens;
            allergens = recipe.getAllergens();
            for (int j = 0; j < allergens.size(); j++) {
                dBRecipe += allergens.get(j) + "_";
            }
            dBRecipe = dBRecipe.substring(0,dBRecipe.length()-1);
            dBRecipe += "," + recipe.getInstructions();
            writer.append(dBRecipe);
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
