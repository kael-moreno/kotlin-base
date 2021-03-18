# Kotlin-Base

Please read and understand the readme guide before adding contribution to the development of the project.

## Etiquette

Please be considerate towards maintainers when raising issues or presenting pull requests. Let's show the
world that developers are civilized and selfless people.

It's the duty of the maintainer to ensure that all submissions to the project are of sufficient
quality to benefit the project. Many developers have different skillsets, strengths, and weaknesses. Respect the maintainer's decision, and do not be upset or abusive if your submission is not used.

## Issue Filing

Before filing an issue:

- Attempt to replicate the problem, to ensure that it wasn't a coincidental incident.
- Check to make sure your feature suggestion isn't already present within the project.
- Check if the issue is within the initial spec stated.
- Take note of every detail of the issue. Make sure to include (if possible) the device used, device OS Version, if using an emulator/real device, and steps to replicate.

## Branch Structure

This project must contain the following branches:

- master - This branch should always have the latest *Production* codebase and version tags.
- develop - This branch should be used as the codebase of the project for Client's testing builds. 
- internal - This branch should be used as the codebase for the developers of this project. 

*Sub branches*
- {issue-number}-{issue-slug} - These branches should be the issue branches.

## Developing

*Installation*

Clone this project using SSH.

Once done, do `git fetch` and checkout from `internal` branch.

*Gradle Properties*

The file `gradle.properties` should contain all the key configurations for your app base.

*Build Flavors*

Build flavors are located in `app/build.gradle` file.
- internal - This flavor should contain internal configuration.
- staging - This flavor should contain staging configuration.
- production - This flavor should contain live configuration.

If you will use a `local` server, create a new flavor and name it `internal`.


## CI Builds

All branches should have the following remote CI builds. See latest webhooks for Bitrise Builds.

