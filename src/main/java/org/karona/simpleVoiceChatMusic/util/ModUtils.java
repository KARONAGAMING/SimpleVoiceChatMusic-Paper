package org.karona.simpleVoiceChatMusic.util;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import org.bukkit.entity.Player;
import org.karona.simpleVoiceChatMusic.VoiceChatPlugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.karona.simpleVoiceChatMusic.util.MessageUtils.formatColor;

public class ModUtils {

    public static String hyperlink(String string, String url) {
        return string + " (Click here to open: " + url + ")";
    }

    public static String trackInfo(AudioTrackInfo track) {
        return trackInfo(track, false);
    }

    public static String trackInfo(AudioTrackInfo track, boolean longFormat) {
        StringBuilder text = new StringBuilder(track.title)
                .append(" by ").append(track.author);

        // if long format, add more track data
        if (longFormat) {
            text.append(" [" + formatMMSS(track.length) + "]");
        }

        return text.toString();
    }

    public static String formatMMSS(long millis) {
        String seconds = Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))).toString();

        if (seconds.length() == 1) seconds = "0" + seconds;

        return String.format("%d:%s",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            seconds
        );
    }

    public static String parseTrackId(String userInput) {
        if (userInput.startsWith("ytsearch:") || userInput.startsWith("ytmsearch:") || userInput.startsWith("scsearch:")) {
            return userInput;
        }

        // if starts with id:, parse ourselves
        if (userInput.startsWith("id:")) {
            return userInput.substring(3);
        }

        // try and parse as URL
        try {
            new URL(userInput);
        } catch (MalformedURLException e) {
            return "ytsearch:" + userInput;
        }

        return userInput;
    }

    public static CheckPlayerGroup checkPlayerGroup(Player source) {

        if (VoiceChatPlugin.voicechatServerApi == null) {
            source.sendMessage(formatColor("#FC0000") + "VoiceChat API connection has not been established yet! Please try again later.");
            return null;
        }

        if (source == null) {
            source.sendMessage(formatColor("#FC0000") + "This command is player only!");
            return null;
        }

        VoicechatConnection connection = VoiceChatPlugin.voicechatServerApi.getConnectionOf(source.getUniqueId());

        if (connection == null) {
            source.sendMessage(formatColor("#FC0000") + "You are not connected to voice chat!");
            return null;
        }

        Group group = connection.getGroup();

        if (group == null) {
            source.sendMessage(formatColor("#FC0000") + "You're not in a group! Just use spotify smh..");
            return null;
        }

        CheckPlayerGroup result = new CheckPlayerGroup(source, group);
        return result;
    }

    public record CheckPlayerGroup(Player source, Group group) {
    }
}
