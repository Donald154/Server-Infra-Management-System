//
public class Server {
    //Attributes
    private int serverId;
    private String hostname;
    private String operatingSystem;
    private double memoryGB;
    private double storageGB;
    private String ipAddress;
    private boolean isOnline;

    //Constructor
    public Server(int serverId, String hostname, String operatingSystem, double memoryGB, double storageGB, String ipAddress, boolean isOnline){

        this.serverId = serverId;
        this.hostname = hostname;
        this.operatingSystem = operatingSystem;
        this.memoryGB = memoryGB;
        this.storageGB = storageGB;
        this.ipAddress = ipAddress;
        this.isOnline = isOnline;
    }

    //Getters & Setters
    public int getServerId() {
        return serverId;
    }

    public String getHostname() {
        return hostname;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public double getMemoryGB() {
        return memoryGB;
    }

    public double getStorageGB() {
        return storageGB;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String setHostname(String hostname) {
        this.hostname = hostname;
        return this.hostname;
    }

    public String setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this.operatingSystem;
    }

    public double setMemoryGB(double memoryGB) {
        this.memoryGB = memoryGB;
        return this.memoryGB;
    }

    public double setStorageGB(double storageGB) {
        this.storageGB = storageGB;
        return this.storageGB;
    }

    public String setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this.ipAddress;
    }

    public boolean setOnline(boolean online) {
        isOnline = online;
        return isOnline;
    }
    //ToString method
    @Override
    public String toString(){
            return "Server ID: " + serverId +
                    ", Hostname: " + hostname +
                    ", OS: " + operatingSystem +
                    ", Memory: " + memoryGB + " GB" +
                    ", Storage: " + storageGB + " GB" +
                    ", IP Address: " + ipAddress +
                    ", Online: " + isOnline;
        }
}
