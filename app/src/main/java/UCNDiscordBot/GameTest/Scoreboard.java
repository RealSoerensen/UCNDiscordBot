package UCNDiscordBot.GameTest;

import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class Scoreboard {
    public static MessageEmbed generateScoreCard(ArrayList<User> winners, ArrayList<Player> players) {
        EmbedBuilder outputEmbed = new EmbedBuilder();
        String valueString = "";
        String winnerString = "";
        // Building valueString to that each player will have one line, "Playername:
        // Score"
        for (Player element : players) {
            valueString += element.getUser().getName() + ": " + element.getScore() + " point(s)\n";
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
}
