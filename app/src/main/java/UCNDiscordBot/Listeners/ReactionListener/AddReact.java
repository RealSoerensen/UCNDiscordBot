package UCNDiscordBot.Listeners.ReactionListener;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddReact extends ListenerAdapter {
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (event.getChannel().getName().equals("roles")) {

            event.getGuildChannel().addReactionById(event.getMessageId(), Emoji.fromUnicode("U+2705")).queue();
            event.getGuild().createRole().setName(event.getMessage().getContentDisplay()).setPermissions(0L).complete();
        }
    }
}