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

package fr.quatrevieux.araknemu.game.fight.ai.factory;

import fr.quatrevieux.araknemu.game.fight.ai.AI;
import fr.quatrevieux.araknemu.game.fight.ai.FighterAI;
import fr.quatrevieux.araknemu.game.fight.ai.action.builder.GeneratorBuilder;
import fr.quatrevieux.araknemu.game.fight.fighter.Fighter;

import java.util.Optional;

/**
 * AiFactory implementation using a generator builder for configure the AI
 */
public abstract class AbstractAiBuilderFactory implements AiFactory<Fighter> {
    /**
     * Configure the AI generator
     */
    protected void configure(GeneratorBuilder<Fighter> builder) {
        // To implements if configure(GeneratorBuilder, Fighter) is not implemented
    }

    /**
     * Configure the AI generator
     */
    public void configure(GeneratorBuilder<Fighter> builder, Fighter fighter) {
        configure(builder);
    }

    @Override
    public final Optional<AI<Fighter>> create(Fighter fighter) {
        final GeneratorBuilder<Fighter> builder = new GeneratorBuilder<>();

        configure(builder, fighter);

        return Optional.of(new FighterAI(fighter, fighter.fight(), builder.build()));
    }
}
