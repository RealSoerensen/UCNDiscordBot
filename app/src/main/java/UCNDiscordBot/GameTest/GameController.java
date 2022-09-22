package UCNDiscordBot.GameTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import UCNDiscordBot.APIS.APICalls.RandomQuestion;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GameController extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

    }

    // This runs if anyone reacts to a message
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        System.out.println("reaction detected");
        System.out.println(event.getEmoji().asUnicode().toString());
        ;

        // deleteMessage(event);
        // TextChannel channel =
        // event.getGuild().getTextChannelById(event.getChannel().getId());
        // event.getChannel().deleteMessageById(event.getMessageId()).queue();

        // purgeMessages(channel, 2);
        // event.getChannel().pinMessageById(event.getMessageId()).queue();

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

    public static void generateQuestion(SlashCommandInteractionEvent event) {
        // 0: category
        // 1: question
        // 2: correct_answer
        // 3,4,5 incorrect_answers
        String[] questionDetails = RandomQuestion.getQuestion();
        int[] placement = random4Ints();
        Emoji emojiOne = Emoji.fromUnicode("U+31U+fe0fU+20e3");
        Emoji emojiTwo = Emoji.fromUnicode("U+32U+fe0fU+20e3");
        Emoji emojiThree = Emoji.fromUnicode("U+33U+fe0fU+20e3");
        Emoji emojiFour = Emoji.fromUnicode("U+34U+fe0fU+20e3");

        event.reply(
                "category: " + questionDetails[0] + "\n" +
                        "question: " + questionDetails[1] + "\n" +
                        emojiOne.getFormatted() + ": " + questionDetails[placement[0]] + "\n" +
                        emojiTwo.getFormatted() + ": " + questionDetails[placement[1]] + "\n" +
                        emojiThree.getFormatted() + ": " + questionDetails[placement[2]] + "\n" +
                        emojiFour.getFormatted() + ": " + questionDetails[placement[3]] + "\n"

        ).queue();

    }

    public static int[] random4Ints() {
        int[] outputInt = new int[4];
        List<Integer> inputNumbers = new ArrayList<>();
        inputNumbers.add(2);
        inputNumbers.add(3);
        inputNumbers.add(4);
        inputNumbers.add(5);
        Random rand = new Random();
        for (int a = 0; a < 4; a++) {
            int randomIndex = rand.nextInt(0, inputNumbers.size());
            outputInt[a] = inputNumbers.get(randomIndex);
            inputNumbers.remove(randomIndex);

        }

        return outputInt;
    }
}
