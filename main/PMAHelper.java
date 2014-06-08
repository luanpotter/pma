package main;

import parser.*;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

final class PMAHelper {
	private static final String CONFIG = "config.dat", FILE = "log.dat", BKP_FILE = "log.dat.bkp";

	//configuration variables
	private static int task_id;
	private static String description;

	private static Date dateNow;
	private static Time timeNow;

	private PMAHelper() {}

	public static void main(String[] args) {
		Call[] c = Config.parse(args);
		Caller caller = new Caller();
		caller.registerClass("config", ConfigController.class);
		caller.call(c[0], null);
		{
			java.util.Calendar now = java.util.Calendar.getInstance();
			dateNow = new Date(now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH) + 1, now.get(java.util.Calendar.DAY_OF_MONTH));
			timeNow = new Time(now.get(java.util.Calendar.HOUR_OF_DAY), now.get(java.util.Calendar.MINUTE));
			System.out.println("Current day: " + dateNow);
			System.out.println("Current hour: " + timeNow);
			System.out.println("Now running...");
		}

		if (args == null || args.length < 1) {
			halt("Wrong arguments. Must be used: pma action");
		}

		readConfigs();

		switch (args[0]) {
			case "cheguei":
			case "start":
			case "inicio":
			case "here":
				logWithChar('>');
				break;
			case "sai":
			case "parti":
			case "partiu":
			case "fim":
			case "exit":
			case "end":
				logWithChar('<');
				break;
			case "salvar":
			case "save":
			case "write":
			case "send":
			case "put":
				try {
					readLogs();
				} catch (InvalidFormatException ex) {
					halt("Error while reading " + FILE + " file: " + ex.getMessage());
				}
				break;
			default:
				halt("Wrong argument for pma.");
				break;
		}

