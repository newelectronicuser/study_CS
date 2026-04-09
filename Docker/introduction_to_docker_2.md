# 🐳 Introduction to Docker: Study Guide

Docker is a powerful platform for building, running, and shipping applications by utilizing **containerization**. It solves the classic "it works on my machine" problem by packaging software into standardized units.

---

## 1. What is Docker? 🚀
Docker is a tool that allows developers to package an application with all of its dependencies (code, runtime, libraries, settings) into a **container**. This ensures that the application runs identically on any machine, whether it's a developer's laptop, a testing environment, or a production server.

---

## 2. Virtual Machines vs. Containers 🏗️
Understanding the difference between VMs and Containers is a common interview topic.

| Feature | Virtual Machines (VMs) | Containers |
| :--- | :--- | :--- |
| **Isolation** | Full hardware-level isolation. | Process-level isolation via OS kernel. |
| **Operating System** | Each VM includes a full-blown OS. | Shares the host machine's OS kernel. |
| **Startup Time** | Slow (minutes) due to OS boot. | Fast (seconds) – just another process. |
| **Resources** | High overhead (RAM/CPU/Disk). | Lightweight and highly efficient. |
| **Scalability** | Difficult and resource-heavy. | Easy and near-instantaneous. |

> [!IMPORTANT]
> **Key Takeaway**: Containers are much more lightweight and start faster because they don't need to boot a separate guest Operating System.

---

## 3. Docker Architecture 🏛️
Docker uses a client-server architecture.

- **Client**: The Docker CLI tool where you type commands (e.g., `docker run`).
- **REST API**: The bridge that communicates commands between the Client and Server.
- **Server (Docker Engine)**: The background process (Daemon) that builds, runs, and manages your containers.

### Kernel Handling by OS
- **Linux**: Containers run directly on the Linux kernel. Native and most efficient.
- **Windows**: Can run both Windows and Linux containers (requires WSL2/Linux kernel support).
- **Mac**: The macOS kernel doesn't natively support containers, so Docker Desktop uses a **lightweight Linux VM** (Moby Linux) to run containers.

---

## 4. The Docker Workflow: Build, Ship, Run 🔄
The standard development cycle in Docker follows these three steps:

1.  **BUILD (Dockerfile)**: You write a text file called a `Dockerfile` that contains instructions on how to build your app's environment.
2.  **SHIP (Image)**: You "build" the Dockerfile into an **Image** (a read-only blueprint). This image is typically pushed to a **Registry** (like Docker Hub).
3.  **RUN (Container)**: You pull the image and "run" it. A running instance of an image is called a **Container**.

> [!TIP]
> **Image vs. Container**: An image is like a **Class** (the blueprint), while a container is an **Object** (the live, running instance).

---

## 5. Essential Data & Networking 🌐
### Persistence (Volumes)
By default, data inside a container is **ephemeral** (lost if the container is deleted).
- **Volumes**: Used to map a folder inside the container to the host machine, ensuring data persists.

### Networking
- **Bridge**: The default network that allows containers on the same host to talk to each other.
- **Host**: Removes isolation and uses the host machine's network directly.

---

## 6. Installation & Tools 🛠️

### Official Installation
- **Ubuntu/Linux**: [Official Docker Engine Install](https://docs.docker.com/engine/install/ubuntu/)
- **Mac/Windows**: Install **Docker Desktop** for a bundled GUI and CLI.

### Management Tools
- **Portainer**: A powerful, open-source container management tool with a clean web UI.
  - [Portainer Official Site](https://www.portainer.io/)

> [!NOTE]
> For multi-container applications (e.g., App + Database), **Docker Compose** is used to manage the entire stack with a single YAML file.
