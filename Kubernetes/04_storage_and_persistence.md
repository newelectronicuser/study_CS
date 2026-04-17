# Storage and Persistence

By default, data in a container is ephemeral. Kubernetes provides a robust storage abstraction to handle persistent data.

## 1. Volume
A directory accessible to the containers in a Pod. 
- The lifetime of a volume is the same as the Pod that encloses it.
- **`emptyDir`**: Created when a Pod is assigned to a Node. Initially empty. Deleted when Pod is removed. Good for scratch space.
- **`hostPath`**: Mounts a file or directory from the host node's filesystem into your Pod.

## 2. PersistentVolumes (PV)
A piece of storage in the cluster that has been provisioned by an administrator or dynamically provisioned using Storage Classes.
- It is a resource in the cluster just like a node is a cluster resource.
- PVs have a lifecycle independent of any individual pod that uses the PV.

## 3. PersistentVolumeClaims (PVC)
A request for storage by a user.
- It is similar to a Pod. Pods consume node resources and PVCs consume PV resources.
- PVCs can request specific sizes and access modes (e.g., `ReadWriteOnce`, `ReadOnlyMany`, `ReadWriteMany`).

## 4. StorageClasses
Allows administrators to describe the "classes" of storage they offer.
- Different classes might map to quality-of-service levels (SSD vs HDD) or to backup policies.
- **Dynamic Provisioning**: When a user creates a PVC, Kubernetes uses the StorageClass to automatically provision a matching PV.

## 5. CSI (Container Storage Interface)
Similar to CNI, CSI is a standard for exposing arbitrary block and file storage systems to containerized workloads. It allows storage providers (AWS EBS, Google Persistent Disk, NFS) to write their own drivers.

## 6. Access Modes
- **RWO (ReadWriteOnce)**: The volume can be mounted as read-write by a single node.
- **ROX (ReadOnlyMany)**: The volume can be mounted read-only by many nodes.
- **RWX (ReadWriteMany)**: The volume can be mounted as read-write by many nodes.

> [!WARNING]
> **HostPath Security**: 
> Avoid using `hostPath` for production applications unless strictly necessary (like for system agents). It creates a security risk by allowing containers to access the underlying host OS files.

> [!TIP]
> **Reclaim Policy**: 
> Defines what happens to a PV when its PVC is deleted.
> - **Retain**: PV is kept; data must be manually recovered/deleted.
> - **Delete**: PV and the associated storage asset in the cloud are deleted.
