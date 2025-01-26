package me.typical.offlineclaim.command;

import me.typical.offlineclaim.OfflineClaimPlugin;
import me.typical.offlineclaim.util.ItemSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClaimCommand implements CommandExecutor {
    private final OfflineClaimPlugin plugin;

    public ClaimCommand(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("claim").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().onlyPlayersCanUse);
            return true;
        }

        Player player = (Player) sender;

        // Handle the claiming process asynchronously
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Fetch and give claimable items to the player
                OfflineClaimPlugin.api.giveClaimableItemsToPlayer(player);
            } catch (Exception e) {
                plugin.getLogger().severe("Error claiming items: " + e.getMessage());
            }
        });

        return true;
    }

}
