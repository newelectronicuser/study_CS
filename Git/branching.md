# 🌿 Git: Branching Study Guide

Branching is one of Git's most powerful features, allowing developers to diverge from the main line of development and work on features or bug fixes in isolation.

---

## 📑 Overview

In this module, we cover:

- **Management**: Creating, switching, and renaming branches.
- **Evaluation**: Comparing branches and viewing merged status.
- **Integration**: Merging (Fast-forward vs. 3-way) and Rebasing.
- **Conflict Resolution**: Handling and undoing merges.
- **Utilities**: Stashing, cherry-picking, and selective file restoration.

---

## 🏗️ Working with Branches

A branch in Git is simply a pointer to a specific commit.

```bash
git branch bugfix          # Create a new branch
git branch                 # List all local branches (* indicates current)
git switch bugfix          # Switch to the 'bugfix' branch
git switch -C feature/login # Create and switch in one command

# Rename a branch
git branch -m bugfix bugfix/signup-form

# Standard workflow
git add .
git commit -m "Fix the signup bug"
```

---

## 📈 Comparing Branches

```bash
# View commits in 'bugfix' that are not in 'master'
git log master..bugfix/signup-form

# Compare the actual code changes
git diff master..bugfix/signup-form

# Compare a specific branch with your current branch
git diff bugfix/signup-form
```

---

## 📥 Stashing

Used to temporarily shelve (stash) changes so you can switch branches without committing.

```bash
# Stash changes with a description
git stash push -m "new tax rules"

# Include untracked files in the stash
git stash push -a -m "Full backup"

# Managing the Stash List
git stash list             # View all stashes (stash@{0}, stash@{1}, etc.)
git stash show stash@{1}   # View changes in a specific stash

# Applying & Deleting
git stash apply stash@{1}  # Apply changes (keeps stash in the list)
git stash drop stash@{1}   # Remove a specific stash
```

> [!TIP]
> If you drop `stash@{0}`, the remaining stashes shift their positions up. You can repeatedly drop at index 0 to clear the stack.

---

## 🤝 Merging

Integrating changes from one branch into another.

### 1. Fast-forward Merges

![Fast-forward Merges](files/Screenshot%20from%202026-03-25%2011-13-38.png)
![Fast-forward Merges](files/Screenshot%20from%202026-03-25%2011-14-06.png)

Occurs when the target branch hasn't diverged. Git simply moves the pointer forward.

```bash
git merge bugfix/signup-form
```

### 2. Three-way Merges

![Fast-forward Merges](files/Screenshot%20from%202026-03-25%2011-18-36.png)
![Fast-forward Merges](files/Screenshot%20from%202026-03-25%2011-19-05.png)

Occurs when both branches have diverged. Git creates a new "Merge Commit".

```bash
git merge feature/change-password
```

### 🚫 Controlling Fast-forward

```bash
# Disable fast-forward for a specific merge (force a merge commit)
git merge --no-ff bugfix/login-form

# Globally disable fast-forward (often used for company policy)
git config --global ff no
```

---

## 👁️ Viewing Merged Branches

```bash
git branch --merged      # List branches already integrated into current branch
git branch --no-merged   # List branches with work yet to be merged
```

---

## ⚔️ Resolving Merge Conflicts

Conflicts happen when the same line in the same file is modified in both branches.

### Manual Resolution

1. Open the conflicted file and choose the correct version.
2. `git add [filename]` to mark it as resolved.
3. `git commit` to finalize the merge.

### Graphical & Tools

- **P4Merge**: A dedicated visual tool for complex merges.

### Aborting a Merge

If things go wrong during a merge, use the following command to return to the state before the merge started:

```bash
git merge bugfix/change-password
git merge --abort
```

---

## ⏪ Undoing a Faulty Merge

### Before Pushing (Reset)

```bash
git reset --soft HEAD~1   # Undo commit, keep changes staged
git reset --mixed HEAD~1  # Undo commit, keeps changes unstaged
git reset --hard HEAD~1   # Undo commit and DELETE all changes (CAUTION)
```

### After Pushing (Revert)

```bash
# Create a NEW commit that reverses the merge
git revert -m 1 HEAD      # Revert to the first parent (usually master)
```

---

## 🔸 Squash Merging

Condense all commits from a feature branch into a single commit on the master branch.

```bash
git switch master
git merge --squash bugfix/photo-upload
git commit -m "Fix photo upload bug"

# Note: The original branch must be force-deleted after squashing
git branch -D bugfix/photo-upload
```

---

## 🏗️ Rebasing

Moves the entire feature branch so it begins on the tip of the master branch.

> [!CAUTION]
> **Golden Rule of Rebasing**: Only rebase local commits. Never rebase commits that have been pushed to a public repository.

```bash
git switch feature/shopping-cart
git rebase master

# Handling Conflicts during Rebase
git rebase --continue    # Continue after manual resolution
git rebase --skip        # Skip the current problematic commit
git rebase --abort       # Cancel the rebase entirely
```

### 🛠️ Mergetool Configuration

To prevent `.orig` backup files after using tools like P4Merge:

```bash
git config --global mergetool.keepBackup false
git clean -fd  # Clean existing backup files
```

---

## 🍒 Essential Tools

### Cherry-picking

Apply a specific commit from any branch to your current location.

```bash
git cherry-pick 5670ec
```

### Selective File Restoration

Pull a single file from another branch without merging the whole branch.

```bash
git restore --source=feature/send-email -- toc.txt
```

---

> [!NOTE]
> For a visual experience, use the **Git Graph** and **Timeline** views in **VS Code** to manage your branches.
