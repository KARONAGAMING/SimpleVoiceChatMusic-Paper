package org.karona.simpleVoiceChatMusic.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class MessageUtils {

    public static void sendOffline(Player player) {
        String message = formatColor("#FC0000") + "That player is currently offline";
        player.sendMessage(message);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static String formatColor(String hex) {
        return "§x§" + hex.charAt(1) + "§" + hex.charAt(2)
                + "§" + hex.charAt(3) + "§" + hex.charAt(4)
                + "§" + hex.charAt(5) + "§" + hex.charAt(6);
    }

    public static String formatNumber(int value) {
        double t;
        if (value >= 1_000_000) {
            t = (double) value / 1_000_000.0;
            return new DecimalFormat("#.##").format(t) + "m";
        } else if (value >= 1_000) {
            t = (double) value / 1_000.0;
            return new DecimalFormat("#.##").format(t) + "k";
        } else {
            return String.valueOf(value);
        }
    }
}
