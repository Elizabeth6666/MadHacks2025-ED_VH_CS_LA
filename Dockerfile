# Use Ubuntu with OpenJDK
FROM ubuntu:20.04

# Install OpenJDK 11
RUN apt-get update && apt-get install -y openjdk-11-jdk && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy all Java source files
COPY *.java ./

# Compile Java files
RUN javac *.java

# Create a script to run the main class
RUN echo '#!/bin/bash\njava HandleInput "$@"' > run.sh && chmod +x run.sh

# Expose any ports if needed (though this is command-line only)
# EXPOSE 8000

# Expose port for API
EXPOSE 8080

# Set entrypoint
ENTRYPOINT ["./run.sh"]