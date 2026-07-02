# Docker — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is Docker and how does it differ from a virtual machine?**

**A:** Docker is a containerization platform that packages an application and its dependencies into a lightweight, portable unit called a container. Unlike a VM, which runs a full guest OS on top of a hypervisor, Docker containers share the host OS kernel. This makes containers faster to start, smaller in size, and more resource-efficient. VMs provide stronger isolation (full OS boundary), while containers provide process-level isolation using Linux namespaces and cgroups.

---

**Q2. Explain the difference between a Docker image and a Docker container.**

**A:** A Docker **image** is a read-only, layered filesystem snapshot that contains the application code, runtime, libraries, and configuration. An image is a blueprint. A Docker **container** is a running (or stopped) instance of an image — it adds a thin writable layer on top of the image layers. Multiple containers can run from the same image without affecting each other.

---

**Q3. What is a Dockerfile? Describe the most commonly used Dockerfile instructions.**

**A:** A Dockerfile is a text file containing a series of instructions to build a Docker image. Common instructions:
- `FROM` — sets the base image
- `RUN` — executes commands during the build
- `COPY` / `ADD` — copies files into the image
- `WORKDIR` — sets the working directory
- `ENV` — sets environment variables
- `EXPOSE` — documents the port the container listens on
- `CMD` — default command to run at container start
- `ENTRYPOINT` — the executable that always runs

---

**Q4. What is the Docker build context and why does it matter?**

**A:** The build context is the set of files sent to the Docker daemon when running `docker build`. By default, it's the current directory. Large contexts slow down builds because all files are transferred to the daemon — even if most aren't used. Use `.dockerignore` to exclude unnecessary files and reduce context size.

---

**Q5. Explain the concept of Docker layers. How does layer caching work?**

**A:** Each Dockerfile instruction creates a new read-only layer on top of the previous one. Docker caches each layer by its instruction and context. On a rebuild, Docker reuses cached layers until it encounters a change, then rebuilds from that point forward. To maximize cache hits, put rarely changing instructions (like installing dependencies) before frequently changing ones (like copying source code).

---

**Q6. What is the difference between `CMD` and `ENTRYPOINT` in a Dockerfile?**

**A:** `ENTRYPOINT` defines the executable that always runs when the container starts. `CMD` provides default arguments to `ENTRYPOINT` (or a default command if no `ENTRYPOINT` is set). When both are used together, `CMD` acts as default args that can be overridden at runtime, while `ENTRYPOINT` remains fixed.

```dockerfile
ENTRYPOINT ["java", "-jar"]
CMD ["app.jar"]  # overridable: docker run image other.jar
```

---

**Q7. What is the difference between `COPY` and `ADD` in a Dockerfile?**

**A:** Both copy files from the build context into the image. `ADD` has extra capabilities: it can auto-extract `.tar` archives and fetch files from URLs. However, `COPY` is preferred for simple file copying because its behavior is explicit and predictable. Use `ADD` only when you specifically need its extra features.

---

**Q8. What is a multi-stage build and when would you use it?**

**A:** A multi-stage build uses multiple `FROM` statements in a single Dockerfile, each starting a new stage. You can copy artifacts from one stage into another, discarding the build tools and intermediate files. This produces much smaller final images.

```dockerfile
FROM maven:3.9 AS builder
RUN mvn package

FROM openjdk:17-jre-slim
COPY --from=builder /app/target/app.jar /app.jar
CMD ["java", "-jar", "/app.jar"]
```

Use it when build tools (compilers, test frameworks) should not be in the production image.

---

**Q9. What is `.dockerignore` and why is it important?**

**A:** `.dockerignore` works like `.gitignore` — it specifies files and directories to exclude from the build context. Benefits: faster builds (smaller context), prevents accidentally including secrets or large files like `node_modules`, and avoids cache invalidation from irrelevant file changes.

---

**Q10. What is Docker Hub and what alternatives exist for private image registries?**

**A:** Docker Hub is the default public container registry. Alternatives for private registries:
- **AWS ECR** (Elastic Container Registry)
- **Google Artifact Registry** (GCP)
- **Azure Container Registry**
- **JFrog Artifactory**
- **Harbor** (open-source, self-hosted)
- **GitHub Container Registry (GHCR)**

---

## Networking

**Q11. What are the different Docker network drivers? When do you use each?**

**A:**
- `bridge` (default): Isolated private network on the host. Containers on the same bridge can communicate. Best for single-host apps.
- `host`: Container shares the host's network stack. No NAT overhead. Best for performance-critical apps.
- `none`: No networking. Container is fully isolated.
- `overlay`: Multi-host network for Docker Swarm or distributed apps across hosts.
- `macvlan`: Assigns a MAC address to the container, making it appear as a physical device on the network.

---

**Q12. How does DNS resolution work between containers on the same Docker network?**

