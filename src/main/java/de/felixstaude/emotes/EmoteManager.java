package de.felixstaude.emotes;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EmoteManager implements Listener {

    private final JavaPlugin plugin;

    public EmoteManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String originalMessage = event.getMessage();

        String replacedMessage = replaceWordsInString(originalMessage);

        event.setCancelled(true);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(String.format("<%s> %s", player.getName(), replacedMessage));
        }

        Bukkit.getConsoleSender().sendMessage(String.format("<%s> %s", player.getName(), originalMessage));

    }

    private Map<String, String> getReplacements() {
        Map<String, String> replacements = new HashMap<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("replacements");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                replacements.put(key, section.getString(key));
            }
        }
        return replacements;
    }

    private String replaceWordsInString(String input) {
        Map<String, String> replacements = getReplacements();

        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }

        return input;
    }

}
