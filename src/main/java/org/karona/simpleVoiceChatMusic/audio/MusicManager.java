package org.karona.simpleVoiceChatMusic.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.maxhenkel.voicechat.api.Group;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidTestsuite;
import dev.lavalink.youtube.clients.Music;
import dev.lavalink.youtube.clients.Web;
import dev.lavalink.youtube.clients.skeleton.Client;
import org.bukkit.Server;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;

import java.util.HashMap;
import java.util.UUID;

public class MusicManager {
    private static final MusicManager instance = new MusicManager();
    public AudioPlayerManager playerManager;
    private final HashMap<UUID, GroupManager> groups = new HashMap<>();

    public MusicManager() {
        SimpleVoiceChatMusic.LOGGER.info("Loading sources...");
        this.playerManager = new DefaultAudioPlayerManager();

        // allow hotswapping EQ levels
        this.playerManager.getConfiguration().setFilterHotSwapEnabled(true);

        AudioSourceManagers.registerRemoteSources(
            this.playerManager,
            // we will load v2 of yt music player
            com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class
        );

        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(true, new Client[] { new Music(), new Web(), new AndroidTestsuite() });
        Web.setPoTokenAndVisitorData("MnSzG9MFD8_fSrMj_UGFaFko5rpzoNlNNf8q1qrOXdb7x2FxQk9lYqXbndAOO7Sr_SC3qXTHYbBu2h--EIqjAjFEpF_OdHXXLPaHKXu9xBJLVTK26toml3t9iagYQiSqaPabW-nBj_W82KBU8najZ8eT9D0UVA==",  "CgtVWmFCVloyTjlYayj93dq5BjIKCgJTRxIEGgAgaw%3D%3D");

        this.playerManager.registerSourceManager(ytSourceManager);
        SimpleVoiceChatMusic.LOGGER.info("Loaded all sources!");
    }

    public static MusicManager getInstance() {
        return instance;
    }

    public GroupManager getGroup(Group group, Server server) {
        if (groups.containsKey(group.getId())) {
            return groups.get(group.getId());
        } else {
            GroupManager gm = new GroupManager(group, playerManager.createPlayer(), server);
            groups.put(group.getId(), gm);
            return gm;
        }
    }

    public GroupManager deleteGroup(Group group) {
        return groups.remove(group.getId());
    }

    /**
     * Destroys all groups
     */
    public void cleanup() {
        for (GroupManager gm : groups.values()) {
            gm.cleanup();
        }

        groups.clear();
    }
}
