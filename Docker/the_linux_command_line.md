# 🐧 The Linux Command Line: Study Guide

Mastering the Linux command line is essential for Docker development, server management, and devops workflows. This guide covers the foundational commands and concepts for working efficiently in a Linux environment.

---

## 📦 Linux Distributions

There are many different versions of Linux, known as **Distributions** (or distros). Each comes with its own package manager and default toolset.

- **Ubuntu** (Most popular for beginners/desktop)
- **Debian** (Rock solid stability, base for Ubuntu)
- **Alpine** (Ultra-lightweight, favorite for Docker images)
- **Fedora**
- **CentOS** (Common in enterprise server environments)

---

## 🚀 Running Linux & Basics

You can quickly spin up a Linux environment using Docker.

```bash
docker run ubuntu          # Runs an Ubuntu image (exits immediately if no command)
docker run -it ubuntu      # Runs Ubuntu in Interactive Terminal mode
```

### The Shell

- **Bash**: Stands for **Bourne-Again SHell**. It is the most common default shell on Linux.
- **Case Sensitivity**: Bash is **case sensitive**. `File.txt` and `file.txt` are different.
- **Command History**:
  ```bash
  echo $0      # Shows the name of the current shell (e.g., /bin/bash)
  history      # Lists previously executed commands
  !2           # Executes the second command in your history
  ```

---

## 📦 Managing Packages

Used to install, update, and remove software.

| Manager     | Description                                         |
| :---------- | :-------------------------------------------------- |
| **apt**     | Advanced Package Tool (Standard for Debian/Ubuntu). |
| **apt-get** | Lower-level version of `apt`.                       |
| **snap**    | Containerized software packages for Linux.          |

### Common Commands:

```bash
apt list           # List available packages
sudo apt update    # Update the local package index
```

---

## 📂 Linux File System

In Linux, **everything is a file**. The structure is a single tree starting from `/` (root).

| Directory | Description                                        |
| :-------- | :------------------------------------------------- |
| **/bin**  | Essential binaries and programs.                   |
| **/boot** | Static files of the boot loader.                   |
| **/dev**  | Device nodes (accessing hardware as files).        |
| **/etc**  | Editable Text Configuration (System-wide configs). |
| **/home** | Home directories for users (e.g., `/home/john`).   |
| **/root** | Home directory for the root (admin) user.          |
| **/lib**  | Essential shared libraries and kernel modules.     |
| **/var**  | Variable files (log files, spool directories).     |
| **/proc** | Virtual files representing running processes.      |

---

## 🗺️ Navigating the File System

```bash
pwd        # Print Working Directory (Where am I?)

ls         # See files and directories in current location
ls -1      # Show one item per line
ls -l      # Long listing (shows permissions, owner, size)

cd etc/a [TAB]   # Use TAB for auto-completion
cd ..            # Go up one directory
cd ../..         # Go up two directories
cd ~             # Go to current user's home directory
```

---

## 🛠️ Manipulating Files & Directories

```bash
cd ~
mkdir test               # Make a directory
mv test docker           # Move or rename a file/directory
cd docker
touch file1.txt file2.txt # Create empty files
rm hello.txt             # Remove a file
rm file*                 # Remove all files starting with "file"
cd ..
rm -r docker/            # Remove a directory recursively
```

---

## 📑 Editing & Viewing Files

```bash
nano file1.txt           # Light, easy-to-use terminal text editor
cat file1.txt            # Print the entire file content to the terminal

more /etc/adduser.conf   # View file page by page (Forward only)
less /etc/adduser.conf   # Enhanced "more" (Go up/down with arrow keys)

head -n 5 /etc/adduser.conf # Show the FIRST 5 lines
tail -n 5 /etc/adduser.conf # Show the LAST 5 lines
```

---

## ↪️ Redirection

Direct the output of a command into a file instead of the screen.

```bash
# Combine two files into a third
cat file1.txt file2.txt > file3.txt

# Create/Overwrite a file with specific text
echo hello > hello.txt

# Save a directory listing to a text file
ls -l /etc > etc.txt
```

---

## 🔍 Searching & Finding

### Grep (Global Regular Expression Print)

```bash
grep -i hello file1.txt      # Search for "hello" (case-insensitive)
grep -i hello file1.txt file2.txt
grep -i hello file*
grep -ir hello .             # Search recursively in all files in current dir
grep -ir datasource . --include='application*.yaml'
```

### Finding Files

```bash
find                         # List ALL files/folders in current directory
find /etc                    # List all files/folders in /etc
find -type d                 # Search for DIRECTORIES only
find -type f                 # Search for FILES only
find -type f -name "f*"      # Case-sensitive search for files starting with "f"
find -type f -iname "F*"     # Case-insensitive search
find ../../ -type f -name "*.py" # Find all python files in parent's parent dir
```

---

## ⛓️ Chaining Commands

```bash
# Sequential execution
mkdir test ; cd test ; echo done

# AND: Execute only if previous command succeeded
mkdir test && cd test && echo done

# OR: Execute only if previous command FAILED
mkdir test || echo "directory exists"

# PIPE: Use output of one command as input for another
ls /etc | less
ls /etc | head -n 5

# MULTI-LINE: Use "\" to split a long command
mkdir hello;\
cd hello;\
echo done
```

---

## 🌍 Environment Variables

```bash
printenv                 # Print all environment variables
printenv | grep -i PATH  # Find specific variables
printenv PATH            # Print value of specific variable
echo $PATH               # Print value (common shorthand)

source .bashrc           # Reload the shell configuration without restarting
```

---

## ⚙️ Managing Processes

```bash
ps                       # List running processes for current user
sleep 10 &               # Run a command in the BACKGROUND
kill 146415              # Terminate a process using its Process ID (PID)
```

---

## 👥 Managing Users & Groups

### Users

- **useradd**: Lower-level utility.
- **adduser**: More interactive and user-friendly wrapper for `useradd`.
  ```bash
  useradd -m john        # Create user with a home directory
  adduser bob            # (Recommended) Interactive setup
  usermod                # Modify user attributes
  userdel                # Delete user
  ```

### Groups

```bash
groupadd developers      # Create a new group
usermod -G developers john # Add user 'john' to the 'developers' group
```

---

## 🔐 File Permissions

Use `chmod` (Change Mode) to set permissions.

- **u**: User (Owner)
- **g**: Group
- **o**: Others

```bash
chmod u+x deploy.sh      # Add executable (+x) permission for user
chmod u-x deploy.sh      # Remove executable (-x) permission for user
chmod o+x deploy.sh      # Add executable permission for others

# Complex permissions: add execute/write for owner/group, remove read for all .sh files
chmod og+x+w-r *.sh
```

> [!NOTE]
> In Linux, permissions are often represented by numbers: `7` (Full), `6` (Read/Write), `4` (Read Only). For example, `chmod 755 file.sh`.
