package UCNDiscordBot.Listeners;

import java.util.HashMap;
import java.util.Random;

import UCNDiscordBot.APIS.GiphyAPI;
import UCNDiscordBot.Listeners.RoleListener.AvailableRoles;
import UCNDiscordBot.Listeners.RoleListener.ChangeRole;
import UCNDiscordBot.Listeners.RoleListener.PlayerCount;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// This class is a listener for messages
public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Check if the message is from a bot
        if (event.getAuthor().isBot()) {
            return;
        }

        // // Check if the message is "!ping"
        // if (event.getMessage().getContentDisplay().equals("!ping")) {
        //     event.getChannel().sendMessage("Pong!").queue();
        // }

        // // Simulate coinflip
        // if (event.getMessage().getContentDisplay().equals("!coinflip")) {
        //     final String[] side = { "Heads", "Tails" };
        //     Random random = new Random();
        //     int index = random.nextInt(side.length);
        //     event.getChannel().sendMessage(side[index]).queue();
        // }

        // // Search for a gif
        // if (event.getMessage().getContentDisplay().startsWith("!gif")) {
        //     String gif = "";
        //     EmbedBuilder eb = new EmbedBuilder();

        //     if (event.getMessage().getContentDisplay().equals("!gif")) {
        //         try {
        //             gif = (String) GiphyAPI.getRandomGif();
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         eb.setImage(gif);
        //         event.getChannel().sendMessageEmbeds(eb.build()).queue();
        //     } else {
        //         String searchTerm = event.getMessage().getContentDisplay().substring(5);
        //         try {
        //             gif = GiphyAPI.getGif(searchTerm);
        //         } catch (Exception e) {
        //             e.printStackTrace();
        //         }
        //         eb.setImage(gif);
        //         event.getChannel().sendMessageEmbeds(eb.build()).queue();
        //     }
        // }

        // // Simulate dice roll
        // if (event.getMessage().getContentDisplay().equals("!roll")) {
        //     Random random = new Random();
        //     int index = random.nextInt(6) + 1;
        //     event.getChannel().sendMessage(Integer.toString(index)).queue();
        // }

        // // Check if the message is "!give" and arguments
        // if (event.getMessage().getContentDisplay().startsWith("!give")) {
        //     String args = event.getMessage().getContentDisplay().substring(6);
        //     if (args.length() > 0) {
        //         ChangeRole.giveRole(event, args);
        //     } else {
        //         event.getChannel().sendMessage("Invalid arguments").queue();
        //     }
        // }

        // // Check if the message is "!remove" and arguments
        // if (event.getMessage().getContentDisplay().startsWith("!remove")) {
        //     String args = event.getMessage().getContentDisplay().substring(8);
        //     if (args.length() > 0) {
        //         ChangeRole.removeRole(event, args);
        //     } else {
        //         event.getChannel().sendMessage("Invalid arguments").queue();
        //     }
        // }

        // // Check if the message is "!roles"
        // if (event.getMessage().getContentDisplay().equals("!roles")) {
        //     event.getChannel().sendMessage(AvailableRoles.getRoles(event)).queue();
        // }

        // // Check if message is "!playercount"
        // if (event.getMessage().getContentDisplay().equals("!playercount")) {
        //     HashMap<String, Integer> roles = new HashMap<String, Integer>();
        //     roles = PlayerCount.getRoleCount(event);
        //     // Print the roles and the number of people in each role in one message
        //     String message = "";
        //     for (String key : roles.keySet()) {
        //         message += key + ": " + roles.get(key) + "\n";
        //     }
        //     event.getChannel().sendMessage(message).queue();
        // }

        // // Check if the message is "!help"
        // if (event.getMessage().getContentDisplay().equals("!help")) {
        //     event.getChannel().sendMessage("Available commands:\n"
        //             + "!ping - Response with Pong!\n"
        //             + "!coinflip - Simulate a coinflip\n"
        //             + "!roll - Simulate a dice roll\n"
        //             + "!gif <search> - Search for a gif. If blank a random gif will be found\n"
        //             + "!roles - Gives a list of available roles you can get\n"
        //             + "!give <role> - Assign you with a role from the list\n"
        //             + "!remove <role> - Remove the role you specified\n"
        //             + "!playercount - Gives a list of roles and the number of people in each role\n"
        //             + "\nMusic Player commands:\n"
        //             + "!play <URL> - Takes YouTube URl as input\n"
        //             + "!pause - Pauses the current song\n"
        //             + "!resume - Resumes the current song\n"
        //             + "!skip - Skips the current song\n"
        //             + "!queue - Shows the current queue\n"
        //             + "!clear - Clears the current queue\n"
        //             + "!disconnect - Disconnects the bot from the voice channel").queue();
        // }
    }
}