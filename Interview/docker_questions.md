# Docker — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

1. What is Docker and how does it differ from a virtual machine?
2. Explain the difference between a Docker image and a Docker container.
3. What is a Dockerfile? Describe the most commonly used Dockerfile instructions.
4. What is the Docker build context and why does it matter?
5. Explain the concept of Docker layers. How does layer caching work?
6. What is the difference between `CMD` and `ENTRYPOINT` in a Dockerfile?
7. What is the difference between `COPY` and `ADD` in a Dockerfile?
8. What is a multi-stage build and when would you use it?
9. What is `.dockerignore` and why is it important?
10. What is Docker Hub and what alternatives exist for private image registries?

---

## Networking

11. What are the different Docker network drivers? Explain when you would use each.
12. How does DNS resolution work between containers on the same Docker network?
13. What is the difference between `host`, `bridge`, and `none` network modes?
14. How would you connect two containers running in separate Compose files?
15. What is a Docker overlay network and when is it used?

---

## Storage & Volumes

16. What is the difference between a Docker volume, a bind mount, and a tmpfs mount?
17. When would you prefer a named volume over a bind mount?
18. How do you share a volume between multiple containers?
19. What happens to data in a container when the container is removed?
20. How would you back up and restore data from a Docker volume?

---

## Docker Compose

21. What is Docker Compose and what problem does it solve?
22. What is the difference between `docker compose up` and `docker compose up --build`?
23. How do you define service dependencies in a Compose file? What are the limitations of `depends_on`?
24. How do you pass environment variables to a container in Docker Compose?
25. What is the purpose of the `healthcheck` directive in a Compose service?
26. What is a Compose override file and how do you use it for multiple environments?

---

## Security

27. What is the risk of running containers as root? How do you mitigate this?
28. What are Docker security best practices you follow in production?
29. How does Docker image scanning work? Name some tools used for it.
30. What is the difference between `--privileged` and `--cap-add` flags?
31. How would you store and manage secrets in Docker securely?

---

## Performance & Optimization

32. How do you reduce the size of a Docker image?
33. What is the impact of the number of layers on Docker image performance?
34. How do you profile and debug a running Docker container?
35. What is `docker stats` and what metrics does it expose?
36. How would you limit CPU and memory usage for a Docker container?

---

## Advanced / Production

37. What is Docker BuildKit and what improvements does it bring?
38. How do you implement a blue-green deployment strategy using Docker?
39. What is the difference between Docker Swarm and Kubernetes?
40. How do you handle container logging in production? What log drivers are available?
41. How do you manage rolling updates and zero-downtime deployments with Docker?
42. What are init containers? How can you simulate the pattern in plain Docker?
43. How does Docker handle signal propagation and graceful shutdown?
44. What is `docker exec` vs `docker attach`? When would you use each?
45. How would you set up a CI/CD pipeline that builds, tags, and pushes Docker images?

---
