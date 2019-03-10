package fr.quatrevieux.araknemu.game.player;

import fr.quatrevieux.araknemu.core.event.DefaultListenerAggregate;
import fr.quatrevieux.araknemu.data.constant.Characteristic;
import fr.quatrevieux.araknemu.data.constant.Race;
import fr.quatrevieux.araknemu.data.constant.Sex;
import fr.quatrevieux.araknemu.data.living.entity.account.Account;
import fr.quatrevieux.araknemu.data.living.entity.player.Player;
import fr.quatrevieux.araknemu.data.living.entity.player.PlayerItem;
import fr.quatrevieux.araknemu.data.living.entity.player.PlayerSpell;
import fr.quatrevieux.araknemu.data.value.Colors;
import fr.quatrevieux.araknemu.data.value.Position;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.account.AccountService;
import fr.quatrevieux.araknemu.game.account.GameAccount;
import fr.quatrevieux.araknemu.game.chat.ChannelType;
import fr.quatrevieux.araknemu.game.player.characteristic.PlayerLife;
import fr.quatrevieux.araknemu.game.player.experience.GamePlayerExperience;
import fr.quatrevieux.araknemu.game.player.experience.PlayerExperienceService;
import fr.quatrevieux.araknemu.game.player.inventory.InventoryService;
import fr.quatrevieux.araknemu.game.player.race.PlayerRaceService;
import fr.quatrevieux.araknemu.game.player.spell.SpellBook;
import fr.quatrevieux.araknemu.game.player.spell.SpellBookService;
import fr.quatrevieux.araknemu.game.world.creature.characteristics.DefaultCharacteristics;
import fr.quatrevieux.araknemu.game.world.creature.characteristics.MutableCharacteristics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.NOPLogger;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDataTest  extends GameBaseCase {
    private GamePlayer player;
    private Player entity;
    private DefaultListenerAggregate dispatcher;
    private PlayerData data;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet
            .pushMaps()
            .pushRaces()
            .pushSpells()
            .pushExperience()
            .use(PlayerItem.class)
            .use(PlayerSpell.class)
        ;

        container.get(PlayerExperienceService.class).preload(NOPLogger.NOP_LOGGER);

        MutableCharacteristics characteristics = new DefaultCharacteristics();

        characteristics.set(Characteristic.INTELLIGENCE, 150);
        characteristics.set(Characteristic.VITALITY, 50);

        entity = dataSet.push(new Player(5, 2, 1, "Other", Race.CRA, Sex.MALE, new Colors(-1, -1, -1), 50, characteristics, new Position(10300, 308), EnumSet.allOf(ChannelType.class), 0, 0, -1, 5450000, new Position(10540, 210), 0));

        SpellBook spellBook = container.get(SpellBookService.class).load(session, entity);
        GamePlayerExperience experience = container.get(PlayerExperienceService.class).load(session, entity);

        player = new GamePlayer(
            new GameAccount(
                new Account(2),
                container.get(AccountService.class),
                1
            ),
            entity,
            container.get(PlayerRaceService.class).get(Race.CRA),
            session,
            container.get(PlayerService.class),
            container.get(InventoryService.class).load(entity),
            spellBook,
            experience
        );

        dispatcher = new DefaultListenerAggregate();
        data = new PlayerData(dispatcher, player, entity, spellBook, experience);
    }

    @Test
    void characteristics() {
        data.build();

        assertEquals(150, data.characteristics().get(Characteristic.INTELLIGENCE));
        assertEquals(3, data.characteristics().get(Characteristic.MOVEMENT_POINT));
        assertEquals(6, data.characteristics().get(Characteristic.ACTION_POINT));
    }

    @Test
    void life() {
        data.build();

        assertInstanceOf(PlayerLife.class, data.life());
        assertEquals(345, data.life().current());
        assertEquals(345, data.life().max());

        player.entity().setLife(10);
        assertEquals(10, data.life().current());
    }

    @Test
    void lifeBuildMoreThanMax() {
        player.entity().setLife(100000);
        data.build();

        assertInstanceOf(PlayerLife.class, data.life());

        assertEquals(345, data.life().current());
        assertEquals(345, data.life().max());
    }

    @Test
    void experience() {
        assertEquals(50, data.experience().level());
        assertEquals(5350000, data.experience().min());
        assertEquals(5450000, data.experience().current());
        assertEquals(5860000, data.experience().max());
    }

    @Test
    void spells() {
        assertSame(player.properties().spells(), data.spells());
    }

    @Test
    void kamas() {
        entity.setKamas(1456);

        assertEquals(1456, data.kamas());
    }
}
