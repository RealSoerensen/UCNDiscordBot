package UCNDiscordBot.Functions;

import java.util.Random;
import java.util.stream.IntStream;

public class DiceRoller {

    public static String rollXDXSend(String message) {
        String outputString = "";
        int diceAmount = 0;
        int diceSize = 0;
        int[] values;
        String[] arg;

        try {
            if (message == null) {
                // roll 6-sided dice
                outputString += rollDX(6);
                return outputString;
            }

            arg = message.split("d");
            diceAmount = Integer.parseInt(arg[0]);
            diceSize = Integer.parseInt(arg[1]);
            values = rollXDX(diceAmount, diceSize);
            if (diceAmount > 100000) {
                return "Total amount of dice cant be larger than: 10000";
            }

            for (int i = 0; i < values.length; i++) {
                // add dice value to output string
                // add + if not first value
                if (i != 0) {
                    outputString += " + ";
                }
                outputString += values[i];
            }

            int sum = IntStream.of(values).sum();
            double avg = (double) sum / (double) diceAmount;
            outputString += " = " + sum + "\nAverage: " + avg;

            if (outputString.length() > 2000) {
                outputString = "Sum of all dice rolls: " + sum + "\nAverage: " + avg;
            }
            return outputString;

        } catch (Exception e) {
            System.out.println(e);
            return "Invalid input";
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
