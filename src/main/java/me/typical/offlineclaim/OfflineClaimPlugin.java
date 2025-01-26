package me.typical.offlineclaim;

import lombok.Getter;
import me.typical.offlineclaim.api.OfflineClaimAPI;
import me.typical.offlineclaim.command.*;
import me.typical.offlineclaim.command.admin.SendItemCommand;
import me.typical.offlineclaim.config.ConfigManager;
import me.typical.offlineclaim.database.Database;
import me.typical.offlineclaim.database.TransactionLogger;
import me.typical.offlineclaim.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class OfflineClaimPlugin extends JavaPlugin {

    public Database database;
    @Getter
    public static OfflineClaimAPI api;
    @Getter public static ConfigManager configManager;
    @Getter public static TransactionLogger transactionLogger;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Initialize the database
        database = new Database(this);
        database.init();
        configManager = new ConfigManager(this);
        transactionLogger = new TransactionLogger(this);

        // Register event listener
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);

        api = new OfflineClaimAPI(this);
        // Register commands
    new ClaimCommand(this);
    new SendItemCommand(this);
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.close();
        }
    }
}
