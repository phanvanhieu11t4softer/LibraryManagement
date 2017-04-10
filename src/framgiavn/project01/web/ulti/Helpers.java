package framgiavn.project01.web.ulti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Helpers class, which contains static methods helper methods like loading the
 * given property file etc.
 */
public final class Helpers {

	// file name
	private static final String CGS_CONFIG_FILE = "cgs_config.properties";
	private static final String CGS_SERVER_FILE = "spring-server-config.properties";

	// for logging
	// private static final Logit m_log = Logit.getInstance(Helpers.class);

	// singleton
	private Helpers() {
		;
	}

	/**
	 * Loads the given property file by searching the CLASSPATH or
	 * java.class.path system property value and returns the Properties object.
	 * 
	 * @param propertyFileName
	 *            Name of the property file.
	 * @return Returns Properties object containing the contents of the
	 *         specified Properties file.
	 * @exception java.io.FileNotFoundException
	 *                Thrown if the given property file could not found in the
	 *                CLASSPATH.
	 */
	@SuppressWarnings("unused")
	public static Properties getProperties(String propertyFileName)
			throws java.io.FileNotFoundException {

		InputStream is = null;
		try {
			String configPath = System.getProperty("configPath");
			File file = new File(configPath + File.separator + propertyFileName);
			System.out
					.println("####################################################### "
							+ configPath);
			is = new FileInputStream(file);

			if (is == null) {
				throw new FileNotFoundException(propertyFileName + " not found");
			}

			// load properties
			Properties props = new Properties();
			props.load(is);
			return props;

		} catch (Exception ignore) {
			ignore.printStackTrace();
			throw new java.io.FileNotFoundException(propertyFileName
					+ " not found");
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Loads the given property file by searching the CLASSPATH or
	 * java.class.path system property value and returns the Properties object.
	 * 
	 * @param propertyFileName
	 *            Name of the property file.
	 * @param onNotFound
	 *            Properties to return if the named properties file is not
	 *            found.
	 * @return Returns Properties object containing the contents of the
	 *         specified Properties file if found, or the onNotFound value if
	 *         not found.
	 */
	public static Properties getProperties(String propertyFileName,
			Properties onNotFound) {
		try {
			return getProperties(propertyFileName);
		} catch (java.io.FileNotFoundException fe) {
			// m_log.warn("Properties file not found: " + propertyFileName);
			return onNotFound;
		}
	}

	public static int getIntegerProperty(Properties p, String name,
			int defaultValue) {
		String l = p.getProperty(name);
		return l == null ? defaultValue : Integer.valueOf(l).intValue();
	}

	public static String getStringProperty(Properties p, String name,
			String defaultValue) {
		String propertyValue = p.getProperty(name);
		return propertyValue == null ? defaultValue : propertyValue;
	}

	public static boolean getBooleanProperty(Properties p, String name,
			boolean defaultValue) {
		String propertyValue = p.getProperty(name);
		return propertyValue == null ? defaultValue
				: new Boolean(propertyValue).booleanValue();
	}

	public static String createStringId(String header, int n, long id) {
		NumberFormat idFormat = createNumberFormat(n);
		return header + idFormat.format(id);
	}

	public static String createStringId(String header, int n, int id) {
		NumberFormat idFormat = createNumberFormat(n);
		return header + idFormat.format(id);
	}

	/**
	 * get value from cgs_config.properties
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getCgsProperty(String key) throws Exception {
		return getStringProperty(getProperties(CGS_CONFIG_FILE), key, "");
	}

	/**
	 * get value from spring-server-config.properties
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getServerProperty(String key) throws Exception {
		return getStringProperty(getProperties(CGS_SERVER_FILE), key, "");
	}

	private static NumberFormat createNumberFormat(int n) {
		NumberFormat idFormat = null;
		try {

			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < n; i++) {
				sb = sb.append("0");
			}
			idFormat = new DecimalFormat(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idFormat;
	}

	public static String getFullPath(String fileName)
			throws java.io.FileNotFoundException {
		try {
			String configPath = System.getProperty("configPath");
			if (configPath == null) {
				configPath = "";
			}
			// System.out.println("configPath=" + configPath);
			// System.out.println("File.separatorChar = " + File.separatorChar);
			return configPath + File.separator + fileName;

		} catch (Exception ignore) {
			ignore.printStackTrace();
			throw new java.io.FileNotFoundException(fileName);
		}

	}

	public static boolean isEmpty(List value) {
		return value == null || value.size() == 0;
	}

	public static boolean isEmpty(Collection value) {
		return value == null || value.size() == 0;
	}

	public static boolean isEmpty(Queue value) {
		return value == null || value.size() == 0;
	}

	public static boolean isEmpty(Set value) {
		return value == null || value.size() == 0;
	}

	public static boolean isEmpty(String value) {
		return value == null || value.equals("") || value.trim().equals("");
	}

	public static boolean isEmpty(String value, boolean isTrim) {
		if (isTrim) {
			return value == null || value.trim().equals("");
		} else {
			return value == null || value.equals("");
		}
	}

	public static boolean isEmpty(Object[] value) {
		return value == null || value.length == 0;
	}

	public static boolean isEmpty(Map value) {
		return value == null || value.size() == 0;
	}

	public static Integer toInteger(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static BigDecimal toBigDecimal(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return new BigDecimal(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

}
