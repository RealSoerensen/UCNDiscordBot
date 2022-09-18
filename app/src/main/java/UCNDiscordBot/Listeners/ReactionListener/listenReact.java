package UCNDiscordBot.Listeners.ReactionListener;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Class to give rank on react
public class listenReact extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        //This runs if anyone reacts to a message
        
        //The eventhandler
        if (event.getUser().isBot())
            //checks if the owner of the message is a bot
            return;
        if (isChannel(event, "roles")) {
            //This is true if the reaction happened in the channel called role

            //PATRICK! could you please describe this
            assignRole(event);
        }
    }

    private void assignRole(MessageReactionAddEvent event){
        //PATRICK! could you please describe this
        if (event.getReaction().getEmoji().asUnicode().equals(Emoji.fromUnicode("U+2705"))) {
            String messageID = event.getMessageId();
            String message = event.getChannel().retrieveMessageById(messageID).complete()
                    .getContentDisplay();
            for (int i = 1; i < event.getGuild().getRoles().size(); i++) {
                // Get role id
                String roleID = event.getGuild().getRoles().get(i).getId();
                

                // Get the role name
                String roleName = event.getGuild().getRoles().get(i).getName();

                // If the message content equals the role name then add the role to the user
                if (message.equals(roleName)) {
                    setRole(roleID, event);
                }
            }
        }
        resetReactions(event);
    }

    private boolean isChannel(MessageReactionAddEvent event, String channel){
        //Checks if the message was written in a specific channel
        //this will be used is the eventhandler
        return event.getChannel().getName().equals(channel);
    }

    private void setRole(String roleId, MessageReactionAddEvent event) {
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleId)).queue();
    }

    private void resetReactions(MessageReactionAddEvent event) {
        event.getGuildChannel().clearReactionsById(event.getMessageId()).queue();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        event.getGuildChannel().addReactionById(event.getMessageId(), Emoji.fromUnicode("U+2705")).queue();
    }
}
