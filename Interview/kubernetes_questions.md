# Kubernetes — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

1. What is Kubernetes and what problem does it solve?
2. Explain the Kubernetes control plane components and their roles.
3. What is a Pod? Why is it the basic unit in Kubernetes instead of a container?
4. What is the difference between a Deployment, a StatefulSet, and a DaemonSet?
5. What is a ReplicaSet and how does it relate to a Deployment?
6. Explain the role of the `kubelet`, `kube-proxy`, and `kube-scheduler`.
7. What is `etcd` and why is it critical to the cluster?
8. What is the difference between a Namespace and a cluster?
9. What is a ConfigMap and how does it differ from a Secret?
10. What is the purpose of a `ServiceAccount`?

---

## Networking

11. How does Kubernetes networking work? What is the CNI plugin?
12. What are the four types of Kubernetes Services? When do you use each?
13. What is an Ingress resource and what role does an Ingress controller play?
14. What is a `ClusterIP`, `NodePort`, and `LoadBalancer` service?
15. How does kube-proxy implement service load balancing?
16. What is a NetworkPolicy and how do you restrict pod-to-pod communication?
17. How does DNS work inside a Kubernetes cluster?

---

## Storage

18. What is a PersistentVolume (PV) and PersistentVolumeClaim (PVC)?
19. What is a StorageClass and what is dynamic provisioning?
20. When would you use a StatefulSet over a Deployment for storage?
21. How do you handle database migrations in a Kubernetes deployment?

---

## Scheduling & Resource Management

22. How does the Kubernetes scheduler assign a Pod to a Node?
23. What is the difference between `requests` and `limits` for CPU and memory?
24. What is a LimitRange and a ResourceQuota?
25. How do you implement node affinity, pod affinity, and taints/tolerations?
26. What is a PodDisruptionBudget and when is it used?
27. What is the Cluster Autoscaler and how does it work?
28. What is the Horizontal Pod Autoscaler (HPA) and the Vertical Pod Autoscaler (VPA)?

---

## Deployments & Rollouts

29. How do you perform a rolling update in Kubernetes?
30. How do you roll back a failed deployment?
31. What are `maxSurge` and `maxUnavailable` in a Deployment strategy?
32. How would you implement a canary deployment in Kubernetes?
33. What is a blue-green deployment and how do you achieve it in Kubernetes?

---

## Observability

34. How do you view logs for a crashed Pod that no longer exists?
35. What is the difference between liveness, readiness, and startup probes?
36. How do you integrate Prometheus and Grafana with Kubernetes?
37. What is the Kubernetes Events API and how is it useful for debugging?

---

## Security

38. What is RBAC in Kubernetes? Explain Role, ClusterRole, RoleBinding, ClusterRoleBinding.
39. What is a Pod Security Admission (PSA) policy?
40. How do you store and inject secrets securely into a Pod?
41. What is the principle of least privilege in the context of Kubernetes?
42. How do you scan Kubernetes manifests for misconfigurations?

---

## Advanced / Production

43. What is a Helm chart and why is it used?
44. What is a Kubernetes Operator and when would you build one?
45. What is GitOps and how does ArgoCD or Flux fit into a Kubernetes workflow?
46. How does Kubernetes handle graceful pod termination?
47. What is the difference between Kubernetes Autopilot (GKE) and Standard mode?
48. How would you design a multi-tenant Kubernetes cluster?
49. What are init containers and sidecar containers? Give use cases.
50. How do you debug a Pod stuck in `CrashLoopBackOff` or `Pending` state?

---
