# Kubernetes — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is Kubernetes and what problem does it solve?**

**A:** Kubernetes (K8s) is an open-source container orchestration platform that automates the deployment, scaling, management, and networking of containerized applications.
*   *Problems Solved:* High availability (auto-restart/self-healing failed containers), horizontal scaling (up/down based on CPU/traffic), service discovery/load balancing, storage orchestration, automated rollouts/rollbacks, and maximizing hardware utilization.

---

**Q2. Explain the Kubernetes control plane components and their roles.**

**A:** The Control Plane coordinates and manages the cluster state:
*   **kube-apiserver:** The entry point and hub of the cluster. Exposes the Kubernetes API, handles authentication, authorization, and schema validation.
*   **etcd:** Distributed, consistent key-value store that serves as the cluster's database, recording all configurations and state details.
*   **kube-scheduler:** Watches for newly created Pods with no assigned Node, selects the best node based on resource requests, constraints, and policies.
*   **kube-controller-manager:** Runs controller background processes that maintain cluster state (e.g. Node Controller, Deployment Controller).
*   **cloud-controller-manager:** Links the cluster with the cloud provider's API (e.g., managing load balancers or node instances).

---

**Q3. What is a Pod? Why is it the basic unit in Kubernetes instead of a container?**

**A:** A Pod is the smallest deployable unit in Kubernetes. It represents a single instance of a running process in the cluster and wraps one or more tightly coupled containers.
*   *Why a Pod:* Containers within a Pod share the same network namespace (same IP and port space - communicate via `localhost`), storage volumes, and cgroup limits. This allows helper containers (like sidecars, log forwarders) to work in close isolation alongside the main application container without running in the same image.

---

**Q4. What is the difference between a Deployment, a StatefulSet, and a DaemonSet?**

**A:**
*   **Deployment:** Best for stateless workloads (e.g., web servers, REST APIs). Pod names get random hashes, and they share persistent storage (cannot have unique volumes per pod easily).
*   **StatefulSet:** Best for stateful workloads (e.g., databases, Kafka). Pods have sticky, ordinal names (`db-0`, `db-1`) and link to dedicated persistent volumes (PVCs) that persist even if a pod is rescheduled.
*   **DaemonSet:** Ensures that a copy of a specific Pod runs on **all** (or selected) Nodes in the cluster. Used for background node daemons (e.g. log collectors like Fluentd, monitoring agents like Prometheus node exporter).

---

**Q5. What is a ReplicaSet and how does it relate to a Deployment?**

**A:**
*   **ReplicaSet:** A controller that ensures a specified number of identical Pod replicas are running at any given time.
*   *Relation:* A Deployment is a higher-level abstraction that manages ReplicaSets. When you update a Deployment's image, the Deployment controller creates a new ReplicaSet, scales it up, and scales down the old ReplicaSet, facilitating zero-downtime rolling updates. You should manage Deployments, not ReplicaSets directly.

---

**Q6. Explain the role of the `kubelet`, `kube-proxy`, and `kube-scheduler`.**

**A:**
*   **kubelet:** An agent running on every worker node. It registers the node, watches the API server for assigned Pods, communicates with the container runtime (Docker/containerd) to start/stop containers, and monitors container health (probes).
*   **kube-proxy:** A network agent running on every node. It implements the Kubernetes Service networking rules on the host, handling load balancing for requests sent to Service IPs (using iptables or IPVS).
*   **kube-scheduler:** Runs on master nodes. It selects the target node for unscheduled Pods based on resource availability, affinity, and taints.

---

**Q7. What is `etcd` and why is it critical to the cluster?**

**A:** `etcd` is a strongly consistent, highly available, distributed key-value store. It stores the single source of truth for the entire Kubernetes cluster state, configuration, and runtime metadata. If `etcd` is lost or corrupted, the cluster cannot recover or accept new requests. High availability clusters run `etcd` as an odd-numbered (3 or 5) quorum group using the Raft consensus algorithm.

---

**Q8. What is the difference between a Namespace and a cluster?**

**A:**
*   **Cluster:** A physical group of virtual or physical machine nodes managed together as a single computing pool.
*   **Namespace:** A logical partition inside a single cluster. It allows segregating resources, access rules, quotas, and network policies (e.g. separating `development`, `staging`, and `production` workloads on the same physical cluster hardware).

---

