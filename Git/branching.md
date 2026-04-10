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

Squash merging condenses all commits from a feature branch into a single, clean commit on the destination branch. This is ideal for keeping the `master` history concise by hiding small, incremental work-in-progress commits.

### Why Squash?
- **Cleaner History**: Avoids "Merge branch..." noise.
- **Atomic Features**: Each feature is represented by exactly one commit.
- **Easy Reverts**: If a feature breaks something, you only need to revert one commit.

### 🛠️ Squash Workflow

Imagine a bugfix that takes several small, messy commits to solve. You want to bring those changes into `master` as one clean, atomic commit.

#### 1. The Scenario
Make several commits on a dedicated bugfix branch.
```bash
git switch -C bugfix/photo-upload
echo "bugfix" >> audience.txt
git commit -am "Update audience.txt"

echo "bugfix" >> toc.txt
git commit -am "Update toc.txt"

# View your messy work
git log --oneline --all --graph
```

#### 2. The Squash Merge
Switch back to master and condense the work.
```bash
git switch master
git merge --squash bugfix/photo-upload

# Check the state: changes are staged but not committed.
git status
```

#### 3. Finalizing the Commit
Create the single, "squashed" commit with a professional message.
```bash
git commit -m "Fix the bug on the photo upload page"

# Now master is one commit ahead, but the individual 'fix' commits are gone from its timeline.
git log --oneline --all --graph
```

#### 4. The Cleanup (Crucial)
Because the history was rewritten into a single commit, Git doesn't recognize the feature branch as being "merged" in the traditional sense.
```bash
git branch -d bugfix/photo-upload
# Error: the branch 'bugfix/photo-upload' is not fully merged.

# Use capital -D to force delete
git branch -D bugfix/photo-upload
```

> [!TIP]
> Squash merging is perfect for "Git-flow" where feature branches are short-lived. Use it to keep your main integration branch clean and readable.

---

## 🏗️ Rebasing

Rebasing is the process of moving or combining a sequence of commits to a new base commit. In practical terms, it "replays" your feature branch commits one by one on top of the latest commit of the master branch, resulting in a perfectly linear project history.

> [!CAUTION]
> **The Golden Rule**: Never rebase commits that have been pushed to a public/shared repository. Rebasing rewrites history, which will cause major issues for other collaborators.

### Rebase vs. Merge
- **Merge**: Preserves the complete history and the chronological order of commits, but can result in a messy "branchy" graph.
- **Rebase**: Creates a clean, linear history by eliminating unnecessary merge commits, but obscures the actual timing of when changes were made.

### Standard Rebase Workflow

Imagine a scenario where both your `feature` branch and `master` branch have moved forward with new commits. Rebasing allows you to "re-base" your feature work on top of the latest master.

#### 1. The Scenario
You create a feature and commit work, while someone else (or you) adds a commit to master.
```bash
git switch -C feature/shopping-cart
echo "hello" > cart.txt
git commit -m "Add cart.txt"

git switch master
echo "hello" > toc.txt
git commit -am "Update toc.txt"

# View the divergence
git log --oneline --all --graph
```

![Diverged Branches](files/Screenshot%20from%202026-03-26%2017-49-11.png)

#### 2. The Rebase
Move your feature commits so they start after the latest master commit.
```bash
git switch feature/shopping-cart
git rebase master

# Now the history is linear! 
git log --oneline --all --graph
```

![After Rebase](files/Screenshot%20from%202026-03-26%2017-51-50.png)

#### 3. The Payoff (Fast-forward Merge)
Now that the feature branch is directly "ahead" of master, you can fast-forward master to the feature tip.
```bash
git switch master
git merge feature/shopping-cart 

# Result: master now includes the feature work without a merge commit.
git log --oneline --all --graph
```

![Master Fast-forwarded](files/Screenshot%20from%202026-03-26%2017-58-46.png)

### ⚔️ Handling Rebase Conflicts
If Git hits a conflict, the rebase will pause. You must resolve the conflict at each step:

1. **Fix & Stage**: Open the conflicted files, resolve the issues, and stage them:
   ```bash
   # Use a mergetool if you have one configured
   git mergetool
   git add .
   ```
2. **Continue**: Tell Git to move to the next commit:
   ```bash
   git rebase --continue
   ```

**Other Rebase Controls:**
- `git rebase --skip`: Skips the current commit and its changes (use with caution).
- `git rebase --abort`: If the conflicts become too complex or frustrating, use this to cancel everything and return to your original state.

### 🛠️ Interactive Rebase
Interactive rebasing allows you to clean up your commit history before merging. Use it to squash, rename, or reorder commits.

```bash
# Open an interactive session for the last 5 commits
git rebase -i HEAD~5
```

Inside the editor, you can change the keyword next to each commit:
- `pick`: Use the commit as is.
- `reword`: Use the commit, but edit the message.
- `squash`: Meld the commit into the previous one.
- `drop`: Remove the commit entirely.

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
