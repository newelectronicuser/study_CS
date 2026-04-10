# 📜 Git: Rewriting History
 
Mastering the art of rewriting history allows you to maintain a clean, readable, and professional commit log. This guide covers why, when, and how to safely modify your Git history.
 
---
 
## 📑 Overview
 
In this module, we cover:
- **Philosophy**: Why and when to rewrite history.
- **Undoing Changes**: Difference between `reset` and `revert`.
- **Reflog**: How to recover "lost" commits.
- **Interactive Rebasing**: Squashing, splitting, and rewording commits.
 
---
 
## 🎯 Why Rewrite History?
 
History isn't just a backup; it's documentation. We need it to see **WHAT** was changed, **WHY**, and **WHEN**.
 
### 🔴 Characteristics of "Bad" History
- Poor or cryptic commit messages.
- **Large Commits**: Combining unrelated changes.
- **Tiny Commits**: "typo fix", "testing", "fix again" (commit noise).
 
### 🟢 The Goal: A Clean History
A clean history helps team members (and your future self) understand the evolution of the codebase.
 
**Available Tools:**
- **Squash**: Combine small, related commits.
- **Split**: Break large, complex commits into smaller pieces.
- **Reword**: Fix typos or clarify commit messages.
- **Drop**: Remove unwanted commits entirely.
- **Modify**: Edit the content of an existing commit.
 
> [!CAUTION]
> ### 🛡️ The Golden Rule of Rewriting History
> **NEVER REWRITE PUBLIC HISTORY!**
> Only rewrite commits that only exist on your local machine. Once you push commits to a shared repository, rewriting them can cause major conflicts for your teammates.
 
---
 
## 📉 Example of a Bad History
 
Below is an example of a messy commit history that needs cleaning:
 
![Bad History Example](files/Screenshot%20from%202026-03-28%2014-26-02.png)
 
---
 
## ⏪ Undoing Commits (Local Only)
 
If your changes are still local, you can use `git reset` to move your branch pointer back.
 
![Undoing Commits Visual](files/Screenshot%20from%202026-03-28%2014-28-47.png)
 
```bash
git log --oneline --all --graph
 
# Option 1: Soft Reset (Keeps changes in Staging)
git reset --soft HEAD~1
 
# Option 2: Mixed Reset (Keeps changes in Working Directory, Unstages them)
git reset --mixed HEAD~1
 
# Option 3: Hard Reset (DELETES all changes - BE CAREFUL)
git reset --hard HEAD~1
```
 
---
 
## 🔄 Reverting Commits (Public Safe)
 
When changes are already pushed, use `git revert` to create a new commit that "undoes" the previous work without deleting it from history.
 
![Reverting Commits Initial](files/Screenshot%20from%202026-03-28%2015-37-51.png)
 
```bash
git log --oneline --all --graph
 
# Revert a range of commits without auto-committing
git revert --no-commit HEAD~3..
 
# Finalize the revert
git revert --continue
```
 
![Reverted Result](files/Screenshot%20from%202026-03-28%2015-39-51.png)
 
---
 
## 🔋 Recovering Lost Commits
 
If you accidentally performed a `hard reset` or lost a commit during a rebase, Git's **Reflog** acts as a safety net.
 
```bash
git log --oneline --all --graph
```
![Before Reset](files/Screenshot%20from%202026-03-28%2015-39-51.png)
 
```bash
# Accidentally resetting back
git reset --hard HEAD~3
```
![After Reset](files/Screenshot%20from%202026-03-28%2015-41-28.png)
 
### 🛠️ Using Reflog to Rescue
 
```bash
# 1. View the log of all actions
git reflog
```
![Reflog List](files/Screenshot%20from%202026-03-28%2015-46-31.png)
 
```bash
# 2. Reset back to the state at a specific index (e.g., index 2)
git reset --hard HEAD@{2}
```
![Recovered State](files/Screenshot%20from%202026-03-28%2015-47-58.png)
 
> [!TIP]
> You can also check the history of a specific branch using:
> `git reflog show branchName`
 
---
 
## 📝 Modifying Commits
 
### Amending the Last Commit
If you forgot to add a file or made a typo in the message of your **most recent** commit:
 
```bash
# 1. Make your changes and stage them
git add .
 
# 2. Update the last commit
git commit --amend
```
 
### Amending an Earlier Commit
To fix something further back in history, use **Interactive Rebase**.
 
