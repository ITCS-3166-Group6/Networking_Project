import java.util.*;

public class RoutingService {

    public static String route(String inputIPv4Address, Map<String, String> table) {
        long inputIPNum = ipv4ToDecimal(inputIPv4Address);

        // parse through routing table for IPs
        List<String> matches = new ArrayList<>();
        Map<String, Long> ipToMaskMap = new LinkedHashMap<>();
        for (String key : table.keySet()) {
            if (key.equals("Default"))
                continue;

            String[] splitStrings = key.split("/");
            long decimalRepOfIP = ipv4ToDecimal(splitStrings[0]);
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

        String nextHop = table.get("Default");
        long max = Long.MIN_VALUE;
        for (String key : matches) {
            if (ipToMaskMap.get(key) > max) {
                max = ipToMaskMap.get(key);
                nextHop = table.get(key);
            }
        }

        return nextHop;
    }

    private static long ipv4ToDecimal(String ip) {
        String[] ipBytes = ip.split("\\.");
        long decimalRep = 0;
        for (int i = 0; i < ipBytes.length; i++) {
            int ipInt = Integer.parseInt(ipBytes[i]);
            int power = 3 - i;
            decimalRep += ipInt * Math.pow(256, power);
        }
        return decimalRep;
    }
}