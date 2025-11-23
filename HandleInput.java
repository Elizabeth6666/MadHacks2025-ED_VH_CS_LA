import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class HandleInput {

	public static void main(String[] args) {
		try {
			String query = System.getenv("QUERY_STRING");
			if ((query == null || query.isEmpty()) && args.length > 0) {
				query = args[0];
			}

			Map<String, String> params = parseQuery(query);

			String name = params.getOrDefault("RecipeName", "");
			String cookTime = params.getOrDefault("CookTime", "0");
			String allergens = params.getOrDefault("Allergens", "");
			String ingredients = params.getOrDefault("Ingredients", "");
			String instructions = params.getOrDefault("Instructions", "");

			if (name == null || name.isEmpty()) {
				// nothing to add
				outputHtml("Missing recipe name; nothing saved.");
				return;
			}

			name = decode(name);
			instructions = decode(instructions);
			allergens = decode(allergens);
			ingredients = decode(ingredients);

			Recipe recipe = new Recipe(name);

			// cook time
			try {
				double ct = Double.parseDouble(cookTime);
				recipe.addCookingTime(ct);
			} catch (Exception e) {
				// ignore, leave default
			}

			// allergens (single select in current form). If comma-separated, allow multiple.
			if (allergens != null && !allergens.isEmpty()) {
				String[] al = allergens.split(",");
				for (String a : al) {
					if (!a.trim().isEmpty()) recipe.addAllergens(a.trim());
				}
			}

			// ingredients: allow multiple separated by '|' or ';' or comma
			if (ingredients != null && !ingredients.isEmpty()) {
				String[] parts = ingredients.split("\\||;|,");
				for (String part : parts) {
					part = part.trim();
					if (part.isEmpty()) continue;
					// expected ingredient format: name-amount-measurement OR plain name
					if (part.contains("-")) {
						String[] pieces = part.split("-");
						String iname = pieces[0].trim();
						int amount = 0;
						String measure = "";
						if (pieces.length > 1) {
							try { amount = Integer.parseInt(pieces[1].trim()); } catch (Exception ex) { amount = 0; }
						}
						if (pieces.length > 2) measure = pieces[2].trim();
						recipe.addIngredient(iname, amount, measure);
					} else {
						recipe.addIngredient(part, 0, "");
					}
				}
			}

			// instructions
			recipe.setInstructions(instructions == null ? "" : instructions);

			// Save to CSV via existing helper
			new RecipeToDb(recipe);

			outputHtml("Recipe saved successfully.");

		} catch (Exception e) {
			outputHtml("Error saving recipe: " + e.getMessage());
		}
	}

	private static Map<String, String> parseQuery(String query) {
		Map<String, String> map = new HashMap<>();
		if (query == null) return map;
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			if (pair == null || pair.length() == 0) continue;
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

	private static String decode(String s) {
		if (s == null) return null;
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	private static void outputHtml(String message) {
		// If running as CGI, include content-type header. Otherwise print simple message.
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
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
	}
}

