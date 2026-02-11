package sep.banaanatjuh.event.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class StorageManager {

    private final JavaPlugin plugin;
    private Connection connection;
    private final String databasePath;

    public StorageManager(JavaPlugin plugin) {
        this.plugin = plugin;
        // The database file will be stored in the plugin's data folder
        this.databasePath = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "event.db";
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Ensure the plugin's data folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        try {
            Class.forName("org.sqlite.JDBC"); // Ensure SQLite JDBC driver is loaded
            connection = DriverManager.getConnection(databasePath);
            createTable();
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not initialize database: " + e.getMessage());
        }
    }

    private void createTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS data (" +
                         "key TEXT PRIMARY KEY," +
                         "value TEXT)";
            statement.execute(sql);
            plugin.getLogger().info("Database table 'data' checked/created successfully.");
        }
    }

    public void writeData(String key, String value) {
        String sql = "INSERT OR REPLACE INTO data (key, value) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, key);
            pstmt.setString(2, value);
            pstmt.executeUpdate();
            plugin.getLogger().info("Data written: Key='" + key + "', Value='" + value + "'");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not write data: " + e.getMessage());
        }
    }

    public String readData(String key) {
        String sql = "SELECT value FROM data WHERE key = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String value = rs.getString("value");
                plugin.getLogger().info("Data read: Key='" + key + "', Value='" + value + "'");
                return value;
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not read data: " + e.getMessage());
        }
        return null;
    }

    public void saveLocation(String key, Location location) {
        String serializedLocation = serializeLocation(location);
        if (serializedLocation != null) {
            writeData(key, serializedLocation);
            plugin.getLogger().info("Location saved: Key='" + key + "', Location='" + serializedLocation + "'");
        } else {
            plugin.getLogger().warning("Failed to serialize location for key: " + key);
        }
    }

    public Location loadLocation(String key) {
        String serializedLocation = readData(key);
        if (serializedLocation != null) {
            Location location = deserializeLocation(serializedLocation);
            if (location != null) {
                plugin.getLogger().info("Location loaded: Key='" + key + "', Location='" + serializedLocation + "'");
                return location;
            } else {
                plugin.getLogger().warning("Failed to deserialize location for key: " + key + ": " + serializedLocation);
            }
        }
        return null;
    }

    private String serializeLocation(Location location) {
        if (location == null || location.getWorld() == null) return null;
        return String.format("%s,%.2f,%.2f,%.2f,%.2f,%.2f",
                             location.getWorld().getName(),
                             location.getX(),
                             location.getY(),
                             location.getZ(),
                             location.getYaw(),
                             location.getPitch());
    }

    private Location deserializeLocation(String serializedLocation) {
        if (serializedLocation == null || serializedLocation.isEmpty()) return null;
        String[] parts = serializedLocation.split(",");
        if (parts.length == 6) {
            try {
                return new Location(Bukkit.getWorld(parts[0]),
                                    Double.parseDouble(parts[1]),
                                    Double.parseDouble(parts[2]),
                                    Double.parseDouble(parts[3]),
                                    Float.parseFloat(parts[4]),
                                    Float.parseFloat(parts[5]));
            } catch (NumberFormatException e) {
                plugin.getLogger().log(Level.WARNING, "Failed to parse location coordinates: " + serializedLocation, e);
            }
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database connection closed.");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not close database connection: " + e.getMessage());
        }
    }
}
