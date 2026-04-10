# 🐳 Introduction to Docker: Study Guide

Docker is a powerful platform for building, running, and shipping applications by utilizing **containerization**.

---

## 1. What is Docker? 🚀

A platform designed for building, running, and shipping applications efficiently.

---

## 2. Virtual Machines vs. Containers 🏗️

### Definitions

- **Container**: An isolated environment specifically for running an application.
- **VM (Virtual Machine)**: An abstraction of a machine (physical hardware).

### Ecosystem Components

- **OS Support**: Windows, Linux, Mac.
- **Hypervisor**: The software layer that creates and runs VMs.
- **Common Hypervisors**:
  - VirtualBox
  - VMWare
  - Hyper-V (Windows only)

### The Comparison

| Feature            | Virtual Machines (VMs)             | Containers                                 |
| :----------------- | :--------------------------------- | :----------------------------------------- |
| **OS Requirement** | Needs a full-blown OS for each VM. | Uses the OS of the host.                   |
| **Startup Speed**  | Slow to start.                     | Start quickly.                             |
| **Resources**      | Resource intensive.                | Lightweight / Need less hardware.          |
| **Isolation**      | Hardware-level abstraction.        | Allows running multiple apps in isolation. |

---

## 3. Docker Architecture 🏛️

The system operates on a client-server model:
**Client** — — — `[Rest API]` — — — > **Server (Docker Engine)**

### The Kernel Role

A **Kernel** manages application and hardware resources. Docker's interaction with the kernel varies by Operating System:

![Docker Architecture - Kernel Handling](files/Screenshot%20from%202026-03-19%2008-24-46.png)

- **Linux**: Containers can run directly on the Linux kernel (Native support).
- **Windows**: Containers can run on both Windows and Linux kernels.
- **Mac**: The kernel does not have native support for containers. Instead, it uses a **lightweight VM** to run Docker.

---

## 4. Installing Docker 🛠️

- **Official Ubuntu Guide**: [Install Docker on Ubuntu](https://docs.docker.com/engine/install/ubuntu/)
- **Management Tools**: [Portainer.io](https://www.portainer.io/)

![Docker Installation](files/Screenshot%20from%202026-03-19%2008-28-28.png)

---

## 5. Development Workflow 🔄

1.  **Dockerfile**: An image of an app is created using a set of instructions in a `Dockerfile`.
2.  **Registry**: Developers can push the image to a registry (like **Docker Hub**).
3.  **Deployment**: This image can then be pulled to other machines (Test, Production, etc.) for execution.

---

## 6. Docker in Action 🎬

_(Content coming soon...)_
