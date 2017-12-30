package fr.quatrevieux.araknemu.data.living.entity.player;

import fr.quatrevieux.araknemu.data.constant.Race;
import fr.quatrevieux.araknemu.data.constant.Sex;
import fr.quatrevieux.araknemu.data.value.Colors;
import fr.quatrevieux.araknemu.game.world.creature.characteristics.Characteristics;

/**
 * Entity class for player
 */
final public class Player {
    final private int id;
    final private int accountId;
    final private int serverId;
    private String name;
    private Race race;
    private Sex sex;
    private Colors colors;
    private int level;
    private Characteristics stats;

    public Player(int id, int accountId, int serverId, String name, Race race, Sex sex, Colors colors, int level, Characteristics stats) {
        this.id = id;
        this.accountId = accountId;
        this.serverId = serverId;
        this.name = name;
        this.race = race;
        this.sex = sex;
        this.colors = colors;
        this.level = level;
        this.stats = stats;
    }

    public Player(int id) {
        this(id, 0, 0, null, null, null, null, 0, null);
    }

    public int id() {
        return id;
    }

    public int accountId() {
        return accountId;
    }

    public int serverId() {
        return serverId;
    }

    public String name() {
        return name;
    }

    public Race race() {
        return race;
    }

    public Sex sex() {
        return sex;
    }

    public Colors colors() {
        return colors;
    }

    public int level() {
        return level;
    }

    public Characteristics stats() {
        return stats;
    }

    /**
     * Create a new player with new race
     *
     * @param newId The new race
     */
    public Player withId(int newId) {
        return new Player(
            newId,
            accountId,
            serverId,
            name,
            race,
            sex,
            colors,
            level,
            stats
        );
    }

    /**
     * Constructor for character creation
     * The player race will be set to -1
     */
    static public Player forCreation(int accountId, int serverId, String name, Race race, Sex sex, Colors colors) {
        return new Player(-1, accountId, serverId, name, race, sex, colors, 1, null);
    }

    /**
     * Get a player for load for entering game
     *
     * @see fr.quatrevieux.araknemu.data.living.repository.player.PlayerRepository#getForGame(Player)
     */
    static public Player forGame(int playerId, int accountId, int serverId) {
        return new Player(playerId, accountId, serverId, null, null, null, null, 0, null);
    }
}
