package org.karona.simpleVoiceChatMusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import java.util.logging.Level;

public class TrackScheduler extends AudioEventAdapter {
    private final GroupManager group;

    TrackScheduler(GroupManager groupManager) {
        super();
        this.group = groupManager;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        this.group.broadcast("Now playing: " + ModUtils.trackInfo(track.getInfo()));
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // only start next if applicable
        if (endReason.mayStartNext) {
            this.group.nextTrack();
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        if (exception.severity == FriendlyException.Severity.COMMON) {
            SimpleVoiceChatMusic.LOGGER.log(Level.SEVERE, "Failed to play "+track.getInfo().title+" due to error: {}", exception.getMessage());
            this.group.broadcast("Failed to play song: " + exception.getMessage());
        } else {
            SimpleVoiceChatMusic.LOGGER.log(Level.SEVERE, "Failed to play "+track.getInfo().title+" due to error: {}", exception.getMessage());
            this.group.broadcast("Failed to play song due to an internal error.");
        }

        this.group.nextTrack();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        this.group.broadcast("Track stuck -- skipping!");
        this.group.nextTrack();
    }
}
