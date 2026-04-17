# Architecture and Components

Kubernetes (K8s) is an open-source system for automating deployment, scaling, and management of containerized applications.

## 1. Master Node (The Control Plane)
The brain of the cluster. It makes global decisions about the cluster (e.g., scheduling) and detects/responds to cluster events.

### Core Components
- **API Server (`kube-apiserver`)**: The front end for the Kubernetes control plane. It's the only component that communicates with etcd.
- **`etcd`**: Consistent and highly-available key-value store used as Kubernetes' backing store for all cluster data.
- **Scheduler (`kube-scheduler`)**: Watches for newly created Pods with no assigned node, and selects a node for them to run on.
- **Controller Manager (`kube-controller-manager`)**: Runs controller processes.
    - **Node Controller**: Noticing and responding when nodes go down.
    - **Replication Controller**: Maintaining the correct number of pods for every replication controller object in the system.

## 2. Worker Nodes
The machines that run your applications.

### Core Components
- **`kubelet`**: An agent that runs on each node in the cluster. It makes sure that containers are running in a Pod.
- **`kube-proxy`**: A network proxy that runs on each node in your cluster. it maintains network rules on nodes that allow network communication to your Pods.
- **Container Runtime**: The software that is responsible for running containers (e.g., Docker, containerd, CRI-O).

## 3. The Declarative Model
Kubernetes operates on a declarative model.
1.  **Desired State**: You define the desired state of your cluster in a YAML file (e.g., "I want 3 replicas of my web app").
2.  **API Server**: Receives the YAML and stores it in etcd.
3.  **Controllers**: Constantly compare the **Desired State** with the **Actual State**. 
4.  **Action**: If they differ, the controllers take action to move the cluster Toward the desired state (e.g., starting a new pod if one crashed).

> [!IMPORTANT]
> **etcd and Consistency**: 
> Because etcd stores the entire state of the cluster, it is the most critical component. It uses the **Raft consensus algorithm** to ensure that data is consistent across all master nodes.

> [!TIP]
> **Cloud Controller Manager**: 
> If you are running K8s on a cloud provider (AWS, GCP, Azure), this component links your cluster into your cloud provider's API.
