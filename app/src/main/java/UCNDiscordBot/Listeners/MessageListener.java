package UCNDiscordBot.Listeners;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import java.awt.*;

// This class is a listener for messages
public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // eventhandler
        if (event.getAuthor().isBot())
            // checking if the owner of the event is a bot
            return;
        if (isChannel(event, "roles")) {
            // If message wes sent in the channel called roles

            // Adds the initial emote to this message
            addReactionToMessage(event, "U+2705");

            // Creates a new roll, with the name of the message
            // gives the role a random color
            createNewRole(event);
        }

    }

    private boolean isChannel(MessageReceivedEvent event, String channel) {
        // Checks if the message was written in a specific channel
        // this will be used is the eventhandler
        return event.getChannel().getName().equals(channel);
    }

    private void createNewRole(MessageReceivedEvent event) {
        /*
         * Creates a new roll, with the name of the message
         * gives the role a random color
         * the new role has no permission
         */
        RoleAction newRole = event.getGuild().createRole();
        newRole.setName(event.getMessage().getContentDisplay());
        newRole.setPermissions(0L);
        newRole.setColor(getColor());
        newRole.complete();
    }

    private void addReactionToMessage(MessageReceivedEvent event, String unicodeEmoji) {
        // Adds the initial emote to this message
        event.getGuildChannel().addReactionById(event.getMessageId(), Emoji.fromUnicode(unicodeEmoji)).queue();
    }

    public Color getColor() {
        // Returns a random color
        int R = (int) (Math.random() * 255);
        int G = (int) (Math.random() * 255);
        int B = (int) (Math.random() * 255);
        return new Color(R, G, B); // random color, but can be bright or dull
    }

}