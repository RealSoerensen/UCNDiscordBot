package UCNDiscordBot;

import javax.security.auth.login.LoginException;

import UCNDiscordBot.APIS.GetAPIKey;
import UCNDiscordBot.GameTest.GameController;
import UCNDiscordBot.Listeners.CommandManager;
import UCNDiscordBot.Listeners.MessageListener.MessageListener;
import UCNDiscordBot.Listeners.MessageListener.MusicPlayer.MusicPlayer;
import UCNDiscordBot.Listeners.ReactionListener.ReactionListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class App extends ListenerAdapter {
    private static String discordToken = new GetAPIKey().getDiscordKey();

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(discordToken);

        // Set the activity for the session
        builder.setActivity(Activity.playing("with your sister"));

        // Set event listeners
        builder.addEventListeners(new MessageListener());
        builder.addEventListeners(new MusicPlayer());
        builder.addEventListeners(new ReactionListener());
        builder.addEventListeners(new CommandManager());
        builder.addEventListeners(new GameController());

        // Set enabled intents
        builder.setEnabledIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS_AND_STICKERS);

        // Set member cache and chunking
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);

        // Build that bitch
        builder.build();
    }
}