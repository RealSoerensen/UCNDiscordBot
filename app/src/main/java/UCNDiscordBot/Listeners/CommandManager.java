package UCNDiscordBot.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.GiphyAPI;
import UCNDiscordBot.Listeners.RoleListener.ChangeRole;
import UCNDiscordBot.Listeners.RoleListener.PlayerCount;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("welcome")) {
            // Run the '/welcome' command.
            String userTag = event.getUser().getAsTag();
            event.reply("Welcome to the server, **" + userTag + "**!").setEphemeral(true).queue();

        } else if (command.equals("roles")) {
            // Init hasmap for roles
            HashMap<String, Integer> roles = new HashMap<String, Integer>();
            // Get all roles from method
            roles = PlayerCount.getRoleCount(event);
            // Print the roles and the number of people in each role in one message
            String message = "";
            for (String key : roles.keySet()) {
                message += key + ": " + roles.get(key) + "\n";
            }
            event.reply(message).setEphemeral(true).queue();

        } else if (command.equals("playercount"))

        {
            // Run the '/playercount' command.
            String playerCount = event.getGuild().getMemberCount() + "";
            event.reply("__There is **" + playerCount + "** users on the server!__").setEphemeral(true).queue();
        } else if (command.equals("roll")) {
            // run the '/roll' command.
            String userTag = event.getUser().getAsMention();
            int roll = (int) (Math.random() * 99) + 1;
            event.reply(userTag + " rolled a **" + roll + "**!").queue();
        } else if (command.equals("help")) {
            // run the '/commands' command.
            String userTag = event.getUser().getAsMention();
            event.reply("__**Here are the commands**__ " + userTag + ":"
                    + "\n '__**/welcome**__' - welcomes you to the server. "
                    + "\n '__**/roles**__' - lists all the roles on the server. "
                    + "\n '__**/playercount**__' - tells you how many users are on the server. "
                    + "\n '__**/roll**__' - rolls a random number between 1 and 100. "
                    + "\n '__**/coinflip**__' - flips a coin. "
                    + "\n '__**/removerole**__' - <role> removes a specifc role. "
                    + "\n '__**/addrole**__' - <role> adds a specific role to a user. "
                    + "\n '__**/help**__' - Shows this information. "
                    + "\n '__**/gif**__' <search> - Search for a gif. If blank a random gif will be found. "
                    + "\n '__**/ping**__' - Response with Pong! "
                    + "\n '__**/fact**__' - Prints a random Chuck Norris fact "
                    + "\n "
                    + "\n __**Music Player commands:**__ "
                    + "\n '__**!play**__' - <url> - Plays a song from a url. "
                    + "\n '__**!skip**__' - Skips the current song. "
                    + "\n '__**!disconnect**__' - Disconnects the bot from the voice channel. "
                    + "\n '__**!pause**__' - Pauses the current song. "
                    + "\n '__**!resume**__' - Resumes the current song. "
                    + "\n '__**!queue**__' - Shows the current queue. "
                    + "\n '__**!clear**__' - Clears the current queue. ").setEphemeral(true).queue();

        } else if (command.equals("removerole")) {
            // run the '/removeRole' command to remove a role.
            String role = event.getOption("role").getAsString();
            if (role.length() > 0) {
                ChangeRole.removeRole(event, role);
            }
            // If role is empty
            else {
                // Send message to user
                event.reply("Please specify a role").setEphemeral(true).queue();
            }
        } else if (command.equals("addrole")) {
            // run the '/addRole' command to add a role.
            String role = event.getOption("role").getAsString();
            if (role.length() > 0) {
                // Give role to user
                ChangeRole.giveRole(event, role);
            }
            // If args is empty
            else {
                // Send message to user
                event.reply("__Please specify a role__").setEphemeral(true).queue();
            }
        } else if (command.equals("coinflip")) {
            // Init string array
            final String[] side = { "Heads", "Tails" };
            // Init random
            Random random = new Random();
            // Get random number
            int index = random.nextInt(side.length);
            // Send message
            event.reply("You've rolled : **" + side[index] + "**").setEphemeral(true).queue();
        } else if (command.equals("ping")) {
            // run the '/ping' command.
            event.reply("Pong!").setEphemeral(true).queue();
        } else if (command.equals("gif")) {
            gifOutput(event);

        }

    }

    private void gifOutput(SlashCommandInteractionEvent event) {
        // Init variables
        String gif = "";

        // If the message contains no arguments
        if (event.getOption("search") == null) {
            // Get a random gif
            try {
                gif = (String) GiphyAPI.getRandomGif();
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.reply(gif).queue();
        }
        // If the message contains arguments
        else {
            String args = event.getOption("search").getAsString();
            // Try and get gif from GiphyAPI
            try {
                gif = (String) GiphyAPI.getGif(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            event.reply(gif).queue();
        }
    }
    // Guild command -- Instantly updated (max 100)

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        commandData.add(Commands.slash("roles", "Check the roles on the server."));
        commandData.add(Commands.slash("playercount", "Check the playercount on the server."));
        commandData.add(Commands.slash("roll", "Roll a random number between 1 and 100."));
        commandData.add(Commands.slash("help", "List all the commands."));
        commandData.add(Commands.slash("coinflip", "description"));
        commandData.add(Commands.slash("ping", "Return a pong."));
        commandData.add(Commands.slash("gif", "Search for a gif. If blank a random gif will be found.")
                .addOption(OptionType.STRING, "search", "Search for a gif.", false));
        commandData.add(Commands.slash("removerole", "Remove a role from yourself.")
                .addOption(OptionType.STRING, "role", "The role you want to remove.", true));
        commandData.add(Commands.slash("addrole", "Add a role from yourself.")
                .addOption(OptionType.STRING, "role", "The role you want to add.", true));
        event.getGuild().updateCommands().addCommands(commandData).queue();

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    // Global command -- Updated after 1 hour (max 100)
}
