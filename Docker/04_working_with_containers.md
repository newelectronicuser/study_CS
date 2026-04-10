# 📦 Docker: Working with Containers Study Guide

Mastering container management is essential for developing and deploying scalable applications. This guide covers the lifecycle, networking, and data management of Docker containers.

---

## 🚀 Starting and Stopping Containers
Control the lifecycle of your containers with these essential commands.

```bash
# Run a container (Stops if terminal is exited/closed)
docker run react-app

# Run in Detached Mode (Background)
docker run -d react-app

# Stop a running container
docker stop c1

# Start a stopped container
docker start c1
```

> [!TIP]
> Use Detached Mode (`-d`) for long-running services like web servers or databases to keep your terminal accessible.

---

## 📑 Viewing Logs
Logs are critical for debugging applications running inside containers.

```bash
# View all current logs
docker logs 85cd8481f327

# Follow logs in real-time
docker logs -f 85cd8481f327

# View a specific number of lines
docker logs -n 5 85cd8481f327

# View lines with timestamps
docker logs -n 5 -t 85cd8481f327
```

---

## 🌐 Publishing Ports
Map your container's internal ports to your host machine to make your app accessible.

```bash
# Map host port 2000 to container port 3000
docker run -d -p 2000:3000 --name c1 react-app
```
**Syntax**: `-p [HostPort]:[ContainerPort]`

---

## 🛠️ Executing Commands in Containers
Interact with running containers directly using the `exec` command.

```bash
# Execute a single command (e.g., list files)
docker exec c1 ls

# Access the container shell (Interactive)
docker exec -it c1 sh
docker exec -it c1 bash
```

---

## 🗑️ Removing Containers
Clean up your environment by removing unwanted containers.

```bash
# Remove a stopped container
docker rm c1

# Force remove a running container
docker rm -f c1

# Find a specific container in the list
docker ps -a | grep c1

# Remove all stopped containers
docker container prune
```

---

## 💾 Persisting Data using Volumes
By default, container file systems are temporary. Use volumes to keep data safe across container restarts and deletions.

```bash
# Create a named volume
docker volume create app-data

# Inspect volume details (Mountpoint, Driver, etc.)
docker volume inspect app-data
```

### Mounting a Volume
You can mount a volume to a specific directory inside the container.
```bash
# Syntax: -v [VolumeName]:[ContainerPath]
docker run -d -p 4000:3000 -v app-data:/app/data react-app
```

> [!NOTE]
> Docker will automatically create the volume for you if it doesn't already exist when you run the command.

---

## 📂 Copying Files
Move files between your host machine and a container instance.

```bash
# Copy FROM container TO host
docker cp 9e2d9166949e:/app/log.txt .

# Copy FROM host TO container
docker cp secret.txt 9e2d9166949e:/app
```

---

## 🔄 Sharing Source Code (Bind Mounts)
Live-link your host code to the container so that changes are reflected immediately without rebuilding the image.

```bash
# Maps current directory to /app inside the container
docker run -d -p 5001:3000 -v $(pwd):/app react-app
```

---

> [!IMPORTANT]
> Refer to **Docker - Working with Containers.pdf** for detailed visual diagrams of container networking and volume mounting.
