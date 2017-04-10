package framgiavn.project01.web.ulti;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class Logit2 {
	private static String propertiesFilename = "log4j.properties";
	protected Log log;

	static {
		Properties p = null;
		try {
			p = Helpers.getProperties(propertiesFilename);
		} catch (java.io.FileNotFoundException e) {
		}
		if (p != null) {
			PropertyConfigurator.configure(p);
		} else {
			System.err.println("Logit could not find " + propertiesFilename);
			System.err.println("Logit will use default configuration");
			BasicConfigurator.configure();
		}
	}

	/**
	 * Get an instance of Logit.
	 * 
	 * @param classInstance
	 *            Class to hold the instance.
	 * @retrun An instance of Logit.
	 */
	public static Logit2 getInstance(Class classInstance) {
		return new Logit2(classInstance.getName());
	}

	/**
	 * Get an instance of Logit.
	 * 
	 * @param String
	 *            to hold the instance. add by wangxl . 20050228
	 * @retrun An instance of Logit.
	 */
	public static Logit2 getInstance(String className) {
		return new Logit2(className);
	}

	/**
	 * Constructor with a class name.
	 * 
	 * @param className
	 *            Class name to hold the instance.
	 */
	private Logit2(String className) {
		log = LogFactory.getLog(className);
	}

	/**
	 * Prevent the default constructor.
	 */
	private Logit2() {
	}

	/**
	 * Check if DEBUG priority enabled.
	 * 
	 * @return true if enabled, false otherwise.
	 */
	public boolean ison() {
		return log.isDebugEnabled();
	}

	/**
	 * Log a text debug message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void debug(String message) {
		log.debug(message);
	}

	/**
	 * Log a object debug message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void debug(Object message) {
		log.debug(message);
	}

	/**
	 * Log a text debug message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void debug(String message, Throwable t) {
		log.debug(message, t);
	}

	/**
	 * Log a object debug message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void debug(Object message, Throwable t) {
		log.debug(message, t);
	}

	/**
	 * Log a text info message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void info(String message) {
		log.info(message);
	}

	/**
	 * Log a object info message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void info(Object message) {
		log.info(message);
	}

	/**
	 * Log a text info message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void info(String message, Throwable t) {
		log.info(message, t);
	}

	/**
	 * Log a object info message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void info(Object message, Throwable t) {
		log.info(message, t);
	}

	/**
	 * Log a text warn message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void warn(String message) {
		log.warn(message);
	}

	/**
	 * Log a object warn message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void warn(Object message) {
		log.warn(message);
	}

	/**
	 * Log a text warn message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void warn(String message, Throwable t) {
		log.warn(message, t);
	}

	/**
	 * Log a object warn message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void warn(Object message, Throwable t) {
		log.warn(message, t);
	}

	/**
	 * Log a text error message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void error(String message) {
		log.error(message);
	}

	/**
	 * Log a object error message.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void error(Object message) {
		if (message instanceof Throwable) {
			error(message, (Throwable) message);
		} else {
			log.error(message);
		}
	}

	/**
	 * Log a text error message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void error(String message, Throwable t) {
		log.error(message, t);
	}

	/**
	 * Log a object error message with a stack trace.
	 * 
	 * @param message
	 *            Message to log.
	 * @param t
	 *            Throwable instance to log.
	 */
	public void error(Object message, Throwable t) {
		log.error(message, t);
	}
}
