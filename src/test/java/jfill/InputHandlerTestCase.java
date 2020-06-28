package jfill;

import org.jline.reader.impl.LineReaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

final class InputHandlerTestCase {

    private static LineReaderImpl reader;

    @BeforeAll
    static void initReader() {
        reader = mock(LineReaderImpl.class);
        when(reader.readLine(anyString())).thenReturn("user value");
    }

    @Test
    void testInput() {
        final InputHandler handler = new InputHandler(reader);
        Assertions.assertEquals(
                handler.getValue(
                        "port",
                        new Suggestions.Plain(
                                Collections.emptyList()
                        )
                ),
                "user value"
        );
    }
}
