# 📜 Git: Browsing History Study Guide

Git provides powerful tools to explore project history, find specific changes, attribute authorship, and even hunt down bugs using automated search.

---

## 📑 Overview
In this module, we cover:
- **Search**: Finding commits by author, date, or message.
- **Inspection**: Viewing specific commits and their changes.
- **Comparison**: Diffing commits and files across time.
- **Restoration**: Recovering deleted files or moving to earlier points in history.
- **Debugging**: Using `bisect` to find the exact commit that introduced a bug.

---

## 🔎 Viewing the History
The basic `log` command is your primary tool for navigating the timeline.

```bash
git log                  # Full history (Space: next page, q: quit)
git log --oneline        # Condensed single-line history
git log --oneline --stat  # Shows one-liner commits with file change statistics
git log --oneline --patch # Shows one-liner commits with the actual code diffs
```

---

## 🏹 Filtering the History
Narrow down your search to find exactly what you are looking for.

```bash
# Limit the number of commits
git log --oneline -3

# Filter by Author
git log --oneline --author="Mosh"

# Filter by Date
git log --oneline --after="2020-08-17"
git log --oneline --after="yesterday"
git log --oneline --after="one week ago"

# Filter by Commit Message (Case sensitive)
git log --oneline --grep="GUI"

# Filter by File Content (Search for specific code/strings)
git log --oneline -S"hello()"
git log --oneline -S"OBJECTIVE" --patch

# Filter by Range
git log --oneline fb0d184..edb3594 # From fb0d184 (exclusive) to edb3594 (inclusive)

# Filter by File
git log --oneline -- toc.txt # Use '--' to avoid ambiguity between filenames and branches
```

---

## 🎨 Formatting the Log Output
Customize how Git presents the log data.

```bash
# Custom formatting with placeholders
git log --pretty=format:"%an committed %H" # %an: author name, %H: full hash
git log --pretty=format:"%an committed %h" # %h: short hash

# Formatting with Colors
git log --pretty=format:"%Cgreen%an%Creset committed %Cred%h%Creset on %cd"
```

### ⌨️ Creating Aliases
Simplify long commands into short, memorable aliases.

```bash
# Create a custom 'lg' alias
git config --global alias.lg "log --pretty=format:'%an committed %h'"

# Create an 'unstage' alias
git config --global alias.unstage "restore --staged ."

# Accessing the alias
git lg
git unstage
```

---

## 👁️ Viewing a Specific Commit
```bash
git show HEAD~2           # Show the commit 2 steps before the current one
git show HEAD~2:path/to/file.txt # Show a specific file at that commit point
git show HEAD~2 --name-only # Only list changed file names
git show HEAD~2 --name-status # List names with change type (A/M/D)
```

---

## 📈 View Changes Across Commits
Compare the state of your project or specific files between two different points in time.

```bash
git diff HEAD~2 HEAD                 # Compare 2 versions ago to now
git diff HEAD~2 HEAD audience.txt    # Compare specific file across commits
git diff HEAD~2 HEAD --name-only     # List of changed files only
git diff HEAD~2 HEAD --name-status   # List of changed files with status
```

---

## 🚀 Checking Out a Commit
Move the project back to a specific point in time (Detached HEAD).

```bash
git log --oneline
git checkout fb0d184      # Move to a specific commit
git log --oneline --all   # View history including commits unreachable from HEAD
git checkout master       # Return to the main branch
```

---

## 🐞 Finding Bugs Using Bisect
`bisect` uses binary search to find the commit that introduced a bug.

```bash
git bisect start
git bisect bad            # Mark current version as 'bad'
git log --oneline         # Find a known good commit
git bisect good ca4918    # Mark a past version as 'good'

# Git will now switch commits. Test the app and report:
git bisect good           # If the bug is GONE
git bisect bad            # If the bug is STILL THERE

# Once the bad commit is identified, return to normal:
git checkout master
```

---

## 👥 Finding Contributors Using Shortlog
Summarize the commit history by author.

```bash
git shortlog              # Grouped by author
git shortlog -n           # Sorted by number of commits
git shortlog -n -s        # Summary only (count + name)
git shortlog -n -s -e      # Include email addresses
git shortlog -n -s -e --before="2026-01-01" --after="2020-01-01"
```

---

## 📂 File History & Authorship
### History of a Single File
```bash
git log toc.txt
git log --oneline toc.txt
git log --oneline --stat toc.txt
git log --oneline --patch toc.txt
```

### Finding the Author of a Specific Line (Blame)
```bash
git blame audience.txt
git blame -e audience.txt       # Include emails
git blame -e -L 1,3 audience.txt # Lines 1 through 3
```

---

## ⏪ Restoring a Deleted File
If you've deleted a file and committed the change, you can recover it from a previous commit.

1. Find the commit before it was deleted.
2. Use `checkout` to pull it back.

```bash
git log --oneline -- toc.txt      # Find the history of the deleted file
git checkout a642e12 toc.txt      # Extract file from the 'good' commit hash
git status -s                     # It will be staged for commit
git commit -m "Restore toc.txt"
```

---

## 🏷️ Tagging
Tags are pointers to specific points in history, often used for releases.

```bash
# Lightweight Tag (just a name)
git tag v1.0 1509686

# Annotated Tag (stores message, author, date)
git tag -a v1.1 -m "My version 1.1"

# Listing & Managing Tags
git tag                   # List all tags
git tag -n                # List tags with messages
git show v1.1             # View tag details
git tag -d v1.1           # Delete a tag
```

---

## 🛠️ GUI Tools
While the CLI is powerful, GUI tools excel at complex diff-ing and history visualization.
- **VS Code**: Use the built-in timeline or the **GitLens** extension for deep insights.
- **GitKraken**: A premier cross-platform GUI for visual history browsing.
