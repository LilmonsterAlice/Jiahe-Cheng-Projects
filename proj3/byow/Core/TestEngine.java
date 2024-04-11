package byow.Core;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestEngine {

    @Test
    public void testExtractSeed1() {
        String input = "n1234567890s";
        long expectedOutput = 1234567890L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed2() {
        String input = "n0s";
        long expectedOutput = 0L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed3() {
        String input = "n123sabcd";
        long expectedOutput = 123L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed4() {
        String input = "n12345";
        long expectedOutput = 12345L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed5() {
        String input = "n01234sabcd";
        long expectedOutput = 1234L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed6() {
        String input = "ns";
        long expectedOutput = 0L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed7() {
        String input = "n12345swxyz";
        long expectedOutput = 12345L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed8() {
        String input = "n12345s12345";
        long expectedOutput = 12345L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed9() {
        String input = "n9999999999999999999999999999999999999s";
        long expectedOutput = 999999999999999999L;
        long actualOutput = Engine.extractSeed(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testExtractSeed10() {
        String input = "n-123sabcd";
        try {
            Engine.extractSeed(input);
        } catch (NumberFormatException e) {
            assertEquals("For input string: \"-123\"", e.getMessage());
        }
    }

}
