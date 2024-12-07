package org.karona.simpleVoiceChatMusic.commands;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class VolumeCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;
        int volume = Integer.parseInt(args[0]);
        if (args.length < 1) {
            result.source().sendMessage(formatColor("#FC0000") + "Usage: /music volume <number>");
            result.source().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatColor("#FC0000") + "Usage: /music bassboost <number>"));
            return 1;
        }

        if (volume > 100 || volume <= 0) {
            result.source().sendMessage(formatColor("#FC0000") + "Incorrect number of arguments! (1 < x < 100)%");
            result.source().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatColor("#FC0000") + "Incorrect number of arguments! (1 < x < 100)%"));
            return 1;
        }

        SimpleVoiceChatMusic.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            gm.broadcast(formatColor("#A8A8A8") + "Volume set to " + formatColor("#37BFF8") + volume + "%" + formatColor("#A8A8A8") + " by :" + result.source().getName());
            gm.setVolume(volume);
        });

        return 0;
    }

}
