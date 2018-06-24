import java.util.concurrent.atomic.AtomicReference

/**
 * @Author: zhengye.zhang
 * @Description:
 * @Date: 2018/6/24 上午11:21
 */
class Player {
    def userId
    def team
    def username
    AtomicReference aggregatedScore = new AtomicReference(new AggregatedScore());
    AtomicReference aggregatedAchievements = new AtomicReference(new AggregatedAchievements());

    Player(userId, team, username) {
        this.userId = userId
        this.team = team
        this.username = username
    }

    Player(team) {
        this(UUID.randomUUID().toString(), team, UserNameGenerator.generate())
    }

    Map toMap() {
        return [
                'userId'  : userId,
                'team'    : team,
                'username': username
        ]
    }
}
