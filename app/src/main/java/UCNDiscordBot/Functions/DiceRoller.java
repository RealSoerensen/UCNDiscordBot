package UCNDiscordBot.Functions;

import java.util.Random;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiceRoller {

    public static void rollXDXSend(MessageReceivedEvent event) {
        String message = event.getMessage().getContentDisplay();
        String outputString = "";
        int diceAmount = 0;
        int diceSize = 0;
        int[] values;
        double avg = 0;

        try {
            if (!message.equals("!roll")) {
                String[] arg = message.split(" ");
                arg = arg[1].split("d");
                diceAmount = Integer.parseInt(arg[0]);
                diceSize = Integer.parseInt(arg[1]);
                values = rollXDX(diceAmount, diceSize);
                if (diceAmount > 100000) {
                    event.getChannel().sendMessage("Total amount of dice cant be larger than: 10000").queue();
                    return;
                }

                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        outputString += values[i];
                    } else {
                        outputString += " + " + values[i];
                    }
                }

                // Get sum of int array
                int sum = 0;
                for (int i = 0; i < values.length; i++) {
                    sum += values[i];
                }

                outputString += " = " + sum;

                if (outputString.length() >= 2000) {
                    avg = (double) sum / ((double) values.length);
                    event.getChannel().sendMessage("Total: " + sum + " - Avg: " + avg).queue();
                } else {
                    event.getChannel().sendMessage(outputString).queue();
                }
            } else {
                // roll 6-sided dice
                outputString += rollDX(6);
                event.getChannel().sendMessage(outputString).queue();
            }
        } catch (Exception e) {
            event.getChannel().sendMessage("not a diceroll or too many characters").queue();
        }

    }

    public static int rollDX(int diceSize) {
        Random random = new Random();
        return random.nextInt(1, diceSize + 1);
    }

    public static int[] rollXDX(int diceAmount, int diceSize) {
        int[] values = new int[diceAmount];

        for (int i = 0; i < diceAmount; i++) {
            values[i] = rollDX(diceSize);
        }
        return values;
    }
}
