import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private Scanner scanner;
    private ServerManager serverManager;
    private InputValidator validator;
    private FileHandler fileHandler;

    // Creates the objects needed by the application
    public Main() {
        scanner = new Scanner(System.in);
        serverManager = new ServerManager();
        validator = new InputValidator();
        fileHandler = new FileHandler();
    }

    // Standard Java application entry point
    public static void main(String[] args) {
        Main application = new Main();
        application.run();
    }

    // Runs the program until the user chooses Exit
    public int run() {

        boolean running = true;

        System.out.println("=========================================");
        System.out.println(" Server Infrastructure Management System");
        System.out.println("=========================================");

        while (running) {

            int choice = displayMenu();

            switch (choice) {

                case 1:
                    System.out.println(addServer());
                    break;

                case 2:
                    System.out.println(loadServersFromFile());
                    break;

                case 3:
                    System.out.println(viewServers());
                    break;

                case 4:
                    System.out.println(updateServer());
                    break;

                case 5:
                    System.out.println(removeServer());
                    break;

                case 6:
                    System.out.println(calculateOnlinePercentage());
                    break;

                case 7:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid menu option. Please select 1 through 7.");
            }
        }

        scanner.close();

        System.out.println("\nExiting Server Infrastructure Management System.");
        return 0;
    }

    // Displays the main menu and returns the user's selection
    private int displayMenu() {

        System.out.println("\n=========================================");
        System.out.println("              Main Menu");
        System.out.println("=========================================");
        System.out.println("1. Add Server");
        System.out.println("2. Load Servers From File");
        System.out.println("3. View All Servers");
        System.out.println("4. Update Server");
        System.out.println("5. Remove Server");
        System.out.println("6. Calculate Online Server Percentage");
        System.out.println("7. Exit");

        return readInteger("Select an option: ");
    }

    // Adds a server manually
    private String addServer() {

        System.out.println("\n--- Add Server ---");

        int serverId;

        while (true) {

            serverId = readInteger("Enter Server ID: ");

            if (!validator.isValidServerId(serverId)) {
                System.out.println("Server ID must be greater than 0.");
                continue;
            }

            if (serverManager.findServerById(serverId) != null) {
                System.out.println("That Server ID already exists.");
                continue;
            }

            break;
        }

        String hostname = readValidHostname();
        String operatingSystem = readValidOperatingSystem();
        double memoryGB = readValidMemory();
        double storageGB = readValidStorage();
        String ipAddress = readValidIpAddress();
        boolean isOnline = readOnlineStatus();

        Server server = new Server(
                serverId,
                hostname,
                operatingSystem,
                memoryGB,
                storageGB,
                ipAddress,
                isOnline
        );

        if (serverManager.addServer(server)) {
            return "Server added successfully.";
        }

        return "Unable to add server.";
    }

    // Loads multiple servers from a text file
    private String loadServersFromFile() {

        System.out.println("\n--- Load Servers From File ---");
        System.out.print("Enter or paste the file path: ");

        String filePath = scanner.nextLine();

        if (!fileHandler.fileExists(filePath)) {
            return "File could not be found. Please verify the path and try again.";
        }

        ArrayList<Server> loadedServers =
                fileHandler.loadServers(filePath);

        if (loadedServers.isEmpty()) {
            return "No valid server records were found in the file.";
        }

        int addedCount = 0;
        int skippedCount = 0;

        for (Server server : loadedServers) {

            if (serverManager.addServer(server)) {
                addedCount++;
            } else {
                skippedCount++;
            }
        }

        return "File loading complete. "
                + addedCount
                + " server(s) added, "
                + skippedCount
                + " duplicate server(s) skipped.";
    }

    // Displays all currently stored servers
    private String viewServers() {

        ArrayList<Server> servers =
                serverManager.getAllServers();

        if (servers.isEmpty()) {
            return "\nNo servers are currently stored.";
        }

        StringBuilder output = new StringBuilder();

        output.append("\n--- Server Inventory ---\n");

        for (Server server : servers) {
            output.append(server).append("\n");
        }

        output.append("\nTotal Servers: ")
                .append(serverManager.getServerCount());

        return output.toString();
    }

    // Allows the user to update one field of an existing server
    private String updateServer() {

        System.out.println("\n--- Update Server ---");

        int serverId =
                readInteger("Enter the Server ID to update: ");

        Server server =
                serverManager.findServerById(serverId);

        if (server == null) {
            return "Server ID " + serverId + " was not found.";
        }

        System.out.println("\nCurrent Server:");
        System.out.println(server);

        System.out.println("\nSelect the field to update:");
        System.out.println("1. Hostname");
        System.out.println("2. Operating System");
        System.out.println("3. Memory");
        System.out.println("4. Storage");
        System.out.println("5. IP Address");
        System.out.println("6. Online Status");
        System.out.println("7. Cancel");

        int choice = readInteger("Select an option: ");

        switch (choice) {

            case 1:
                server.setHostname(readValidHostname());
                return "Hostname updated successfully.";

            case 2:
                server.setOperatingSystem(
                        readValidOperatingSystem()
                );
                return "Operating system updated successfully.";

            case 3:
                server.setMemoryGB(readValidMemory());
                return "Memory updated successfully.";

            case 4:
                server.setStorageGB(readValidStorage());
                return "Storage updated successfully.";

            case 5:
                server.setIpAddress(readValidIpAddress());
                return "IP address updated successfully.";

            case 6:
                server.setOnline(readOnlineStatus());
                return "Online status updated successfully.";

            case 7:
                return "Update canceled.";

            default:
                return "Invalid update option.";
        }
    }

    // Removes a server using its unique ID
    private String removeServer() {

        System.out.println("\n--- Remove Server ---");

        int serverId =
                readInteger("Enter the Server ID to remove: ");

        Server server =
                serverManager.findServerById(serverId);

        if (server == null) {
            return "Server ID " + serverId + " was not found.";
        }

        System.out.println("\nServer Found:");
        System.out.println(server);

        System.out.print(
                "Are you sure you want to remove this server? (yes/no): "
        );

        String confirmation =
                scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
            return "Server removal canceled.";
        }

        if (serverManager.removeServer(serverId)) {
            return "Server removed successfully.";
        }

        return "Unable to remove server.";
    }

    // Calculates and formats the online server percentage
    private String calculateOnlinePercentage() {

        int totalServers =
                serverManager.getServerCount();

        if (totalServers == 0) {
            return "\nNo servers are currently stored.\n"
                    + "Online Server Percentage: 0.00%";
        }

        int onlineServers = 0;

        for (Server server : serverManager.getAllServers()) {

            if (server.isOnline()) {
                onlineServers++;
            }
        }

        double percentage =
                serverManager.calculateOnlinePercentage();

        return String.format(
                "\nOnline Servers: %d of %d%n"
                        + "Online Server Percentage: %.2f%%",
                onlineServers,
                totalServers,
                percentage
        );
    }

    // Reads an integer without allowing invalid input to crash the program
    private int readInteger(String prompt) {

        while (true) {

            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Please enter a whole number."
                );
            }
        }
    }

    // Reads a positive double without crashing on invalid input
    private double readPositiveDouble(String prompt) {

        while (true) {

            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {

                double value =
                        Double.parseDouble(input);

                if (value > 0) {
                    return value;
                }

                System.out.println(
                        "Value must be greater than 0."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a numeric value."
                );
            }
        }
    }

    // Prompts until a valid hostname is entered
    private String readValidHostname() {

        while (true) {

            System.out.print("Enter Hostname: ");
            String hostname = scanner.nextLine().trim();

            if (validator.isValidHostname(hostname)) {
                return hostname;
            }

            System.out.println(
                    "Hostname cannot be blank."
            );
        }
    }

    // Prompts until a valid operating system is entered
    private String readValidOperatingSystem() {

        while (true) {

            System.out.print("Enter Operating System: ");
            String operatingSystem =
                    scanner.nextLine().trim();

            if (validator.isValidOperatingSystem(
                    operatingSystem)) {

                return operatingSystem;
            }

            System.out.println(
                    "Operating system cannot be blank."
            );
        }
    }

    // Prompts until a valid memory amount is entered
    private double readValidMemory() {

        while (true) {

            double memory =
                    readPositiveDouble(
                            "Enter Memory in GB: "
                    );

            if (validator.isValidMemory(memory)) {
                return memory;
            }

            System.out.println(
                    "Memory must be greater than 0."
            );
        }
    }

    // Prompts until a valid storage amount is entered
    private double readValidStorage() {

        while (true) {

            double storage =
                    readPositiveDouble(
                            "Enter Storage in GB: "
                    );

            if (validator.isValidStorage(storage)) {
                return storage;
            }

            System.out.println(
                    "Storage must be greater than 0."
            );
        }
    }

    // Prompts until a valid IPv4 address is entered
    private String readValidIpAddress() {

        while (true) {

            System.out.print("Enter IP Address: ");
            String ipAddress =
                    scanner.nextLine().trim();

            if (validator.isValidIpAddress(ipAddress)) {
                return ipAddress;
            }

            System.out.println(
                    "Invalid IPv4 address. Example: 192.168.1.10"
            );
        }
    }

    // Prompts until a valid online status is entered
    private boolean readOnlineStatus() {

        while (true) {

            System.out.print(
                    "Is the server online? (yes/no): "
            );

            String status =
                    scanner.nextLine().trim();

            if (validator.isValidOnlineStatus(status)) {

                return validator.convertOnlineStatus(
                        status
                );
            }

            System.out.println(
                    "Invalid input. Enter yes, no, true, or false."
            );
        }
    }
}