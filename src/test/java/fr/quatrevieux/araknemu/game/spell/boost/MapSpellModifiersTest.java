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

package fr.quatrevieux.araknemu.game.spell.boost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapSpellModifiersTest {
    private Map<SpellsBoosts.Modifier, Integer> map;
    private MapSpellModifiers modifiers;

    void setUp() {
        map = new HashMap<>();
        modifiers = new MapSpellModifiers(5, map);
    }

    void spellId() {
        assertEquals(5, modifiers.spellId());
    }

    void valueNotFound() {
        assertEquals(0, modifiers.value(SpellsBoosts.Modifier.DAMAGE));
    }

    void valueFound() {
        map.put(SpellsBoosts.Modifier.DAMAGE, 5);

        assertEquals(5, modifiers.value(SpellsBoosts.Modifier.DAMAGE));
    }

    void has() {
        map.put(SpellsBoosts.Modifier.DAMAGE, 5);

        assertTrue(modifiers.has(SpellsBoosts.Modifier.DAMAGE));
        assertFalse(modifiers.has(SpellsBoosts.Modifier.LAUNCH_LINE));
    }

    void range() {
        map.put(SpellsBoosts.Modifier.RANGE, 5);
        assertEquals(5, modifiers.range());
    }

    void modifiableRange() {
        assertFalse(modifiers.modifiableRange());

        map.put(SpellsBoosts.Modifier.MODIFIABLE_RANGE, 5);
        assertTrue(modifiers.modifiableRange());
    }

    void damage() {
        map.put(SpellsBoosts.Modifier.DAMAGE, 5);
        assertEquals(5, modifiers.damage());
    }

    void baseDamage() {
        map.put(SpellsBoosts.Modifier.BASE_DAMAGE, 5);
        assertEquals(5, modifiers.baseDamage());
    }

    void heal() {
        map.put(SpellsBoosts.Modifier.HEAL, 5);
        assertEquals(5, modifiers.heal());
    }

    void apCost() {
        map.put(SpellsBoosts.Modifier.AP_COST, 5);
        assertEquals(5, modifiers.apCost());
    }

    void delay() {
        map.put(SpellsBoosts.Modifier.REDUCE_DELAY, 5);
        assertEquals(5, modifiers.delay());
    }

    void hasFixedDelay() {
        assertFalse(modifiers.hasFixedDelay());

        map.put(SpellsBoosts.Modifier.SET_DELAY, 5);
        assertTrue(modifiers.hasFixedDelay());
    }

    void fixedDelay() {
        map.put(SpellsBoosts.Modifier.SET_DELAY, 5);
        assertEquals(5, modifiers.fixedDelay());
    }

    void launchOutline() {
        assertFalse(modifiers.launchOutline());

        map.put(SpellsBoosts.Modifier.LAUNCH_LINE, 5);
        assertTrue(modifiers.launchOutline());
    }

    void lineOfSight() {
        assertFalse(modifiers.lineOfSight());

        map.put(SpellsBoosts.Modifier.LINE_OF_SIGHT, 5);
        assertTrue(modifiers.lineOfSight());
    }

    void launchPerTarget() {
        map.put(SpellsBoosts.Modifier.LAUNCH_PER_TARGET, 5);
        assertEquals(5, modifiers.launchPerTarget());
    }

    void launchPerTurn() {
        map.put(SpellsBoosts.Modifier.LAUNCH_PER_TURN, 5);
        assertEquals(5, modifiers.launchPerTurn());
    }

    void criticalHit() {
        map.put(SpellsBoosts.Modifier.CRITICAL, 5);
        assertEquals(5, modifiers.criticalHit());
    }
}
