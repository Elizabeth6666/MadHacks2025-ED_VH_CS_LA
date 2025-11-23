import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DBToRecipe {
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private String line;
    private String cookMethod;
    private String allergen;

    public DBToRecipe(String cookMethodIn, String allergenIn) {
        this.cookMethod = cookMethodIn;
        this.allergen = allergenIn;
        getAll();
        for (Recipe rec : recipeList) {
            System.out.println(rec);
        }
    }

    public void getAll() {
        BufferedReader reader = null;
        String filePath = "recipe.csv";
        try {
            reader = new BufferedReader(new FileReader(filePath));
            Recipe recipe;
            String line;
            String subLine;
            int index;
            while ((line = reader.readLine()) != null) {
                index = line.indexOf(",");
                line = line.substring(index + 1);
                index = line.indexOf(",");
                subLine = line.substring(0,index);
                line = line.substring(index + 1);
                recipe = new Recipe(subLine);
                index = line.indexOf(",");
                subLine = line.substring(0,index);
                line = line.substring(index + 1);
                int ind;
                String ingSubLine;
                while (subLine.indexOf("_") > 0) {
                    ind = subLine.indexOf("_");
                    ingSubLine = subLine.substring(0,ind);
                    subLine = subLine.substring(ind + 1);
                    recipe.addIngredient(ingSubLine);
                }
                recipe.addIngredient(subLine);
                index = line.indexOf(",");
                subLine = line.substring(0,index);
                line = line.substring(index + 1);
                recipe.addCookingTime(Double.parseDouble(subLine));
                index = line.indexOf(",");
                subLine = line.substring(0,index);
                line = line.substring(index + 1);
                recipe.addCookMethod(subLine);
                index = line.indexOf(",");
                subLine = line.substring(0,index);
                line = line.substring(index + 1);
                while (subLine.indexOf("_") > 0) {
                    ind = subLine.indexOf("_");
                    ingSubLine = subLine.substring(0,ind);
                    subLine = subLine.substring(ind + 1);
                    recipe.addAllergens(ingSubLine);
                }
                recipe.addAllergens(subLine);
                recipe.setInstructions(line);
                recipeList.add(recipe);
            }
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

    }

    public ArrayList<Recipe> sort() {
        ArrayList<Recipe> returnList = new ArrayList<Recipe>();

        return returnList;
    }


}
