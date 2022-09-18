package UCNDiscordBot.Listeners.RoleListener;

import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PlayerCount {
    public static HashMap getRoleCount(SlashCommandInteractionEvent event) {
        // return a hashmap of roles bot can interact with excluding @everyone
        HashMap<String, Integer> roleCount = new HashMap<String, Integer>();
        // get all roles
        List<Role> roles = event.getGuild().getRoles();
        // loop through all roles
        for (Role role : roles) {
            // exclude the @everyone role, op and UCN Bot
            if (!role.getName().equals("@everyone") && !role.getName().equals("OP")
                    && !role.getName().equals("UCN Bot")) {
                // get the number of members with the role
                int count = event.getGuild().getMembersWithRoles(role).size();
                // add the role and count to the hashmap
                roleCount.put(role.getName(), count);
            }
        }
        return roleCount;
    }
}
