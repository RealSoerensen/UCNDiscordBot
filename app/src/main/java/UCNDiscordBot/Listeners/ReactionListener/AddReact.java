package UCNDiscordBot.Listeners.ReactionListener;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import java.awt.*;

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
            newRole.setColor(getColor());
            newRole.complete();
        }
    }

    public Color getColor() {
        int R = (int)(Math.random()*255);
        int G = (int)(Math.random()*255);
        int B= (int)(Math.random()*255);
        return new Color(R, G, B); //random color, but can be bright or dull
    }
}