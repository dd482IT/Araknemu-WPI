package fr.quatrevieux.araknemu.game.monster.environment;

import fr.quatrevieux.araknemu.data.value.Interval;
import fr.quatrevieux.araknemu.data.value.Position;
import fr.quatrevieux.araknemu.data.world.entity.monster.MonsterGroupData;
import fr.quatrevieux.araknemu.data.world.entity.monster.MonsterGroupPosition;
import fr.quatrevieux.araknemu.data.world.repository.monster.MonsterGroupDataRepository;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.exploration.ExplorationPlayer;
import fr.quatrevieux.araknemu.game.exploration.map.ExplorationMap;
import fr.quatrevieux.araknemu.game.exploration.map.ExplorationMapService;
import fr.quatrevieux.araknemu.game.fight.Fight;
import fr.quatrevieux.araknemu.game.fight.FightService;
import fr.quatrevieux.araknemu.game.fight.fighter.monster.MonsterFighter;
import fr.quatrevieux.araknemu.game.fight.type.PvmType;
import fr.quatrevieux.araknemu.game.monster.group.MonsterGroup;
import fr.quatrevieux.araknemu.game.monster.group.MonsterGroupFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LivingMonsterGroupPositionTest extends GameBaseCase {
    private ExplorationMap map;
    private LivingMonsterGroupPosition monsterGroupPosition;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet
            .pushMaps()
            .pushMonsterTemplates()
            .pushMonsterGroups()
            .pushMonsterSpells()
        ;

        map = container.get(ExplorationMapService.class).load(10340);

        monsterGroupPosition = new LivingMonsterGroupPosition(
            container.get(MonsterGroupFactory.class),
            container.get(FightService.class),
            new MonsterGroupPosition(new Position(10340, -1), 1),
            container.get(MonsterGroupDataRepository.class).get(1)
        );
    }

    @Test
    void populate() {
        assertCount(0, map.creatures());

        monsterGroupPosition.populate(map);
        assertCount(2, map.creatures());

        monsterGroupPosition.populate(map);
        assertCount(2, map.creatures());
    }

    @Test
    void available() {
        monsterGroupPosition.populate(map);

        assertArrayEquals(
            map.creatures().toArray(),
            monsterGroupPosition.available().toArray()
        );
    }

    @Test
    void spawn() {
        monsterGroupPosition.populate(map);
        assertCount(2, monsterGroupPosition.available());

        monsterGroupPosition.spawn();
        assertCount(3, monsterGroupPosition.available());
    }

    @Test
    void fixedGroup() {
        monsterGroupPosition = new LivingMonsterGroupPosition(
            container.get(MonsterGroupFactory.class),
            container.get(FightService.class),
            new MonsterGroupPosition(new Position(10340, 123), 3),
            new MonsterGroupData(3, 0, 0, 1, Arrays.asList(
                new MonsterGroupData.Monster(36, new Interval(5, 5)),
                new MonsterGroupData.Monster(31, new Interval(2, 2))
            ), "")
        );

        monsterGroupPosition.populate(map);

        assertEquals(123, monsterGroupPosition.cell());

        assertCount(1, monsterGroupPosition.available());
        MonsterGroup group = monsterGroupPosition.available().get(0);

        assertEquals(123, group.cell());
        assertEquals(36, group.monsters().get(0).id());
        assertEquals(5, group.monsters().get(0).level());
        assertEquals(31, group.monsters().get(1).id());
        assertEquals(2, group.monsters().get(1).level());
    }

    @Test
    void cell() {
        monsterGroupPosition.populate(map);

        assertNotEquals(monsterGroupPosition.cell(), monsterGroupPosition.cell());
        assertTrue(map.get(monsterGroupPosition.cell()).free());
    }

    @Test
    void startFight() throws SQLException {
        monsterGroupPosition.populate(map);

        ExplorationPlayer player = explorationPlayer();
        player.join(map);

        MonsterGroup group = monsterGroupPosition.available().get(0);
        Fight fight = monsterGroupPosition.startFight(group, player);

        assertFalse(map.creatures().contains(group));
        assertFalse(map.creatures().contains(player));

        assertCount(group.monsters().size() + 1, fight.fighters());
        assertContainsType(MonsterFighter.class, fight.fighters());
        assertContains(player.player().fighter(), fight.fighters());
        assertInstanceOf(PvmType.class, fight.type());
    }
}