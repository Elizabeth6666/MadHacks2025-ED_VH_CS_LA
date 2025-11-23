# MadHacks2025-ED_VH_CS_LA Recipe Management System - Design Document

## Project Overview

This is a Java 11-based recipe management system designed for students to organize and access recipes based on dietary restrictions, cooking methods, and time constraints. The system provides both command-line and web-based interfaces for uploading, searching, and viewing recipes. The project aims to help busy students plan nutritious meals efficiently.

## Architecture Overview

The system follows a simple layered architecture:
- **Presentation Layer**: HTML forms and static pages for user interaction
- **Application Layer**: Java classes handling business logic and data processing
- **Data Layer**: CSV files serving as the "database" for recipe storage

## File Structure and Components

### Java Source Files

#### `mainMethod.java`
- **Purpose**: Main entry point for command-line execution
- **Methods**:
  - `main(String[] args)`: Demonstrates recipe loading and printing functionality
- **Functionality**: Creates a `DBToRecipe` instance with "All" cook methods and "None" allergens, loads recipes, and prints them to console

#### `HandleInput.java`
- **Purpose**: CGI handler for processing web form submissions
- **Methods**:
  - `main(String[] args)`: Parses query string or command-line arguments, creates Recipe objects, saves to database
  - `parseQuery(String query)`: Parses URL-encoded query strings into parameter maps
  - `getFirst(String[] arr)`: Extracts first element from string array
  - `decode(String s)`: URL-decodes strings
  - `outputHtml(String message)`: Outputs HTML response or plain text based on environment
  - `escapeHtml(String s)`: Escapes HTML special characters
- **Functionality**: Handles recipe submission from web forms, validates input, creates Recipe instances, and persists them

#### `Recipe.java`
- **Purpose**: Core data model representing a recipe
- **Fields**:
  - `ingredients`: ArrayList<String> of ingredient names
  - `recipeName`: String recipe title
  - `allergens`: ArrayList<String> of allergens
  - `cookTimeInMinutes`: double cooking time
  - `cookMethod`: String cooking method
  - `recipeID`: String unique identifier
  - `instructions`: String cooking instructions
- **Methods**:
  - `Recipe(String name)`: Constructor
  - `addIngredient(String)`, `addCookingTime(double)`, `addCookMethod(String)`, `addAllergens(String)`: Mutators
  - `removeIngredient(String)`, `removeAllergen(String)`: Removal methods
  - `setID(String)`, `setInstructions(String)`: Setters
  - `getIngredients()`, `getName()`, `getCookMethod()`, `getCookTimeInMinutes()`, `getRecipeID()`, `getAllergens()`, `getInstructions()`: Accessors
  - `toString()`: Returns formatted string representation
- **Notes**: Ingredients are stored as simple strings, not using the `Ingredient` class

#### `Ingredient.java`
- **Purpose**: Detailed ingredient model (currently unused)
- **Fields**: `nameIngredient`, `amountNumber`, `measurement`
- **Methods**: Constructor, getters, setters, `toString()`
- **Status**: Not integrated into the Recipe class

#### `User.java`
- **Purpose**: User profile model (currently unused)
- **Fields**: `allergies`, `methods`, `favoriteRecipes`, `userID`, `username`, `isStudent`
- **Methods**: Constructor, getters/setters, allergy/recipe management, `printUserSummary()`
- **Status**: Not integrated into the main application flow

#### `DBToRecipe.java`
- **Purpose**: Loads recipes from CSV and provides filtering functionality
- **Fields**: `recipeList`, file paths (`recipeIn.csv`, `recipeOut.csv`)
- **Methods**:
  - `DBToRecipe(String cookMethodIn, String allergenIn)`: Constructor that loads all recipes
  - `getAll()`: Parses CSV file and creates Recipe objects
  - `sort()`: Filters recipes by cook method and allergens
- **CSV Format**: `ID,Name,Ingredients_,CookTime,CookMethod,Allergens_,Instructions`
- **Filtering Logic**:
  - Cook Method: "All" returns all, "Dorm" filters to microwave/fridge/no-cook, others exact match
  - Allergens: "None" returns all, otherwise excludes recipes containing specified allergen

#### `RecipeToDB.java`
- **Purpose**: Saves recipes to CSV "database"
- **Methods**:
  - `RecipeToDB(Recipe recipe)`: Constructor that calls `addToDB()`
  - `addToDB()`: Validates recipe completeness, generates ID, formats and appends to CSV
- **ID Generation**: Counts existing lines in `recipeIn.csv` and creates "ID" + count
- **Output**: Writes to `recipeOut.csv` in same format as input

### Web Interface Files

#### `index.html`
- **Purpose**: Landing page with navigation
- **Features**: Navbar with links to My Recipes, Upload Recipes, Search
- **Styling**: Uses `style.css`

#### `AddRecipe.html`
- **Purpose**: Recipe submission form
- **Fields**: Recipe name, cook time, cook method (dropdown), allergens (checkboxes), ingredients (comma-separated), instructions
- **Submission**: GET request to `http://localhost:8000/submit`
- **Styling**: Uses `styleRecipeForm.css`

#### `search.html`
- **Purpose**: Recipe search interface
- **Fields**: Allergen dropdown, cook method dropdown
- **Issues**: Form action points to `HandleInput.java` (incorrect for web), method is POST
- **Styling**: Uses `searchstyle.css`

