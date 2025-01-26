package me.typical.offlineclaim.command.admin;

import com.google.common.base.Charsets;
import me.typical.offlineclaim.OfflineClaimPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class SendItemCommand implements CommandExecutor {
    private final OfflineClaimPlugin plugin;

    public SendItemCommand(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("senditem").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("offlineclaim.admin")) {
            sender.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().notAdmin);
            return true;
        }

        Player player = (Player) sender;

        // Validate arguments
        if (args.length != 1) {
            player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().invalidName);
            return true;
        }

        String targetName = args[0];

        // Generate a cracked/offline UUID for the target player
        UUID targetUuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + targetName).getBytes(Charsets.UTF_8));

        if (targetUuid.equals(player.getUniqueId())) {
            player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().cannotSendToSelf);
            return true;
        }

        // Get the item in the player's hand
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() == Material.AIR) {
            player.sendMessage(OfflineClaimPlugin.getConfigManager().getMessageConfig().notHoldingItem);
            return true;
        }

        // Remove the item from the player's hand and store it for the target player
        player.sendMessage(String.format(OfflineClaimPlugin.getConfigManager().getMessageConfig().itemSentToPlayer, targetName));

        ItemStack[] itemsToSend = { itemInHand };
        OfflineClaimPlugin.api.addClaimItemsToInventory(targetUuid, itemsToSend);

        return true;
    }
}
