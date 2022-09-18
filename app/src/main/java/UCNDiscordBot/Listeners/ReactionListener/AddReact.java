package UCNDiscordBot.Listeners.ReactionListener;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;

public class AddReact extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (event.getChannel().getName().equals("roles")) {
            event.getGuildChannel().addReactionById(event.getMessageId(), Emoji.fromUnicode("U+2705")).queue();
            RoleAction newRole = event.getGuild().createRole();
            newRole.setName(event.getMessage().getContentDisplay());
            newRole.setPermissions(0L);
            newRole.complete();
        }
    }
}