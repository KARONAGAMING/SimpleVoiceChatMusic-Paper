package org.karona.simpleVoiceChatMusic.commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import java.util.HashSet;
import java.util.UUID;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class KillCommand implements Command {
    private static HashSet<UUID> warned = new HashSet<>();

    public int execute(Player player, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(player);
        if (result == null) return 1;

        if (warned.add(result.source().getUniqueId())) {
            result.source().sendMessage(formatColor("#A8A8A8") + "Are you sure you want to do this? This command should be used when everything is broken and you need to alt-f4 the plugin. Group members may hear a bit of earrape as the opus packets abruptly end.\n\nIf you understand this, run the command again.");

            return 0;
        }

        SimpleVoiceChatMusic.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            gm.broadcast(formatColor("#FC0000") + "Playback forcibly killed by " + result.source().getName() + ".");
            gm.cleanup();
        });

        return 0;
    }

}
