package fr.quatrevieux.araknemu.game.player;

import fr.quatrevieux.araknemu.data.constant.Sex;
import fr.quatrevieux.araknemu.data.living.entity.player.Player;
import fr.quatrevieux.araknemu.data.value.Colors;
import fr.quatrevieux.araknemu.data.value.Position;
import fr.quatrevieux.araknemu.game.account.GameAccount;
import fr.quatrevieux.araknemu.game.chat.ChannelSet;
import fr.quatrevieux.araknemu.game.chat.ChannelType;
import fr.quatrevieux.araknemu.game.event.DefaultListenerAggregate;
import fr.quatrevieux.araknemu.game.event.Dispatcher;
import fr.quatrevieux.araknemu.game.event.ListenerAggregate;
import fr.quatrevieux.araknemu.game.exploration.ExplorationPlayer;
import fr.quatrevieux.araknemu.game.fight.fighter.PlayerFighter;
import fr.quatrevieux.araknemu.game.player.characteristic.Life;
import fr.quatrevieux.araknemu.game.player.characteristic.PlayerCharacteristics;
import fr.quatrevieux.araknemu.game.player.experience.GamePlayerExperience;
import fr.quatrevieux.araknemu.game.player.inventory.PlayerInventory;
import fr.quatrevieux.araknemu.game.player.race.GamePlayerRace;
import fr.quatrevieux.araknemu.game.player.spell.SpellBook;
import fr.quatrevieux.araknemu.network.game.GameSession;

import java.util.Set;

/**
 * GamePlayer object
 * A player is a logged character, with associated game session
 */
final public class GamePlayer extends AbstractCharacter implements Dispatcher, PlayerData {
    final private PlayerService service;
    final private GameSession session;
    final private GamePlayerRace race;
    final private PlayerCharacteristics characteristics;
    final private Set<ChannelType> channels;
    final private PlayerInventory inventory;
    final private Life life;
    final private SpellBook spells;
    final private GamePlayerExperience experience;

    final private ListenerAggregate dispatcher = new DefaultListenerAggregate();

    public GamePlayer(GameAccount account, Player entity, GamePlayerRace race, GameSession session, PlayerService service, PlayerInventory inventory, SpellBook spells, GamePlayerExperience experience) {
        super(account, entity);

        this.race = race;
        this.session = session;
        this.service = service;
        this.spells = spells;
        this.experience = experience;
        this.channels = new ChannelSet(entity.channels(), dispatcher);
        this.inventory = inventory.attach(this);

        characteristics = new PlayerCharacteristics(dispatcher, this, entity);
        characteristics.rebuildSpecialEffects();

        life = new Life(this, entity);
    }

    @Override
    public void print(Printer printer) {
        super.print(printer);
        printer.accessories(inventory.accessories());
    }

    @Override
    public void dispatch(Object event) {
        session.dispatch(event);
    }

    public ListenerAggregate dispatcher() {
        return dispatcher;
    }

    @Override
    public PlayerCharacteristics characteristics() {
        return characteristics;
    }

    /**
     * Send a packet to the player
     */
    public void send(Object packet) {
        session.write(packet);
    }

    /**
     * Get the current player position
     */
    public Position position() {
        return entity.position();
    }

    /**
     * Set new position
     */
    public void setPosition(Position position) {
        entity.setPosition(position);
    }

    /**
     * Get the player race
     */
    public GamePlayerRace race() {
        return race;
    }

    public String name() {
        return entity.name();
    }

    /**
     * Get channels subscriptions
     */
    public Set<ChannelType> subscriptions() {
        return channels;
    }

    /**
     * Check if the player is exploring
     */
    public boolean isExploring() {
        return session.exploration() != null;
    }

    /**
     * Get the exploration player
     *
     * @throws IllegalStateException When the player is not on exploration state
     */
    public ExplorationPlayer exploration() {
        if (!isExploring()) {
            throw new IllegalStateException("The current player is not an exploration state");
        }

        return session.exploration();
    }

    /**
     * Remove the exploration player
     */
    public void stopExploring() {
        if (!isExploring()) {
            throw new IllegalStateException("The current player is not an exploration state");
        }

        session.setExploration(null);
    }

    /**
     * Attach a fighter to the player
     */
    public void attachFighter(PlayerFighter fighter) {
        if (isFighting()) {
            throw new IllegalStateException("The current player is already in fight");
        }

        session.setFighter(fighter);
    }

    /**
     * Get the attached fighter
     */
    public PlayerFighter fighter() {
        if (!isFighting()) {
            throw new IllegalStateException("The current player is not in fight");
        }

        return session.fighter();
    }

    /**
     * Check if the current player is in fight
     */
    public boolean isFighting() {
        return session.fighter() != null;
    }

    /**
     * Save the player
     */
    public void save() {
        service.save(this);
    }

    public PlayerInventory inventory() {
        return inventory;
    }

    @Override
    public Life life() {
        return life;
    }

    @Override
    public GamePlayerExperience experience() {
        return experience;
    }

    public SpellBook spells() {
        return spells;
    }

    /**
     * Get the player GfxId
     *
     * @todo refactor
     */
    public String gfxId() {
        return Integer.toString(10 * entity.race().ordinal() + entity.sex().ordinal()) + "^100x100";
    }

    /**
     * Get the player sex
     */
    public Sex sex() {
        return entity.sex();
    }

    /**
     * Get player colors
     */
    public Colors colors() {
        return entity.colors();
    }
}
