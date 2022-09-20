package UCNDiscordBot.Listeners.MessageListener.MusicPlayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class MusicPlayer extends ListenerAdapter {
    AudioPlayerManager playerManager;
    Map<Long, GuildMusicManager> musicManagers = new HashMap<>();

    public MusicPlayer() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {

            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (!event.getChannel().getName().equals("bot-commands")) {
            return;
        }
        String[] command = event.getMessage().getContentRaw().split(" ", 2);

        if ("!play".equals(command[0]) && command.length == 2) {
            loadAndPlay(event, command[1]);
        }
        if ("!skip".equals(command[0])) {
            skipTrack(event);
        }
        if ("!disconnect".equals(command[0])) {
            disconnect(event);
        }
        if ("!clear".equals(command[0])) {
            clear(event);
        }
        if ("!queue".equals(command[0])) {
            queue(event);
        }
        if ("!pause".equals(command[0])) {
            pause(event);
        }
        if ("!resume".equals(command[0])) {
            resume(event);
        }

        super.onMessageReceived(event);
    }

    private void loadAndPlay(MessageReceivedEvent channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getMessage().getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(channel, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                channel.getChannel()
                        .sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist "
                                + playlist.getName() + ")")
                        .queue();

                play(channel, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.getChannel().sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.getChannel().sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    private void pause(MessageReceivedEvent channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getMessage().getGuild());
        if (!musicManager.player.isPaused()) {
            musicManager.player.setPaused(true);
            channel.getChannel().sendMessage("Pausing " + musicManager.player.getPlayingTrack().getInfo().title)
                    .queue();
        } else {
            channel.getChannel().sendMessage("Music is already paused").queue();
        }
    }

    private void resume(MessageReceivedEvent channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getMessage().getGuild());
        if (musicManager.player.isPaused()) {
            musicManager.player.setPaused(false);
            channel.getChannel().sendMessage("Resuming " + musicManager.player.getPlayingTrack().getInfo().title)
                    .queue();
        } else {
            channel.getChannel().sendMessage("Music is not paused").queue();
        }
    }

    private void skipTrack(MessageReceivedEvent channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.getChannel().sendMessage("Skipped to next track.").queue();
        if (musicManager.scheduler.getQueue().isEmpty()) {
            channel.getChannel().sendMessage("Queue is empty").queue();
        } else {
            channel.getChannel().sendMessage("Now playing: " + musicManager.player.getPlayingTrack().getInfo().title)
                    .queue();
        }
    }

    private void disconnect(MessageReceivedEvent channel) {
        // CHeck if the bot is connected to a voice channel
        if (channel.getGuild().getAudioManager().isConnected()) {
            // Disconnect the bot from the voice channel
            channel.getGuild().getAudioManager().closeAudioConnection();
            channel.getChannel().sendMessage("Disconnected from the voice channel.").queue();
        } else {
            channel.getChannel().sendMessage("The bot is not connected to a voice channel.").queue();
        }
    }

    private void clear(MessageReceivedEvent channel) {
        // Clear the queue
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.clearQueue();

        channel.getChannel().sendMessage("Cleared the queue.").queue();
    }

    private void queue(MessageReceivedEvent channel) {
        // Get the queue
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        String queue = musicManager.scheduler.getQueue();

        // Check if the queue is empty
        if (queue.equals("")) {
            channel.getChannel().sendMessage("The queue is empty.").queue();
        } else {
            channel.getChannel().sendMessage(queue).queue();
        }
    }

    private void play(MessageReceivedEvent channel, GuildMusicManager musicManager, AudioTrack track) {
        boolean isConnected = connectToVoiceChannel(channel.getGuild().getAudioManager(), channel);

        if (isConnected) {
            channel.getChannel().sendMessage("Now Playing: " + track.getInfo().title).queue();
            musicManager.scheduler.queue(track);
        }
    }

    private static boolean connectToVoiceChannel(AudioManager audioManager, MessageReceivedEvent event) {
        boolean isConnected = false;
        if (!audioManager.isConnected()) {
            // Get the voice channel the user is in
            VoiceChannel voiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();
            for (VoiceChannel vc : event.getGuild().getVoiceChannels()) {
                if (vc.getMembers().contains(event.getMember())) {
                    audioManager.openAudioConnection(voiceChannel);
                    isConnected = true;
                    break;
                }
            }
        } else {
            isConnected = true;
        }
        return isConnected;
    }
}
