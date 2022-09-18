package UCNDiscordBot.Listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.APICalls.Facts;
import UCNDiscordBot.APIS.APICalls.ProgrammerMeme;
import UCNDiscordBot.Functions.DiceRoller;
import UCNDiscordBot.Functions.Magic8Ball;
import UCNDiscordBot.Listeners.RoleListener.ChangeRole;
import UCNDiscordBot.Listeners.RoleListener.PlayerCount;
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
        }

        else if (command.equals("roles")) {
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
        }

        else if (command.equals("playercount")) {
            // Run the '/playercount' command.
            String playerCount = event.getGuild().getMemberCount() + "";
            event.reply("__There is **" + playerCount + "** users on the server!__").setEphemeral(true).queue();
        }

        else if (command.equals("roll")) {
            // run the '/roll' command.
            String message = "";
            if (event.getOption("xdx") == null) {
                message = null;
            } else {
                message = event.getOption("xdx").getAsString();
            }
            event.reply(DiceRoller.rollXDXSend(message)).setEphemeral(true).queue();
        }

        else if (command.equals("removerole")) {
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
        }

        else if (command.equals("addrole")) {
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
        }

        else if (command.equals("coinflip")) {
            // Init string array
            final String[] side = { "Heads", "Tails" };
            // Init random
            Random random = new Random();
            // Get random number
            int index = random.nextInt(side.length);
            // Send message
            event.reply("You've flipped : **" + side[index] + "**").setEphemeral(true).queue();
        }

        else if (command.equals("ping")) {
            // run the '/ping' command.
            event.reply("Pong!").setEphemeral(true).queue();
        }

        else if (command.equals("fact")) {
            // run the '/fact' command.
            event.reply(Facts.getFact()).setEphemeral(true).queue();
        }

        else if (command.equals("gif")) {
            // run the '/gif' command.
            String url = gifOutput(event);
            event.reply(url).queue();
        }

        else if (command.equals("meme")) {
            String url = ProgrammerMeme.getMeme();
            event.reply(url).queue();
        }

        // Get an answer from the magic 8-ball
        else if (command.equals("8ball")) {
            String answer = Magic8Ball.ask8Ball();
            String posterName = event.getUser().getAsMention();
            String message = event.getOption("question").getAsString();
            event.reply(posterName + " asked '" + message + "?'...\n" + answer).queue();
        }

        else if (command.equals("help")) {
            // run the '/commands' command.
            String userTag = event.getUser().getAsMention();
            event.reply("__**Here are the commands**__ " + userTag + ":"
                    + "\n '__**/welcome**__' - welcomes you to the server. "
                    + "\n '__**/roles**__' - lists all the roles on the server. "
                    + "\n '__**/playercount**__' - tells you how many users are on the server. "
                    + "\n '__**/roll**__' - rolls a dice from 1-6 or in the format of 'xdx'. "
                    + "\n '__**/coinflip**__' - flips a coin. "
                    + "\n '__**/removerole**__' - <role> removes a specifc role. "
                    + "\n '__**/addrole**__' - <role> adds a specific role to a user. "
                    + "\n '__**/help**__' - Shows this information. "
                    + "\n '__**/gif**__' <search> - Search for a gif. If blank a random gif will be found. "
                    + "\n '__**/ping**__' - Response with Pong! "
                    + "\n '__**/fact**__' - Prints a random Chuck Norris fact "
                    + "\n '__**/meme**__' - Prints a random programmer meme "
                    + "\n '__**/8ball**__' - Ask the magic 8-ball a question. "
                    + "\n "
                    + "\n __**Music Player commands:**__ "
                    + "\n '__**!play**__' - <url> - Plays a song from a url. "
                    + "\n '__**!skip**__' - Skips the current song. "
                    + "\n '__**!disconnect**__' - Disconnects the bot from the voice channel. "
                    + "\n '__**!pause**__' - Pauses the current song. "
                    + "\n '__**!resume**__' - Resumes the current song. "
                    + "\n '__**!queue**__' - Shows the current queue. "
                    + "\n '__**!clear**__' - Clears the current queue. ").setEphemeral(true).queue();

        }
    }
    // Guild command -- Instantly updated (max 100)

    private String gifOutput(@NotNull SlashCommandInteractionEvent event) {
        return null;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("welcome", "Get welcomed by the bot."));
        commandData.add(Commands.slash("roles", "Check the roles on the server."));
        commandData.add(Commands.slash("playercount", "Check the playercount on the server."));
        commandData.add(Commands.slash("help", "List all the commands."));
        commandData.add(Commands.slash("coinflip", "description"));
        commandData.add(Commands.slash("ping", "Return a pong."));
        commandData.add(Commands.slash("fact", "Get a random Chuck Norris fact."));
        commandData.add(Commands.slash("meme", "Get a random programmer meme."));
        commandData.add(Commands.slash("gif", "Search for a gif. If blank a random gif will be found.")
                .addOption(OptionType.STRING, "search", "Search for a gif.", false));
        commandData.add(Commands.slash("removerole", "Remove a role from yourself.")
                .addOption(OptionType.STRING, "role", "The role you want to remove.", true));
        commandData.add(Commands.slash("addrole", "Add a role from yourself.")
                .addOption(OptionType.STRING, "role", "The role you want to add.", true));
        commandData.add(Commands.slash("8ball", "Get an answer from the magic 8-ball.")
                .addOption(OptionType.STRING, "question", "The question you want to ask.", true));
        commandData.add(Commands.slash("roll", "Roll random numbers")
                .addOption(OptionType.STRING, "xdx", "The dice you want to roll.", false));
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
