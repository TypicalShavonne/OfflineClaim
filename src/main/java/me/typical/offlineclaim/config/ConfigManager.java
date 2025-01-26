package me.typical.offlineclaim.config;

import de.exlll.configlib.YamlConfigurations;
import lombok.Getter;
import me.typical.offlineclaim.OfflineClaimPlugin;
import me.typical.offlineclaim.config.types.MessageConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class ConfigManager {

    private final OfflineClaimPlugin plugin;
    private Path configFile;
    private Path messageFile;
    @Getter private MessageConfig messageConfig;
    @Getter private HashMap<String, Component> miniMessageCache = new HashMap<>();


    public ConfigManager(OfflineClaimPlugin plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        plugin.getLogger().info("Loading config");
        messageFile = Paths.get(plugin.getDataFolder() + "/" + "message.yml");

        if (!plugin.getDataFolder().exists()) {
            YamlConfigurations.save(messageFile, MessageConfig.class, new MessageConfig());
        } else {
            YamlConfigurations.update(messageFile, MessageConfig.class);
        }

        messageConfig = YamlConfigurations.load(messageFile, MessageConfig.class);
        plugin.getLogger().info("Loaded config successfully");
    }
    public void reloadConfig() {
        messageConfig = YamlConfigurations.load(messageFile, MessageConfig.class);
        miniMessageCache.clear();
    }
    public Object getParsedStringValue(String key) {
        try {
            // Get the field from the Message class reflectively
            var field = messageConfig.getClass().getField(key);
            Object value = field.get(messageConfig);

            // If the value is a String, parse it and cache it as a Component
            if (value instanceof String) {
                String stringValue = (String) value;

                // Check if the Component is already cached
                if (miniMessageCache.containsKey(stringValue)) {
                    return miniMessageCache.get(stringValue);
                }

                // Parse and cache the Component if not already cached
                Component parsedComponent = MiniMessage.miniMessage().deserialize(stringValue);
                miniMessageCache.put(stringValue, parsedComponent);
                return parsedComponent;
            }

            // If the value is not a String, just return it directly (handle ints, etc.)
            return value;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null; // Handle this appropriately (e.g., log an error, return default value)
        }
    }

}