**A:** Docker has a built-in DNS server (at 127.0.0.11) that resolves container names and service names within a user-defined network. If container A wants to reach container B, it can use B's container name or service name as a hostname. This only works on user-defined networks, not the default `bridge` network.

---

**Q13. What is the difference between `host`, `bridge`, and `none` network modes?**

**A:** `bridge` creates an isolated virtual network; containers get their own IP but can communicate within the bridge. `host` makes the container share the host's network namespace — it uses the host IP directly (faster, but no port isolation). `none` gives the container no network interfaces at all.

---

**Q14. How would you connect two containers running in separate Compose files?**

**A:** Define an external network in both Compose files:

```yaml
# In each docker-compose.yml
networks:
  shared-net:
    external: true
```

Create the network first: `docker network create shared-net`. Then both Compose stacks can communicate over it.

---

**Q15. What is a Docker overlay network and when is it used?**

**A:** An overlay network spans multiple Docker hosts, enabling containers on different machines to communicate as if on the same LAN. It uses VXLAN encapsulation. Used in Docker Swarm for multi-host service communication.

---

## Storage & Volumes

**Q16. What is the difference between a Docker volume, a bind mount, and a tmpfs mount?**

**A:**
- **Volume**: Managed by Docker, stored in `/var/lib/docker/volumes`. Persists after container removal. Best for production data.
- **Bind mount**: Maps a specific host directory into the container. Gives direct access to host filesystem. Best for development.
- **tmpfs**: Stored in host memory only. Not persisted to disk. Best for sensitive or ephemeral data.

---

**Q17. When would you prefer a named volume over a bind mount?**

**A:** Named volumes are preferred in production because Docker manages their lifecycle, they work consistently across OSes, and they can be backed up, restored, and shared across containers. Bind mounts are better for development when you want live reloading from your host filesystem.

---

**Q18. How do you share a volume between multiple containers?**

**A:** Define the same named volume in multiple container configurations:

```yaml
services:
  app1:
    volumes:
      - shared-data:/data
  app2:
    volumes:
      - shared-data:/data
volumes:
  shared-data:
```

---

**Q19. What happens to data in a container when the container is removed?**

**A:** Data written to the container's writable layer (not a volume or bind mount) is lost permanently when the container is removed. Only data stored in volumes or bind-mounted directories persists.

---

**Q20. How would you back up and restore data from a Docker volume?**

**A:**
```bash
# Backup
docker run --rm -v myvolume:/data -v $(pwd):/backup alpine \
  tar czf /backup/backup.tar.gz /data

# Restore
docker run --rm -v myvolume:/data -v $(pwd):/backup alpine \
  tar xzf /backup/backup.tar.gz -C /
```

---

## Docker Compose

**Q21. What is Docker Compose and what problem does it solve?**

**A:** Docker Compose is a tool for defining and running multi-container applications using a YAML file (`docker-compose.yml`). It eliminates the need to manually run and link multiple `docker run` commands. With a single `docker compose up`, all services, networks, and volumes are created and started together.

---

**Q22. What is the difference between `docker compose up` and `docker compose up --build`?**

**A:** `docker compose up` starts containers using existing images (or pulls them if not cached locally). `docker compose up --build` forces a rebuild of images from their Dockerfiles before starting containers, ensuring the latest code is included.

---

**Q23. How do you define service dependencies in Compose? What are the limitations of `depends_on`?**

**A:** `depends_on` ensures that a service doesn't start until its dependencies are running. However, it only waits for the container to start — **not for the service inside to be ready**. For true readiness, use `healthcheck` combined with `condition: service_healthy` in `depends_on`.

```yaml
depends_on:
  db:
    condition: service_healthy
```

---

**Q24. How do you pass environment variables to a container in Docker Compose?**

**A:** Three ways:
1. Inline in the Compose file: `environment: - APP_ENV=production`
2. From a `.env` file: `env_file: - .env`
3. From the shell: variables set in the shell are interpolated using `${VAR_NAME}` in the Compose file.

---

**Q25. What is the purpose of the `healthcheck` directive in a Compose service?**

**A:** `healthcheck` runs a command periodically inside the container to determine if it's healthy. Docker uses the result to mark the container as `healthy`, `unhealthy`, or `starting`. Other services with `depends_on: condition: service_healthy` will wait for this status before starting.

---

**Q26. What is a Compose override file and how do you use it?**

**A:** `docker-compose.override.yml` is automatically merged with `docker-compose.yml` when you run `docker compose up`. Use it to define environment-specific overrides (e.g., development bind mounts, extra ports). For other environments: `docker compose -f docker-compose.yml -f docker-compose.prod.yml up`.

---

## Security

**Q27. What is the risk of running containers as root? How do you mitigate it?**

