import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DBToRecipe {
    private ArrayList<Recipe> recipeList;
    private String line;
    private String cookMethod;
    private String allergen;

    public DBToRecipe(String cookMethodIn, String allergenIn) {
        this.cookMethod = cookMethodIn;
        if (cookMethodIn.equals("Oven") || cookMethodIn.equals("Microwave") || cookMethodIn.equals("Air Fryer")
          || cookMethodIn.equals("Stove") || cookMethodIn.equals("Fridge") || cookMethodIn.equals("No Cook") 
          || cookMethodIn.equals("Dorm")) {
        }
        this.allergen = allergenIn;
        if (allergenIn.equals("Oven") || allergenIn.equals("Microwave") || allergenIn.equals("Air Fryer")
          || allergenIn.equals("Stove") || allergenIn.equals("Fridge") || allergenIn.equals("No Cook") 
          || allergenIn.equals("Dorm")) {
        }
    }

    public void getAll() {
        BufferedReader reader = null;
        String filePath = "recipe.csv";
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            String subLine;
            int index;
            while ((line = reader.readLine()) != null) {
                index = line.indexOf(",");
                
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
