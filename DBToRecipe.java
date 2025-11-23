import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DBToRecipe {
    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private String line;
    private String cookMethod;
    private String allergen;
    private String filePathIn = "recipeIn.csv";
    private String filePathOut = "recipeOut.csv";

    public DBToRecipe(String cookMethodIn, String allergenIn) {
        this.cookMethod = cookMethodIn;
        this.allergen = allergenIn;
        getAll();
    }

    public void getAll() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePathIn));
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
        ArrayList<Recipe> cookChecked = new ArrayList<Recipe>();
        if (this.cookMethod.equals("All")) {
            cookChecked = recipeList;
        } else {
            for (Recipe rec : recipeList) {
                if (cookMethod.equals("Dorm")) {
                    if (rec.getCookMethod().equals("Microwave") || rec.getCookMethod().equals("Fridge") 
                    || rec.getCookMethod().equals("No Cook")) {
                        cookChecked.add(rec);
                    }
                } else {
                    if (rec.getCookMethod().equals(this.cookMethod)) {
                        cookChecked.add(rec);
                    }
                }
            }
        }

        if (this.allergen.equals("None")) {
            returnList = cookChecked;
        } else {
            for (Recipe rec : cookChecked) {
                ArrayList<String> allergens = rec.getAllergens();
                boolean add = true;
                for (String al : allergens) {
                    if (al.equals(allergen)) {
                        add = false;
                    }
                }
                if (add) {
                    returnList.add(rec);
                }
            }
        }
        
        return returnList;
    }


}
