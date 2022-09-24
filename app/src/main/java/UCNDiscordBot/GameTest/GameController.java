package UCNDiscordBot.GameTest;

import java.lang.reflect.Field;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.lang.Object;

import javax.print.attribute.standard.ColorSupported;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.plaf.synth.ColorType;
import javax.swing.text.AttributeSet.ColorAttribute;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.APICalls.RandomQuestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.internal.requests.Route.Emojis;
import net.dv8tion.jda.internal.utils.message.MessageCreateBuilderMixin;

public class GameController extends ListenerAdapter {
    static String correctAnswer;
    static Emoji emojiCorrect;
    static String messageIDLatestQuestion;
    static boolean isGameActive;
    static boolean isLobbyOpen;
    static ArrayList<Emoji> emojis;
    static ArrayList<User> players;
    static ArrayList<Integer> scores;
    static String currentQuestionID;
    static String currentLobbyID;

    public GameController() {
        emojis = new ArrayList<>();
        emojis.add(Emoji.fromUnicode("U+2705"));
        emojis.add(Emoji.fromUnicode("U+31U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+32U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+33U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+34U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+25B6"));

        correctAnswer = "";

        messageIDLatestQuestion = "";

        isGameActive = false;
        isLobbyOpen = false;

        players = new ArrayList<>();
        scores = new ArrayList<>();
    }

    /*
     * This is an overview of the gamecontroller, this game of travia is done in
     * different steps
     * Step one: Start the game
     * - A slash-command starts the game
     * - Calls method: startGameLobby() - that returns a messageEmbed
     * - setGameActive(true) - game is now active
     * - Sends lobby to channel where the slashcommand is called
     * - Players react to the lobby to join the game
     * - When any user reacts to the lobbymessage they will be added to the
     * playerlist
     * - Message will be updated to show the new player, with the score of 0
     * - playbutten reaction is pressed to start game
     * - From this point on, new players cant join the lobby
     * 
     * Step two: Generate question
     * - The playbutten-reaction on the lobby
     * OR
     * - winner-check: step five
     * is what can ask for a question
     * 
     * -
     * 
     * Step three: Collect users with right answer / add to highscore
     * 
     * Step four: Remove question
     * 
     * Step five: Check if some has 5 right answers
     * - if not repeat step 2-5
     * 
     * Step six: announce winner(s)
     * 
     * Step seven: Set the game to inactive
     */

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        boolean isBot = event.getAuthor().isBot();
        Message message = event.getMessage();
        boolean isLobbyMessage = false;
        boolean isQuestionMessage = false;

        // Needs to use a try/catch here, if a NOT embedded message is sent, it would
        // trigger at out of bound exception on getEmbeds.get(index)
        try {
            isLobbyMessage = event.getMessage().getEmbeds().get(0).getTitle().equals("Question Game");
            isQuestionMessage = event.getMessage().getEmbeds().get(0).getFields().get(0).getName().equals("Question");
        } catch (Exception e) {

        }

        // Step one - bot sends lobby to channel - needs reactions added - stores the
        // lobby ID for later reference - Opens lobby for people to join
        if (isGameActive && isBot && isLobbyMessage) {
            message.addReaction(emojis.get(0)).queue();
            message.addReaction(emojis.get(5)).queue();
            currentLobbyID = message.getId();
            isLobbyOpen = true;
        }

