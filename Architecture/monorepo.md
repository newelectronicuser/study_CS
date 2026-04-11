# Monorepo with Git Submodules: Best Practices

Managing a monorepo that relies significantly on Git submodules can be complex, as it effectively blends the concepts of a monorepo (one giant repository) with a polyrepo (many isolated repositories).

When implementing this architecture, the goal is to keep the developer experience seamless without falling into "submodule hell."

Here are the best practices categorized by different areas of your development workflow.

## References

- [Monorepo Repository](https://github.com/newelectronicuser/monorepo/tree/master)

## 1. Architectural Guidelines

- **Justify the Submodule:** Only use a submodule if the code truly needs to live independently. For example, if it's a shared utility library used by external projects _outside_ the monorepo. If it's only ever used inside this monorepo, it's often better to just merge the code directly into the monorepo tree.
- **Clear Boundaries:** Treat the submodule as a black-box package. The monorepo should consume the submodule via its exposed API/interfaces, rather than deeply coupling with its internal implementation.

## 2. Developer Workflow & CLI Abstractions

Git submodule commands are notoriously verbose and prone to user error (like accidentally detaching the HEAD).

- **Always Clone Recursively:** Train team members to clone the monorepo with the recurse flag:
  ```bash
  git clone --recurse-submodules <repository_url>
  ```
  _Tip: If a developer accidentally clones without this flag, they can initialize and fetch the missing submodules by running:_
  ```bash
  git submodule update --init --recursive
  ```
- **Abstract Git Commands:** Provide a `Makefile`, `Taskfile`, or a shell script at the root of the monorepo to hide the complexity of submodule commands.

  ```makefile
  # Example Makefile
  init:
      git submodule update --init --recursive

  update-all:
      git submodule update --remote --merge
  ```

- **Track Branches (Not Just Commits):** In your `.gitmodules` file, specify which branch the submodule should track. This makes updates more predictable.
  ```ini
  [submodule "libs/shared-auth"]
      path = libs/shared-auth
      url = git@github.com:your-org/shared-auth.git
      branch = main
  ```

## 3. Committing and Pushing Changes

The most common trap with submodules is committing a pointer in the monorepo to a submodule commit that hasn't been pushed to the remote yet, breaking the build for everyone else.

- **Bottom-Up Commits:** Always commit and push changes in the submodule _first_, then commit the updated reference pointer in the root monorepo.
- **Use Push Checks:** Configure Git to automatically check if submodule commits are pushed before allowing the monorepo to be pushed:
  ```bash
  git config push.recurseSubmodules check
  ```
  Alternatively, you can set it to `on-demand` which will attempt to push the submodule automatically:
  ```bash
  git config push.recurseSubmodules on-demand
  ```

## 4. Dependencies and Workspaces

If you are using modern languages, ensure the package managers in the top-level monorepo know how to interact with the submodules.

- **Python (uv / Poetry):** Add the submodule path to your `uv.workspace` or `pyproject.toml` workspace members. This ensures dependencies are solved globally.
- **Node.js / JS (npm / yarn / pnpm):** Add the submodule directories to your `pnpm-workspace.yaml` or `package.json` workspaces array.
- **Java (Maven / Gradle):** Add the submodule path as a standard `<module>` in your parent `pom.xml`.

## 5. CI/CD Pipeline Configuration

Your CI/CD environment needs to be explicitly told how to handle submodules, as by default many CI providers do a shallow clone and skip submodules.

- **Configure Recursive Clone:** In GitHub Actions, for example, enable submodules during checkout:
  ```yaml
  steps:
    - uses: actions/checkout@v4
      with:
        submodules: "recursive"
        # Important if submodules are in private repositories:
        token: ${{ secrets.PAT_TOKEN }}
  ```
- **Authentication:** If your submodules are private repositories, ensure your CI runner has the correct SSH deploy keys or Personal Access Tokens (PAT) loaded before checking out the submodules.

## 6. Avoiding "Detached HEAD" States

When a developer runs `git submodule update`, Git typically checks out the specific commit recorded in the superproject, placing the submodule in a "detached HEAD" state. If they start coding immediately, their commits won't be on any branch.

- Advise developers to explicitly checkout a branch inside the submodule before making changes:
  ```bash
  cd my-submodule
  git checkout main
  ```
- Or update and rebase/merge automatically:
  ```bash
  git submodule update --remote --rebase
  ```
