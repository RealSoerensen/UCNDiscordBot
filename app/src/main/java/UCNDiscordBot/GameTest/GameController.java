package UCNDiscordBot.GameTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import UCNDiscordBot.APIS.APICalls.RandomQuestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GameController extends ListenerAdapter {
    static String correctAnswer;
    static Emoji emojiCorrect;
    static String messageIDLatestQuestion;
    static boolean canSend;
    static ArrayList<Emoji> emojis = new ArrayList<>();

    public GameController() {
        emojis.add(Emoji.fromUnicode("U+2705"));
        emojis.add(Emoji.fromUnicode("U+31U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+32U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+33U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+34U+fe0fU+20e3"));
        emojis.add(Emoji.fromUnicode("U+25B6"));

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // System.out.println(event.getMessage().getEmbeds().get(0).getFields().get(0).getName().toString());
        if (event.getAuthor().isBot() && canSend
                && event.getMessage().getEmbeds().get(0).getFields().get(0).getName().equals("Question")) {
            /*
             * HELP: what can we do to make this shorter
             * it needs to verify that the message send is a requested question
             * so, it is unpacking the message down to fields,
             */
            event.getMessage().addReaction(emojis.get(1)).queue();
            event.getMessage().addReaction(emojis.get(2)).queue();
            event.getMessage().addReaction(emojis.get(3)).queue();
            event.getMessage().addReaction(emojis.get(4)).queue();
            event.getMessage().addReaction(emojis.get(0)).queue();
            event.getMessage().addReaction(emojis.get(5)).queue();
            messageIDLatestQuestion = event.getMessageId();
            canSend = false; // now that the question is sent, this will be set to false - this will be set
                             // to true, if the generateQuestion method is this class
        }

    }

    // This runs if anyone reacts to a message
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        /*
         * checks if the emoji used to react is the checkmark emoji
         * and if the messagesender if NOT the bot.
         * and if the messageid is the messageIDLatestQuestion
         */
        if (event.getEmoji().asUnicode().equals(emojis.get(0)) && !event.getUser().isBot()
                && event.getMessageId().equals(messageIDLatestQuestion)) {
            System.out.println("Evaluate Answer");
            evaluateAnswer(event);
        }
        /*
         * Next question
         */
        if (event.getEmoji().asUnicode().equals(emojis.get(5)) && !event.getUser().isBot()
                && event.getMessageId().equals(messageIDLatestQuestion)) {
            System.out.println("Next Question");
            // Delete

            // New question
            event.getChannel().sendMessageEmbeds(generateQuestion()).queue();
        }

    }

    public static MessageEmbed generateQuestion() {
        /*
         * Method description:
         * This method will return a prebuild embedded message, it will construct it
         * from data gathered from RandomQuestion.getQuestion
         */
        canSend = true; // this will set the instance varible to true. it is a test for when multible
                        // request for a question is issued,
                        // the reactions added wont go to the wrong post

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
        // output messages
        event.getChannel().sendMessage("Corrrect answer: " + correctAnswer).queue();
        if (userListWinners.size() != 0) {
            event.getChannel().sendMessage("Winner(s):").queue();
            event.getChannel().sendMessage(userListWinners.toString()).queue();
        } else {
            event.getChannel().sendMessage("Nobody won").queue();
        }

        /*
         * Loops through all the lists and prints the username of those who voted
         */
        for (int a = 0; a < layer.size(); a++) {
            System.out.println(a + 1 + ". Option:");
            for (int b = 0; b < layer.get(a).size(); b++) {
                System.out.println(layer.get(a).get(b).getName());
            }
            System.out.println("");
        }

        return null;
    }
}
