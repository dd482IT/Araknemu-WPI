package fr.quatrevieux.araknemu.data.living.repository.implementation.sql;

import fr.quatrevieux.araknemu.core.dbal.repository.EntityNotFoundException;
import fr.quatrevieux.araknemu.data.constant.Effect;
import fr.quatrevieux.araknemu.data.living.entity.player.Player;
import fr.quatrevieux.araknemu.data.living.entity.player.PlayerItem;
import fr.quatrevieux.araknemu.data.value.ItemTemplateEffectEntry;
import fr.quatrevieux.araknemu.data.world.transformer.ItemEffectsTransformer;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PlayerItemRepositoryTest extends GameBaseCase {
    private PlayerItemRepository repository;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet.use(PlayerItem.class);

        repository = new PlayerItemRepository(
            app.database().get("game"),
            new ItemEffectsTransformer()
        );
    }

    @Test
    void getNotFound() {
        assertThrows(EntityNotFoundException.class, () -> repository.get(new PlayerItem(123, 123, 0, null, 0, 0)));
    }

    @Test
    void addAndGet() {
        PlayerItem item = repository.add(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        );

        item = repository.get(item);

        assertEquals(1, item.playerId());
        assertEquals(3, item.entryId());
        assertEquals(39, item.itemTemplateId());
        assertEquals(-1, item.position());
        assertEquals(5, item.quantity());
    }

    @Test
    void has() {
        PlayerItem item = repository.add(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        );

        assertTrue(repository.has(item));
        assertFalse(repository.has(new PlayerItem(0, 0, 0, null, 0, 0)));
    }

    @Test
    void update() {
        PlayerItem item = repository.add(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        );

        item.setPosition(0);
        item.setQuantity(1);

        repository.update(item);

        item = repository.get(item);

        assertEquals(1, item.playerId());
        assertEquals(3, item.entryId());
        assertEquals(39, item.itemTemplateId());
        assertEquals(0, item.position());
        assertEquals(1, item.quantity());
    }

    @Test
    void updateNotFound() {
        assertThrows(EntityNotFoundException.class, () -> repository.update(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        ));
    }

    @Test
    void delete() {
        PlayerItem item = repository.add(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        );

        repository.delete(item);

        assertFalse(repository.has(item));
    }

    @Test
    void deleteNotFound() {
        assertThrows(EntityNotFoundException.class, () -> repository.delete(
            new PlayerItem(
                1,
                3,
                39,
                Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
                5,
                -1
            )
        ));
    }

    @Test
    void byPlayer() {
        repository.add(new PlayerItem(
            1,
            3,
            39,
            Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
            1,
            0
        ));

        repository.add(new PlayerItem(
            1,
            5,
            39,
            Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
            5,
            -1
        ));

        repository.add(new PlayerItem(
            5,
            6,
            123,
            Arrays.asList(new ItemTemplateEffectEntry(Effect.ADD_INTELLIGENCE, 2, 0, 0, "")),
            2,
            1
        ));

        Collection<PlayerItem> items = repository.byPlayer(new Player(1));

        assertCount(2, items);
    }
}