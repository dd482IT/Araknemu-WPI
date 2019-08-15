package fr.quatrevieux.araknemu.game.item.inventory.event;

import fr.quatrevieux.araknemu.game.item.inventory.ItemEntry;

/**
 * An object is added to the inventory
 */
final public class ObjectAdded {
    final private ItemEntry entry;

    public ObjectAdded(ItemEntry entry) {
        this.entry = entry;
    }

    public ItemEntry entry() {
        return entry;
    }
}