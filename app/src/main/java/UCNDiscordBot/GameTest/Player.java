package UCNDiscordBot.GameTest;

import net.dv8tion.jda.api.entities.User;

public class Player {
    private User user;
    private int score;
    private boolean isLeader;

    public Player(User user, boolean leader) {
        this.user = user;
        score = 0;
        isLeader = leader;
    }

    public User getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public void setName(User user) {
        this.user = user;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void scoreIncrease() {
        score += 1;
    }
}
