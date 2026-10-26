import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerManagerTest {

    private ServerManager serverManager;

    @BeforeEach
    public void setUp() {
        serverManager = new ServerManager();
    }

    // Test 1: Adds a valid server successfully
    @Test
    public void testAddServer() {

        Server server = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        boolean result = serverManager.addServer(server);

        assertTrue(result);
        assertEquals(1, serverManager.getServerCount());
        assertEquals(server, serverManager.findServerById(1001));
    }

    // Test 2: Prevents duplicate server IDs
    @Test
    public void testAddDuplicateServerId() {

        Server server1 = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        Server server2 = new Server(
                1001,
                "DB-SRV-01",
                "Windows Server",
                64.0,
                1000.0,
                "192.168.1.11",
                true
        );

        assertTrue(serverManager.addServer(server1));
        assertFalse(serverManager.addServer(server2));

        assertEquals(1, serverManager.getServerCount());
    }

    // Test 3: Finds an existing server by ID
    @Test
    public void testFindServerById() {

        Server server = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        serverManager.addServer(server);

        Server foundServer = serverManager.findServerById(1001);

        assertNotNull(foundServer);
        assertEquals(1001, foundServer.getServerId());
        assertEquals("WEB-SRV-01", foundServer.getHostname());
    }

    // Test 4: Returns null when server ID does not exist
    @Test
    public void testFindServerByIdNotFound() {

        Server foundServer = serverManager.findServerById(9999);

        assertNull(foundServer);
    }

    // Test 5: Updates every server attribute successfully
    @Test
    public void testUpdateServer() {

        Server originalServer = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        serverManager.addServer(originalServer);

        Server updatedServer = new Server(
                2001,
                "WEB-SRV-02",
                "Rocky Linux 9",
                64.0,
                1000.0,
                "192.168.1.20",
                false
        );

        boolean result = serverManager.updateServer(1001, updatedServer);

        assertTrue(result);

        Server resultServer = serverManager.findServerById(2001);

        assertNotNull(resultServer);

        assertEquals(2001, resultServer.getServerId());
        assertEquals("WEB-SRV-02", resultServer.getHostname());
        assertEquals("Rocky Linux 9", resultServer.getOperatingSystem());
        assertEquals(64.0, resultServer.getMemoryGB());
        assertEquals(1000.0, resultServer.getStorageGB());
        assertEquals("192.168.1.20", resultServer.getIpAddress());
        assertFalse(resultServer.isOnline());

        // Old ID should no longer exist
        assertNull(serverManager.findServerById(1001));
    }

    // Test 6: Rejects update if new ID already belongs to another server
    @Test
    public void testUpdateServerWithDuplicateId() {

        Server server1 = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        Server server2 = new Server(
                1002,
                "DB-SRV-01",
                "Windows Server",
                64.0,
                1000.0,
                "192.168.1.11",
                true
        );

        serverManager.addServer(server1);
        serverManager.addServer(server2);

        Server updatedServer = new Server(
                1002,
                "WEB-SRV-UPDATED",
                "Rocky Linux",
                64.0,
                750.0,
                "192.168.1.50",
                false
        );

        boolean result = serverManager.updateServer(
                        1001,
                        updatedServer
                );

        assertFalse(result);

        assertNotNull(serverManager.findServerById(1001));
        assertNotNull(serverManager.findServerById(1002));
    }

    // Test 7: Removes an existing server
    @Test
    public void testRemoveServer() {

        Server server = new Server(
                1001,
                "WEB-SRV-01",
                "Ubuntu Server",
                32.0,
                500.0,
                "192.168.1.10",
                true
        );

        serverManager.addServer(server);

        boolean result = serverManager.removeServer(1001);

        assertTrue(result);
        assertNull(serverManager.findServerById(1001));
        assertEquals(0, serverManager.getServerCount());
    }

    // Test 8: Returns false when removing a server that does not exist
    @Test
    public void testRemoveServerNotFound() {

        boolean result = serverManager.removeServer(9999);

        assertFalse(result);
    }

    // Test 9: Calculates online server percentage correctly
    @Test
    public void testCalculateOnlinePercentage() {

        serverManager.addServer(new Server(
                        1001,
                        "WEB-SRV-01",
                        "Ubuntu",
                        32.0,
                        500.0,
                        "192.168.1.10",
                        true
                )
        );

        serverManager.addServer(new Server(
                        1002,
                        "DB-SRV-01",
                        "Windows Server",
                        64.0,
                        1000.0,
                        "192.168.1.11",
                        true
                )
        );

        serverManager.addServer(new Server(
                        1003,
                        "APP-SRV-01",
                        "Rocky Linux",
                        16.0,
                        250.0,
                        "192.168.1.12",
                        true
                )
        );

        serverManager.addServer(new Server(
                        1004,
                        "BACKUP-SRV-01",
                        "Ubuntu",
                        32.0,
                        2000.0,
                        "192.168.1.13",
                        false
                )
        );

        double result = serverManager.calculateOnlinePercentage();

        assertEquals(75.0, result, 0.01);
    }

    // Test 10: Returns 0 percent when no servers exist
    @Test
    public void testCalculateOnlinePercentageWithNoServers() {

        double result = serverManager.calculateOnlinePercentage();

        assertEquals(0.0, result, 0.01);
    }
}