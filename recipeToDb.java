/**
 * This class is designed to bring the recipies in recipe class to the db
 * It will be very convoluted and inefficient, but it will work
 */

/* Plan:
0) get array of all recipes
1) read all ids in db into an array
2) create 3rd array to determine which recipes are not in array
3) write 3rd array into db
 */

public class RecipeToDB {
    private Recipe[] currRecipes;
    private String[] dBRecipes;
    private String[] toDBRecipes;
    }
}