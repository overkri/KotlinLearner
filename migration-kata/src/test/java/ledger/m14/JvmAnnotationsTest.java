package ledger.m14;

import ledger.migration.JvmAnnotationsKata;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("m14")
class JvmAnnotationsTest {

    private final JvmAnnotationsKata kata = new JvmAnnotationsKata();

    @Test
    void javaSeesTheOverloadWithTheDefaultTier() {
        assertEquals(200L, kata.price(2, 100L));
    }

    @Test
    void javaSeesTheFullSignature() {
        assertEquals(180L, kata.price(2, 100L, "GOLD"));
    }

    @Test
    void javaSeesTheDeclaredException() {
        assertThrows(IllegalArgumentException.class, () -> kata.parseTier("platinum"));
    }
}
