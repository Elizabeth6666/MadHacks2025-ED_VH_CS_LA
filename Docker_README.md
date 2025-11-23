# Docker Container for MadHacks Recipe Management System

This Docker container runs the Java-based recipe management system that processes recipe submissions from query strings, as analyzed in the design document.

## Prerequisites

- Docker installed on your system

## Building the Container

```bash
docker build -t madhacks-recipe-app .
```

## Running the Application

### Using Docker Compose (recommended)

```bash
docker-compose up
```

This starts both the API backend and the Caddy web server:
- Frontend served at `http://localhost:8000`
- API available at `http://localhost:8080` (proxied through Caddy)

### Using Docker Run (for API only)

To run just the API container with a recipe submission query string:

```bash
docker run --rm -v $(pwd)/recipeIn.csv:/app/recipeIn.csv -v $(pwd)/recipeOut.csv:/app/recipeOut.csv madhacks-recipe-app "RecipeName=Chocolate+Cake&CookTime=45&CookMethod=Baking&Allergens=Eggs&Allergens=Milk&Allergens=Gluten&Ingredients=Flour&Ingredients=Sugar&Ingredients=Eggs&Ingredients=Cocoa+Powder&Ingredients=Butter&Ingredients=Milk&Instructions=Mix+ingredients+and+bake+at+350%C2%B0F+for+45+minutes."
```

## What it does

The container runs the `HandleInput.java` class as a REST API server on port 8080, which:
1. Accepts POST requests to `/submit` with `application/x-www-form-urlencoded` data containing recipe information
2. Parses the form data, creates a Recipe object, and saves it to `recipeOut.csv`
3. Returns a JSON response indicating success or error

Alternatively, the container can be run with command-line arguments for direct processing.

## Volume Mounting

The container mounts the CSV files as volumes so that:
- Recipe data can persist between runs
- You can add recipes to `recipeIn.csv` and see them when running the container
- New recipes saved by the web interface would go to `recipeOut.csv`

## Expected Output

When running as API server, the container starts the server and logs "Server started on port 8080".

For API requests, it returns JSON responses like:

```json
{"message": "Recipe saved successfully."}
```

or

```json
{"error": "Error message"}
```

When run with command-line arguments, it outputs the JSON result to console.

## Web Interface

The Docker Compose setup includes Caddy as a reverse proxy that:
- Serves the static HTML files from the project directory
- Proxies API requests to `/submit` to the backend service
- Eliminates CORS issues by serving everything from the same origin

Simply run `docker-compose up` and access `http://localhost:8000` for the full application.