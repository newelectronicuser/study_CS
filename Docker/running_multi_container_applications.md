# 🐳 Running Multi-container Applications: Study Guide

Managing complex applications with multiple services (frontend, backend, database) is simplified using **Docker Compose**. This tool allows you to define and run multi-container applications with a single YAML file.

---

## 📑 Overview
In this module, we cover:
- **Docker Compose**: Orchestrating multiple containers.
- **Docker Networking**: Enabling communication between services.
- **Database Migration**: Managing schema changes in a containerized environment.
- **Running Automated Tests**: Integrating testing into your container workflow.

---

## ⚡ Docker Compose Commands
Essential commands for managing your application stack.

```bash
docker compose build            # Build or rebuild services
docker compose build --no-cache # Build without using cache
docker compose up               # Create and start containers
docker compose up -d            # Run in detached mode (background)
docker compose up --build        # Rebuild images before starting
docker compose down             # Stop and remove containers, networks, and images
docker compose ps               # List containers for the current project
docker compose logs             # View output from all services
```

### Installation
Official guide: [Install Docker Compose](https://docs.docker.com/compose/install/)

---

## 🧹 Cleaning Up the Workspace
Sometimes you need a fresh start. Use these commands to mass-remove images and containers.

```bash
# List all image IDs
docker images -q

# Remove all containers forcefully
docker container rm -f $(docker container ls -q)

# Remove all images
docker image rm $(docker image ls -q)
```

---

## 🏗️ Creating a Compose File
The `docker-compose.yml` file is the heart of your multi-container setup.

> [!NOTE]
> `version: "3.8"` is becoming obsolete. Modern versions use the `name:` top-level element. See [Docker Docs](https://docs.docker.com/compose/compose-file/04-version-and-name/) for details.

### Sample Docker Compose File
```yaml
version: "3.8"

services:
  frontend:
    depends_on:
      - backend
    build: ./frontend
    ports:
      - 3000:3000

  backend:
    depends_on:
      - db
    build: ./backend
    ports:
      - 3001:3001
    environment:
      DB_URL: mongodb://db/vidly
    command: ./docker-entrypoint.sh

  db:
    image: mongo:4.0-xenial
    ports:
      - 27017:27017
    volumes:
      - vidly:/data/db

volumes:
  vidly:
```

---

## 🔨 Building Images
You can build all services or specific ones defined in your compose file.

```bash
docker compose build            # Build all services
docker compose build --no-cache # Build from scratch
```

### Common Options:
- `--build-arg`: Set build-time variables.
- `--pull`: Always attempt to pull a newer version of the image.
- `--no-cache`: Do not use cache.
- `-q, --quiet`: Suppress output.

---

## 🚀 Starting and Stopping the Application
```bash
docker compose up          # Start all services
docker compose up --build   # Force rebuild before starting
docker compose up -d       # Run in background

# Inspecting running services
docker compose ps          # Only shows services for this project
docker ps                  # Global view of ALL containers on the host
```

---

## 🌐 Docker Networking
Containers on the same network can communicate using their **service name** (e.g., `frontend`, `backend`, `db`).

```bash
# Check existing networks
docker network ls

# Example: From inside the backend container
ping frontend
ping db
```

> [!TIP]
> Docker includes an **embedded DNS server** that automatically resolves service names to their internal IP addresses within the network.

---

## 📑 Viewing Logs
Monitor the output of your entire stack or specific services.

```bash
docker compose logs        # Merged logs from all containers
docker logs -f [id]        # Follow logs for a specific container
```

---

## 🔄 Publishing Changes
To see code changes immediately without rebuilding images, use **Volumes** to link your host directory to the container.

```yaml
services:
  frontend:
    volumes:
      - ./frontend:/app
```

---

## 🗄️ Migrating the Database
Controlling the startup order is crucial for database-dependent services.

> [!IMPORTANT]
> `depends_on` only ensures the container starts, not that the database *inside* is ready. Use a wait-for script to control execution.

### Overriding the CMD
In your `docker-compose.yml`:
```yaml
backend:
  command: ./docker-entrypoint.sh
```

### The Entrypoint Script (`docker-entrypoint.sh`)
```bash
#!/bin/sh

echo "Waiting for MongoDB to start..."
./wait-for db:27017

echo "Migrating the database..."
npm run db:up

echo "Starting the server..."
npm start
```

---

## 🧪 Running Tests
You can define specialized services just for running automated tests.

```yaml
services:
  backend:
    # ... backend config ...

  backend-tests:
    image: vidly_web # Uses the same image as the backend
    command: npm test
```
