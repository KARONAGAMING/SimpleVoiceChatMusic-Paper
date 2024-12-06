package org.karona.simpleVoiceChatMusic.commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;

public class BassboostCommand implements Command {
    public int execute(Player player, String[] args) throws Exception {
        float bass = Float.parseFloat(args[0]);
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(player);
        if (result == null) return 1;

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            gm.broadcast("Bassboost set to " + bass + "% by " + player.getName());
            gm.setBassBoost(bass);
        });

        return 0;
    }

}
