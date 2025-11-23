# Docker Container for MadHacks Recipe Management System

This Docker container runs the Java-based recipe management system that processes recipe submissions from query strings, as analyzed in the design document.

## Prerequisites

- Docker installed on your system

## Building the Container

```bash
docker build -t madhacks-recipe-app .
```

## Running the Container

### Option 1: Using Docker Compose (if available)

```bash
docker-compose up
```

### Option 2: Using Docker Run (recommended)

To run the container with a recipe submission query string:

```bash
docker run --rm -v $(pwd)/recipeIn.csv:/app/recipeIn.csv -v $(pwd)/recipeOut.csv:/app/recipeOut.csv madhacks-recipe-app "RecipeName=Chocolate+Cake&CookTime=45&CookMethod=Baking&Allergens=Eggs&Allergens=Milk&Allergens=Gluten&Ingredients=Flour&Ingredients=Sugar&Ingredients=Eggs&Ingredients=Cocoa+Powder&Ingredients=Butter&Ingredients=Milk&Instructions=Mix+ingredients+and+bake+at+350%C2%B0F+for+45+minutes."
```

## What it does

The container runs the `HandleInput.java` class with a query string argument, which:
1. Parses the URL-encoded query string containing recipe data
2. Creates a Recipe object from the parsed data
3. Saves the recipe to `recipeOut.csv`
4. Outputs a confirmation message

## Volume Mounting

The container mounts the CSV files as volumes so that:
- Recipe data can persist between runs
- You can add recipes to `recipeIn.csv` and see them when running the container
- New recipes saved by the web interface would go to `recipeOut.csv`

## Expected Output

The container will output a confirmation message indicating whether the recipe was successfully saved to `recipeOut.csv`. For example:

```
Recipe added successfully!
```

If there are any errors in parsing or saving, appropriate error messages will be displayed.

## Web Interface

To run the full web interface, you would need to:
1. Start a web server to serve the HTML files (e.g., `python -m http.server`)
2. Configure CGI for the Java classes (more complex setup required)

The Docker container provides a command-line interface for processing recipe submissions via query strings, simulating the CGI functionality.