/*
 * This file is part of Araknemu.
 *
 * Araknemu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Araknemu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Araknemu.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2017-2019 Vincent Quatrevieux
 */

package fr.quatrevieux.araknemu.game.fight.ending.reward.drop.action;

import fr.quatrevieux.araknemu.game.fight.ending.reward.drop.DropReward;
import fr.quatrevieux.araknemu.game.fight.fighter.Fighter;
import fr.quatrevieux.araknemu.game.fight.fighter.operation.FighterOperation;
import fr.quatrevieux.araknemu.game.fight.fighter.player.PlayerFighter;
import fr.quatrevieux.araknemu.game.item.ItemService;
import fr.quatrevieux.araknemu.game.player.inventory.PlayerInventory;

/**
 * Add the dropped items to the fighter
 */
public final class AddItems implements DropRewardAction {
    private final ItemService service;

    public AddItems(ItemService service) {
        this.service = service;
    }

    @Override
    public void apply(DropReward reward, Fighter fighter) {
        fighter.apply(new FighterOperation() {
            @Override
            public void onPlayer(PlayerFighter fighter) {
                final PlayerInventory inventory = fighter.player().inventory();

                reward.items().forEach((itemId, quantity) -> {
                    for (int q = 0; q < quantity; ++q) {
                        inventory.add(service.create(itemId));
                    }
                });
            }
        });
    }
}