**Q9. What is a ConfigMap and how does it differ from a Secret?**

**A:**
*   **ConfigMap:** Stores non-confidential configuration key-value pairs (e.g., app properties, environment variables, config files).
*   **Secret:** Stores sensitive information (e.g., passwords, API keys, certificates). Secrets are stored in base64-encoded format (not encrypted by default in etcd unless configured) and are mounted into Pod memory (tmpfs) to prevent exposure.

---

**Q10. What is the purpose of a `ServiceAccount`?**

**A:** A `ServiceAccount` provides an identity for processes running inside a Pod. Pods present the ServiceAccount token to the Kubernetes API server to authenticate themselves. RBAC policies (Roles/ClusterRoles) can be bound to these ServiceAccounts to control which resources a Pod can access (e.g., allowing an operator pod to list namespaces).

---

## Networking

**Q11. How does Kubernetes networking work? What is the CNI plugin?**

**A:** Kubernetes requires a flat network model where:
1. Every Pod gets a unique, routable IP address inside the cluster.
2. Pods can communicate with all other Pods on any node without NAT.
*   **CNI (Container Network Interface) Plugin:** A specification and library that configures network interfaces for containers (e.g. Calico, Flannel, Cilium). It handles assigning IPs, setting up virtual switches, and implementing routing policies.

---

**Q12. What are the four types of Kubernetes Services? When do you use each?**

**A:**
1.  **ClusterIP (Default):** Exposes the Service on an internal cluster IP. Accessible only within the cluster. Used for backend databases and microservices.
2.  **NodePort:** Exposes the Service on a static port (30000-32767) on each Node's IP. Used for simple testing or basic external routing.
3.  **LoadBalancer:** Provisions a cloud-provider load balancer (e.g. AWS ALB) pointing to the NodePort. Used to expose services directly to the internet.
4.  **ExternalName:** Maps a service to a DNS name (using a CNAME record). Used to bridge external services (like an external DB) into the cluster DNS namespace.

---

**Q13. What is an Ingress resource and what role does an Ingress controller play?**

**A:**
*   **Ingress Resource:** A set of routing rules (HTTP/HTTPS) defining hostnames, paths, and backend services.
*   **Ingress Controller:** A reverse proxy pod (e.g., Nginx Ingress, Traefik, HAProxy) that runs in the cluster, reads the Ingress resources, and routes external traffic to the appropriate cluster Services, handling SSL termination.

---

**Q14. What is a `ClusterIP`, `NodePort`, and `LoadBalancer` service?**

**A:**
*   `ClusterIP`: Internal-only routing IP.
*   `NodePort`: Map of an internal service port to a port exposed on the host interface of every node.
*   `LoadBalancer`: Automatically provisions external cloud routing hardware that maps directly to the `NodePort`.

---

**Q15. How does kube-proxy implement service load balancing?**

**A:** `kube-proxy` does not act as an inline proxy. Instead, it writes network packet translation rules (using **iptables** or **IPVS**) on the host OS kernel. When a packet targets a Service's IP, the kernel translates (DNAT) the address directly to a healthy Pod's IP, load balancing traffic at the network level.

---

**Q16. What is a NetworkPolicy and how do you restrict pod-to-pod communication?**

**A:** A NetworkPolicy is a set of firewall rules controlling how groups of Pods communicate with each other and other network endpoints. It requires a CNI plugin that supports network policies (e.g., Calico). By default, all Pods can communicate. You apply a NetworkPolicy using label selectors to restrict ingress/egress to specific namespaces or pod labels.

---

**Q17. How does DNS work inside a Kubernetes cluster?**

**A:** Kubernetes runs a built-in DNS service (usually **CoreDNS**). When a Service is created, CoreDNS registers its name (e.g. `my-service.my-namespace.svc.cluster.local`) mapping to the Service IP. Pod containers are automatically configured with `/etc/resolv.conf` pointing to the CoreDNS service, allowing them to resolve names.

---

## Storage

**Q18. What is a PersistentVolume (PV) and PersistentVolumeClaim (PVC)?**

**A:**
*   **PersistentVolume (PV):** A physical storage resource provisioned by an administrator or dynamically generated by a StorageClass. It represents actual disk space (e.g. EBS volume, NFS).
*   **PersistentVolumeClaim (PVC):** A request for storage by a user/Pod. It specifies size, access modes (e.g., `ReadWriteOnce`), and a StorageClass. The controller binds the matching PVC to a PV, allowing the Pod to mount it.

