import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.*;

public class HandleInput {

    public static void main(String[] args) {
        if (args.length > 0) {
            // Command line mode
            String query = args[0];
            String result = processRecipe(query);
            System.out.println(result);
        } else {
            // API server mode
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/submit", new SubmitHandler());
                server.setExecutor(null);
                server.start();
                System.out.println("Server started on port 8080");
            } catch (Exception e) {
                System.err.println("Error starting server: " + e.getMessage());
            }
        }
    }

    private static String processRecipe(String query) {
        try {
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
                return "{\"error\": \"Missing recipe name; nothing saved.\"}";
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

            return "{\"message\": \"Recipe saved successfully.\"}";

        } catch (Exception e) {
            return "{\"error\": \"Error saving recipe: " + e.getMessage() + "\"}";
        }
    }

    static class SubmitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), "UTF-8");
                String response = processRecipe(body);
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method not allowed
            }
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