![Starting Rebase](files/Screenshot%20from%202026-03-28%2016-11-07.png)
![Rebase List](files/Screenshot%20from%202026-03-28%2016-11-41.png)
 
```bash
# Start rebase from a specific commit ID
git rebase -i 8527033
```
*Note: In the editor, Git lists commits from oldest (top) to latest (bottom).*
 
![Editing Mode](files/Screenshot%20from%202026-03-28%2016-13-26.png)
![Applying Changes](files/Screenshot%20from%202026-03-28%2016-17-10.png)
 
```bash
# Once Git stops at the commit you want to edit:
echo "license content" > license.txt
git add .
git commit --amend
 
# Finish the rebase
git rebase --continue
```
![Rebase Completed](files/Screenshot%20from%202026-03-28%2016-19-37.png)
 
---
 
## 🗑️ Dropping a Commit
 
Use interactive rebase to completely remove a commit from history.
 
![Target Commit](files/Screenshot%20from%202026-03-28%2016-32-42.png)
 
```bash
# Rebase starting from the parent of the target commit
git rebase -i 6cbd931^
```
![Interactive Editor](files/Screenshot%20from%202026-03-28%2016-34-35.png)
 
1. **Drop**: Delete the line or change the keyword to `drop`.
2. **Resolve**: If conflicts occur, resolve them and continue.
 
![Resolving Conflict](files/Screenshot%20from%202026-03-28%2016-35-59.png)
![Conflict States](files/Screenshot%20from%202026-03-28%2016-36-38.png)
![Conflict States 2](files/Screenshot%20from%202026-03-28%2016-38-27.png)
![Drop Successful](files/Screenshot%20from%202026-03-28%2016-39-12.png)
 
---
 
## 🔀 Advanced Interactive Rebase Operations
 
### Reword Commit Messages
Change the message of any commit without affecting its content.
 
![Initial Log](files/Screenshot%20from%202026-03-28%2016-41-03.png)
![Reword Selection](files/Screenshot%20from%202026-03-28%2016-41-20.png)
![New Message Editor](files/Screenshot%20from%202026-03-28%2016-42-11.png)
![Reword Result](files/Screenshot%20from%202026-03-28%2016-43-06.png)
 
### Reordering Commits
Simply swap the order of lines in the interactive rebase editor to change the chronological order of commits.
 
### Squashing Commits
Combine multiple commits into one.
1. Reorder the commits if they aren't adjacent.
2. Change keywords to `squash` (melds into the commit above).
 
![Squash Initial](files/Screenshot%20from%202026-03-28%2016-55-47.png)
 
> [!TIP]
> **Fixup**: Similar to squash, but it automatically discards the squashed commit's message and keeps the message of the parent commit.
 
![Fixup Result](files/Screenshot%20from%202026-03-28%2016-58-42.png)
 
---
 
## ✂️ Splitting a Commit
 
Break down a single commit that contains too many changes.
 
![Target Commit for Splitting](files/Screenshot%20from%202026-03-28%2017-02-46.png)
![Edit Selection](files/Screenshot%20from%202026-03-28%2017-03-23.png)
 
1. Start `rebase -i` and mark the commit with `edit`.
2. When Git stops, perform a **Mixed Reset** to unstage the changes.
   ```bash
   git reset --mixed HEAD^
   ```
![Mixed Reset Visual](files/Screenshot%20from%202026-03-28%2017-04-26.png)
![Mixed Reset Visual 2](files/Screenshot%20from%202026-03-28%2017-04-49.png)
![Mixed Reset Visual 3](files/Screenshot%20from%202026-03-28%2017-05-46.png)
 
3. Create separate commits for the changes.
   ```bash
   # Commit 1
   git add file1.txt
   git commit -m "Partial changes 1"
 
   # Commit 2
   git add file2.txt
   git commit -m "Partial changes 2"
   ```
![Partial Commit 1](files/Screenshot%20from%202026-03-28%2017-07-14.png)
![Partial Commit 2](files/Screenshot%20from%202026-03-28%2017-07-27.png)
![Split Process](files/Screenshot%20from%202026-03-28%2017-08-44.png)
![Split Result](files/Screenshot%20from%202026-03-28%2017-08-56.png)
 
4. Finish rebase: `git rebase --continue`.
 
---
 
> [!IMPORTANT]
> Always verify your `git log` after a rebase to ensure the history looks exactly how you intended!
