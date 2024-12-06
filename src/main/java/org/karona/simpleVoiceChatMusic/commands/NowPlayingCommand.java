package org.karona.simpleVoiceChatMusic.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.GroupSettingsManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class NowPlayingCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            AudioTrack track = gm.getPlayer().getPlayingTrack();
            GroupSettingsManager settings = gm.getSettingsStore();

            if (track == null) {
                result.source().sendMessage("Nothing is playing.");
                return;
            }

            result.source().sendMessage("Currently Playing "
                    + ModUtils.trackInfo(track.getInfo())
                    + "\n" + ModUtils.formatMMSS(track.getPosition()) + "/" + ModUtils.formatMMSS(track.getDuration())
                    + " • " + settings.volume + "% volume"
                    + " • " + settings.bassboost + "% bassboost");
        });

        return 0;
    }

}
