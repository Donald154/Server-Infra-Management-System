import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @TempDir
    Path tempDirectory;

    @BeforeEach
    public void setUp() {
        // Captures console output so JUnit can check it
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void tearDown() {
        // Restores normal console output after each test
        System.setOut(originalOut);
    }

    // Test 1: Displays the CRUD menu
    @Test
    public void testDisplayMenu() {

        Scanner scanner = new Scanner("7\n");

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("SIMS Main Menu"));
        assertTrue(output.contains("1. Add Server"));
        assertTrue(output.contains("2. Import Servers From File"));
        assertTrue(output.contains("3. View All Servers"));
        assertTrue(output.contains("4. Update Server"));
        assertTrue(output.contains("5. Remove Server"));
        assertTrue(output.contains("6. Calculate Online Server Percentage"));
        assertTrue(output.contains("7. Exit"));
    }

    // Test 2: Invalid menu choices do not crash the program
    @Test
    public void testInvalidMenuOptions() {

        Scanner scanner = new Scanner(
                "abc\n" +
                        "0\n" +
                        "8\n" +
                        "7\n"
        );

        Main application = new Main(scanner);

        assertDoesNotThrow(application::run);

        String output = outputStream.toString();

        assertTrue(output.contains("Invalid input. Please enter a whole number.")
        );

        assertTrue(output.contains("Invalid menu option. Please select 1 through 7.")
        );

        assertTrue(output.contains("Exiting Server Infrastructure Management System.")
        );
    }

    // Test 3: Valid server data can be loaded from a file
    @Test
    public void testReadDataFromFile() throws IOException {

        Path serverFile = tempDirectory.resolve("servers.txt");

        Files.writeString(serverFile, "1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,true\n"
        );

        Scanner scanner = new Scanner(
                "2\n" +
                        serverFile + "\n" +
                        "7\n"
        );

        Main application = new Main(scanner);

        assertDoesNotThrow(application::run);

        String output = outputStream.toString();

        assertTrue(output.contains("File loading complete."));

        assertTrue(output.contains("1 server(s) added"));
    }

    // Test 4: Blank and invalid file paths are handled safely
    @Test
    public void testInvalidFilePath() {

        Scanner scanner = new Scanner(
                "2\n" +
                        "\n" +
                        "2\n" +
                        "missing-file.txt\n" +
                        "7\n"
        );

        Main application = new Main(scanner);

        assertDoesNotThrow(application::run);

        String output = outputStream.toString();

        assertTrue(output.contains("File could not be found. Please verify the path and try again."));
    }

    // Test 5: Loaded server data can be displayed
    @Test
    public void testDisplayLoadedData() throws IOException {

        Path serverFile = tempDirectory.resolve("servers.txt");

        Files.writeString(serverFile, "1001,WEB-SRV-01,Ubuntu Server,32.0,500.0,192.168.1.10,true\n"
        );

        Scanner scanner = new Scanner(
                "2\n" +
                        serverFile + "\n" +
                        "3\n" +
                        "7\n"
        );

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("Server Inventory"));

        assertTrue(output.contains("1001"));

        assertTrue(output.contains("WEB-SRV-01"));

        assertTrue(output.contains("Ubuntu Server"));

        assertTrue(output.contains("192.168.1.10"));

        assertTrue(output.contains("Total Servers: 1"));
    }

    // Test 6: A server can be manually created and displayed
    @Test
    public void testCreateServer() {

        Scanner scanner = new Scanner(
                "1\n" +
                        "1001\n" +
                        "WEB-SRV-01\n" +
                        "Ubuntu Server\n" +
                        "32\n" +
                        "500\n" +
                        "192.168.1.10\n" +
                        "Y\n" +
                        "3\n" +
                        "7\n"
        );

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("Server added successfully."));

        assertTrue(output.contains("1001"));

        assertTrue(output.contains("WEB-SRV-01"));

        assertTrue(output.contains("192.168.1.10"));
    }

    // Test 7: Bad data during server creation is rejected
    @Test
    public void testCreateServerWithInvalidData() {

        Scanner scanner = new Scanner(
                "1\n" +

                        // Invalid integer
                        "abc\n" +

                        // Invalid server ID
                        "-1\n" +

                        // Valid server ID
                        "1001\n" +

                        // Invalid hostname, then valid
                        "\n" +
                        "WEB-SRV-01\n" +

                        // Invalid OS, then valid
                        "\n" +
                        "Ubuntu Server\n" +

                        // Invalid memory text
                        "abc\n" +

                        // Invalid memory value
                        "0\n" +

                        // Valid memory
                        "32\n" +

                        // Invalid storage
                        "-500\n" +

                        // Valid storage
                        "500\n" +

                        // Invalid IP
                        "999.999.1.1\n" +

                        // Valid IP
                        "192.168.1.10\n" +

                        // Invalid online status
                        "yes\n" +

                        // Valid online status
                        "Y\n" +

                        // Exit
                        "7\n"
        );

        Main application = new Main(scanner);

        assertDoesNotThrow(application::run);

        String output = outputStream.toString();

        assertTrue(output.contains("Invalid input. Please enter a whole number."));

        assertTrue(output.contains("Server ID must be greater than 0."));

        assertTrue(output.contains("Hostname cannot be blank."));

        assertTrue(output.contains("Operating system cannot be blank."));

        assertTrue(output.contains("Invalid input. Please enter a numeric value."));

        assertTrue(output.contains("Value must be greater than 0."));

        assertTrue(output.contains("Invalid IPv4 address."));

        assertTrue(output.contains("Invalid input. Please enter Y or N."));

        assertTrue(output.contains("Server added successfully."));
    }

    // Test 8: A server can be removed
    @Test
    public void testRemoveServer() {

        Scanner scanner = new Scanner(
                // Add server
                "1\n" +
                        "1001\n" +
                        "WEB-SRV-01\n" +
                        "Ubuntu Server\n" +
                        "32\n" +
                        "500\n" +
                        "192.168.1.10\n" +
                        "Y\n" +

                        // Remove server
                        "5\n" +
                        "1001\n" +
                        "Y\n" +

                        // View inventory
                        "3\n" +

                        // Exit
                        "7\n"
        );

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("Server removed successfully."));

        assertTrue(output.contains("No servers are currently stored.")
        );
    }

    // Test 9: A server field can be updated
    @Test
    public void testUpdateServer() {

        Scanner scanner = new Scanner(
                // Add server
                "1\n" +
                        "1001\n" +
                        "WEB-SRV-01\n" +
                        "Ubuntu Server\n" +
                        "32\n" +
                        "500\n" +
                        "192.168.1.10\n" +
                        "Y\n" +

                        // Update server
                        "4\n" +
                        "1001\n" +

                        // Select hostname
                        "2\n" +

                        // New hostname
                        "WEB-SRV-UPDATED\n" +

                        // View data
                        "3\n" +

                        // Exit
                        "7\n"
        );

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("Hostname updated successfully.")
        );

        assertTrue(output.contains("WEB-SRV-UPDATED")
        );
    }

    // Test 10: Custom online percentage calculation works
    @Test
    public void testCustomOnlinePercentage() {

        Scanner scanner = new Scanner(
                // Server 1 - online
                "1\n" +
                        "1001\n" +
                        "WEB-SRV-01\n" +
                        "Ubuntu Server\n" +
                        "32\n" +
                        "500\n" +
                        "192.168.1.10\n" +
                        "Y\n" +

                        // Server 2 - offline
                        "1\n" +
                        "1002\n" +
                        "DB-SRV-01\n" +
                        "Windows Server\n" +
                        "64\n" +
                        "1000\n" +
                        "192.168.1.11\n" +
                        "N\n" +

                        // Calculate percentage
                        "6\n" +

                        // Exit
                        "7\n"
        );

        Main application = new Main(scanner);
        application.run();

        String output = outputStream.toString();

        assertTrue(output.contains("Online Servers: 1 of 2")
        );

        assertTrue(output.contains("Online Server Percentage: 50.00%")
        );
    }

    // Test 11: Program continues running until Exit is selected
    @Test
    public void testProgramRunsUntilExit() {

        Scanner scanner = new Scanner(
                "3\n" +
                        "6\n" +
                        "3\n" +
                        "7\n"
        );

        Main application = new Main(scanner);

        assertDoesNotThrow(application::run);

        String output = outputStream.toString();

        int firstMenu = output.indexOf("SIMS Main Menu");

        int secondMenu = output.indexOf("SIMS Main Menu", firstMenu + 1);

        assertTrue(firstMenu >= 0);
        assertTrue(secondMenu >= 0);

        assertTrue(output.contains("Exiting Server Infrastructure Management System."));
    }
}