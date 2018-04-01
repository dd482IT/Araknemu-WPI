package fr.quatrevieux.araknemu.data.value;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Value object for spell effect area
 */
final public class EffectArea {
    public enum Type {
        CELL('P'),
        CIRCLE('C'),
        CHECKERBOARD('D'),
        LINE('L'),
        CROSS('X'),
        PERPENDICULAR_LINE('T'),
        RECTANGLE('R'),
        RING('O');

        final private char c;

        final static private Map<Character, Type> typeByC = new HashMap<>();

        static {
            for (Type type : values()) {
                typeByC.put(type.c, type);
            }
        }

        Type(char c) {
            this.c = c;
        }

        /**
         * Get the effect area type by char id
         */
        static public Type byChar(char c) {
            if (!typeByC.containsKey(c)) {
                throw new NoSuchElementException("Invalid effect area " + c);
            }

            return typeByC.get(c);
        }
    }

    final private Type type;
    final private int size;

    public EffectArea(Type type, int size) {
        this.type = type;
        this.size = size;
    }

    public Type type() {
        return type;
    }

    public int size() {
        return size;
    }
}