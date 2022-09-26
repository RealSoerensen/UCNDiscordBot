package UCNDiscordBot.GameTest;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class CheckAnswers {
    public static void evaluateAnswer(MessageReactionAddEvent event, ArrayList<Emoji> emojis, Emoji emojiCorrect,
            ArrayList<Player> players) {
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
        for (MessageReaction reaction : allReactions) {
            layer.add(reaction.retrieveUsers().complete());
        }
        System.out.println("Correct index: " + correctIndex);

        // Loop for winners/losers
        for (int i = 0; i < 4; i++) {
            if (i == correctIndex) {
                // Winners
                for (int b = 0; b < layer.get(i).size(); b++) {
                    userListWinners.add((layer.get(i).get(b).getName()));
                }
            } else {
                // Losers
                for (int b = 0; b < layer.get(i).size(); b++) {
                    userListLosers.add((layer.get(i).get(b).getName()));
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
        for (Player element : players) {
            if (userListWinners.contains(element.getUser().getName()))
                element.scoreIncrease();
        }
    }
}
