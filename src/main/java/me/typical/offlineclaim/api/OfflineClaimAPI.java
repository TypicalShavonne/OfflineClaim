package me.typical.offlineclaim.api;

import me.typical.offlineclaim.OfflineClaimPlugin;
import me.typical.offlineclaim.util.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

import static me.typical.offlineclaim.OfflineClaimPlugin.transactionLogger;

public class OfflineClaimAPI {
    private final OfflineClaimPlugin plugin;

    public OfflineClaimAPI(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Adds claimable items for a player, saving them to the database.
     *
     * @param player Player.
     */
    public void giveClaimableItemsToPlayer(Player player) {
        try (Connection connection = plugin.database.getConnection()) {
            String query = "SELECT id, item_stack FROM offline_items WHERE player_uuid = ? AND claimed = FALSE";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String serializedItem = resultSet.getString("item_stack");

                // Deserialize the item
                ItemStack item = ItemSerializer.deserializeItemStack(serializedItem);

                // Add the item to the player's inventory
                Bukkit.getScheduler().runTask(plugin, () -> {
                    player.getInventory().addItem(item);
                    player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().itemClaimed);
                });

                // Mark the item as claimed
                markAsClaimed(id, player.getUniqueId().toString(), serializedItem);
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Error fetching claimable items: " + e.getMessage());
        }
    }
    /**
     * Marks an item as claimed in the database.
     *
     * @param id The ID of the item to mark as claimed.
     */
    private void markAsClaimed(int id, String playerUuid, String serializedItem) {
        try (Connection connection = plugin.database.getConnection()) {
            String query = "UPDATE offline_items SET claimed = TRUE WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

            // Log the transaction
            transactionLogger.logTransaction(playerUuid, serializedItem, "out");

        } catch (Exception e) {
            plugin.getLogger().severe("Error marking item as claimed: " + e.getMessage());
        }
    }


    public void addClaimItemsToInventory(UUID playerUuid, ItemStack[] items) {
        if (Bukkit.getPlayer(playerUuid) != null) {
            giveItemsToOnlinePlayer(Bukkit.getPlayer(playerUuid), items);
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = plugin.database.getConnection()) {
                String query = "INSERT INTO offline_items (player_uuid, item_stack, claimed) VALUES (?, ?, FALSE)";
                PreparedStatement statement = connection.prepareStatement(query);

                for (ItemStack item : items) {
                    String serializedItem = ItemSerializer.serializeItemStack(item);
                    statement.setString(1, playerUuid.toString());
                    statement.setString(2, serializedItem);
                    statement.addBatch();
                    transactionLogger.logTransaction(playerUuid.toString(), ItemSerializer.serializeItemStack(item), "in");

                }

                statement.executeBatch();
            } catch (SQLException e) {
                plugin.getLogger().severe("Failed to add claimable items: " + e.getMessage());
            }
        });
    }

    /**
     * Helper method to immediately give items to a player if they are online.
     *
     * @param player The player.
     * @param items  The array of ItemStack to give.
     */
    public void giveItemsToOnlinePlayer(Player player, ItemStack[] items) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
           for(ItemStack item : items) {
               transactionLogger.logTransaction(player.getUniqueId().toString(), ItemSerializer.serializeItemStack(item), "give_online");
           }
        });

        Bukkit.getScheduler().runTask(plugin, () -> {
            Arrays.stream(items).forEach(item -> player.getInventory().addItem(item));
            player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().itemClaimed);
        });
    }
}
