import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerTest {

    private FileHandler fileHandler;

    @BeforeEach
    public void setUp() {
        fileHandler = new FileHandler();
    }

    // Test 1: Confirms that an existing file is found
    @Test
    public void testFileExists() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        assertTrue(fileHandler.fileExists(tempFile.getAbsolutePath())
        );

        tempFile.delete();
    }

    // Test 2: Confirms that a missing file is rejected
    @Test
    public void testFileDoesNotExist() {

        String invalidPath = "C:\\fakeFolder\\missingServers.txt";

        assertFalse(fileHandler.fileExists(invalidPath));
    }

    // Test 3: Confirms quoted file paths are accepted
    @Test
    public void testFileExistsWithQuotes() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        String quotedPath = "\"" + tempFile.getAbsolutePath() + "\"";

        assertTrue(fileHandler.fileExists(quotedPath));

        tempFile.delete();
    }

    // Test 4: Loads one valid server record
    @Test
    public void testLoadValidServer() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write("1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,true");

        writer.close();

        ArrayList<Server> servers =
                fileHandler.loadServers(tempFile.getAbsolutePath());

        assertEquals(1, servers.size());

        Server server = servers.get(0);

        assertEquals(1001, server.getServerId());
        assertEquals("WEB-SRV-01", server.getHostname());
        assertEquals("Ubuntu Server", server.getOperatingSystem());
        assertEquals(32.0, server.getMemoryGB());
        assertEquals(500.0, server.getStorageGB());
        assertEquals("192.168.1.10", server.getIpAddress());
        assertTrue(server.isOnline());

        tempFile.delete();
    }

    // Test 5: Loads multiple valid server records
    @Test
    public void testLoadMultipleServers() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write("1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,true\n");

        writer.write("1002,DB-SRV-01,Windows Server,64.0,1000.0,192.168.1.11,false\n");

        writer.write("1003,APP-SRV-01,Rocky Linux,16.0,250.0,192.168.1.12,true");

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertEquals(3, servers.size());

        assertEquals(1001, servers.get(0).getServerId()
        );

        assertEquals(
                1002,
                servers.get(1).getServerId()
        );

        assertEquals(1003, servers.get(2).getServerId());

        tempFile.delete();
    }

    // Test 6: Skips a record with an invalid server ID
    @Test
    public void testInvalidServerIdIsSkipped() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write(
                "-1,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,true"
        );

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertTrue(servers.isEmpty());

        tempFile.delete();
    }

    // Test 7: Skips a record with invalid memory
    @Test
    public void testInvalidMemoryIsSkipped() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write(
                "1001,WEB-SRV-01,Ubuntu Server,-32.0,500.0,192.168.1.10,true"
        );

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertTrue(servers.isEmpty());

        tempFile.delete();
    }

    // Test 8: Skips a record with an invalid IP address
    @Test
    public void testInvalidIpAddressIsSkipped() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write("1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,999.168.1.10,true");

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertTrue(servers.isEmpty());

        tempFile.delete();
    }

    // Test 9: Skips a record with an invalid online status
    @Test
    public void testInvalidOnlineStatusIsSkipped() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write("1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,yes");

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertTrue(servers.isEmpty());

        tempFile.delete();
    }

    // Test 10: Skips malformed records with the wrong number of fields
    @Test
    public void testMalformedRecordIsSkipped() throws IOException {

        File tempFile = File.createTempFile(
                "servers",
                ".txt"
        );

        FileWriter writer = new FileWriter(tempFile);

        writer.write("1001,WEB-SRV-01,Ubuntu Server");

        writer.close();

        ArrayList<Server> servers = fileHandler.loadServers(tempFile.getAbsolutePath());

        assertTrue(servers.isEmpty());

        tempFile.delete();
    }
}