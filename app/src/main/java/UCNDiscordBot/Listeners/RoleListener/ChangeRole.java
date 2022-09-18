package UCNDiscordBot.Listeners.RoleListener;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangeRole {
    public static void giveRole(SlashCommandInteractionEvent event, String role) {
        // Check if bot can give the role and if the user has the role
        String rolen = event.getOption("role").getAsString();
        if (event.getGuild().getSelfMember().canInteract(event.getGuild().getRolesByName(role, true).get(0))) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName(role, true).get(0))) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName(role, true).get(0))
                        .queue();
                event.reply("You have been given the role** " + rolen + "**").setEphemeral(true).queue();
            } else {
                event.reply("You already have the role** " + rolen + "**").setEphemeral(true).queue();
            }
        } else {
            event.reply("I can't give you the role** " + rolen + "**").setEphemeral(true).queue();
        }
    }

    public static void removeRole(SlashCommandInteractionEvent event, String role) {
        // Check if bot can remove the role and if the user has the role
        String rolen = event.getOption("role").getAsString();
        if (event.getGuild().getSelfMember().canInteract(event.getGuild().getRolesByName(role, true).get(0))) {
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName(role, true).get(0))) {
                event.getGuild()
                        .removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(role, true).get(0))
                        .queue();
                event.reply("You have been removed from the role** " + rolen + "**").setEphemeral(true).queue();
            } else {
                event.reply("You don't have the role** " + rolen + "**").setEphemeral(true).queue();
            }
        } else {
            event.reply("I can't remove you from the role** " + rolen + "**").setEphemeral(true).queue();
        }
    }
}
