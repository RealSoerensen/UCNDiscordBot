package UCNDiscordBot.Functions;

import java.util.Random;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiceRoller {

    public static void rollXDXSend(MessageReceivedEvent event){
        String message = event.getMessage().getContentDisplay();
        String outputString = "";
        String[] arg;
        int diceAmount = 0;
        int diceSize = 0;
        int[] values;
        double avg = 0;
        //example string: !roll 2d6
        //example string: !roll 4d8
        //find index of "d"
        try{
            if(message.equals("!roll")){
                //roll 6-sided dice
                outputString += rollDX(6);
                event.getChannel().sendMessage(outputString).queue();
            }
            else{
                arg = message.split(" ");
                arg = arg[1].split("d");
                diceAmount = Integer.parseInt(arg[0]);
                diceSize  = Integer.parseInt(arg[1]);
                values = rollXDX(diceAmount, diceSize);
                if(diceAmount > 100000){
                    event.getChannel().sendMessage("Total amount of dice cant be larger than: 10000").queue();
                    
                }
                else{
                    for(int i = 0;i<values.length-1;i++){
                        if(i == 0){
                            outputString += values[i];
                        }
                        else{
                            outputString += " + "+ values[i];
                        }
                        //System.out.println(i);
                    }
                    outputString += " = " + values[values.length-1];
                    
                    if(outputString.length() >= 2000){
                        avg = (double)values[values.length-1]/((double)values.length-1);
                        event.getChannel().sendMessage("Total: " + values[values.length-1] + " - Avg: " + avg).queue();
                    }
                    else{
                        event.getChannel().sendMessage(outputString).queue();
                    }
                }
                
                
            }
        }
        catch(Exception e) {
            event.getChannel().sendMessage("not a diceroll or too many characters").queue();
        }
         
    }

    public static int rollDX(int diceSize){
        Random random = new Random();
        return random.nextInt(1,diceSize+1);
    }

    public static int[] rollXDX(int diceAmount, int diceSize){
        int[] values = new int[diceAmount+1];
        int total = 0;

        for(int i = 0;i<diceAmount;i++){
            values[i] = rollDX(diceSize);
            total += values[i];
        }
        values[values.length-1] = total;

        return values;
    }
    public int rollXDXSum(int diceAmount, int diceSize){
        int[] values = rollXDX( diceAmount, diceSize);
        return values[values.length-1];
    }
    
}
