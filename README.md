pma
===

PMA Helper program. This is a java program that runs on top of https://github.com/raonifn/pma-scripts  
You must download and put pma-scripts files in the same folder of this project in order for it to work.

Usage
---
Just run it like this:
* java PMA_Helper here
* java PMA_Helper exit
* java PMA_Helper save
* 
There are tons of aliases for each action, just check the code.

Behavior
---
here/exit log new events in the current day.  
save saves the day, calculating the start time, end time and the interval based on the log.  
You must add a config.dat file, with a couple options.  
The program will fail if the file doesn't exist or is incorrect, and the reason will be specified.
