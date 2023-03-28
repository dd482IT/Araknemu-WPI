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
 * Copyright (c) 2017-2021 Vincent Quatrevieux
 */

package fr.quatrevieux.araknemu.game.listener.fight;

import fr.quatrevieux.araknemu.game.fight.Fight;
import fr.quatrevieux.araknemu.game.fight.FightBaseCase;
import fr.quatrevieux.araknemu.game.fight.team.event.AllowJoinTeamChanged;
import fr.quatrevieux.araknemu.game.fight.team.event.AllowSpectatorChanged;
import fr.quatrevieux.araknemu.network.game.out.info.Information;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SendJoinTeamOptionChangedMessageTest extends FightBaseCase {
    private SendJoinTeamOptionChangedMessage listener;
    private Fight fight;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        fight = createFight();
        listener = new SendJoinTeamOptionChangedMessage();
    }

    void onlySendToTeam() {
        requestStack.clear();
        listener.on(new AllowJoinTeamChanged(fight.team(1).options(), true));
        requestStack.assertEmpty();
    }

    void onAllow() {
        listener.on(new AllowJoinTeamChanged(fight.team(0).options(), true));
        requestStack.assertLast(Information.joinTeamReleased());
    }

    void onBlock() {
        listener.on(new AllowJoinTeamChanged(fight.team(0).options(), false));
        requestStack.assertLast(Information.joinTeamLocked());
    }
}
