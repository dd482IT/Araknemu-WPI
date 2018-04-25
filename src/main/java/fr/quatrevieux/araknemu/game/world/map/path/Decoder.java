package fr.quatrevieux.araknemu.game.world.map.path;

import fr.quatrevieux.araknemu.game.world.map.Direction;
import fr.quatrevieux.araknemu.game.world.map.GameMap;
import fr.quatrevieux.araknemu.game.world.map.MapCell;
import fr.quatrevieux.araknemu.util.Base64;

import java.util.EnumMap;
import java.util.Map;

/**
 * Decode map data like paths or directions
 */
final public class Decoder<C extends MapCell> {
    final private GameMap<C> map;
    final private Map<Direction, Integer> directionTransformationMap = new EnumMap<>(Direction.class);

    public Decoder(GameMap<C> map) {
        this.map = map;

        directionTransformationMap.put(Direction.EAST,       1);
        directionTransformationMap.put(Direction.SOUTH_EAST, map.dimensions().width());
        directionTransformationMap.put(Direction.SOUTH,      2 * map.dimensions().width() - 1);
        directionTransformationMap.put(Direction.SOUTH_WEST, map.dimensions().width() - 1);
        directionTransformationMap.put(Direction.WEST,       -1);
        directionTransformationMap.put(Direction.NORTH_WEST, -map.dimensions().width());
        directionTransformationMap.put(Direction.NORTH,      -(2 * map.dimensions().width() - 1));
        directionTransformationMap.put(Direction.NORTH_EAST, -(map.dimensions().width() - 1));
    }

    /**
     * Get the immediately next cell if we move by the given direction
     */
    public C nextCellByDirection(C start, Direction direction) throws PathException {
        int nextId = start.id() + directionTransformationMap.get(direction);

        if (nextId >= map.size()) {
            throw new PathException("Invalid path : out of limit");
        }

        return map.get(nextId);
    }

    /**
     * Decode compressed path
     *
     * @param encoded The encoded path
     *
     * @return List of cells ids
     *
     * @throws PathException When an invalid path is given
     */
    public Path<C> decode(String encoded, C start) throws PathException {
        if (encoded.length() % 3 != 0) {
            throw new PathException("Invalid path : bad length");
        }

        Path<C> path = new Path<>(this);

        path.add(new PathStep<>(start, Direction.EAST));

        for (int i = 0; i < encoded.length(); i += 3) {
            Direction direction = Direction.byChar(encoded.charAt(i));
            int cell = (Base64.ord(encoded.charAt(i + 1)) & 15) << 6 | Base64.ord(encoded.charAt(i + 2));

            if (cell < 0 || cell >= map.size()) {
                throw new PathException("Invalid cell number");
            }

            expandRectilinearMove(
                path,
                path.target(),
                map.get(cell),
                direction
            );
        }

        return path;
    }

    /**
     * Encode the computed path
     */
    public String encode(Path<C> path) {
        StringBuilder encoded = new StringBuilder(path.size() * 3);

        for (int i = 0; i < path.size(); ++i) {
            PathStep step = path.get(i);

            encoded.append(step.direction().toChar());

            while (
                i + 1 < path.size()
                && path.get(i + 1).direction() == step.direction()
            ) {
                ++i;
            }

            encoded.append(Base64.encode(path.get(i).cell().id(), 2));
        }

        return encoded.toString();
    }

    private void expandRectilinearMove(Path<C> path, C start, C target, Direction direction) throws PathException {
        int stepsLimit =  2 * map.dimensions().width() + 1;

        while (!start.equals(target)) {
            start = nextCellByDirection(start, direction);
            path.add(new PathStep<>(start, direction));

            if (--stepsLimit < 0) {
                throw new PathException("Invalid path : bad direction");
            }
        }
    }
}
