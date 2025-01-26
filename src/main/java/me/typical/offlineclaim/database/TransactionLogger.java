package me.typical.offlineclaim.database;

import me.typical.offlineclaim.OfflineClaimPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionLogger {
    private final OfflineClaimPlugin plugin;

    public TransactionLogger(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
    }

    public void logTransaction(String playerUuid, String serializedItem, String action) {
        String query = "INSERT INTO transaction_log (player_uuid, item_stack, action) VALUES (?, ?, ?)";

        try (Connection connection = plugin.database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, playerUuid);
            statement.setString(2, serializedItem);
            statement.setString(3, action);
            statement.executeUpdate();

        } catch (Exception e) {
            System.err.println("Failed to log transaction: " + e.getMessage());
        }
    }
}
