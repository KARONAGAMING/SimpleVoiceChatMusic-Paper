package org.karona.simpleVoiceChatMusic.commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.audio.PlayLoadHandler;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class PlayCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        final String query = ModUtils.parseTrackId(String.join(" ", args));
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());

            result.source().sendMessage("Loading songs...");
            MusicManager.getInstance().playerManager.loadItemOrdered(
                gm.getPlayer(),
                query,
                new PlayLoadHandler(result.source(), gm)
            );
        });

        return 0;
    }

}
