package UCNDiscordBot.Listeners.RoleListener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChangeRole {
    public static void giveRole(MessageReceivedEvent event, String role) {
        // Check if bot can give the role and if the user has the role
        if (event.getGuild().getSelfMember().canInteract(event.getGuild().getRolesByName(role, true).get(0))) {
            if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName(role, true).get(0))) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName(role, true).get(0))
                        .queue();
                event.getChannel().sendMessage("You have been given the role").queue();
            } else {
                event.getChannel().sendMessage("You already have the role").queue();
            }
        } else {
            event.getChannel().sendMessage("I can't give you the role").queue();
        }
    }

    public static void removeRole(MessageReceivedEvent event, String role) {
        // Check if bot can remove the role and if the user has the role
        if (event.getGuild().getSelfMember().canInteract(event.getGuild().getRolesByName(role, true).get(0))) {
            if (event.getMember().getRoles().contains(event.getGuild().getRolesByName(role, true).get(0))) {
                event.getGuild()
                        .removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(role, true).get(0))
                        .queue();
                event.getChannel().sendMessage("You have been removed from the role").queue();
            } else {
                event.getChannel().sendMessage("You don't have the role").queue();
            }
        } else {
            event.getChannel().sendMessage("I can't remove you from the role").queue();
        }
    }
}
