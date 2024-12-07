package org.karona.simpleVoiceChatMusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.util.ModUtils;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;

public class SearchLoadHandler implements AudioLoadResultHandler {

    protected final Player source;
    protected final GroupManager group;

    public SearchLoadHandler(Player source, GroupManager group) {
        this.source = source;
        this.group = group;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        group.enqueueSong(track);

        if (source != null) {
            this.group.broadcast(
                    formatColor("#A8A8A8") + "Enqueued: " + formatColor("#00FC00") + ModUtils.trackInfo(track.getInfo(), true) + formatColor("#A8A8A8") + " - " + formatColor("#37BFF8") + source.getName()
            );
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        List<AudioTrack> loaded = playlist.getTracks().subList(0, 5);
        if (source != null) {
            StringBuilder text = new StringBuilder(formatColor("#A8A8A8") + "Found " + formatColor("#37BFF8") + loaded.size() + formatColor("#A8A8A8") + " results: \n");

            for (AudioTrack track : loaded) {
                text.append("  - ").append(ModUtils.trackInfo(track.getInfo(), true)).append("\n")
                        .append("    [Click to add to queue] /music play \"").append(track.getIdentifier()).append("\"\n\n");
            }

            source.sendMessage(text.toString());
        }
    }

    @Override
    public void noMatches() {
        if (source != null) {
            source.sendMessage(formatColor("#FC0000") +  "No matches found!");
        }
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (!exception.severity.equals(FriendlyException.Severity.COMMON)) {
            SimpleVoiceChatMusic.LOGGER.log(Level.WARNING, "Failed to load track from query", exception);
        }

        if (source != null) {
            String message = exception.severity == FriendlyException.Severity.COMMON
                    ? formatColor("#FC0000") + "Failed to load track: " + exception.getMessage()
                    : formatColor("#FC0000") + "Track failed to load! Check server logs for more information";
            source.sendMessage(message);
        }
    }
}
