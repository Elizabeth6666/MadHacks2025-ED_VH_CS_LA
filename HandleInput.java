import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
                return renderTemplate("templates/success.html", "Error", "Missing recipe name; nothing saved.");
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

            return renderTemplate("templates/success.html", "Success!", "the record was written");

        } catch (Exception e) {
            return renderTemplate("templates/success.html", "Error", "Error saving recipe: " + e.getMessage());
        }
    }

    private static String renderTemplate(String file, String arg1, String arg2) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(file)));
            content = content.replace("{arg1}", arg1).replace("{arg2}", arg2);
            return content;
        } catch (Exception e) {
            return "<html><body><h1>Error</h1><p>Template error: " + e.getMessage() + "</p></body></html>";
        }
    }

    static class SubmitHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Log request
            System.out.println("=== REQUEST RECEIVED ===");
            System.out.println("Method: " + exchange.getRequestMethod());
            System.out.println("URI: " + exchange.getRequestURI());
            System.out.println("Headers:");
            exchange.getRequestHeaders().forEach((key, values) ->
                System.out.println("  " + key + ": " + String.join(", ", values)));

            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), "UTF-8");
                System.out.println("Body: " + body);

                String response = processRecipe(body);

                // Log response
                System.out.println("=== RESPONSE TO SEND ===");
                System.out.println("Status: 200");
                System.out.println("Content-Type: text/html");
                System.out.println("Access-Control-Allow-Origin: *");
                System.out.println("Body: " + response);
                System.out.println("========================");

                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Log response for method not allowed
                System.out.println("=== RESPONSE TO SEND ===");
                System.out.println("Status: 405");
                System.out.println("========================");

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
