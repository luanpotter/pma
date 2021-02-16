# pma

[![Build Status](https://github.com/luanpotter/pma/workflows/Test/badge.svg?branch=master&event=push)](https://github.com/luanpotter/pma/actions)

This is a simple and powerful PMA helper program. It can help you use the Dextra's PMA system with few, consistent commands and elegance.  
It uses the powerful `console-parser` lib for the console commands binding, `console-fn` flavored.  
It is lightweight (only 5 MB with dependencies) and is distributed via `bintray`.  

## Download

The latest version can be found on [bintray](https://bintray.com/artifact/download/luanpotter/pma/pma-helper.jar).  
Other releases can be found at the project's [page](https://bintray.com/luanpotter/pma/pma).  

## Basic Usage

The program basically logs the times in which you start each activity, including leaving, and then creates the correct records using PMA's api.  

Running `pma here :projectNameOrIdOrAlias` will log that you started that activity.  
Running `pma exit` will log that you exited to lunch or something.  
Running `pma save` will save all completed days (a day is complete if it ends in a `exit` command).  
Running `pma show :date` will show the appointments currently on the server on that day.  

Here, of course, `pma` is an alias to `java -jar pma-helper.jar`.
There are tons of other features, they can be listed with `pma help` command.  

## Advanced Usage

### Logging

There are a few commands to log the times of the activities.  

`here` - Start counting on default task  
`here :taskNameOrAliasOrId...` - Start counting on taskNameOrId task  
`start :taskNameOrAliasOrId on :description...` - Start counting on taskNameOrId task with description description  
`exit` - Start counting for the interval (lunch or something else)  

The default task can be set with `options set default-task :taskNameOrId`.  
If not provided, the `default-description` (`options set default-description :desc`) will be used.  
If the description is null, a dot will be added, because the field is required.  

You can also edit the file (log.dat) in vim, because it is in plain text.  
The format of each line is something like `yyyy-mm-dd+HH:MM+id+desc`, but you can play with the commands and then check the file to see the specificalities.  

The current log can be seen with:  

`log` - Show current log, that is, all complete days on the file  
`log :backup` - Show current log backup  
`today` - Show what has been done today already (today being the last day on the file, that might be incomplete)  

### Aliases

You can add aliases to tasks you use daily to save work.  

Firstly, one needs to run update to see all projects or tasks:  

`update` - Update the list of projects and tasks  

Then, one can:

`list projects` - List all projects  
`list tasks` - List all tasks  
`list tasks from :projectNameOrId...` - List all tasks from projectNameOrId project  

Knowing the task id, then one can define aliases:  

`aliases` - List all aliases with their values  
`aliases get :alias` - Return the current value of alias  
`aliases set :alias :taskNameOrId...` - Set the value of alias to taskNameOrId  

### Options

There a handfull of options that can be handled with:  

`options` - List all options with their values  
`options get :option` - Return the current value of option  
`options set :option :value...` - Set the value of option to value  

The options are the following:  

`log-file` [default: log.dat] : the file in which to save the log  
`backup-file` [default: log.bkp.dat] : a backup of the file is mainteined by default, because the file is deleted when saved in the server. Set to null to remove.  
`default-task` [default: null] : the default task, will be used when none specified
`default-description` [default: null] : the default description, will be used when none specified

### Server

To interact with the server, one must first login.  

`login` - Login to pma, creating a token that is valid for a few hours.  
`logout` - Logout from pma, enabling you to choose a different user.  

The token is saved in the token.dat file.  

Then, you can:  

`save` - Save all completed days of the log file to the service, deleting those lines in the original file  
`update` - Update the list of projects and tasks  

One can also see what's on the server with:

`show :date` - Show the given date (how it is in PMA)  
`show record :date` - Show the given date (how it is in PMA) as record lines  
`show record :start :end` - Show the records in the interval  

Or count the minutes he worked:

`minutes :start :end` - Sums all time worked in between the dates provided (inclusive)  
`minutes :month` - Sums all time worked in given month (as yyyy-mm)  

And most importantly, see current satus (time balance):  

`status :date` - Show the status for the given date.  
`status` - Show the status for today.  

In order for that to work, though, one must create periods.  
Periods must be defined in order to inform how much hours per day you are supposed to work.  

`periods list` - List current periods  
`periods add :start :end :hoursPerDay` - Add new period (saying that you should have worked hoursPerDay every day from start to end inclusive)  
`periods remove :index` - Remove period at given index (based at `periods list` ordered list)  
`periods default get` - Get default hours per day  
`periods default set :defaultHoursPerDay` - Set default hours per day  

By default, defaultHoursPerDay = 8, and there are no periods. So it will work for regular cases.  
However, if you take vacations, for example, you must add then with something like: `periods add 2015-05-05 2015-06-05 0`, with zero meaning you had to work 0 hours in this interval (because, well, it was a vacation).  
If you work 6 hours a day, for example, change defaultHoursPerDay. If you just changed for 8 hours to 5 hours per day, change defaultHoursPerDay to 5 and then add a period from the begining of time (null) to the day you changed with time set to 8.  

## More Advanced Usage

Check the code, PR, or contact me (Luan). Feel free to use as you wish!  
