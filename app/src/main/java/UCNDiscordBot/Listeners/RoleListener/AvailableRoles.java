package UCNDiscordBot.Listeners.RoleListener;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AvailableRoles {
    public static String getRoles(MessageReceivedEvent event) {
        // return a string of roles bot can interact with excluding @everyone
        String roles = "";
        for (int i = 1; i < event.getGuild().getRoles().size() - 1; i++) {
            // Check if bot can interact with the role and if author has the role
            if (event.getGuild().getSelfMember().canInteract(event.getGuild().getRoles().get(i))) {
                roles += event.getGuild().getRoles().get(i).getName();
            }
            // add a comma if not first
            if (i < event.getGuild().getRoles().size() - 2 && event.getGuild().getSelfMember()
                    .canInteract(event.getGuild().getRoles().get(i))) {
                roles += ", ";
            }
        }
        if (roles.length() == 0) {
            roles = "No roles available";
        }
        return "Available roles: " + roles;
    }
}
