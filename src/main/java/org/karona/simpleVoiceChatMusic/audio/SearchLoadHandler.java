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
                    "Enqueued " + ModUtils.trackInfo(track.getInfo(), true) + " - " + source.getName()
            );
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        // if over 10, trim
        List<AudioTrack> loaded = playlist.getTracks().subList(0, 5);

        if (source != null) {
            // get all titles and create one large string
            StringBuilder text = new StringBuilder("Found " + loaded.size() + " results: \n");

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
            source.sendMessage("No matches found!");
        }
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        if (!exception.severity.equals(FriendlyException.Severity.COMMON)) {
            SimpleVoiceChatMusic.LOGGER.log(Level.WARNING, "Failed to load track from query", exception);
        }

        if (source != null) {
            String message = exception.severity == FriendlyException.Severity.COMMON
                    ? "Failed to load track: " + exception.getMessage()
                    : "Track failed to load! Check server logs for more information";
            source.sendMessage(message);
        }
    }
}
