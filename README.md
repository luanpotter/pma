pma
===

[![Build Status](https://travis-ci.org/luanpotter/pma.svg?branch=master)](https://travis-ci.org/luanpotter/pma)

This is a simple and powerful pma helper program. It can help you use the Dextra's pma system with few, consistent commands and elegance.  
It uses the powerful console-parser lib for the console commands binding, console-fn flavored.  
It is lightweifht and [will be] is currently being distributed via bintray. [TODO]

Usage
---
The program basically logs the times in which you start each activity, including leaving, and then creates the correct records using pma's api.
Running `pma here :projectNameOrIdOrAlias` will log that you started that activity.  
Running `pma exit` will log that you exited to lunch or something.  
Running `pma save` will save all completed days (a day is complete if it ends in a `exit` command).  
There are tons of other features, they can be listed with `pma help` command.