        // If the message is a travia question asked for by the bot, the ID need to be
        // logged
        // And reactions will be added so players can react to answer questions
        if (isBot && isQuestionMessage && isGameActive) {
            currentQuestionID = message.getId();

            event.getMessage().addReaction(emojis.get(1)).queue();
            event.getMessage().addReaction(emojis.get(2)).queue();
            event.getMessage().addReaction(emojis.get(3)).queue();
            event.getMessage().addReaction(emojis.get(4)).queue();
            event.getMessage().addReaction(emojis.get(0)).queue();
        }

    }

    // This runs if anyone reacts to a message
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {

        boolean isCheckmark = event.getEmoji().asUnicode().equals(emojis.get(0));
        boolean isPlaybutton = event.getEmoji().asUnicode().equals(emojis.get(5));
        boolean isBot = event.getUser().isBot();
        boolean isPlayer = players.contains(event.getUser());
        boolean isCurrentQuestion = event.getMessageId().equals(currentQuestionID);
        boolean isLobbyMessage = event.getMessageId().equals(currentLobbyID);
        User user = event.getUser();
        MessageEmbed message = null;
        // Needs to use a try/catch here, if a NOT embedded message is sent, it would
        // trigger at out of bound exception on getEmbeds.get(index)
        try {
            message = event.retrieveMessage().complete().getEmbeds().get(0);
        } catch (Exception e) {

        }
        /*
         * checks if the emoji used to react is the checkmark emoji
         * and if the messagesender if NOT the bot.
         * and if the messageid is the messageIDLatestQuestion
         */

        // Users reacting with checkmark on lobbymessage while lobby is open will be
        // added to the game
        if (!isBot && isCheckmark && isLobbyMessage && isLobbyOpen) {
            addUserToLobby(user); // Adds the user that reacted to players
            updateLobby(event, message);
        }

        // Any user reacting with "playbutton" on lobby will close the lobby and start
        // the game
        if (!isBot && isPlaybutton && isLobbyMessage && isLobbyOpen) {
            isLobbyOpen = false;
            updateLobby(event, message);
            event.getChannel().sendMessageEmbeds(generateQuestion()).queue();

        }

        // If a player reacts with checkmark to the current question, the question will
        // be evaluated
        // Lobby will be updated
        // Question will be deleted
        if (isPlayer && isCheckmark && isCurrentQuestion) {
            evaluateAnswer(event);
            updateLobby(event, event.getChannel().retrieveMessageById(currentLobbyID).complete().getEmbeds().get(0));
            event.getChannel().deleteMessageById(currentQuestionID).queue();
            checkForWinners(event);
        }

    }

    private void checkForWinners(MessageReactionAddEvent event) {
        boolean isWinners = false;
        ArrayList<User> winners = new ArrayList<>();

        for (int a = 0; a < players.size(); a++) {
            if (scores.get(a) == 5) {
                isWinners = true;
                winners.add(players.get(a));
            }
        }
        if (isWinners) {
            // print output message with winners and maybe a scorecard
            event.getChannel().sendMessageEmbeds(generateScoreCard(winners)).queue();
            // Delete lobby
            event.getChannel().deleteMessageById(currentLobbyID).queue();
            // Reset game variables. - call the contructor
            new GameController();
        } else {
            // no winners yet, send another question
            event.getChannel().sendMessageEmbeds(generateQuestion()).queue();
        }
    }

    private void addUserToLobby(User inputUser) {
        /*
         * Checks if the player is joined already
         * if NOT:
         * adds the player and score, so they share index
         */
        if (!players.contains(inputUser)) {
            players.add(inputUser);
            scores.add(0);
        }
    }

    private MessageEmbed generateScoreCard(ArrayList<User> winners) {
        EmbedBuilder outputEmbed = new EmbedBuilder();
        String valueString = "";
        String winnerString = "";
        // Building valueString to that each player will have one line, "Playername:
        // Score"
        for (User element : players) {
            valueString += element.getName() + ": " + scores.get(players.indexOf(element)) + " point(s)\n";
        }
        for (User element : winners) {
            winnerString += element.getName() + "\n";
        }

        /*
         * Message builder
         * Sets the title
         * Constructs the field
         * Sets the Footer
         */

        outputEmbed.setTitle("Scorecard");
        outputEmbed.addField("Winner(s)", winnerString, false);
        outputEmbed.setFooter("Participants: \n" + valueString);

        // Calls the method that will update the start/highscore message
        return outputEmbed.build();
    }

    private void updateLobby(MessageReactionAddEvent event, MessageEmbed reactedMessage) {
        EmbedBuilder outputEmbed = new EmbedBuilder();
        String valueString = "";
        // Building valueString to that each player will have one line, "Playername:
        // Score"
        for (User element : players) {
            valueString += element.getName() + ": " + scores.get(players.indexOf(element)) + "\n";
        }

        /*
         * Message builder
         * Sets the title
         * Constructs the field
         * Sets the Footer
         */

        outputEmbed.setTitle(reactedMessage.getTitle());
        outputEmbed.addField(
                reactedMessage.getFields().get(0).getName(),
                valueString,
                reactedMessage.getFields().get(0).isInline());
        if (isLobbyOpen) {
            outputEmbed.setFooter("Lobby is open, React to the lobby to join");
        } else {
            outputEmbed.setFooter("Lobby is Closed, Game is progress");
        }

        // Calls the method that will update the start/highscore message
        event.getChannel().editMessageEmbedsById(currentLobbyID, outputEmbed.build()).queue();
    }

    private void purgeMessages(TextChannel channel, int numberofMessages) {
        MessageHistory history = new MessageHistory(channel);
        List<Message> msgs;

        msgs = history.retrievePast(2).complete();
        channel.deleteMessages(msgs).queue();

    }

    private void deleteMessage(MessageReactionAddEvent event) {
        event.getChannel().deleteMessageById(event.getMessageId()).queue();
    }

    public static MessageEmbed generateQuestion() {
        /*
         * Method description:
         * This method will return a prebuild embedded message, it will construct it
         * from data gathered from RandomQuestion.getQuestion
         */

        String[] questionDetails = RandomQuestion.getQuestion(); // This string-array will contain a formatted
                                                                 // datastructure getQuestion retreive from WEB
                                                                 // Datastructure:
                                                                 // 0: category
                                                                 // 1: question
                                                                 // 2: correct_answer
                                                                 // 3,4,5 incorrect_answers

        int[] placement = random4Ints(); // This int-array will call the random4Ints-method,
                                         // so indexes from questionDetails 2,3,4,5 will come back scrabled/randomized

        EmbedBuilder embedBuilder = new EmbedBuilder(); // Embedbuilder will be used to prebuild the embedded message
                                                        // that will be return at the end of the method

        // Assign correct answer
        correctAnswer = questionDetails[2];
        /*
         * Here the method will construct the message
         */
        embedBuilder.setTitle(questionDetails[0]);
        embedBuilder.addField("Question", questionDetails[1], false);
        embedBuilder.setFooter(
                emojis.get(1).getFormatted() + ": " + questionDetails[placement[0]] + "\n" +
                        emojis.get(2).getFormatted() + ": " + questionDetails[placement[1]] + "\n" +
                        emojis.get(3).getFormatted() + ": " + questionDetails[placement[2]] + "\n" +
                        emojis.get(4).getFormatted() + ": " + questionDetails[placement[3]] + "\n");

        return embedBuilder.build();

    }

    public static int[] random4Ints() {
        int[] outputInt = new int[4]; // A place to hold the data as is getting randomized

        List<Integer> inputNumbers = new ArrayList<>(); // The list that will contain the indexes that we need to
                                                        // scrambled

        int correctIndex = 0; // to keep the index for the right answer

        Random rand = new Random();// Generic randomizer

        /*
         * Indexes added to the list
         */
        inputNumbers.add(2);
        inputNumbers.add(3);
        inputNumbers.add(4);
        inputNumbers.add(5);

        /*
         * For-loop that will loop 4 times
         * the random-Class will provide a number from (0 to inputnumbers arraysize)
         * that number will be used to get the number from inputnumbers at the
         * randomized index
         * the stored value will be copied to outputInt array
         * the newly stored number will to checked if it the correct answer
         * the number is that index will be removed.
         */
        for (int a = 0; a < 4; a++) {
            int randomIndex = rand.nextInt(0, inputNumbers.size());
            outputInt[a] = inputNumbers.get(randomIndex);
            if (outputInt[a] == 2) {
                correctIndex = a;
            }
            inputNumbers.remove(randomIndex);
        }
        // save correct answer emoji
        /*
         * this will check what emoji will be assigned to the correct answer.
         * if the correct was found during the 3rd loop-round the index is 2 and will
         * get assigned to the 3rd emoji
         */
        if (correctIndex == 0) {
            emojiCorrect = emojis.get(1);
        }
        if (correctIndex == 1) {
            emojiCorrect = emojis.get(2);
        }
        if (correctIndex == 2) {
            emojiCorrect = emojis.get(3);
        }
        if (correctIndex == 3) {
            emojiCorrect = emojis.get(4);
        }

        return outputInt;
    }

    private Message evaluateAnswer(MessageReactionAddEvent event) {
        List<MessageReaction> allReactions = event.retrieveMessage().complete().getReactions();
        ArrayList<List<User>> layer = new ArrayList<>();
        int correctIndex = emojis.indexOf(emojiCorrect) - 1;

        ArrayList<String> userListWinners = new ArrayList<>();
        ArrayList<String> userListLosers = new ArrayList<>();

        /*
         * This loop will go through all the reactions on the message, and retrieve the
         * userlist for each reaction from left to right
         * index 0: First answer option
         * index 1: Second answer option
         * index 2: Third answer option
         * index 3: Fourth answer option
         * index 4: Checkmark
         */
        for (int a = 0; a < allReactions.size(); a++) {
            layer.add(allReactions.get(a).retrieveUsers().complete());
        }
        System.out.println("Correct index: " + correctIndex);

        // Loop for winners/losers
        for (int a = 0; a < 4; a++) {
            if (a == correctIndex) {
                // Winners
                for (int b = 0; b < layer.get(a).size(); b++) {
                    userListWinners.add((layer.get(a).get(b).getName()));
                }
            } else {
                // Losers
                for (int b = 0; b < layer.get(a).size(); b++) {
                    userListLosers.add((layer.get(a).get(b).getName()));
                }
            }
        }

        // remove winners who voted for more than the winning choise

        for (String element : userListLosers) {
            // if winner user is in loser list return true
            if (userListWinners.contains(element)) {
                userListWinners.remove(element);
            }
        }
        // All winners will get their score incremented by one
        for (String element : userListWinners) {
            int index = userListWinners.indexOf(element);
            scores.set(index, scores.get(index) + 1);
        }

        // // output messages
        // event.getChannel().sendMessage("Corrrect answer: " + correctAnswer).queue();
        // if (userListWinners.size() != 0) {
        // event.getChannel().sendMessage("Winner(s):").queue();
        // event.getChannel().sendMessage(userListWinners.toString()).queue();
        // } else {
        // event.getChannel().sendMessage("Nobody won").queue();
        // }

        // /*
        // * Loops through all the lists and prints the username of those who voted
        // */
        // for (int a = 0; a < layer.size(); a++) {
        // System.out.println(a + 1 + ". Option:");
        // for (int b = 0; b < layer.get(a).size(); b++) {
        // System.out.println(layer.get(a).get(b).getName());
        // }
        // System.out.println("");
        // }

        return null;
    }

    public static MessageEmbed startGameLobby() {

        // This is being called from the slashcommands and returns the lobby message

        EmbedBuilder outputMessage = new EmbedBuilder();

        outputMessage.setTitle("Question Game");
        outputMessage.addField("Players", "No players", false);
        outputMessage.setFooter("React to this message to join game");

        isGameActive = true;

        return outputMessage.build();

    }
}
