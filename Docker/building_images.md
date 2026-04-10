# 🏗️ Docker: Building Images Study Guide

An image is the blueprint of your application, containing everything needed to run it in an isolated environment.

---

## 1. Images vs. Containers 📂

| Component    | Image (Blueprint)                                 | Container (Live Process)                   |
| :----------- | :------------------------------------------------ | :----------------------------------------- |
| **Contents** | Cut-down OS, libraries, app files, env variables. | An isolated environment running the image. |
| **State**    | Read-only / Immutable.                            | Can be stopped, restarted, and modified.   |
| **Nature**   | A file/package.                                   | A running process on the host.             |

---

## 2. Dockerfile Instructions 📜

A `Dockerfile` is a text file containing instructions for building an image.

| Instruction    | Purpose                                                             |
| :------------- | :------------------------------------------------------------------ |
| **FROM**       | Specifies the base image to build upon.                             |
| **WORKDIR**    | Sets the working directory inside the container.                    |
| **COPY**       | Copies files/directories from the host into the container.          |
| **ADD**        | Similar to COPY, but supports remote URLs and automatic extraction. |
| **RUN**        | Executes a command during the image build process.                  |
| **ENV**        | Sets environment variables.                                         |
| **EXPOSE**     | Documents the port the container intends to use.                    |
| **USER**       | Sets the user to run subsequent instructions.                       |
| **CMD**        | Provides defaults for a running container (Runtime).                |
| **ENTRYPOINT** | Configures a container that will run as an executable.              |

> [!TIP]
> **Choosing the Right Base Image**: Visit the official [Docker Samples](https://docs.docker.com/reference/samples/) to find optimized images for your platform.

---

## 3. Building and Running 🚀

To build an image, navigate to the directory containing your `Dockerfile` and run:

```bash
# Build an image tagged as 'react-app' using current directory (.)
sudo docker build -t react-app .

# Run the image in interactive terminal mode
docker run -it react-app:latest

# Override entrypoint to access the shell
docker run -it react-app bash     # May fail if Alpine (use 'sh' instead)
docker run -it react-app sh       # Safest for Alpine-based images
```

---

## 4. Workflows: Copying & Ignoring Files 🛠️

### Setting the Workspace

```dockerfile
WORKDIR /app
COPY . .  # Copy all files from current host dir to /app
```

### Excluding Files (.dockerignore)

Create a `.dockerignore` file to prevent large or sensitive directories from being sent to the Docker daemon.

```text
node_modules/
```

---

## 5. Environment & Configuration 🌍

### Environment Variables

```dockerfile
ENV API_URL=http://api.myapp.com/
```

### Exposing Ports

```dockerfile
EXPOSE 3000
```

### Setting the User (Security Best Practice)

Avoid running as root by creating a dedicated user and group.

```dockerfile
RUN addgroup app && adduser -S -G app app
USER app
```

---

## 6. CMD vs. ENTRYPOINT ⚙️

- **RUN**: Executed at **Build Time**.
- **CMD / ENTRYPOINT**: Executed at **Run Time**.

### Shell Form vs. Exec Form

| Form           | Syntax                 | Behavior                                                |
| :------------- | :--------------------- | :------------------------------------------------------ |
| **Shell Form** | `CMD npm start`        | Runs in `/bin/sh`. Spawns a separate shell process.     |
| **Exec Form**  | `CMD ["npm", "start"]` | **Recommended**. Runs directly without a shell process. |

> [!IMPORTANT]
> You can override **CMD** at runtime: `docker run react-app npm run start`.
> **ENTRYPOINT** is harder to override and is used for executables.

---

## 7. Optimizing Builds (Caching) ⚡

To speed up builds, separate the installation of dependencies from the copying of source code. This allows Docker to cache the `npm install` layer independently.

```dockerfile
# Step 1: Install dependencies (only reruns if package.json changes)
COPY --chown=dev:dev package*.json ./
RUN npm install

# Step 2: Copy the rest of the code
COPY --chown=dev:dev . .
```

## ![Docker Installation](files/Screenshot%20from%202026-03-23%2019-04-44.png)

## 8. Managing Images & Containers 🧹

### Pruning (Cleanup)

```bash
docker image prune      # Removes unused/dangling images
docker container prune  # Removes all stopped containers
```

### Tagging

```bash
# Tag during build
docker build -t react-app:1 .

# Tag an existing image
docker image tag react-app:latest react-app:2
```

---

## 9. Sharing Images ☁️

1. Create an account at [hub.docker.com](https://hub.docker.com/).
2. Create a repository.
3. Tag and push:

```bash
# Tag with repository name
docker image tag [IMAGE_ID] codewithmosh/react-app:2

# Login and push
docker login
docker push codewithmosh/react-app:2

# Re-tag and check local list
docker image tag react-app:3 codewithmosh/react-app:3
docker images
```

---

## 10. Saving & Loading Images 💾

Ideal for transferring images without a registry.

```bash
# Save to a tar file
docker image save -o react-app.tar ae8320aeea80

# Load from a tar file
docker image load -i react-app.tar
```

> [!NOTE]
> Refer to **Docker - Building Images.pdf** for additional detailed diagrams.
