# Kubernetes — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is Kubernetes and what problem does it solve?**

**A:** Kubernetes (K8s) is an open-source container orchestration system. It automates deployment, scaling, scheduling, self-healing, and management of containerized applications across a cluster of machines. It solves the problem of running many containers across many hosts reliably — ensuring availability, rollouts, resource utilization, and service discovery without manual intervention.

---

**Q2. Explain the Kubernetes control plane components and their roles.**

**A:**
- **API Server**: Front-end for the control plane. All communication goes through it.
- **etcd**: Distributed key-value store holding all cluster state.
- **Scheduler**: Assigns unscheduled Pods to Nodes based on resource requests, affinity, taints.
- **Controller Manager**: Runs reconciliation loops (Deployment, ReplicaSet, Node controllers) to drive actual state to desired state.
- **Cloud Controller Manager**: Integrates with cloud providers for load balancers, node lifecycle, storage.

---

**Q3. What is a Pod? Why is it the basic unit in Kubernetes?**

**A:** A Pod is one or more containers that share the same network namespace (same IP), storage volumes, and lifecycle. Containers within a Pod communicate via `localhost`. Pods are the unit of scheduling because some applications need tightly coupled sidecar containers (e.g., log shippers, proxies).

---

**Q4. What is the difference between a Deployment, StatefulSet, and DaemonSet?**

**A:**
- **Deployment**: For stateless apps. Manages ReplicaSets; supports rolling updates and rollbacks.
- **StatefulSet**: For stateful apps (databases). Each Pod gets a stable hostname (`pod-0`, `pod-1`) and its own PVC. Ordered start/stop.
- **DaemonSet**: Ensures one Pod runs on every (or selected) Node. Used for node-level agents (log collectors, monitoring).

---

**Q5. What is `etcd` and why is it critical?**

**A:** etcd is the distributed key-value store that is Kubernetes' source of truth. Every object (Pods, Deployments, Secrets) is stored there. Loss of etcd without a backup means the cluster state is unrecoverable. In production, run a 3 or 5 node etcd cluster for HA and back it up regularly.

---

**Q6. What is the difference between `requests` and `limits` for CPU and memory?**

**A:**
- **Requests**: Amount guaranteed to the container. Used by the scheduler to find a suitable Node.
- **Limits**: Maximum allowed. Exceeding CPU limit causes throttling; exceeding memory limit causes OOMKill.

Always set both in production. Base requests on actual usage; set limits slightly above the peak.

---

**Q7. What are taints and tolerations?**

**A:** **Taints** are applied to Nodes to repel Pods. **Tolerations** allow a Pod to be scheduled on a tainted Node. Example: taint a GPU node with `gpu=true:NoSchedule` so only GPU workloads with that toleration are scheduled there.

---

**Q8. What is the Horizontal Pod Autoscaler (HPA)?**

**A:** HPA automatically scales the replica count of a Deployment or StatefulSet based on metrics (CPU, memory, or custom). It reads from the Metrics Server and adjusts replicas to maintain a target utilization (e.g., 70% CPU).

---

## Networking

**Q9. What are the four types of Kubernetes Services?**

**A:**
- **ClusterIP** (default): Cluster-internal IP only. Not reachable from outside.
- **NodePort**: Exposes the service on each Node's IP at a static port. Accessible externally.
- **LoadBalancer**: Provisions an external cloud load balancer.
- **ExternalName**: Maps the service to an external DNS name via CNAME.

---

**Q10. What is an Ingress resource and what role does an Ingress controller play?**

**A:** An **Ingress** resource defines HTTP/HTTPS routing rules (host/path-based) to Services. An **Ingress Controller** (nginx, Traefik, GKE Ingress) implements those rules by configuring an actual load balancer or proxy. Without a controller, the Ingress resource has no effect.

---

**Q11. What is a NetworkPolicy?**

**A:** A NetworkPolicy uses label selectors to define ingress/egress traffic rules for Pods. By default, all Pods communicate freely. A NetworkPolicy whitelists specific sources/destinations. Requires a CNI plugin that supports it (Calico, Cilium).

---

## Observability

**Q12. What is the difference between liveness, readiness, and startup probes?**

**A:**
- **Liveness**: Is the container alive? Failure triggers a restart.
- **Readiness**: Is the container ready for traffic? Failure removes the Pod from Service endpoints (no restart).
- **Startup**: Is the app still initializing? Disables other probes until it succeeds. Prevents killing slow-starting apps.

---

**Q13. How do you view logs for a crashed Pod that no longer exists?**

**A:** For a crashed but still-present Pod: `kubectl logs <pod> --previous`. If the Pod is gone entirely, you need an external log aggregator (EFK stack, Loki, Datadog) that captured logs before termination.

---

## Security

**Q14. What is RBAC in Kubernetes?**

**A:** RBAC controls what API actions a subject can perform on which resources:
- **Role/ClusterRole**: Defines permissions (verbs: get, list, create, delete — on resources: pods, secrets).
- **RoleBinding/ClusterRoleBinding**: Binds a Role to a user, group, or ServiceAccount within a namespace or cluster-wide.

---

## Deployments & Advanced

**Q15. How would you implement a canary deployment in Kubernetes?**

**A:** Run two Deployments (stable + canary) sharing the same Service label selector. Control traffic ratio via replica counts (9 stable : 1 canary = 10% canary traffic). For precise control, use a service mesh like Istio with weighted VirtualService routing.

---

**Q16. What is a Helm chart?**

**A:** Helm is the K8s package manager. A chart is a collection of templated YAML manifests with a `values.yaml` for configuration. Helm enables versioned, repeatable deployments and simplifies complex multi-resource applications (`helm install`, `helm upgrade`, `helm rollback`).

---

**Q17. What is a Kubernetes Operator?**

**A:** An Operator extends K8s using CRDs and a custom controller to automate Day-2 operations for a specific application (e.g., a PostgreSQL Operator handles backups, failover, and upgrades). It encodes operational knowledge into code.

---

**Q18. How do you debug a Pod stuck in `CrashLoopBackOff` or `Pending`?**

**A:**
- **CrashLoopBackOff**: `kubectl logs <pod> --previous` for crash cause. Check probe configs, app startup errors.
- **Pending**: `kubectl describe pod <pod>` to see Events. Common causes: insufficient node resources, unbound PVC, image pull error, taint mismatch.

---

**Q19. What is GitOps and how does ArgoCD fit in?**

**A:** GitOps is a practice where the desired cluster state is declared in Git. ArgoCD (or Flux) continuously syncs the live cluster state to match the Git repository, automatically applying changes and detecting drift.

---

**Q20. How does Kubernetes handle graceful pod termination?**

**A:** On deletion: (1) Pod is removed from Service endpoints; (2) SIGTERM is sent to PID 1; (3) `terminationGracePeriodSeconds` (default 30s) allows in-flight requests to complete; (4) after the period, SIGKILL is sent. Apps must handle SIGTERM gracefully.

---

**Q21. What is a PodDisruptionBudget?**

**A:** A PDB limits voluntary disruptions (node drains, rolling updates) by specifying `minAvailable` or `maxUnavailable` for a set of Pods. For example, `minAvailable: 2` ensures at least 2 replicas stay running during maintenance.

---

**Q22. What is the Cluster Autoscaler?**

**A:** The Cluster Autoscaler automatically adds Nodes when Pods are unschedulable due to resource constraints, and removes underutilized Nodes. It integrates with cloud provider node group APIs (AWS ASG, GKE node pools).

---
