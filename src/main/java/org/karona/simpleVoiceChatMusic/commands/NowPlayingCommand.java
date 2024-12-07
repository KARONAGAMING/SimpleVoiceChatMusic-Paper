package org.karona.simpleVoiceChatMusic.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.SimpleVoiceChatMusic;
import org.karona.simpleVoiceChatMusic.audio.GroupManager;
import org.karona.simpleVoiceChatMusic.audio.GroupSettingsManager;
import org.karona.simpleVoiceChatMusic.audio.MusicManager;
import org.karona.simpleVoiceChatMusic.util.ModUtils;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;
import static org.karona.simpleVoiceChatMusic.util.ModUtils.checkPlayerGroup;


public class NowPlayingCommand implements Command {
    public int execute(Player context, String[] args) throws Exception {
        ModUtils.CheckPlayerGroup result = checkPlayerGroup(context);
        if (result == null) return 1;

        SimpleVoiceChatMusic.getScheduler().runTask(SimpleVoiceChatMusic.get(), () -> {
            GroupManager gm = MusicManager.getInstance().getGroup(result.group(), result.source().getServer());
            AudioTrack track = gm.getPlayer().getPlayingTrack();
            GroupSettingsManager settings = gm.getSettingsStore();

            if (track == null) {
                result.source().sendMessage(formatColor("#FC0000") + "Nothing is playing.");
                result.source().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(formatColor("#FC0000") + "Nothing is playing."));
                return;
            }

            result.source().sendMessage(formatColor("#A8A8A8") + "Currently Playing: "
                    + formatColor("#00FC00") + ModUtils.trackInfo(track.getInfo())
                    + formatColor("#37BFF8") + "\n" + ModUtils.formatMMSS(track.getPosition()) + "/" + ModUtils.formatMMSS(track.getDuration())
                    + formatColor("#A8A8A8") + " • " + formatColor("#37BFF8") + settings.volume + "%" + formatColor("#A8A8A8") + " volume" + formatColor("#A8A8A8") + " • " + formatColor("#37BFF8")
                    + settings.bassboost + "%" + formatColor("#A8A8A8") + " bassboost");
        });

        return 0;
    }

}
