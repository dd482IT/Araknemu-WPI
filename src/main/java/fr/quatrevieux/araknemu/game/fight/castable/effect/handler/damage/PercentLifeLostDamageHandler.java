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

package fr.quatrevieux.araknemu.game.fight.castable.effect.handler.damage;

import fr.quatrevieux.araknemu.game.fight.Fight;
import fr.quatrevieux.araknemu.game.fight.castable.FightCastScope;
import fr.quatrevieux.araknemu.game.fight.castable.effect.EffectValue;
import fr.quatrevieux.araknemu.game.fight.castable.effect.Element;
import fr.quatrevieux.araknemu.game.fight.castable.effect.handler.EffectHandler;
import fr.quatrevieux.araknemu.game.fight.fighter.Fighter;
import fr.quatrevieux.araknemu.game.fight.fighter.FighterLife;

/**
 * Handle damage based on the lost caster life
 * Damage cannot be boosted, but can be reduced using armor because it's related to an element
 * Buffs are also called by this effect
 *
 * This effect works like the opposite of {@link PercentLifeDamageHandler}
 */
public final class PercentLifeLostDamageHandler implements EffectHandler {
    private final DamageApplier applier;

    public PercentLifeLostDamageHandler(Element element, Fight fight) {
        this.applier = new DamageApplier(element, fight);
    }

    @Override
    public void handle(FightCastScope cast, FightCastScope.EffectScope effect) {
        final Fighter caster = cast.caster();
        final FighterLife casterLife = caster.life();
        final int lostLife = casterLife.max() - casterLife.current();

        EffectValue.forEachTargets(effect.effect(), caster, effect.targets(), (target, effectValue) -> {
            applier.applyFixed(caster, lostLife * effectValue.value() / 100, target);
        });
    }

    @Override
    public void buff(FightCastScope cast, FightCastScope.EffectScope effect) {
        throw new UnsupportedOperationException("Percent life lost effect do not support buffs");
    }
}
