# Best Practices and Troubleshooting

Running Jenkins in production requires a focus on stability, performance, and recoverability.

## 1. Top Best Practices
- **Master as Scheduler, not Worker**: Never run heavy build tasks on the Master node. 
- **Pipeline as Code**: Always use `Jenkinsfile` instead of Freestyle jobs.
- **Avoid "Dependency Hell"**: Limit the number of plugins and keep them updated.
- **Use Groovy sparingly**: While Pipeline is Groovy-based, try to move complex logic into external shell scripts or specialized tools (e.g., used Maven for build logic, not Groovy code).
- **Restart Jenkins regularly**: Jenkins is a long-lived Java application; periodic restarts help clear out memory fragmentation.

## 2. Scaling Jenkins
- **Vertical Scaling**: Give more CPU/RAM to the Master node. (Limited).
- **Horizontal Scaling**: Add more agents. This is the preferred way to handle increased build demand.
- **Distributed Master**: Using **CloudBees Jenkins Enterprise** or similar tools to run multiple Master instances for high availability.

## 3. Backup and Restore
If your Jenkins Master dies, you lose everything (Jobs, Config, History).
- **What to back up**: `$JENKINS_HOME` directory.
- **Tools**: 
    - **ThinBackup Plugin**: Regular automated backups of XML config files.
    - **Filesystem Snapshots**: E.g., taking a Snapshot of the EBS volume in AWS.
    - **Git**: If you use **JCasC** and **Pipelines**, your entire Jenkins setup is already in Git!

## 4. Troubleshooting common issues
- **"Waiting for an available executor"**: All agents are busy. Add more agents or increase the number of executors per agent.
- **"Jenkins is sluggish"**: Check the heap size of the JVM. You might need to increase `-Xmx`.
- **"No space left on device"**: Build workspaces grow very fast. Implement a "Discard Old Builds" policy on every job.
- **Plugin conflicts**: Check the Jenkins log (`/var/log/jenkins/jenkins.log`) for StackTraces during startup.

## 5. Discarding Old Builds
By default, Jenkins keeps every build forever. This will eventually crash your server.
- **Solution**: Always enable the "Discard Old Builds" option to keep only the last N builds or builds from the last N days.

> [!IMPORTANT]
> **Performance Tip**: 
> Move the `$JENKINS_HOME/workspace` directory to a separate fast disk (SSD) to prevent build I/O from slowing down the Jenkins Controller's UI and background processing.

> [!TIP]
> **Health Check**: 
> Use the **Metrics Plugin** along with Prometheus to monitor your Jenkins master's health (Heap usage, number of jobs, number of failures).
