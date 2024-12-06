package org.karona.simpleVoiceChatMusic.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import org.bukkit.entity.Player;

public class PlayLoadHandler extends SearchLoadHandler{
    public PlayLoadHandler(Player source, GroupManager group) {
        super(source, group);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        super.trackLoaded(playlist.getTracks().get(0));
    }
}