**A:** A root container, if compromised, can escalate privileges and access the host. Mitigation: use the `USER` directive in the Dockerfile to switch to a non-root user, and run containers with `--user` flag. Use read-only filesystems (`--read-only`) where possible.

---

**Q28. What are Docker security best practices in production?**

**A:**
- Never run containers as root
- Use minimal base images (distroless, alpine)
- Scan images regularly (Trivy, Snyk, Docker Scout)
- Use read-only filesystems
- Drop unnecessary Linux capabilities (`--cap-drop=ALL`)
- Don't embed secrets in images — use Docker Secrets or external secret managers
- Enable Content Trust (image signing)
- Use network policies to limit container communication

---

**Q29. How does Docker image scanning work? Name some tools.**

**A:** Image scanners analyze image layers for known CVEs (Common Vulnerabilities and Exposures) in OS packages and language dependencies. Tools: **Trivy** (open-source, fast), **Snyk**, **Docker Scout** (built-in to Docker Desktop), **Anchore**, **Grype**.

---

**Q30. What is the difference between `--privileged` and `--cap-add`?**

**A:** `--privileged` grants the container nearly all Linux capabilities and access to host devices — essentially root on the host. `--cap-add` surgically adds only specific capabilities (e.g., `NET_ADMIN`). Always prefer `--cap-add` with the minimum capability needed.

---

**Q31. How would you store and manage secrets in Docker securely?**

**A:**
- **Docker Secrets** (Swarm): Secrets are stored encrypted in Raft and injected as in-memory files at `/run/secrets/`.
- **External secret managers**: HashiCorp Vault, AWS Secrets Manager, GCP Secret Manager.
- Never use environment variables for sensitive secrets in production (they appear in `docker inspect`).

---

## Performance & Optimization

**Q32. How do you reduce the size of a Docker image?**

**A:**
- Use a minimal base image (alpine, distroless, slim)
- Use multi-stage builds to discard build tools
- Combine `RUN` commands to reduce layer count
- Clean up package caches in the same `RUN` statement: `apt-get install -y curl && rm -rf /var/lib/apt/lists/*`
- Use `.dockerignore` to exclude unnecessary files

---

**Q33. How do you profile and debug a running Docker container?**

**A:**
- `docker exec -it <container> sh` — interactive shell inside the container
- `docker logs <container>` — view stdout/stderr
- `docker stats` — live resource usage (CPU, memory, I/O)
- `docker inspect` — detailed metadata
- `docker top <container>` — running processes
- Tools: `nsenter`, `strace`, `tcpdump` for advanced diagnostics

---

**Q34. What is `docker stats` and what metrics does it expose?**

**A:** `docker stats` streams live resource utilization for running containers: CPU usage %, memory usage and limit, network I/O (bytes in/out), block I/O (disk read/write), and number of PIDs.

---

**Q35. How would you limit CPU and memory usage for a Docker container?**

**A:**
```bash
docker run --memory="512m" --cpus="1.5" myimage
```
Or in Compose:
```yaml
deploy:
  resources:
    limits:
      cpus: "1.5"
      memory: 512M
```

---

## Advanced / Production

**Q36. What is Docker BuildKit and what improvements does it bring?**

**A:** BuildKit is Docker's next-generation build engine. Improvements: parallel layer building, more efficient caching, build secrets (never stored in layers), SSH forwarding for private repos, better output formatting, and support for custom build frontends.

---

**Q37. How does Docker handle signal propagation and graceful shutdown?**

**A:** Docker sends `SIGTERM` to PID 1 in the container. If PID 1 doesn't handle it, after `--stop-timeout` (default 10s) it sends `SIGKILL`. Use `exec` form for `CMD`/`ENTRYPOINT` (not shell form) so the process is PID 1 and receives the signal directly. Alternatively, use `tini` as an init process.

---

**Q38. What is `docker exec` vs `docker attach`? When would you use each?**

**A:** `docker exec` runs a **new** process inside a running container (e.g., an interactive shell). `docker attach` connects your terminal to the container's **existing** PID 1 stdin/stdout — pressing Ctrl+C kills the main process. Use `exec` for debugging; avoid `attach` in production.

---

**Q39. How would you implement a blue-green deployment using Docker?**

**A:** Run two versions of the service simultaneously (blue and green). Switch the load balancer/reverse proxy (e.g., nginx, Traefik) to route traffic to the new (green) version. Keep blue running for quick rollback. Once confident, remove blue containers.

---

**Q40. How would you set up a CI/CD pipeline that builds, tags, and pushes Docker images?**

**A:** Typical pipeline:
1. Trigger on git push/PR
2. Run tests
3. `docker build -t registry/app:$GIT_SHA .`
4. `docker push registry/app:$GIT_SHA`
5. Also tag as `latest` or a semantic version
6. Update Kubernetes deployment or trigger Helm upgrade

---
