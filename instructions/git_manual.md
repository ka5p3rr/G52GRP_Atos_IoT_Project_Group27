# Git Manual <!-- omit in toc -->

## Table of contents <!-- omit in toc -->

- [1 Introduction](#1-introduction)
  - [1.1 Git](#11-git)
- [2 GitKraken](#2-gitkraken)
  - [2.1 Set up](#21-set-up)
  - [2.2 Basics](#22-basics)
  - [2.3 Branching](#23-branching)
- [3 Git Branching Strategy](#3-git-branching-strategy)
  - [3.1 Initial Strategy](#31-initial-strategy)
  - [3.2 Advanced Strategy](#32-advanced-strategy)
    - [Update](#update)

## 1 Introduction

Written by [David Mares](https://github.com/ka5p3rr)
(if you find an error in the text or you have any other issues please contact me - psydm6@nottingham.ac.uk)

This document is created to set standard git workflow, guidelines and also introduce you to GitKraken. Please follow the instructions carefully. 

> Note: All instructions are written with focus on Windows 10 and GitKraken.

### 1.1 Git

Make sure you have the current version of git installed on your machine.

If you don't have git installed just go to this [website](https://git-scm.com/). Download it and follow the install instruction. I recommend using the default settings if you are unsure about the functionality.

First of all there are three main options how to use git:

1. CLI (Command Line Interface)
2. IDE
3. Git GUI client
    - there are many [options](https://git-scm.com/downloads/guis/)

You can use your IDE git features or command line, but I do recommend using GitKraken (the only reason for this is consistency and standardised commenting).

## 2 GitKraken

We get a free GitKraken PRO license from GitHub Student Developer Pack. If you haven't signed up already go to [Student Developer Pack - GitHub Education](https://education.github.com/pack). Click Join GitHub Education in the top right corner and follow the instructions. GitKraken is a great git tool as it provides user-friendly GUI and functionality for local and remote branching, commit messages, merging, pull requests and much more.

### 2.1 Set up

Download [GitKraken](https://www.gitkraken.com/download) and install it. Once the installation is done you are asked to sign in. Just click the Sign in with GitHub account button and connect your GitHub account with GitKraken. Next step is to set up your local git profile. In the Profile set up dialog leave your Profile Name as default and set your Name (should be loaded automatically). Email setup is a little bit more complicated. It is important to check what your GitHub email is, because you might have set your email as private and GitHub generated a special email address for you to use. Please log in to GitHub and go to settings, under Emails look for Keep my email address private. If the box is checked then you have set your email to be private and you need to use the generated one, which will be specified bellow (`xxxxxx@users.noreply.github.com`). Copy the email address and paste it to the GitKraken email field. Otherwise, if you have not set your email as private just use your Primary profile email address. Save the changes.

All the values you have just set in GitKraken are saved in your .gitconfig file, which has all the git configuration information used for your authentication, settings etc. To view the gitconfig file type `git config --list` in the Git Bash (which is part of the git installation on Windows, just search for it in your apps).

In GitKraken click the Open a Project button, go to the Clone section. Click GitHub.com in the list and scroll all the way down, where you should see the shared project. Choose where you want to clone the remote repository and click Clone the repo! button. Once the cloning is done, click Open Now and when the project loads click the down arrow next to the Pull button in the top bar and choose Pull (rebase) as your default by clicking the empty circle next to it. Now close GitKraken.

Open Git Bash and using cd commands navigate to the directory of the cloned project. When you are in the directory run: `git config --local commit.template instructions/.gitmessage` in your Git Bash. This command will set the .gitmessage file as our default commit message template.

Now you can close the Git Bash and open up GitKraken again. In the top left corner click on file and go to preferences. Under Repo-Specific Preferences go to Commit Template and make sure that Apply this template to commit messages and Remove comments from commit messages are checked. Also there should be some text in the Description section (please don't change that). Now you can Exit Preferences.

> Remember: You have to run the Git Bash template command and change the Repo-Specific Preferences every time you clone the repository, because the settings are individual per project.

That is basically it as far as the initial setup goes.

### 2.2 Basics

GitKraken is really easy to use, in the middle you see a visual representation of the current remote and local branches and also individual commits with their authors. You can click on each commit and see what files have been changed, who made the changes etc. When you make a change locally on your machine, you can view it by clicking the very first line in the middle section (the one with the empty circle). Or there will be a little bar in the top right corner saying View changes. On the right you should now see a new section. The top part shows you all the changes (file changes, renaming, deletions etc.) that you have made. To upload your changes to our GitHub repository you need to follow these steps:

> Note: You will not be able to upload anything in the beginning, because the only local branch you have is master (you can see that in the left panel), which has a restricted access. More about that later in [3 Git Branching Strategy](#3-git-branching-strategy).

1. Stage all changes
    - button in the right section
        - you can either stage all files
        - or just some of them
    - staging is basically making your files ready for commit
        - it moves the files from unstaged to staged state
2. Writing a commit message
    - there are two parts to every commit message
        - the summary
        - the description/ additional details

> Important: there is a commit template you should see in the commit message box (please follow the instructions specified). If you decide to use command line or your IDE git features, please make sure you always follow the commit template .gitmessage located in the instructions folder.

3. Commit your changes
    - this command logs all your changes locally
4. Push
    - push will upload all your commits to our remote repository

### 2.3 Branching

Another very useful feature of GitKraken is in the left panel, where you can see all the remote branches of the GitHub repository and also your local personal branches. There is also a functionality that allows us to create Pull Requests directly from GitKraken.

When you initially clone the repo the only local branch will be master. I will set it up in such a way that the access to master will be restricted (this is better explained in [3 Git Branching Strategy](#3-git-branching-strategy)). To be able to upload your code just double click the development branch in the left panel under REMOTE. This will checkout the origin/development branch for you. It basically pulls all the data from the remote repository development branch to your local machine.

So far, you don't have to worry about branching that much, because I will write additional instructions if necessary as we progress through the project. The basic git strategy is explained below.

## 3 Git Branching Strategy

Git strategy allows us to stay organised and keep our repository clean.

### 3.1 Initial Strategy

I think that the best strategy in the beginning is to have two branches:

1. master
    - our default branch with working code
2. development
    - for development

We will use the development branch for all our coding and implementation. The access to master branch will be restricted, so that the only way to update it will be through GitHub's Pull Request system. No direct merging or pushing is allowed. Pull Requests will always have all team members assigned as reviewers, who can comment on the request and decline or approve the changes. Only if it code passes our tests and is approved by all reviewers it can be merged to master.

The reason for having our master locked is to make it as bug free as possible. Only tested, reviewed and working code will be available in the master branch.

### 3.2 Advanced Strategy

Later we might use the little bit more complicated Branch for Feature strategy, where we maintain the development branch as a tested working system and from that we branch for individual features. When a feature is implemented and tested we merge back to development and test the whole system with the new feature added. When all tests done on the development branch we merge back to master and start the process over again.

#### Update

In the end we decided not use the Branch for Feature approach, because it didn't suit our project. We initially made the development branch as mentioned in [3.1 Initial Strategy](#31-initial-strategy). From development branch we created three other branches:

1. AndroidAppDevelopment
   - Used for initial development of the Android App.
  
> Note: this branch is not used anymore, because it was merged into the DesktopAppDevelopment branch. The Desktop App and Android App work together in one system, therefore we merged the branches and develop both on one.

2. DesktopAppDevelopment
   - Used for Android and Desktop App development.

3. SimulationDevelopment
   - Used for development of the Simulation.

Those branches are being used for develepmont of each component as their naming suggests. Similarly, as explained in [3.2 Advanced Strategy](#32-advanced-strategy), when the system is implemented and tested we merge to the development branch. Once everything is approved we create a Pull Request to merge development to master.