package me.typical.offlineclaim.listeners;

import me.typical.offlineclaim.OfflineClaimPlugin;
import me.typical.offlineclaim.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JoinListener implements Listener {
    private final OfflineClaimPlugin plugin;

    public JoinListener(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = plugin.database.getConnection()) {
                String query = "SELECT COUNT(*) AS count FROM offline_items WHERE player_uuid = ? AND claimed = FALSE";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, player.getUniqueId().toString());

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().claimableItems);
                }
            } catch (SQLException event) {
                plugin.getLogger().severe("Error checking unclaimed items: " + event.getMessage());
            }
        });
    }
}
