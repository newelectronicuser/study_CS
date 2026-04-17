# Networking and Services

Kubernetes networking has a "IP-per-pod" model. Every Pod gets its own IP address, and Pods can communicate with every other Pod in the cluster without NAT.

## 1. Services
A Service is an abstract way to expose an application running on a set of Pods as a network service.
- **ClusterIP (Default)**: Exposes the service on a cluster-internal IP. Choosing this value makes the service only reachable from within the cluster.
- **NodePort**: Exposes the service on each Node's IP at a static port (the NodePort). You can contact the NodePort service from outside the cluster by requesting `<NodeIP>:<NodePort>`.
- **LoadBalancer**: Exposes the service externally using a cloud provider's load balancer (e.g., AWS ELB, GCP LB).
- **ExternalName**: Maps the service to the contents of the `externalName` field (e.g., `foo.bar.example.com`).

## 2. Ingress
Ingress is NOT a service type. It is an API object that manages external access to the services in a cluster, typically HTTP.
- It can provide load balancing, SSL termination, and name-based virtual hosting.
- Requires an **Ingress Controller** (like Nginx, Traefik, or Istio) to be running in the cluster.

## 3. CoreDNS
Provides DNS-based service discovery. 
- Every Service is assigned a DNS name (e.g., `my-service.my-namespace.svc.cluster.local`) which resolves to its ClusterIP.

## 4. Kube-proxy
Runs on each node and is responsible for implementing the Service networking.
- It uses **iptables** or **IPVS** to forward traffic aimed at a Service IP to one of the backend Pods.

## 5. Network Policies
Control the traffic flow at the Pod level (similar to firewall rules).
- By default, Pods are non-isolated; they accept traffic from any source.
- You can use NetworkPolicies to restrict ingress/egress based on labels, namespaces, or IP blocks.

## 6. CNI (Container Network Interface)
Kubernetes doesn't provide a network implementation itself. It defines a standard (CNI) and relies on plugins like **Calico**, **Flannel**, or **Cilium** to handle the actual creation of IP addresses and routing.

> [!TIP]
> **Why use Ingress over LoadBalancer?**: 
> A LoadBalancer service creates a cloud resource for EVERY service, which can be expensive. Ingress allows you to use ONE load balancer to route traffic to MANY different services based on URL paths or hostnames.
