Rewriting History

Why and when to rewrite history
Undo or revert commits
Use interactive rebasing
Recover lost commits

Why Rewrite History

Why do we need history in the first place?
We need it see, WHAT was changed, WHY, WHEN

Bad History
Poor commit messages
Large commits
Small commits

We need a CLEAN HISTORY

Tools
Squash small, related commits
Split large commits
Reword commit messages
Drop unwanted commits
Modify commits

The Golden Rule of Rewriting History

DON’T REWRITE PUBLIC HISTORY!

Example of a Bad History

![Bad History](files/Screenshot%20from%202026-03-28%2014-26-02.png)

Undoing Commits

Applicable if changes are local

![Bad History](files/Screenshot%20from%202026-03-28%2014-28-47.png)

git log --oneline --all --graph

git reset --soft HEAD~1

git log --oneline --all --graph

git reset --mixed HEAD

git log --oneline --all --graph

git reset --hard HEAD

Reverting Commits

![Bad History](files/Screenshot%20from%202026-03-28%2015-37-51.png)

git log --oneline --all --graph

git revert --no-commit HEAD~3..

git revert --continue
Give new meaningful message

![Bad History](files/Screenshot%20from%202026-03-28%2015-39-51.png)

Recovering Lost Commits

git log --oneline --all --graph

![Bad History](files/Screenshot%20from%202026-03-28%2015-39-51.png)

git reset --hard HEAD~3

git reset --hard HEAD~3

git log --oneline --all --graph

![Bad History](files/Screenshot%20from%202026-03-28%2015-41-28.png)

git reflog

![Bad History](files/Screenshot%20from%202026-03-28%2015-46-31.png)

git reset --hard HEAD@{2}

git log --oneline --all --graph

![Bad History](files/Screenshot%20from%202026-03-28%2015-47-58.png)

git reflog show branchName

Amending the Last Commit

Make changes

git add .

git commit --amend

Amending an Earlier Commit

![Bad History](files/Screenshot%20from%202026-03-28%2016-11-07.png)
![Bad History](files/Screenshot%20from%202026-03-28%2016-11-41.png)

git rebase -i 8527033
Now, we can edit commits from old commit on top to latest commit on bottom.

![Bad History](files/Screenshot%20from%202026-03-28%2016-13-26.png)
![Bad History](files/Screenshot%20from%202026-03-28%2016-17-10.png)

echo license > license.txt

git add .

git commit –amend

git log --oneline --all --graph

![Bad History](files/Screenshot%20from%202026-03-28%2016-19-37.png)

git rebase --continue
Or --abort

Dropping a Commit

![Bad History](files/Screenshot%20from%202026-03-28%2016-32-42.png)

git rebase -i 6cbd931~1
Or 6cbd931^

![Bad History](files/Screenshot%20from%202026-03-28%2016-34-35.png)

    Drop or delete the line

![Bad History](files/Screenshot%20from%202026-03-28%2016-35-59.png)

    Now, resolve the conflict.

![Bad History](files/Screenshot%20from%202026-03-28%2016-36-38.png)

![Bad History](files/Screenshot%20from%202026-03-28%2016-38-27.png)

![Bad History](files/Screenshot%20from%202026-03-28%2016-39-12.png)

Rewording Commit Messages

![Bad History](files/Screenshot%20from%202026-03-28%2016-41-03.png)

![Bad History](files/Screenshot%20from%202026-03-28%2016-41-20.png)

![Bad History](files/Screenshot%20from%202026-03-28%2016-42-11.png)

![Bad History](files/Screenshot%20from%202026-03-28%2016-43-06.png)

Reordering Commits

Reorder the lines after running rebase command

Squashing Commits

![Bad History](files/Screenshot%20from%202026-03-28%2016-55-47.png)

Reorder and then squash if commits are not next to each other

There is another way called fixup

![Bad History](files/Screenshot%20from%202026-03-28%2016-58-42.png)

Git will use the message of the first commit and apply it to the commit that combines all these.

Splitting a Commit

![Bad History](files/Screenshot%20from%202026-03-28%2017-02-46.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-03-23.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-04-26.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-04-49.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-05-46.png)

Git reset –mixed HEAD^

![Bad History](files/Screenshot%20from%202026-03-28%2017-07-14.png)

Now create two different commits for two different files.

![Bad History](files/Screenshot%20from%202026-03-28%2017-07-27.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-08-44.png)

![Bad History](files/Screenshot%20from%202026-03-28%2017-08-56.png)
