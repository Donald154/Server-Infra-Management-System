import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    private InputValidator validator;

    // Creates a FileHandler with access to validation methods
    public FileHandler() {
        validator = new InputValidator();
    }

    // Removes surrounding quotation marks and extra spaces from a file path
    private String cleanFilePath(String filePath) {

        if (filePath == null) {
            return null;
        }

        String cleanedPath = filePath.trim();

        // Allows for file paths copied with quotation marks
        if (cleanedPath.startsWith("\"") && cleanedPath.endsWith("\"")) {
            cleanedPath = cleanedPath.substring(1, cleanedPath.length() - 1);
        }

        return cleanedPath;
    }

    // Checks if the requested file exists
    public boolean fileExists(String filePath) {

        filePath = cleanFilePath(filePath);

        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);

        return file.exists() && file.isFile();
    }

    // Loads valid Server objects from a comma-separated text file
    public ArrayList<Server> loadServers(String filePath) {

        ArrayList<Server> loadedServers = new ArrayList<>();

        filePath = cleanFilePath(filePath);

        if (!fileExists(filePath)) {
            return loadedServers;
        }

        File file = new File(filePath);

        try (Scanner fileScanner = new Scanner(file)) {

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine().trim();

                // Skip blank lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                // Each server record must contain exactly 7 fields
                if (parts.length != 7) {
                    continue;
                }

                try {

                    int serverId =
                            Integer.parseInt(
                                    parts[0].trim()
                            );

                    String hostname =
                            parts[1].trim();

                    String operatingSystem =
                            parts[2].trim();

                    double memoryGB =
                            Double.parseDouble(
                                    parts[3].trim()
                            );

                    double storageGB =
                            Double.parseDouble(
                                    parts[4].trim()
                            );

                    String ipAddress =
                            parts[5].trim();

                    String onlineStatus =
                            parts[6].trim();

                    // Validate the server values
                    if (!validator.isValidServerId(serverId)
                            || !validator.isValidHostname(hostname)
                            || !validator.isValidOperatingSystem(
                            operatingSystem
                    )
                            || !validator.isValidMemory(memoryGB)
                            || !validator.isValidStorage(storageGB)
                            || !validator.isValidIpAddress(ipAddress)) {

                        continue;
                    }

                    // Text files must use true or false
                    if (!onlineStatus.equalsIgnoreCase("true")
                            && !onlineStatus.equalsIgnoreCase("false")) {

                        continue;
                    }

                    // Converts the text value into a boolean
                    boolean isOnline =
                            Boolean.parseBoolean(onlineStatus);

                    Server server = new Server(
                            serverId,
                            hostname,
                            operatingSystem,
                            memoryGB,
                            storageGB,
                            ipAddress,
                            isOnline
                    );

                    loadedServers.add(server);

                } catch (NumberFormatException e) {

                    // Skip records containing invalid numeric values
                    continue;
                }
            }

        } catch (FileNotFoundException e) {

            return loadedServers;
        }

        return loadedServers;
    }
}