---

**Q19. What is a StorageClass and what is dynamic provisioning?**

**A:**
*   **StorageClass:** Defines the profile, provider (e.g., AWS gp3), and parameters for provisioned storage.
*   **Dynamic Provisioning:** Allows K8s to automatically create a physical PV in the cloud when a developer submits a PVC, eliminating the need for admins to manually pre-allocate disks.

---

**Q20. When would you use a StatefulSet over a Deployment for storage?**

**A:** Use a **StatefulSet** when each Pod replica requires its own independent, unique persistent state (e.g., database replica sets where each node needs its own disk). In a StatefulSet, K8s matches `pod-0` with `pvc-0`, `pod-1` with `pvc-1`, maintaining this binding even if a pod crashes and is recreated on another node. Deployments mount the same shared volume across all replicas, which is unsuitable for distributed databases.

---

**Q21. How do you handle database migrations in a Kubernetes deployment?**

**A:**
1.  **Kubernetes Job:** Run migration scripts as a one-off `Job` or `InitContainer` before launching the main database application containers.
2.  **Schema Tooling:** Use migration tools (Liquibase, Flyway) configured to execute inside the application bootstrap process.
3.  **Helm Hooks:** Use pre-install/pre-upgrade Helm hooks to execute migration jobs before updating pods.

---

## Scheduling & Resource Management

**Q22. How does the scheduler assign a Pod to a Node?**

**A:** It goes through two main phases:
1.  **Filtering (Predicates):** Filters out nodes that do not meet the Pod's requirements (e.g. insufficient CPU/memory, taint mismatch, port conflict).
2.  **Scoring (Priorities):** Ranks the remaining nodes based on metrics (e.g. data locality, image availability, resource balance). The node with the highest score is selected.

---

**Q23. What is the difference between `requests` and `limits` for CPU and memory?**

**A:**
*   **Requests:** The minimum amount of CPU/Memory guaranteed to the container. The scheduler uses this to decide which node has enough space to run the Pod.
*   **Limits:** The maximum amount of CPU/Memory a container is allowed to consume.
    *   *CPU Limit Exceeded:* The container is throttled (slowed down) but not killed.
    *   *Memory Limit Exceeded:* The container is Out-Of-Memory killed (**OOMKilled**) by the OS.

---

**Q24. What is a LimitRange and a ResourceQuota?**

**A:**
*   **LimitRange:** Enforces default resource request/limit constraints at the container level within a Namespace.
*   **ResourceQuota:** Restricts the *aggregate* resource consumption (e.g., total CPU limit cannot exceed 20 Cores, maximum of 50 Pods) in a Namespace.

---

**Q25. How do you implement node affinity, pod affinity, and taints/tolerations?**

**A:**
*   **Node Affinity:** Attracts Pods to specific nodes based on node labels (e.g., require GPU nodes).
*   **Pod Affinity / Anti-Affinity:** Schedules Pods close to (or away from) other Pods (e.g. anti-affinity ensures replicas of `web-server` run on different nodes for high availability).
*   **Taints (Node) & Tolerations (Pod):** Taints allow a Node to repel Pods. A Pod must have a matching Toleration to be scheduled on a tainted node (e.g. keeping user workloads off master nodes).

---

**Q26. What is a PodDisruptionBudget and when is it used?**

**A:** A PDB limits the number of Pods of a replicated application that can be down simultaneously during voluntary disruptions (e.g., node drains for upgrades). It ensures that at least N replicas or percentage of pods remain healthy, maintaining service availability.

---

**Q27. What is the Cluster Autoscaler and how does it work?**

**A:** The Cluster Autoscaler automatically adjusts the physical node count of your cluster. If a Pod cannot be scheduled due to insufficient resource capacity on worker nodes (stuck in `Pending` state), the Autoscaler calls cloud APIs to launch new VM nodes. It also deletes idle, underutilized nodes to optimize costs.

---

**Q28. What is the Horizontal Pod Autoscaler (HPA) and the Vertical Pod Autoscaler (VPA)?**

**A:**
*   **HPA:** Scales the number of Pod replicas (horizontal) based on CPU/memory utilization or custom metrics.
*   **VPA:** Adjusts the CPU/memory requests and limits of existing Pods (vertical) by analyzing historic usage, but requires restarting the pods to apply changes.
*   *Warning:* Do not use HPA and VPA together on the same resource metrics (like CPU) as they will conflict.