#### `myrecipes.html`
- **Purpose**: User recipes display page
- **Status**: Empty/placeholder

### Data Files

#### `recipeIn.csv`
- **Purpose**: Input data source for recipes
- **Current State**: Contains only an empty line
- **Format**: ID,Name,Ingredients_,CookTime,CookMethod,Allergens_,Instructions

#### `recipeOut.csv`
- **Purpose**: Output destination for new recipes
- **Current State**: Empty
- **Issue**: Mismatch with input file - recipes saved here but loaded from `recipeIn.csv`

### Configuration and Documentation

#### `commands`
- **Content**: `python -m http.server`
- **Purpose**: Command to start simple HTTP server for serving static files

#### `plan.txt`
- **Content**: High-level TODO list including ingredient sorter, upload/view functionality, text-to-speech, etc.
- **Status**: Many features not implemented

#### `ElevatorPitch.txt`
- **Content**: Project description emphasizing student-focused meal planning with allergy/cooking filters

#### `README.md`
- **Content**: Minimal - just project title and "MadHacks Repository"

### Styling Files
- `style.css`: Main page styling
- `styleRecipeForm.css`: Recipe form styling
- `searchstyle.css`: Search page styling

## Data Flow

### Recipe Creation Flow
1. User fills `AddRecipe.html` form
2. Form submits GET request to `http://localhost:8000/submit` (CGI endpoint)
3. `HandleInput.main()` parses query string
4. Creates `Recipe` object from form data
5. Instantiates `RecipeToDB(recipe)` which saves to `recipeOut.csv`

### Recipe Retrieval Flow
1. `mainMethod` or other code creates `DBToRecipe(cookMethod, allergen)`
2. `getAll()` reads and parses `recipeIn.csv`
3. `sort()` filters recipes based on criteria
4. Returns `ArrayList<Recipe>` for display/processing

### Command Line Execution
- `java mainMethod` loads all recipes and prints them to console

## Current Functionality Assessment

### What Works
- ✅ Basic recipe object creation and manipulation
- ✅ CSV parsing and recipe loading from file
- ✅ Recipe filtering by cook method and allergens
- ✅ Web form for recipe submission
- ✅ CGI-style form processing
- ✅ Recipe persistence to CSV
- ✅ Command-line recipe listing
- ✅ Basic HTML interface structure

### What Doesn't Work / Issues
- ❌ **Data Persistence Mismatch**: Recipes saved to `recipeOut.csv` but loaded from `recipeIn.csv`
- ❌ **Search Functionality**: `search.html` form action incorrect, no backend handler
- ❌ **Recipe Display**: No mechanism to display recipes in web interface
- ❌ **User Management**: `User` class exists but not integrated
- ❌ **Ingredient Details**: `Ingredient` class not used; recipes use simple strings
- ❌ **ID Generation**: Counts lines in wrong file (`recipeIn.csv` instead of `recipeOut.csv`)
- ❌ **Error Handling**: Minimal validation in `RecipeToDB.addToDB()`
- ❌ **CGI Setup**: Requires specific server configuration on port 8000
- ❌ **Data Migration**: No way to move saved recipes to input file
- ❌ **Search Form Issues**: Duplicate options, wrong action URL
- ❌ **Empty Pages**: `myrecipes.html` is blank
- ❌ **CSV Format Assumptions**: Parsing assumes specific underscore separators
- ❌ **No Sample Data**: CSV files empty, hard to test

### Partially Working
- ⚠️ **Web Interface**: Basic structure exists but search/display incomplete
- ⚠️ **Filtering Logic**: Works but "Dorm" method grouping is hardcoded

## Technical Debt and Recommendations

### High Priority
1. **Fix Data Flow**: Decide on single CSV file or implement data migration between input/output files
2. **Implement Search**: Create proper CGI handler for search functionality
3. **Add Recipe Display**: Build pages to show recipe details and lists
4. **Fix ID Generation**: Use correct file for counting existing recipes

### Medium Priority
1. **Integrate User Class**: Add user profiles and favorites functionality
2. **Use Ingredient Class**: Replace string ingredients with detailed objects
3. **Improve Error Handling**: Add validation and user-friendly error messages
4. **Add Sample Data**: Populate CSV with test recipes

### Low Priority
1. **Text-to-Speech**: Implement audio recipe reading (mentioned in plan)
2. **Ingredient Sorting**: Add shopping list generation
3. **Recipe Deletion**: Add remove functionality
4. **Advanced Filtering**: Time-based, ingredient-based searches

## Deployment and Execution

### Requirements
- Java 11+
- HTTP server for CGI (or Python's `http.server` for static files)
- CGI setup for Java execution

### Running the Application
1. Run `docker-compose up` to start both frontend (Caddy on port 8000) and backend (API on port 8080)
2. Access `index.html` at `http://localhost:8000`
3. Forms submit POST requests to `/submit` (proxied to API)
4. Command line: `java HandleInput "query_string"` or Docker with arguments

### Testing
- Add recipes via web form (submits to API)
- Check API responses and CSV files for data persistence
- Use command-line mode for direct testing

## Conclusion

The project demonstrates a solid foundation for a recipe management system with proper separation of concerns and extensible architecture. The core functionality of recipe creation, storage, and filtering is implemented and working. However, several integration issues and missing features prevent it from being a complete, user-friendly application. The web interface needs completion, data flow requires fixing, and additional features from the plan need implementation to fulfill the project's goals of helping students with meal planning.