		System.out.println("Command was executed successfully.");
	}

	private static void readConfigs() {
		try (BufferedReader b = new BufferedReader(new FileReader(new File(CONFIG)))) {
			String line;
			int ln = 0;
			HashSet<String> set = new HashSet<>();
			while((line = b.readLine()) != null) {
				ln++;
				String[] ar = line.split(":");
				if (ar.length < 2) {
					halt("Invalid config file. Each line must be in the following format: 'key: value'");
				}
				String key = ar[0].trim(), value = ar[1];
				for (int i = 2; i < ar.length; i++)
					value += ":" + ar[i];
				value = value.trim();

				if (set.contains(key))
					halt("Duplicated key found at config file. Choose the correct one and try again.");
				set.add(key);

				switch (key) {
				case "task":
					try {
						task_id = Integer.parseInt(value);
					} catch (NumberFormatException ex) {
						halt("Invalid config file at line " + ln + ": 'task' key must have an integer value.");
					}
					break;
				case "message":
					description = value;
					break;
				default:
					halt("Invalid config file at line " + ln + ": '" + key + "' key does not exist.");
					break;
				}
			}

			if (!set.contains("task"))
				halt("Required key 'task' not found in the config.");

			if (!set.contains("message")) {
				description = "Development.";
			}
		} catch (EOFException e) {
			//expected
		} catch (IOException e) {
			halt("Couldn't find the config file. You must specify at least the 'task' key. The 'message' can be specified on the fly.");
		}
	}

	private static void logWithChar(char c) {
		logWithChar(c, FILE);
		if (BKP_FILE != null)
			logWithChar(c, BKP_FILE);
	}

	private static void logWithChar(char c, String fileName) {
		try (PrintWriter p = new PrintWriter(new FileWriter(new File(fileName), true))) {
			p.println(c + dateNow.toString() + '+' + timeNow.toString());
		} catch (IOException ex) {
			halt("Unable to write to file: " + fileName);
		}
	}

	private static void readLogs() throws InvalidFormatException {
		readLogs(FILE);
	}

	private static final class InvalidFormatException extends Exception {
		private static final long serialVersionUID = 376344169681487559L;

		public InvalidFormatException(String error, int line) {
			super(error + " [at line " + line + "]");
		}		
	}

	private static void readLogs(String fileName) throws InvalidFormatException {
		File file = new File(fileName);
		int line_number = 1;
		try (BufferedReader p = new BufferedReader(new FileReader(file))) {
			String line;
			Date curDay = null;
			Time startTime = null, currentStartTime = null, cur = null, currentInterval = null;
			while ((line = p.readLine()) != null) {
				char c = line.charAt(0);
				if (c != '<' && c != '>')
					throw new InvalidFormatException("Invalid Log File! First character of each line must be '<' or '>'.", line_number);
				String[] parts = line.substring(1).split("\\+");
				cur = new Time(parts[1]);

				if (curDay != null && parts[0].equals(curDay.toString())) { //same day
					if (c == '<') {
						if (currentStartTime != null)
							throw new InvalidFormatException("Invalid Log File! You can't have two '<' in a row", line_number);
						currentStartTime = new Time(cur);
					} else {
						if (currentStartTime == null)
							throw new InvalidFormatException("Invalid Log File! You can't have two '>' in a row", line_number);
						int delta = cur.getDifference(currentStartTime);
						currentInterval.addMinutes(delta); //add new interval to the intervals
						currentStartTime = null;
					}
				} else { //new day
					if (c == '<')
						throw new InvalidFormatException("Invalid Log File! New day must always start with '>'.", line_number);
					if (curDay != null) {
						createDayAtPma(curDay, startTime, cur, currentInterval);
					}
					curDay = new Date(parts[0]);
					startTime = cur;
					currentStartTime = null;
					currentInterval = new Time();
				}
				line_number++;
			}

			createDayAtPma(curDay, startTime, cur, currentInterval);
			file.delete();
		} catch (EOFException ex) {
		} catch (IOException ex) {
			halt("Unable to read from file: " + fileName);
		}
	}
	private static void createDayAtPma(Date date, Time start, Time end, Time interval) {
		//create day
		String command_day = "./pma_create_day -d " + date + " -s " + start + " -e " + end + " -i " + interval;
		runCommand(command_day);

		//create task
		int totalTime = end.getDifference(start) - interval.getTotalMinutes();
		String command_task = "./pma_create_task -d " + date + " -e " + totalTime + " -s concluded " + task_id + " " + description;
		runCommand(command_task);
	}

	private static void runCommand(String command) {
		System.out.println("Command generated: " + command);
		try {
			Runtime.getRuntime().exec(command).waitFor();
		} catch (InterruptedException | IOException e) {
			halt("Unexpected error! Unable to run command: " + e.getMessage());
		}
	}

	private static void halt(String t) {
		System.err.println(t);
		System.exit(1);
	}

	public static String toString(int num, int digits) {
		String ns = String.valueOf(num);
		while (ns.length() < digits)
			ns = '0' + ns;
		return ns;
	}

	private static String toString(int num) {
		return toString(num, 2);
	}

	private static class Date {
		private int year, month, day;

		public Date(String date) {
			String[] p = date.split("-");
			if (p.length != 3)
				throw new IllegalArgumentException("String must be in format yyyy-mm-dd");
			this.year = Integer.parseInt(p[0]);
			this.month = Integer.parseInt(p[1]);
			this.day = Integer.parseInt(p[2]);
		}

		public Date(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
		}

		@Override
		public boolean equals(Object o) {
			if (! (o instanceof Date))
				return false;
			Date d = (Date) o;
			return this.year == d.year && this.month == d.month && this.day == d.day;
		}

		@Override
		public String toString() {
			return PMAHelper.toString(year) + '-' + PMAHelper.toString(month) + '-' + PMAHelper.toString(day);
		}
	}

	private static class Time {
		private int hours, minutes;

		public Time() {}

		public Time(Time t) {
			this.hours = t.hours;
			this.minutes = t.minutes;
		}

		public Time(String t) {
			String[] p = t.split(":");
			if (p.length != 2)
				throw new IllegalArgumentException("String must be in format hh:mm");
			this.hours = Integer.parseInt(p[0]);
			if (this.hours < 0 || this.hours >= 24)
				throw new IllegalArgumentException("Hours must be between 0 and 24, " + this.hours + " found.");
			this.minutes = Integer.parseInt(p[1]);
			if (this.minutes < 0 || this.minutes >= 60)
				throw new IllegalArgumentException("Minutes must be between 0 and 60, " + this.minutes + " found.");
		}

		public Time(int minutes) {
			this.hours = minutes / 60;
			if (this.hours < 0 || this.hours >= 24)
				throw new IllegalArgumentException("Hours must be between 0 and 24");
			this.minutes = minutes % 60;
		}

		public Time(int hours, int minutes) {
			this.hours = hours;
			this.minutes = minutes;
		}

		public int getRoundedMinutes() {
			return Math.round((float) minutes / 5) * 5;
		}

		public int getTotalMinutes() {
			return hours * 60 + getRoundedMinutes();
		}

		public int getActualTotalMinutes() {
			return hours * 60 + minutes;
		}

		public int getDifference(Time h) {
			return this.getActualTotalMinutes() - h.getActualTotalMinutes();
		}

		public void addMinutes(int minutes) {
			this.minutes += minutes;
			this.hours += this.minutes / 60;
			this.minutes %= 60;
		}

		@Override
		public String toString() {
			int minutes = getRoundedMinutes();
			int hours = this.hours + minutes / 60;
			minutes %= 60;
			return PMAHelper.toString(hours) + ':' + PMAHelper.toString(minutes);
		}
	}
}
