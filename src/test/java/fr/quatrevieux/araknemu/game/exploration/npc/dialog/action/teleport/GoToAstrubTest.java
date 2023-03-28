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

package fr.quatrevieux.araknemu.game.exploration.npc.dialog.action.teleport;

import fr.quatrevieux.araknemu.data.world.entity.environment.npc.ResponseAction;
import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.exploration.ExplorationPlayer;
import fr.quatrevieux.araknemu.game.exploration.interaction.action.ActionType;
import fr.quatrevieux.araknemu.game.exploration.map.ExplorationMapService;
import fr.quatrevieux.araknemu.network.game.out.game.MapData;
import fr.quatrevieux.araknemu.network.game.out.game.action.GameActionResponse;
import fr.quatrevieux.araknemu.network.game.out.info.Information;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GoToAstrubTest extends GameBaseCase {
    private ExplorationPlayer player;
    private ExplorationMapService service;
    private GoToAstrub.Factory factory;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        service = container.get(ExplorationMapService.class);
        player = explorationPlayer();
        factory = new GoToAstrub.Factory(service);

        requestStack.clear();
    }

    void factory() {
        assertInstanceOf(GoToAstrub.class, factory.create(new ResponseAction(1, "", "")));
    }

    void check() {
        assertTrue(factory.create(new ResponseAction(1, "", "")).check(player));
    }

    void apply() {
        factory.create(new ResponseAction(1, "", "")).apply(player);

        assertEquals(player.player().race().astrubPosition(), player.position());
        assertEquals(player.player().race().astrubPosition(), player.player().savedPosition());

        assertEquals(10340, player.map().id());
        assertEquals(250, player.cell().id());

        requestStack.assertAll(
            new MapData(player.map()),
            new GameActionResponse("", ActionType.CHANGE_MAP, player.id(), "7"),
            Information.positionSaved()
        );
    }
}
