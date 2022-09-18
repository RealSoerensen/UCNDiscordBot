package UCNDiscordBot.Functions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Magic8Ball {

    List<String> answers = new ArrayList<String>();

    public Magic8Ball() {
        answers.add("It is certain");
        answers.add("It is decidedly so.");
        answers.add("Without a doubt.");
        answers.add("Yes definitely.");
        answers.add("You may rely on it.");
        answers.add("As I see it, yes.");
        answers.add("Most likely.");
        answers.add("Outlook good.");
        answers.add("Yes.");
        answers.add("Signs point to yes.");
        answers.add("Reply hazy, try again.");
        answers.add("Ask again later.");
        answers.add("Better not tell you now.");
        answers.add("Cannot predict now.");
        answers.add("Concentrate and ask again.");
        answers.add("Don't count on it.");
        answers.add("My reply is no.");
        answers.add("My sources say no.");
        answers.add("Outlook not so good.");
        answers.add("Very doubtful.");
    }

    public static void ask8Ball(MessageReceivedEvent event) {

        Magic8Ball test = new Magic8Ball();
        Random random = new Random();
        int index = random.nextInt(0, test.answers.size());

        event.getChannel().sendMessage(test.answers.get(index)).queue();

    }
}
