public class InputValidator {

    //Methods
    // Checks that the server ID is a positive number
    public boolean isValidServerId(int serverId) {
        return serverId > 0;
    }

    // Checks that the hostname is not null or blank
    public boolean isValidHostname(String hostname) {
        return hostname != null && !hostname.trim().isEmpty();
    }

    // Checks that the operating system is not null or blank
    public boolean isValidOperatingSystem(String operatingSystem) {
        return operatingSystem != null && !operatingSystem.trim().isEmpty();
    }

    // Checks that memory is greater than zero
    public boolean isValidMemory(double memoryGB) {
        return memoryGB > 0;
    }

    // Checks that storage is greater than zero
    public boolean isValidStorage(double storageGB) {
        return storageGB > 0;
    }

    // Checks that the IP address is a valid IPv4 address
    public boolean isValidIpAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            return false;
        }
        //Regex for splitting ip address format
        String[] parts = ipAddress.split("\\.");

        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);

                // Each section of an IPv4 address must be between 0 and 255

                if (value < 0 || value > 255) {
                    return false;
                }

            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }

    // Accepts true, false, yes, or no as valid online status input
    public boolean isValidOnlineStatus(String status) {
        if (status == null) {
            return false;
        }

        String value = status.trim().toLowerCase();

        return value.equals("true") || value.equals("false") || value.equals("yes") || value.equals("no");
    }

    // Converts yes or true to true and no or false to false
    public boolean convertOnlineStatus(String status) {
        String value = status.trim().toLowerCase();

        return value.equals("true") || value.equals("yes");
    }
}