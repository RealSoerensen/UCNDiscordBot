package UCNDiscordBot.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.APICalls.Facts;
import UCNDiscordBot.APIS.APICalls.GiphyAPI;
import UCNDiscordBot.APIS.APICalls.ProgrammerMeme;
import UCNDiscordBot.APIS.APICalls.Waifu;
import UCNDiscordBot.APIS.APICalls.JavaCompiler;
import UCNDiscordBot.Functions.DiceRoller;
import UCNDiscordBot.Functions.Magic8Ball;
import UCNDiscordBot.Listeners.RoleListener.AvailableRoles;
import UCNDiscordBot.Listeners.RoleListener.ChangeRole;
import UCNDiscordBot.Listeners.RoleListener.PlayerCount;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String user = event.getUser().getAsMention();
        String status = "";
        switch (event.getName()) {
            case "welcome":
                event.reply("Welcome to the server, **" + user + "**!").setEphemeral(true).queue();
                break;

            case "ping":
                event.reply("Pong!").setEphemeral(true).queue();
                break;

            case "roll":
                String option = "";
                event.deferReply().queue();
                if (event.getOption("xdx") == null) {
                    option = null;
                } else {
                    option = event.getOption("xdx").getAsString();
                }
                String output = DiceRoller.rollXDXSend(option);
                event.getHook().editOriginal(output).queue();
                break;

            case "coinflip":
                Random random = new Random();
                int coin = random.nextInt(2);
                String result = "";
                if (coin == 0) {
                    result = "Heads";
                } else {
                    result = "Tails";
                }
                event.reply(result).queue();
                break;

            case "8ball":
                String answer = Magic8Ball.ask8Ball();
                String question = event.getOption("question").getAsString();
                event.reply(user + " asked '" + question + "?'...\n" + answer).queue();
                break;

            case "meme":
                String meme = ProgrammerMeme.getMeme();
                event.reply(meme).queue();
                break;

            case "fact":
                String fact = Facts.getFact();
                event.reply(fact).queue();
                break;

            case "waifu":
                String waifu = Waifu.getWaifu(event.getMember().getId().equals("217751208008351745"));
                event.reply(waifu).setEphemeral(true).queue();
                break;

            case "gif":
                String gif = getGif(event);
                event.reply(gif).queue();
                break;

            case "roles":
                String allRoles = AvailableRoles.getRoles(event);
                event.reply("Here are the roles you can add to yourself: \n" + allRoles).setEphemeral(true).queue();
                break;

            case "addrole":
                String role = event.getOption("role").getAsString();
                status = ChangeRole.addRole(event, role);
                event.reply(status).setEphemeral(true).queue();
                break;

            case "removerole":
                String roleToRemove = event.getOption("role").getAsString();
                status = ChangeRole.removeRole(event, roleToRemove);
                event.reply(status).setEphemeral(true).queue();
                break;

            case "playercount":
                HashMap<String, Integer> roles = new HashMap<String, Integer>();
                // Get all roles from method
                roles = PlayerCount.getRoleCount(event);
                String outputString = "";
                // Print the roles and the number of people in each role in one message
                for (String key : roles.keySet()) {
                    outputString += key + ": " + roles.get(key) + "\n";
                }
                event.reply(outputString).setEphemeral(true).queue();
                break;

            case "poll":
                String poll = event.getOption("poll").getAsString();
                // create poll
                event.reply(user + " created a poll:\n" + poll).queue();
                // get message id of reply
                String messageId = event.getHook().retrieveOriginal().complete().getId();
                // add reactions to the poll
                event.getMessageChannel().retrieveMessageById(messageId).queue(message -> {
                    message.addReaction(Emoji.fromUnicode("👍")).queue();
                    message.addReaction(Emoji.fromUnicode("👎")).queue();
                });
                break;

            case "javacompiler":
                String code = event.getOption("code").getAsString();
                event.deferReply().queue();
                String codeOutput = JavaCompiler.compile(code);
                String reformatCode = JavaCompiler.formater(code);
                if (codeOutput == null) {
                    event.getHook().editOriginal("There was an error compiling your code.").queue();
                    break;
                }

                event.getHook().editOriginal("Input:\n```java\n" + reformatCode.toString()
                        + "```\nOutput: " + codeOutput).queue();

                break;

            case "help":
                event.reply("__**Here are the commands**__ " + user + ":"
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
                        + "\n '__**/waifu**__' - Prints you a waifu "
                        + "\n '__**/meme**__' - Prints a random programmer meme "
                        + "\n '__**/8ball**__' - Ask the magic 8-ball a question. "
                        + "\n '__**/poll**__' - <poll> - Creates a poll. "
                        + "\n '__**/javacompiler**__' - <code> - Compiles java code. "
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

    private String getGif(SlashCommandInteractionEvent event) {
        String gif = "";
        // If the message contains no arguments
        if (event.getOption("search") == null) {
            // Get a random gif
            gif = GiphyAPI.getRandomGif();
        }
        // If the message contains arguments
        else {
            String args = event.getOption("search").getAsString();
            // Try and get gif from GiphyAPI
            gif = GiphyAPI.getGif(args);
        }
        return gif;
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
        commandData.add(Commands.slash("waifu", "Get a random waifu (SFW)"));
        commandData.add(Commands.slash("meme", "Get a random programmer meme."));
        commandData.add(Commands.slash("poll", "Create a poll.").addOption(OptionType.STRING, "poll",
                "The poll question.", true));
        commandData.add(
                Commands.slash("javacompiler", "Compile java code and get output").addOption(OptionType.STRING, "code",
                        "The java code.", true));
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
}
