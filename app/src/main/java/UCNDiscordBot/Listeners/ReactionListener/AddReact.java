package UCNDiscordBot.Listeners.ReactionListener;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.GiphyAPI;
import UCNDiscordBot.Listeners.RoleListener.AvailableRoles;
import UCNDiscordBot.Listeners.RoleListener.ChangeRole;
import UCNDiscordBot.Listeners.RoleListener.PlayerCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class AddReact extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        //eventhandler
        if (event.getAuthor().isBot())
        //checking if the owner of the event is a bot
            return;
        if (isChannel(event, "roles")) {
            //If message wes sent in the channel called roles

            //Adds the initial emote to this message
            addReactionToMessage(event, "U+2705");
            
            //Creates a new roll, with the name of the message
            //gives the role a random color
            createNewRole(event);
        }
        if(isChannel(event, "gaming")){
            //If message wes sent in the channel called gaming
        }
        if(isChannel(event, "bot-commands")){
            //If message wes sent in the channel called bot command
            //if-statements below are used for commandcalls
            //all commands must start with an !, be unique
            //and contain method calls only

            // Check if the message is "!ping"
            if (isMessage(event,"!ping")) {
                pingPong(event);
            }

            // Simulate coinflip
            if (isMessage(event, "!coinflip")) {
                coinFlip(event);
            }

            // Search for a gif
            if (isMessageStartWith(event, "!gif")) {
                gifOutput(event);
            }

            // Simulate dice roll
            if (isMessage(event, "!roll")) {
                diceRoll(event);
            }
            // Check if the message is "!give" and arguments
            if (isMessageStartWith(event, "!give")) {
                giveRole(event);
            }
            // Check if the message is "!remove" and arguments
            if (isMessageStartWith(event, "!remove")) {
                removeRole(event);
            }
            // Check if the message is "!roles"
            if (isMessage(event, "!roles")) {
                getRoles(event);
            }
            // Check if message is "!playercount"
            if (isMessage(event, "!playercount")) {
                getPlayerCount(event);
            }
            // Check if the message is "!help"
            if (isMessage(event, "!help")) {
                getHelp(event);
            }
        }
    }

    private boolean isChannel(MessageReceivedEvent event, String channel){
        //Checks if the message was written in a specific channel
        //this will be used is the eventhandler
        return event.getChannel().getName().equals(channel);
    }
    private boolean isMessage(MessageReceivedEvent event,String message){
        return event.getMessage().getContentDisplay().equals(message);
    }
    private boolean isMessageStartWith(MessageReceivedEvent event,String prefix){
        return event.getMessage().getContentDisplay().startsWith(prefix);
    }
    private void createNewRole(MessageReceivedEvent event){
        //Creates a new roll, with the name of the message
        //gives the role a random color
        //the new role has no permissions
        RoleAction newRole = event.getGuild().createRole();
            newRole.setName(event.getMessage().getContentDisplay());
            newRole.setPermissions(0L);
            newRole.setColor(getColor());
            newRole.complete();
    }
    private void addReactionToMessage(MessageReceivedEvent event, String unicodeEmoji){
        //Adds the initial emote to this message
        event.getGuildChannel().addReactionById(event.getMessageId(), Emoji.fromUnicode(unicodeEmoji)).queue();
    }

    //Command-methods
    private void pingPong(MessageReceivedEvent event){
        //NEED description
        event.getChannel().sendMessage("Pong!").queue();
    }
    private void coinFlip(MessageReceivedEvent event){
        //NEED description
        final String[] side = { "Heads", "Tails" };
        Random random = new Random();
        int index = random.nextInt(side.length);
        event.getChannel().sendMessage(side[index]).queue();

    }
    private void gifOutput(MessageReceivedEvent event){
        //NEED description
        String gif = "";
        EmbedBuilder eb = new EmbedBuilder();

        if (event.getMessage().getContentDisplay().equals("!gif")) {
            try {
                gif = (String) GiphyAPI.getRandomGif();
            } catch (Exception e) {
                e.printStackTrace();
            }
            eb.setImage(gif);
            event.getChannel().sendMessageEmbeds(eb.build()).queue();
        } else {
            String searchTerm = event.getMessage().getContentDisplay().substring(5);
            try {
                gif = GiphyAPI.getGif(searchTerm);
            } catch (Exception e) {
                e.printStackTrace();
            }
            eb.setImage(gif);
            event.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }
    private void diceRoll(MessageReceivedEvent event){
        //NEED description
        Random random = new Random();
        int index = random.nextInt(6) + 1;
        event.getChannel().sendMessage(Integer.toString(index)).queue();
    }
    private void giveRole(MessageReceivedEvent event){
        //NEED description
        String args = event.getMessage().getContentDisplay().substring(6);
        if (args.length() > 0) {
            ChangeRole.giveRole(event, args);
        }
        else {
            event.getChannel().sendMessage("Invalid arguments").queue();
        }
    }
    private void removeRole(MessageReceivedEvent event){
        //NEED description
        String args = event.getMessage().getContentDisplay().substring(8);
        if (args.length() > 0) {
            ChangeRole.removeRole(event, args);
        } 
        else {
            event.getChannel().sendMessage("Invalid arguments").queue();
        }
    }
    private void getRoles(MessageReceivedEvent event){
        event.getChannel().sendMessage(AvailableRoles.getRoles(event)).queue();
    }
    private void getPlayerCount(MessageReceivedEvent event){
        //NEED description
        HashMap<String, Integer> roles = new HashMap<String, Integer>();
        roles = PlayerCount.getRoleCount(event);
        // Print the roles and the number of people in each role in one message
        String message = "";
        for (String key : roles.keySet()) {
            message += key + ": " + roles.get(key) + "\n";
        }
        event.getChannel().sendMessage(message).queue();
    }
    private void getHelp(MessageReceivedEvent event){
        event.getChannel().sendMessage("Available commands:\n"
                        + "!ping - Response with Pong!\n"
                        + "!coinflip - Simulate a coinflip\n"
                        + "!roll - Simulate a dice roll\n"
                        + "!gif <search> - Search for a gif. If blank a random gif will be found\n"
                        + "!roles - Gives a list of available roles you can get\n"
                        + "!give <role> - Assign you with a role from the list\n"
                        + "!remove <role> - Remove the role you specified\n"
                        + "!playercount - Gives a list of roles and the number of people in each role\n"
                        + "\nMusic Player commands:\n"
                        + "!play <URL> - Takes YouTube URl as input\n"
                        + "!pause - Pauses the current song\n"
                        + "!resume - Resumes the current song\n"
                        + "!skip - Skips the current song\n"
                        + "!queue - Shows the current queue\n"
                        + "!clear - Clears the current queue\n"
                        + "!disconnect - Disconnects the bot from the voice channel").queue();
    }

    public Color getColor() {
        //Returns a random color
        int R = (int)(Math.random()*255);
        int G = (int)(Math.random()*255);
        int B= (int)(Math.random()*255);
        return new Color(R, G, B); //random color, but can be bright or dull
    }
}