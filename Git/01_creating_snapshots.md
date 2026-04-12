# 📸 Git: Creating Snapshots Study Guide

Mastering snapshots is the core of version control. This guide covers how Git tracks changes through the staging area and into the repository.

---

## 🔄 Git Workflow

Git uses a three-tier architecture to manage your code:
**Work Directory** ➔ **Staging Area** ➔ **Repository**

---

## 📤 Staging Files

Before a change is recorded, it must be added to the staging area.

```bash
# Stage all text files
git add *.txt

# Stage the entire directory recursively
git add .
```

---

## 💾 Committing Changes

A commit is a permanent snapshot of the staged changes in the repository.

```bash
# Commit with a short message
git commit -m "initial commit"

# Commit using the default text editor for long messages
git commit
```

### 💡 Committing Best Practices

- **Atomic Commits**: Only commit related changes. Bug fixes and typos should be in separate commits.
- **Meaningful Records**: Commit code once you reach a logical state you want to preserve.
- **Commit Wording**:
  - **Recommended (Present)**: "Fix the bug."
  - **Discouraged (Past)**: "Fixed the bug."

---

## ⚡ Skipping the Staging Area

You can commit tracked files directly from the working directory, bypassing the staging step.

```bash
# Directly commit all modified tracked files
git commit -a -m "Fixed the bug that prevents user login."
# Shorthand version:
git commit -am "Fixed the bug that prevents user login."
```

---

## 🗑️ Removing & Renaming Files

### Removing Files

```bash
# 1. Remove from working directory
rm file2.txt

# 2. Check staging area
git ls-files

# 3. Inform Git of the removal
git add file2.txt
git commit -m "Removed unused code."

# SHORTHAND: Remove from both working dir and staging area simultaneously
git rm file2.txt
git rm *.txt
```

### Renaming or Moving Files

```bash
# Manual rename
mv file1.txt main.js
git add file1.txt main.js

# Git shorthand (automatically stages the rename)
git mv main.js file1.js
```

---

## 🙈 Ignoring Files

The `.gitignore` file specifies intentionally untracked files that Git should ignore.

**Common Patterns in `.gitignore`:**

```text
logs/       # Ignore logs folder
main.log    # Ignore specific file
*.log       # Ignore all log files
```

### ⚠️ Correcting Accidental Commits

If you accidentally committed a file that should be ignored:

```bash
# Remove from staging area/repo but KEEP in working directory
git rm --cached -r bin/
git commit -m "removed bin which was accidentally committed"
```

---

## 🔍 Status & Diff Tools

### Quick Status

```bash
git status -s  # "Short" status to see changes at a glance
```

### Viewing Changes

```bash
# Compare Repository vs. Staged Area
git diff --staged file1.txt

# Compare Staged Area vs. Working Directory
git diff file1.txt
```

### Visual Diff Tools

- **KDiff3**
- **P4Merge**
- **WinMerge** (Windows Only)

#### 🛠️ Setting VS Code as the Diff Tool

```bash
git config --global diff.tool vscode
git config --global difftool.vscode.cmd "code --wait --diff $LOCAL $REMOTE"

# To verify or manually edit
git config --global -e
# Ensure $LOCAL $REMOTE are defined in the config
```

---

## 📜 Viewing History & Commits

```bash
git log                  # Full history
git log --oneline        # Condensed history (one line per commit)
git log --oneline --reverse # History from oldest to newest
```

### Inspecting a Specific Commit

```bash
git show HEAD            # Latest commit
git show HEAD~1          # One commit before HEAD
git show [commit_id]     # Specific commit by its hash

# View a specific file as it existed in a previous commit
git show HEAD~1:.gitignore

# List the tree structure of a previous commit
git ls-tree HEAD~1
```

---

## 🧩 Git Objects

Git stores data using four primary objects:

1. **Commits**: Snapshots of the project.
2. **Blobs**: The actual file contents (Binary Large Objects).
3. **Trees**: Represent directories and link to blobs/other trees.
4. **Tags**: Named pointers to specific commits.

---

## ⏪ Unstaging & Discarding Changes

### Unstaging a File

Move a file from the staging area back to the working directory (restores status from latest commit).

```bash
git restore --staged file1.js
```

### Discarding Local Changes

Revert a file in your working directory to the state it was in the staging area.

```bash
git restore file1.js
```

### Cleaning Untracked Files

```bash
git clean       # Remove all new untracked files
git clean -fd   # Force remove whole untracked directories
```

---

## 🕒 Restoring Earlier Versions

Restore a file to its state from a specific point in history.

```bash
# Restore file1.js to its state from one commit before HEAD
git restore --source=HEAD~1 file1.js
```

---

> [!NOTE]
> For a visual interface to manage snapshots, you can also use tools like **GitKraken**.
