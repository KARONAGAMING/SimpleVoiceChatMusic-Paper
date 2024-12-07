package org.karona.simpleVoiceChatMusic.commands;


import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.audio.PlayLoadHandler;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class PlayCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;
        if (args.length < 1) {
            result.source().sendMessage(formatColor("#FC0000") + "Usage: /music play <song name>");
            result.source().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatColor("#FC0000") + "Usage: /music play <song name>"));
            return 1;
        }

        final String query = ModUtils.parseTrackId(String.join(" ", args));

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());

            result.source().sendMessage(formatColor("#A8A8A8") + "Loading songs...");
            result.source().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatColor("#A8A8A8") + "Loading songs..."));
            MusicManager.getInstance().playerManager.loadItemOrdered(gm.getPlayer(), query, new PlayLoadHandler(result.source(), gm));
        });

        return 0;
    }

}