---

## Deployments & Rollouts

**Q29. How do you perform a rolling update in Kubernetes?**

**A:** By default, changing the image in a Deployment manifest triggers a rolling update. The deployment controller spins up a new ReplicaSet, launches new Pods, waits for them to pass readiness probes, and scales down old Pods incrementally, ensuring zero downtime.

---

**Q30. How do you roll back a failed deployment?**

**A:** Run `kubectl rollout undo deployment/<deployment-name>`. You can check the rollout history using `kubectl rollout history deployment/<deployment-name>`.

---

**Q31. What are `maxSurge` and `maxUnavailable` in a Deployment strategy?**

**A:**
*   `maxSurge`: The maximum number of Pods that can be created over the desired number of replicas during an update (e.g., 25%).
*   `maxUnavailable`: The maximum number of Pods that can be unavailable during the update process (e.g., 25%).

---

**Q32. How would you implement a canary deployment in Kubernetes?**

**A:** Create two Deployments: one for stable (e.g., v1) and one for canary (v2 with 1 replica). Point a single Kubernetes `Service` to both deployments by using a shared label selector (e.g., `app: my-app`). Since v2 has fewer replicas, it naturally receives a fraction of the traffic. Alternatively, use an Ingress controller (e.g., Nginx Ingress) or a Service Mesh (Istio) to split traffic precisely (e.g. 5% to v2).

---

**Q33. What is a blue-green deployment and how do you achieve it in Kubernetes?**

**A:** Deploy two versions: `my-app-blue` (v1) and `my-app-green` (v2) simultaneously. Keep the public Kubernetes `Service` pointed to the `blue` pods. Once `green` is fully tested and verified, update the Service's label selector to point to `my-app-green`. If bugs appear, switch the selector back to `blue`.

---

## Observability

**Q34. How do you view logs for a crashed Pod that no longer exists?**

**A:** If the container crashed, query the logs of the previous instance using `kubectl logs <pod-name> --previous`. If the Pod was deleted or rescheduled entirely, you must query your centralized logging system (e.g. Kibana, Loki) that aggregates logs from the node.

---

**Q35. What is the difference between liveness, readiness, and startup probes?**

**A:**
*   **Startup Probe:** Runs first. Disables other probes until the container finishes bootstrapping. Used for slow-starting legacy apps.
*   **Liveness Probe:** Determines if the container needs to be restarted. If it fails, K8s kills the container and starts a new one.
*   **Readiness Probe:** Determines if the container is ready to receive network traffic. If it fails, the Pod is removed from the Service endpoints list.

---

**Q36. How do you integrate Prometheus and Grafana with Kubernetes?**

**A:** Deploy the **Prometheus Operator** (kube-prometheus-stack) via Helm. It configures Prometheus to scrape metrics from the API server, nodes, and Pods. Developers define `ServiceMonitor` or `PodMonitor` CRDs to tell Prometheus how to collect metrics from their apps. Grafana pulls metrics from Prometheus to display dashboards.

---

**Q37. What is the Kubernetes Events API and how is it useful for debugging?**

**A:** The Events API records key state transitions, errors, and actions taken by controllers (e.g., scheduler failed to bind, image pull failed). You view them using `kubectl get events` or `kubectl describe pod <pod-name>`.

---

## Security

**Q38. What is RBAC in Kubernetes? Explain Role, ClusterRole, RoleBinding, ClusterRoleBinding.**

**A:** Role-Based Access Control regulates access to resources.
*   **Role:** Defines permissions (verbs like get, list, create) on resources within a single Namespace.
*   **ClusterRole:** Defines permissions on cluster-wide resources (nodes, namespaces) or across all namespaces.
*   **RoleBinding:** Grants permissions in a `Role` to a user, group, or ServiceAccount within a specific Namespace.
*   **ClusterRoleBinding:** Grants permissions in a `ClusterRole` cluster-wide.

---

**Q39. What is a Pod Security Admission (PSA) policy?**

**A:** PSA defines security standards for Pod execution. It has three levels:
*   `privileged`: No restrictions.
*   `baseline`: Prevents known privilege escalations, restricts host namespaces/ports.
*   `restricted`: Enforces hardened standards (requires running as non-root, dropping capabilities).

---

