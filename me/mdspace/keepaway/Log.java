package me.mdspace.keepaway;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author RainoBoy97
 */

public class Log {

	private static Logger log;

	private static boolean debug = false;

	static {
		log = KeepAway.get().getLogger();
	}

	public static void log(Level lvl, String msg) {
		log.log(lvl, msg);
	}

	public static void log(String msg) {
		log(Level.INFO, msg);
	}

	public static void log(Exception e) {
		log(Level.WARNING, e.getMessage());
		if (debug) e.printStackTrace();
	}

	public static void debug(String msg) {
		if (debug) log("[DEBUG] " + msg);
	}

	public static void response(String s) {
		if (debug) log(s);
	}

	public static void setDebugging(boolean debug) {
		Log.debug = debug;
	}

}
