# Health and Autoscaling

Ensuring applications are healthy and can handle varying traffic loads is a core strength of Kubernetes.

## 1. Health Checks (Probes)
A probe is a diagnostic performed periodically by the `kubelet` on a container.
- **Liveness Probe**: Indicates whether the container is running. If it fails, the kubelet kills the container and it's subjected to its restart policy.
- **Readiness Probe**: Indicates whether the container is ready to respond to requests. If it fails, the endpoints controller removes the Pod's IP address from the endpoints of all Services that match the Pod.
- **Startup Probe**: Indicates whether the application within the container has started. All other probes are disabled until the startup probe succeeds.

## 2. Resource Management
You can specify how much CPU and Memory (RAM) each container needs.
- **Requests**: The minimum amount of resources the container is guaranteed to have. The scheduler uses this to decide which node to place the Pod on.
- **Limits**: The maximum amount of resources the container is allowed to use. 
    - If a container exceeds its CPU limit, it is **Throttled**.
    - If a container exceeds its Memory limit, it is killed (**OOMKilled**).

## 3. Horizontal Pod Autoscaler (HPA)
Automatically scales the number of Pod replicas in a Deployment or ReplicaSet based on observed CPU utilization (or other metrics).
- It scales the work horizontally by adding/removing Pods.

## 4. Vertical Pod Autoscaler (VPA)
Automatically sets resource requests and limits based on usage. 
- It scales the work vertically by giving more/less CPU and RAM to existing Pods.
- **Note**: VPA will restart Pods as it changes their resource definitions.

## 5. Cluster Autoscaler
Automatically adjusts the size of the Kubernetes cluster (adding/removing worker nodes) when there are Pods that cannot be scheduled due to insufficient resources.

> [!IMPORTANT]
> **Always Set Requests and Limits**: 
> Failing to set resource requests and limits leads to "Noisy Neighbor" problems and makes it impossible for the scheduler to efficiently pack Pods onto nodes. It also makes your cluster behavior unpredictable during high load.

> [!TIP]
> **Readiness vs. Liveness**: 
> Use **Readiness** probes to prevent traffic from hitting a pod while it's still loading data. Use **Liveness** probes to restart a pod that has entered a "deadlocked" state.