**Q40. How do you store and inject secrets securely into a Pod?**

**A:**
1.  **Kubernetes Secrets:** Inject them as environment variables or mount them as files.
2.  **External Secrets Operator:** Retrieve secrets directly from AWS Secrets Manager or HashiCorp Vault and sync them as Kubernetes Secrets.
3.  **Secrets Store CSI Driver:** Mount secrets directly from external managers into Pod volumes as in-memory files.

---

**Q41. What is the principle of least privilege in the context of Kubernetes?**

**A:** Minimizing permissions at all layers:
*   Use dedicated ServiceAccounts per Pod with targeted RBAC roles.
*   Run containers as non-root and drop unnecessary Linux capabilities.
*   Use NetworkPolicies to isolate Pod networks, blocking unauthorized traffic.

---

**Q42. How do you scan Kubernetes manifests for misconfigurations?**

**A:** Use static analysis security tools like **Kubeval**, **Conftest**, or **Trivy** to scan YAML files for security risks (e.g., containers running as root, missing resource limits, host namespaces exposed).

---

## Advanced / Production

**Q43. What is a Helm chart and why is it used?**

**A:** Helm is the package manager for Kubernetes. A **Helm Chart** is a collection of templated Kubernetes manifest files configured by a `values.yaml` file. It allows packaging, versioning, sharing, and deploying complex Kubernetes applications with a single command.

---

**Q44. What is a Kubernetes Operator and when would you build one?**

**A:** An Operator is a design pattern that extends Kubernetes using **Custom Resource Definitions (CRDs)** and custom controllers.
*   *When to build:* To automate the management of complex, stateful applications (like databases). An operator handles backups, upgrades, and clustering logic automatically based on Custom Resource configurations.

---

**Q45. What is GitOps and how does ArgoCD or Flux fit into a Kubernetes workflow?**

**A:** GitOps is a practice where Git is the single source of truth for declarative infrastructure and applications.
*   *ArgoCD / Flux:* A GitOps controller running in the cluster. It continuously compares the desired state stored in a Git repository with the live state in the Kubernetes cluster. If a drift is detected (e.g. someone ran `kubectl edit`), it automatically reconciles the cluster state to match Git.

---

**Q46. How does Kubernetes handle graceful pod termination?**

**A:**
1.  Pod is marked as `Terminating` and removed from Service endpoints.
2.  `preStop` hook executes (if defined).
3.  `SIGTERM` signal is sent to the container's PID 1 process.
4.  K8s waits for a grace period (default 30s) for the app to finish processing active connections.
5.  If still running after the grace period, `SIGKILL` is sent, terminating the container.

---

**Q47. What is the difference between Kubernetes Autopilot (GKE) and Standard mode?**

**A:**
*   **Standard:** You configure and manage the worker node pools (VM instances). You have full access to node operating systems.
*   **Autopilot:** GKE provisions and manages the entire node infrastructure. You are billed based on the resource requests of your running Pods, not node capacity, and security hardening is enforced automatically.

---

**Q48. How would you design a multi-tenant Kubernetes cluster?**

**A:**
1.  Use **Namespaces** to isolate tenants.
2.  Enable **NetworkPolicies** to block inter-namespace traffic.
3.  Configure **ResourceQuotas** and **LimitRanges** to prevent resource starvation.
4.  Use **RBAC** to restrict tenant access to their namespaces.
5.  Enforce **Pod Security Standards** to prevent tenants from gaining node access.

---

**Q49. What are init containers and sidecar containers? Give use cases.**

**A:**
*   **Init Containers:** Run and finish before app containers start. Use: waiting for a database to be online, or downloading config files.
*   **Sidecar Containers:** Run alongside the main app container. Use: exporting logs (Fluentd), proxying network traffic (Envoy in Service Mesh), or fetching secret tokens.

---

**Q50. How do you debug a Pod stuck in `CrashLoopBackOff` or `Pending` state?**

**A:**
*   `CrashLoopBackOff` (App starts and crashes repeatedly): Run `kubectl logs <pod-name>` or `kubectl logs <pod-name> --previous` to see application errors (e.g. missing db connection, environment variable).
*   `Pending` (Pod cannot be scheduled): Run `kubectl describe pod <pod-name>` and look at the `Events` section. Usually caused by insufficient CPU/Memory on nodes, unmet node affinity rules, or missing PVCs.

---
