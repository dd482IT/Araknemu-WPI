package fr.quatrevieux.araknemu.game.listener.player.inventory;

import fr.quatrevieux.araknemu.core.di.ContainerException;
import fr.quatrevieux.araknemu.core.event.DefaultListenerAggregate;
import fr.quatrevieux.araknemu.core.event.ListenerAggregate;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.item.ItemService;
import fr.quatrevieux.araknemu.game.player.GamePlayer;
import fr.quatrevieux.araknemu.game.player.characteristic.event.CharacteristicsChanged;
import fr.quatrevieux.araknemu.game.player.event.GameJoined;
import fr.quatrevieux.araknemu.game.player.inventory.InventoryEntry;
import fr.quatrevieux.araknemu.game.player.inventory.event.ObjectAdded;
import fr.quatrevieux.araknemu.game.player.inventory.event.ObjectDeleted;
import fr.quatrevieux.araknemu.game.player.inventory.event.ObjectQuantityChanged;
import fr.quatrevieux.araknemu.network.game.out.object.InventoryWeight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SendWeightTest extends GameBaseCase {
    private ListenerAggregate dispatcher;
    private GamePlayer player;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet
            .pushItemTemplates()
            .pushItemSets()
        ;

        player = gamePlayer();

        dispatcher = new DefaultListenerAggregate();
        dispatcher.register(new SendWeight(player));

        requestStack.clear();
    }

    @Test
    void onGameJoined() {
        dispatcher.dispatch(new GameJoined());

        requestStack.assertLast(new InventoryWeight(player));
    }

    @Test
    void onCharacteristicsChanged() {
        dispatcher.dispatch(new CharacteristicsChanged());

        requestStack.assertLast(new InventoryWeight(player));
    }

    @Test
    void onObjectQuantityChanged() throws ContainerException {
        InventoryEntry entry = player.inventory().add(container.get(ItemService.class).create(2411));

        dispatcher.dispatch(new ObjectQuantityChanged(entry));

        requestStack.assertLast(new InventoryWeight(player));
    }

    @Test
    void onObjectAdded() throws ContainerException {
        InventoryEntry entry = player.inventory().add(container.get(ItemService.class).create(2411));

        dispatcher.dispatch(new ObjectAdded(entry));

        requestStack.assertLast(new InventoryWeight(player));
    }

    @Test
    void onObjectDeleted() throws ContainerException {
        InventoryEntry entry = player.inventory().add(container.get(ItemService.class).create(2411));

        dispatcher.dispatch(new ObjectDeleted(entry));

        requestStack.assertLast(new InventoryWeight(player));
    }
}
