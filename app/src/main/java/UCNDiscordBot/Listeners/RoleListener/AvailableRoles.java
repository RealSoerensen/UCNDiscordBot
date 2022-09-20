package UCNDiscordBot.Listeners.RoleListener;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AvailableRoles {
    public static String getRoles(SlashCommandInteractionEvent event) {
        // return a string of roles bot can interact with excluding @everyone
        String roles = "";
        for (int i = 1; i < event.getGuild().getRoles().size() - 1; i++) {
            // Check if bot can interact with the role and if author has the role
            // add ", " if item isnt the last item
            Role role = event.getGuild().getRoles().get(i);
            if (event.getGuild().getSelfMember().canInteract(role) && event.getMember().canInteract(role)) {
                roles += role.getName() + "\n";
            }
        }
        if (roles.length() == 0) {
            roles = "No roles available";
        }
        return "Available roles: " + roles;
    }
}
