import java.util.ArrayList;

public class ServerManager{

    private ArrayList<Server> servers;

    // Creates an empty list to store Server objects
    public ServerManager() {
        servers = new ArrayList<>();
    }

    // Adds a server if its ID is not already being used
    public boolean addServer(Server server) {

        if (server == null) {
            return false;
        }

        if (findServerById(server.getServerId()) != null) {
            return false;
        }

        servers.add(server);
        return true;
    }

    // Returns a copy of all servers currently stored
    public ArrayList<Server> getAllServers() {
        return new ArrayList<>(servers);
    }

    // Searches for a server using its unique server ID
    public Server findServerById(int serverId) {

        for (Server server : servers) {

            if (server.getServerId() == serverId) {
                return server;
            }
        }

        return null;
    }

    // Updates all fields of an existing server
    public boolean updateServer(int currentServerId, Server updatedServer) {

        Server existingServer = findServerById(currentServerId);

        if (existingServer == null || updatedServer == null) {
            return false;
        }

        // Prevent duplicate IDs when changing the server ID
        if (currentServerId != updatedServer.getServerId()
                && findServerById(updatedServer.getServerId()) != null) {
            return false;
        }

        existingServer.setServerId(updatedServer.getServerId());
        existingServer.setHostname(updatedServer.getHostname());
        existingServer.setOperatingSystem(updatedServer.getOperatingSystem());
        existingServer.setMemoryGB(updatedServer.getMemoryGB());
        existingServer.setStorageGB(updatedServer.getStorageGB());
        existingServer.setIpAddress(updatedServer.getIpAddress());
        existingServer.setOnline(updatedServer.isOnline());

        return true;
    }

    // Removes a server using its unique server ID
    public boolean removeServer(int serverId) {

        Server server = findServerById(serverId);

        if (server == null) {
            return false;
        }

        servers.remove(server);
        return true;
    }

    // Calculates the percentage of servers currently online
    public double calculateOnlinePercentage() {

        if (servers.isEmpty()) {
            return 0.0;
        }

        int onlineCount = 0;

        for (Server server : servers) {

            if (server.isOnline()) {
                onlineCount++;
            }
        }

        return ((double) onlineCount / servers.size()) * 100;
    }

    // Returns the total number of servers stored
    public int getServerCount() {
        return servers.size();
    }
}