package UCNDiscordBot.Listeners.RoleListener;

import java.util.List;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ChangeRole {
    public static String addRole(SlashCommandInteractionEvent event, String role) {
        // Check if entered role exist
        List<Role> allGuildRoles = event.getGuild().getRolesByName(role, true);
        if (allGuildRoles.size() == 0) {
            return "The role** " + role + "** doesn't exist";
        }
        Role guildRole = allGuildRoles.get(0);
        // Check if bot can give the role and if the user has the role
        if (event.getGuild().getSelfMember().canInteract(guildRole)) {
            if (!event.getMember().getRoles().contains(guildRole)) {
                event.getGuild().addRoleToMember(event.getMember(), guildRole).queue();
                return "You have been given the role** " + role + "**";
            } else {
                return "You already have the role** " + role + "**";
            }
        } else {
            return "I can't give you the role** " + role + "**";
        }
    }

    public static String removeRole(SlashCommandInteractionEvent event, String role) {
        // Check if entered role exist
        List<Role> allGuildRoles = event.getGuild().getRolesByName(role, true);
        if (allGuildRoles.size() == 0) {
            return "The role** " + role + "** doesn't exist";
        }
        Role guildRole = allGuildRoles.get(0);
        // Check if bot can remove the role and if the user has the role
        if (event.getGuild().getSelfMember().canInteract(guildRole)) {
            if (event.getMember().getRoles().contains(guildRole)) {
                event.getGuild().removeRoleFromMember(event.getMember(), guildRole).queue();
                return "You have been removed from the role** " + role + "**";
            } else {
                return "You don't have the role** " + role + "**";
            }
        } else {
            return "I can't remove you from the role** " + role + "**";
        }
    }
}
