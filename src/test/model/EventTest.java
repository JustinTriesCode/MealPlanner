// Modeled on the Event test from the AlarmSystem application

package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event eventOne;
    private Date dateOne;

    // NOTE: these tests might fail if time at which line (2) below is executed
    // is different from time that line (1) is executed. Lines (1) and (2) must
    // run in same millisecond for this test to make sense and pass.

    @BeforeEach
    public void runBefore() {
        eventOne = new Event("Sensor open at door"); // (1)
        dateOne = Calendar.getInstance().getTime(); // (2)
    }

    @Test
    public void testEvent() {
        assertEquals("Sensor open at door", eventOne.getDescription());
        assertEquals(dateOne, eventOne.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(dateOne.toString() + "\n" + "Sensor open at door", eventOne.toString());
    }

    @Test
    public void testEquals() {
        Event e2 = new Event("second");
        assertTrue(eventOne.equals(eventOne));
        assertFalse(eventOne.equals(e2));
        assertFalse(eventOne.equals(null));
        assertFalse(e2.equals("second"));
    }

    @Test
    public void testHash() {
        Event e2 = eventOne;
        assertEquals(e2.hashCode(), (eventOne.hashCode()));
    }
}
