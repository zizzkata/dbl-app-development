# Kamerdate (name is work in progress)
## Description
DBL project for an android application.
## House Rules
### Coding style
Coding style must comply with the following document: [a relative link](files/Coding_standards.pdf)

**IMPORTANT** Always format all the files before commit.

### Commits
All commits should have the following structure:

```text
<scope>: <subject>
<BLANK LINE>
<body>
```

All commits need to be in the imperative form: "Add file/s" or "Fix bug", and not like "Fixed bug" or "Fixes bug".**IMPORTANT** Also they must only have the changes that are described in the message and nothing extra.

* **Scope** is optional. This is used if you need (or want) to specify a module or a group to clarify the commit. It must be no more than 50 chars.
* **Subject** is a clear summary of the commit. It is used to identify the main action/problem behind the commit.
* **Blank line** is just a separator of the subject from the body. If you use terminal you can do it by: 
```
git commit -m"<scope>: <subject>" -m"<body>"
```
Otherwise if you use a GUI it should be straightforward. 
* **Body** is the description of the commit. Here you put extra information about the reason/action/problem behind the commit. Some commits don't necessarily need it, because the subject could be descriptive enough, but it is mandatory nevertheless. It should be wrapped to about 72 characters or so and should have a hard limit of 100 characters to make sure it is easily readable in git tools and services such as gitlab, github etc.

### Branches

Branches are categorized by the following groups:

* **develop**: these branches are reserved for *bleeding edge* code. Code that is not tested and is not known if it behaves correctly. Overall this is for experimental features and looks like the following: *develop/name-of-branch*
* **feature branches**: these branches are like follows: *feature/name-of-branch*. These branches are for new features.
* **fix branches**: these branches are like follows: *fix/name-of-branch*. They are branches for a fix to an already existing problem in the codebase.
* **master** or **main**: this branch is for *release* and is kept clean and safe. No force pushes are allowed except in some exceptions. You should contact your scrum master if such necessity occurs.

### Merge

All merges to main go through your Scrum master - @zizzkata.

**IMPORTANT** If the house rules are not kept or the actions fail, the code will not be merged. It is the sole responsibility of the developer to keep the rules when the code is merged.

**IMPORTANT** Before merging you must squash commits that are of no bigger importance than the others. If you find suitable/important to have some commit un-squashed you are allowed to do so. Keep in mind you can change the order of the commits if necessary(this is encouraged when squashing). A merge usually has in total of 3-5 commits after merging.


### Release
Versioning should comply with these rules: https://semver.org/spec/v2.0.0.html


## Collaborators - Group 03