import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HandleInput {

    public static void main(String[] args) {
        try {
            // Get query string either from CGI environment or command-line argument
            String query = System.getenv("QUERY_STRING");
            if ((query == null || query.isEmpty()) && args.length > 0) {
                query = args[0];
            }

            Map<String, String> params = parseQuery(query);

            String name = decode(params.getOrDefault("RecipeName", ""));
            String cookTime = params.getOrDefault("CookTime", "0");
            String allergens = decode(params.getOrDefault("Allergens", ""));
            String ingredients = decode(params.getOrDefault("Ingredients", ""));
            String instructions = decode(params.getOrDefault("Instructions", ""));

            if (name == null || name.isEmpty()) {
                outputHtml("Missing recipe name; nothing saved.");
                return;
            }

            Recipe recipe = new Recipe(name);

            // Cook time
            try {
                double ct = Double.parseDouble(cookTime);
                recipe.addCookingTime(ct);
            } catch (Exception e) {
                // ignore, leave default
            }

            // Allergens (comma-separated)
            if (allergens != null && !allergens.isEmpty()) {
                String[] al = allergens.split(",");
                for (String a : al) {
                    a = a.trim();
                    if (!a.isEmpty()) recipe.addAllergens(a);
                }
            }

            // Ingredients (just names, separated by |, ;, or ,)
            if (ingredients != null && !ingredients.isEmpty()) {
                String[] parts = ingredients.split("\\||;|,");
                for (String part : parts) {
                    part = part.trim();
                    if (!part.isEmpty()) {
                        recipe.addIngredient(part);
                    }
                }
            }

            // Instructions
            recipe.setInstructions(instructions == null ? "" : instructions);

            // Save to DB (or file) via your helper
            new RecipeToDB(recipe);

            outputHtml("Recipe saved successfully.");

        } catch (Exception e) {
            outputHtml("Error saving recipe: " + e.getMessage());
        }
    }

    // Parse URL-encoded query string into a map
    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            if (pair == null || pair.isEmpty()) continue;
            int idx = pair.indexOf('=');
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    map.put(key, value);
                } else {
                    String key = URLDecoder.decode(pair, "UTF-8");
                    map.put(key, "");
                }
            } catch (UnsupportedEncodingException e) {
                // ignore
            }
        }
        return map;
    }

    // Decode a single string safely
    private static String decode(String s) {
        if (s == null) return null;
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    // Output either HTML (for CGI) or plain text
    private static void outputHtml(String message) {
        String query = System.getenv("QUERY_STRING");
        if (query != null) {
            System.out.println("Content-Type: text/html\n");
            System.out.println("<html><body><p>" + escapeHtml(message) + "</p></body></html>");
        } else {
            System.out.println(message);
        }
    }

    // Escape HTML special characters
    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
