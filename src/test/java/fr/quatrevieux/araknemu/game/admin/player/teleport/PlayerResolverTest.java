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
 * Copyright (c) 2017-2020 Vincent Quatrevieux
 */

package fr.quatrevieux.araknemu.game.admin.player.teleport;

import fr.quatrevieux.araknemu.game.GameBaseCase;
import fr.quatrevieux.araknemu.game.exploration.ExplorationPlayer;
import fr.quatrevieux.araknemu.game.exploration.map.ExplorationMapService;
import fr.quatrevieux.araknemu.game.player.GamePlayer;
import fr.quatrevieux.araknemu.game.player.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerResolverTest extends GameBaseCase {
    private PlayerResolver resolver;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        dataSet
            .pushSubAreas()
            .pushAreas()
            .pushMaps()
        ;

        resolver = new PlayerResolver(container.get(PlayerService.class), container.get(ExplorationMapService.class));
    }

    @Test
    void resolveNotExploring() throws Exception {
        GamePlayer player = makeOtherPlayer();
        Target target = new Target(explorationPlayer().map(), 123);

        resolver.resolve(player.name(), target);

        assertEquals(123, target.cell());
        assertEquals(container.get(ExplorationMapService.class).load(10540), target.map());
    }

    @Test
    void resolveExploringNotOnMap() throws Exception {
        ExplorationPlayer player = makeOtherExplorationPlayer();
        Target target = new Target(explorationPlayer().map(), 123);

        resolver.resolve(player.player().name(), target);

        assertEquals(123, target.cell());
        assertEquals(container.get(ExplorationMapService.class).load(10540), target.map());
    }

    @Test
    void resolveExploring() throws Exception {
        ExplorationPlayer player = makeOtherExplorationPlayer();
        player.changeMap(container.get(ExplorationMapService.class).load(10340), 250);
        Target target = new Target(explorationPlayer().map(), 123);

        resolver.resolve(player.player().name(), target);

        assertEquals(123, target.cell());
        assertEquals(container.get(ExplorationMapService.class).load(10340), target.map());
    }

    @Test
    void resolveNotFound() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve("not found", new Target(null, 0)));
    }
}
