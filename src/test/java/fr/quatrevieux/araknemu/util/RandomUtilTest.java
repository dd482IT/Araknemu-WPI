package fr.quatrevieux.araknemu.util;

import fr.quatrevieux.araknemu._test.TestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomUtilTest extends TestCase {
    private RandomUtil util;

    @BeforeEach
    void setUp() {
        RandomUtil.enableTestingMode();

        util = new RandomUtil();
    }

    @Test
    void randFixed() {
        assertEquals(15, util.rand(15, 0));
    }

    @Test
    void randStartWithZero() {
        assertBetween(0, 15, util.rand(0, 15));
    }

    @Test
    void randInterval() {
        for (int i = 0; i < 100; ++i) {
            assertBetween(25, 45, util.rand(25, 45));
        }
    }

    @Test
    void randArrayOneElement() {
        assertEquals(32, util.rand(new int[] {32}));
    }

    @Test
    void randArrayFixed() {
        assertEquals(32, util.rand(new int[] {32, 0}));
    }

    @Test
    void randArray() {
        assertBetween(12, 23, util.rand(new int[] {12, 23}));
    }

    @Test
    void integerPredictable() {
        assertEquals(60, util.integer(100));
        assertEquals(48, util.integer(100));
        assertEquals(29, util.integer(100));
        assertEquals(47, util.integer(100));
    }

    @Test
    void boolWithPercent() {
        int trueCount = 0;

        for (int i = 0; i < 1000; ++i) {
            if (util.bool(25)) {
                ++trueCount;
            }
        }

        assertBetween(240, 260, trueCount);
    }

    @Test
    void boolWithoutPercent() {
        int trueCount = 0;

        for (int i = 0; i < 1000; ++i) {
            if (util.bool()) {
                ++trueCount;
            }
        }

        assertBetween(490, 510, trueCount);
    }

    @Test
    void reverseBool() {
        int trueCount = 0;

        for (int i = 0; i < 1000; ++i) {
            if (util.reverseBool(2)) {
                ++trueCount;
            }
        }

        assertBetween(490, 510, trueCount);

        trueCount = 0;

        for (int i = 0; i < 1000; ++i) {
            if (util.reverseBool(10)) {
                ++trueCount;
            }
        }

        assertBetween(90, 110, trueCount);

        trueCount = 0;

        for (int i = 0; i < 1000; ++i) {
            if (util.reverseBool(100)) {
                ++trueCount;
            }
        }

        assertBetween(5, 15, trueCount);
    }

    @Test
    void ofChars() {
        char[] chars = new char[] {'a', 'b', 'c', 'd'};

        assertEquals('c', util.of(chars));
        assertEquals('d', util.of(chars));
        assertEquals('a', util.of(chars));
    }

    @Test
    void ofObjects() {
        String[] strings = new String[] {"Hello", "World", "!"};

        assertEquals("Hello", util.of(strings));
        assertEquals("World", util.of(strings));
        assertEquals("World", util.of(strings));
    }

    @Test
    void shuffle() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        List<Integer> randomized = util.shuffle(list);

        assertEquals(Arrays.asList(4, 1, 2, 3), randomized);
        assertEquals(Arrays.asList(1, 2, 3, 4), list);
    }

    @Test
    void createSharedShouldBeResetWhenEnableTestingMode() {
        RandomUtil r1 = RandomUtil.createShared();
        RandomUtil r2 = RandomUtil.createShared();

        assertEquals(0, r1.integer(10));
        assertEquals(8, r1.integer(10));
        assertEquals(0, r2.integer(10));
        assertEquals(8, r2.integer(10));

        RandomUtil.enableTestingMode();

        assertEquals(0, r1.integer(10));
        assertEquals(8, r1.integer(10));
        assertEquals(0, r2.integer(10));
        assertEquals(8, r2.integer(10));
    }
}
