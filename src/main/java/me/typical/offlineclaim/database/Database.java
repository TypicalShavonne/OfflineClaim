package me.typical.offlineclaim.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.typical.offlineclaim.OfflineClaimPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private final OfflineClaimPlugin plugin;
    private HikariDataSource dataSource;

    public Database(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/offlineclaim");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa"); // Default username for H2
        config.setPassword(""); // No password by default

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        // Create the necessary tables if they don't exist
        try (Connection connection = getConnection()) {
            String createItemsTableQuery = "CREATE TABLE IF NOT EXISTS offline_items (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_uuid VARCHAR(36), " +
                    "item_stack TEXT, " +
                    "claimed BOOLEAN DEFAULT FALSE" +
                    ")";
            connection.prepareStatement(createItemsTableQuery).execute();

            String createTransactionLogQuery = "CREATE TABLE IF NOT EXISTS transaction_log (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "player_uuid VARCHAR(36), " +
                    "item_stack TEXT, " +
                    "action VARCHAR(10), " + // 'in' or 'out'
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            connection.prepareStatement(createTransactionLogQuery).execute();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to initialize database: " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
