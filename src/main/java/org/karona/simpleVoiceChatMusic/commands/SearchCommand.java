package org.karona.simpleVoiceChatMusic.commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.audio.SearchLoadHandler;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import java.util.logging.Level;


import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.parseTrackId;

public class SearchCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        final String query = parseTrackId(String.join(" ", args));

        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        SimpleVoiceChatMusic.LOGGER.log(Level.INFO, "Searching for " + query);

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            result.source().sendMessage("Loading songs...");
            MusicManager.getInstance().playerManager.loadItemOrdered(
                gm.getPlayer(),
                query,
                new SearchLoadHandler(result.source(), gm)
            );
        });

        return 0;
    }

}
