import java.util.*;


public class RoutingService {


    //default constructor
    public RoutingService() {

    }

    public static String route(String inputIPv4Address, Map<String, String> table) {
        long inputIPNum = convertIPv4toDec(inputIPv4Address);

        // parse through routing table for IPs
        List<String> matches = new ArrayList<>();
        Map<String, Long> ipToMaskMap = new LinkedHashMap<>();
        for (String key : table.keySet()) {
            if (key.equals("Default"))
                continue;

            String[] splitStrings = key.split("/");
            System.out.println(splitStrings[0]);
            long decimalRepOfIP = convertIPv4toDec(splitStrings[0]);
            long decimalRepOfMask = Long.parseLong(splitStrings[1]);
            ipToMaskMap.put(key, decimalRepOfMask);

            StringBuilder mask = new StringBuilder();
            for (int i = 0; i < 32; i++) {
                if (i < decimalRepOfMask)
                    mask.append('1');
                else
                    mask.append('0');
            }

            long result = Long.parseLong(mask.toString(), 2) & inputIPNum;
            if (result == decimalRepOfIP) {
                matches.add(key);
            }
        }

        String nextHop = "Router 2";
        long max = Long.MIN_VALUE;
        for (String key : matches) {
            if (ipToMaskMap.get(key) > max) {
                max = ipToMaskMap.get(key);
                nextHop = table.get(key);
            }
        }
        return nextHop;
    }

    private static long convertIPv4toDec(String ip) {
        //Creating a decimal representation of the String ip inputted for routing
        String[] ipAddressInArray = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {

            int ipInt = Integer.parseInt(ipAddressInArray[i]);
            int power = 3 - i;
            //Decimal representation of IP address
            result += ipInt * Math.pow(256, power);

        }
        return result;
    }
}