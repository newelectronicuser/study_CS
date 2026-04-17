# Objects and Workloads

Kubernetes objects are persistent entities that represent the state of your cluster.

## 1. The Pod
The primary building block of Kubernetes.
- It represents a single instance of a running process in your cluster.
- A Pod can contain one or more containers (though usually just one).
- Containers in a Pod share the same local network (IP address and port space) and storage (Volumes).

## 2. Controllers (Workloads)
Workload resources manage a set of Pods on your behalf.

### Deployment
The most common workload.
- Manages declarative updates for **Stateless** applications.
- Creates a **ReplicaSet** behind the scenes to ensure the desired number of pods are running.
- Supports rolling updates and rollbacks.

### ReplicaSet
Ensures that a specified number of pod replicas are running at any given time. Usually not managed directly but via Deployments.

### StatefulSet
Manages the deployment and scaling of a set of Pods, and provides guarantees about the **ordering** and **uniqueness** of these Pods.
- Each Pod gets a persistent identifier (`web-0`, `web-1`) that it keeps across restarts.
- Used for stateful applications like databases (MySQL, MongoDB).

### DaemonSet
Ensures that all (or some) Nodes run a copy of a Pod.
- As nodes are added to the cluster, Pods are added to them.
- Use cases: Log collection daemons (`fluentd`), node monitoring daemons (`Prometheus Node Exporter`).

### Jobs and CronJobs
- **Job**: Creates one or more Pods and ensures that a specified number of them successfully terminate.
- **CronJob**: Manages Jobs that run on a periodic schedule (e.g., database backups).

## 3. Labels and Selectors
- **Labels**: Key/value pairs attached to objects (like Pods) that are used to organize and select subsets of objects.
- **Selectors**: The core grouping primitive in Kubernetes. They allow clients to identify a set of objects based on their labels.

> [!IMPORTANT]
> **Deployment vs. StatefulSet**: 
> - If your application is **stateless** (any pod can handle any request), use **Deployment**.
> - If your application is **stateful** (requires unique identity and stable storage), use **StatefulSet**.
