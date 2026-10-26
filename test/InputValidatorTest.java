import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InputValidatorTest {

    private InputValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new InputValidator();
    }

    // Test 1: Valid server ID
    @Test
    public void testValidServerId() {

        assertTrue(validator.isValidServerId(1001));
    }

    // Test 2: Invalid server IDs
    @Test
    public void testInvalidServerId() {

        assertFalse(validator.isValidServerId(0));
        assertFalse(validator.isValidServerId(-1));
    }

    // Test 3: Valid hostname
    @Test
    public void testValidHostname() {

        assertTrue(validator.isValidHostname("WEB-SRV-01"));
    }

    // Test 4: Invalid hostname
    @Test
    public void testInvalidHostname() {

        assertFalse(validator.isValidHostname(""));

        assertFalse(validator.isValidHostname("   "));

        assertFalse(validator.isValidHostname(null));
    }

    // Test 5: Valid operating system
    @Test
    public void testValidOperatingSystem() {

        assertTrue(validator.isValidOperatingSystem("Ubuntu Server 24.04"));
    }

    // Test 6: Invalid operating system
    @Test
    public void testInvalidOperatingSystem() {

        assertFalse(validator.isValidOperatingSystem(""));

        assertFalse(validator.isValidOperatingSystem("   "));

        assertFalse(validator.isValidOperatingSystem(null));
    }

    // Test 7: Valid memory amount
    @Test
    public void testValidMemory() {

        assertTrue(validator.isValidMemory(32.0));

        assertTrue(validator.isValidMemory(0.5));
    }

    // Test 8: Invalid memory amount
    @Test
    public void testInvalidMemory() {

        assertFalse(validator.isValidMemory(0));

        assertFalse(validator.isValidMemory(-32.0));
    }

    // Test 9: Valid storage amount
    @Test
    public void testValidStorage() {

        assertTrue(validator.isValidStorage(500.0));

        assertTrue(validator.isValidStorage(1.0));
    }

    // Test 10: Invalid storage amount
    @Test
    public void testInvalidStorage() {

        assertFalse(validator.isValidStorage(0));

        assertFalse(validator.isValidStorage(-500.0));
    }

    // Test 11: Valid IPv4 addresses
    @Test
    public void testValidIpAddress() {

        assertTrue(validator.isValidIpAddress("192.168.1.10"));

        assertTrue(validator.isValidIpAddress("10.0.0.1"));

        assertTrue(validator.isValidIpAddress("255.255.255.255"));

        assertTrue(validator.isValidIpAddress("0.0.0.0"));
    }

    // Test 12: Invalid IPv4 addresses
    @Test
    public void testInvalidIpAddress() {assertFalse(validator.isValidIpAddress("300.168.1.10"));

        assertFalse(validator.isValidIpAddress("192.168.1"));

        assertFalse(validator.isValidIpAddress("192.168.1.10.5"));

        assertFalse(validator.isValidIpAddress("hello.world.test.ip"));

        assertFalse(validator.isValidIpAddress(""));

        assertFalse(validator.isValidIpAddress(null));
    }

    // Test 13: Accepts uppercase Y and N
    @Test
    public void testValidUppercaseOnlineStatus() {

        assertTrue(validator.isValidOnlineStatus("Y"));

        assertTrue(validator.isValidOnlineStatus("N"));
    }

    // Test 14: Accepts lowercase y and n
    @Test
    public void testValidLowercaseOnlineStatus() {

        assertTrue(validator.isValidOnlineStatus("y"));

        assertTrue(validator.isValidOnlineStatus("n"));
    }

    // Test 15: Rejects invalid online status
    @Test
    public void testInvalidOnlineStatus() {

        assertFalse(validator.isValidOnlineStatus("yes"));

        assertFalse(validator.isValidOnlineStatus("no"));

        assertFalse(validator.isValidOnlineStatus("true"));

        assertFalse(validator.isValidOnlineStatus("false"));

        assertFalse(validator.isValidOnlineStatus(""));

        assertFalse(validator.isValidOnlineStatus(null));
    }

    // Test 16: Converts Y/y to true
    @Test
    public void testConvertOnlineStatusToTrue() {

        assertTrue(validator.convertOnlineStatus("Y"));

        assertTrue(validator.convertOnlineStatus("y"));
    }

    // Test 17: Converts N/n to false
    @Test
    public void testConvertOnlineStatusToFalse() {

        assertFalse(validator.convertOnlineStatus("N"));

        assertFalse(validator.convertOnlineStatus("n"));
    }
}