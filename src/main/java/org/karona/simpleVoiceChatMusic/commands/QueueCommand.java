package org.karona.simpleVoiceChatMusic.commands;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;
import java.util.concurrent.BlockingQueue;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;

public class QueueCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        Bukkit.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());

            StringBuilder text = new StringBuilder();
            AudioTrack currentTrack = gm.getPlayer().getPlayingTrack();
            BlockingQueue<AudioTrack> tracks = gm.getQueue();

            if (currentTrack != null) {
                text.append(formatColor("#A8A8A8") + "Current: ").append(formatColor("#00FC00") + ModUtils.trackInfo(currentTrack.getInfo())).append("\n");
            }

            AudioTrack[] tracksArr = tracks.toArray(AudioTrack[]::new);
            for (int i = 0; i < tracksArr.length; i++) {
                AudioTrack track = tracksArr[i];
                text.append(formatColor("#37BFF8") + (i + 1) + formatColor("#A8A8A8") + ". " + formatColor("#37BFF8")).append(ModUtils.trackInfo(track.getInfo())).append("\n");
            }

            if (text.toString().isBlank()) {
                text.append(formatColor("#FC0000") + "No songs in the queue.");
            }

            result.source().sendMessage(text.toString());
        });

        return 0;
    }

}
