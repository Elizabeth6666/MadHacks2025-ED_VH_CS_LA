import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HandleInput {

    public static void main(String[] args) {
        try {
            // GET query string from CGI environment
            String query = System.getenv("QUERY_STRING");
            if ((query == null || query.isEmpty()) && args.length > 0) {
                query = args[0];
            }

            Map<String, String[]> params = parseQuery(query);

            String name = decode(getFirst(params.get("RecipeName")));
            String cookTime = getFirst(params.get("CookTime"));
            String cookMethod = decode(getFirst(params.get("CookMethod")));
            String[] allergensArr = params.get("Allergens");
            String allergens = "";
            if (allergensArr != null) allergens = String.join(",", allergensArr);
            String ingredients = decode(getFirst(params.get("Ingredients")));
            String instructions = decode(getFirst(params.get("Instructions")));

            if (name == null || name.isEmpty()) {
                outputHtml("Missing recipe name; nothing saved.");
                return;
            }

            Recipe recipe = new Recipe(name);

            try {
                recipe.addCookingTime(Double.parseDouble(cookTime));
            } catch (Exception e) {}

            if (cookMethod != null && !cookMethod.isEmpty()) {
                recipe.addCookMethod(cookMethod);
            }

            if (!allergens.isEmpty()) {
                for (String a : allergens.split(",")) recipe.addAllergens(a.trim());
            }

            if (ingredients != null && !ingredients.isEmpty()) {
                for (String ing : ingredients.split(",")) recipe.addIngredient(ing.trim());
            }

            recipe.setInstructions(instructions == null ? "" : instructions);

            new RecipeToDB(recipe);

            outputHtml("Recipe saved successfully.");

        } catch (Exception e) {
            outputHtml("Error saving recipe: " + e.getMessage());
        }
    }

    private static Map<String, String[]> parseQuery(String query) throws UnsupportedEncodingException {
        Map<String, String[]> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                map.putIfAbsent(key, new String[]{});
                String[] arr = map.get(key);
                String[] newArr = new String[arr.length + 1];
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                newArr[arr.length] = value;
                map.put(key, newArr);
            }
        }
        return map;
    }

    private static String getFirst(String[] arr) {
        if (arr != null && arr.length > 0) return arr[0];
        return "";
    }

    private static String decode(String s) {
        if (s == null) return null;
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    private static void outputHtml(String message) {
        String query = System.getenv("QUERY_STRING");
        if (query != null) {
            System.out.println("Content-Type: text/html\n");
            System.out.println("<html><body><p>" + escapeHtml(message) + "</p></body></html>");
        } else {
            System.out.println(message);
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
