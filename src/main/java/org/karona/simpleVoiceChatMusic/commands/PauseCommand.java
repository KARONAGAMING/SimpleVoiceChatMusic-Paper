package org.karona.simpleVoiceChatMusic.commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class PauseCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            gm.broadcast("Playback paused by " + result.source().getName());
            gm.getPlayer().setPaused(true);
        });

        return 0;
    }

}
