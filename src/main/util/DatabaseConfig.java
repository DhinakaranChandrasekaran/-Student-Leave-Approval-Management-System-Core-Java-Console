package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Database configuration class for MySQL connection.
 * Loads configuration from .env file.
 */
public class DatabaseConfig {
    private static Map<String, String> envVariables = new HashMap<>();
    private static boolean loaded = false;

    // Default values (used if .env file is not found)
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "3306";
    private static final String DEFAULT_DB_NAME = "student_leave_management";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    /**
     * Load environment variables from .env file
     */
    private static void loadEnvFile() {
        if (loaded)
            return;

        File envFile = new File(".env");
        if (!envFile.exists()) {
            System.out.println("Warning: .env file not found. Using default database configuration.");
            loaded = true;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Skip comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                // Parse KEY=VALUE format
                int equalsIndex = line.indexOf('=');
                if (equalsIndex > 0) {
                    String key = line.substring(0, equalsIndex).trim();
                    String value = line.substring(equalsIndex + 1).trim();
                    envVariables.put(key, value);
                }
            }
            loaded = true;
        } catch (IOException e) {
            System.out.println("Error reading .env file: " + e.getMessage());
            loaded = true;
        }
    }

    /**
     * Get environment variable value
     */
    private static String getEnv(String key, String defaultValue) {
        loadEnvFile();
        return envVariables.getOrDefault(key, defaultValue);
    }

    public static String getDbHost() {
        return getEnv("DB_HOST", DEFAULT_HOST);
    }

    public static String getDbPort() {
        return getEnv("DB_PORT", DEFAULT_PORT);
    }

    public static String getDbName() {
        return getEnv("DB_NAME", DEFAULT_DB_NAME);
    }

    public static String getDbUser() {
        return getEnv("DB_USER", DEFAULT_USER);
    }

    public static String getDbPassword() {
        return getEnv("DB_PASSWORD", DEFAULT_PASSWORD);
    }

    public static String getJdbcUrl() {
        return "jdbc:mysql://" + getDbHost() + ":" + getDbPort() + "/" + getDbName() +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    }

    public static String getJdbcDriver() {
        return "com.mysql.cj.jdbc.Driver";
    }

    /**
     * Print current configuration (for debugging)
     */
    public static void printConfig() {
        System.out.println("Database Configuration:");
        System.out.println("  Host: " + getDbHost());
        System.out.println("  Port: " + getDbPort());
        System.out.println("  Database: " + getDbName());
        System.out.println("  User: " + getDbUser());
        System.out.println("  Password: " + (getDbPassword().isEmpty() ? "(empty)" : "********"));
    }
}
