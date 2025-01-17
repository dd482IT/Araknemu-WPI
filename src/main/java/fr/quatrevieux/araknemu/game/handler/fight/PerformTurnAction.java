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

package fr.quatrevieux.araknemu.game.handler.fight;

import fr.quatrevieux.araknemu.core.network.exception.ErrorPacket;
import fr.quatrevieux.araknemu.core.network.parser.PacketHandler;
import fr.quatrevieux.araknemu.game.fight.fighter.Fighter;
import fr.quatrevieux.araknemu.game.fight.turn.FightTurn;
import fr.quatrevieux.araknemu.game.fight.turn.action.ActionType;
import fr.quatrevieux.araknemu.network.game.GameSession;
import fr.quatrevieux.araknemu.network.game.in.game.action.GameActionRequest;
import fr.quatrevieux.araknemu.network.game.out.fight.action.NoneAction;
import org.checkerframework.checker.nullness.util.NullnessUtil;

/**
 * Perform a game action into the fight
 */
public final class PerformTurnAction implements PacketHandler<GameSession, GameActionRequest> {
    @Override
    public void handle(GameSession session, GameActionRequest packet) {
        try {
            final Fighter fighter = NullnessUtil.castNonNull(session.fighter());
            final FightTurn turn = fighter.turn();

            turn.perform(fighter.fight().actions().create(fighter, ActionType.byId(packet.type()), packet.arguments()));
        } catch (Exception e) {
            throw new ErrorPacket(new NoneAction(), e);
        }
    }

    @Override
    public Class<GameActionRequest> packet() {
        return GameActionRequest.class;
    }
}
