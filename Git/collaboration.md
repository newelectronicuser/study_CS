# 🤝 Git: Collaboration Study Guide

Collaboration is where Git truly shines, allowing teams to work on the same codebase through remote repositories and shared workflows.

---

## 📑 Overview
In this module, we cover:
- **Remote Interaction**: Pushing, fetching, and pulling changes.
- **Project Structure**: Milestones, issues, and labels.
- **Modern Workflows**: Pull Requests (PRs) and Open-source contributions.
- **Coordination**: Managing shared branches and syncing forked repositories.

---

## 🏗️ Workflows
There are two primary ways teams collaborate using Git:
1. **Centralized Workflow**: Everyone pushes and pulls from a single shared repository.
2. **Integration-Manager Workflow**: Developers fork the project, and a manager merges their contributions via Pull Requests.

---

## 🌐 Working with Remotes
Connecting your local machine to a server (like GitHub).

```bash
# View current remotes
git remote 
git remote -v # Includes fetch/push URLs

# Cloning an existing repository
git clone [url]
```

---

## 📥 Fetching vs. Pulling

### 1. Fetching
Downloads changes from the remote without merging them into your current work.
```bash
git fetch origin master     # Fetch specific branch
git fetch                   # Fetch all branches

# Reviewing fetched changes
git log --oneline --all --graph
git branch -vv              # Detailed view of tracking status
git merge origin/master     # Manually integrate changes
```

### 2. Pulling
Downloads and automatically merges changes into your current branch.
```bash
git pull                    # Defaults to merge
git pull --rebase           # Reapply your changes on top of remote changes
```

> [!TIP]
> Use `git pull --rebase` to keep a cleaner, linear project history.

---

## 📤 Pushing Changes
Upload your local commits to a remote server.

```bash
git push origin master
git push                    # Shorthand (if upstream is set)

# Handling Conflicts
# 1. git pull (to get remote changes)
# 2. Resolve conflicts & merge
# 3. git push
```

### ⚠️ Dangerous Pushing
```bash
git push -f                 # FORCE push: overwrites remote history. 
                            # Use with extreme caution!
```

---

## 🔑 Storing Credentials
Avoid typing your password for every interaction.

| Platform | Command |
| :--- | :--- |
| **Global Cache** | `git config --global credential.helper cache` (Valid for 15m) |
| **macOS** | `git config --global credential.helper osxkeychain` |
| **Windows** | Use the built-in Windows Credential Manager. |

---

## 🏷️ Sharing Tags & Releases
Tags represent specific release points (v1.0, v2.0).

```bash
# Share a tag with the remote
git tag v1.0
git push origin v1.0

# Deleting a tag
git tag -d v1.0             # Delete locally
git push origin --delete v1.0 # Delete on remote
```

### 📦 GitHub Releases
GitHub builds on tags by adding:
- **Release Notes**: Change logs and descriptions.
- **Assets**: Zip files or binaries of the source code.
- **Tracking**: Version history visible on the repo home page.

---

## 🌿 Sharing Branches
```bash
# Create and Push a new branch
git switch -C feature/change-password
git push -u origin feature/change-password # -u sets the upstream tracker

# Management
git branch -r               # List all remote-tracking branches
git branch -vv              # See tracking status (ahead/behind)
git push -d origin feature/change-password # Delete branch on remote

# Syncing
git remote prune origin     # Remove local copies of deleted remote branches
```

---

## 🔄 Pull Requests & Issues
The modern way to review and integrate code.

1. **Pull Requests (PR)**:
   - Request someone to pull your changes.
   - Assign **Reviewers**.
   - Review **Commits** and **Approve** or request changes.
2. **Project Management**:
   - **Issues**: Track bugs or feature requests.
   - **Labels**: Categorize issues (e.g., "bug", "enhancement").
   - **Milestones**: Group issues into upcoming releases.

---

## 🍴 Contributing to Open-source
The standard workflow for public projects:
1. **Fork**: Create your own copy of the repo on GitHub.
2. **Clone**: Bring the fork to your local machine.
3. **Branch**: Create a new branch for your work.
4. **Push & PR**: Push changes to your fork and open a PR to the original repo.

### 🔄 Keeping a Forked Repo Up to Date
To sync your fork with the original "upstream" repo:
1. `git remote add upstream [original_url]`
2. `git fetch upstream`
3. `git merge upstream/master`

---

## 🛠️ Collaboration Tools
Most developers use specialized tools to manage complex PRs and merges:
- **VS Code**: Native Git support and Pull Request extensions.
- **GitKraken**: Powerful visualization of remote branches and multi-repo management.
