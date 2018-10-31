package com.jole3970;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;

public class Main {

    private static List<String> argumentFlags = Arrays.asList("-f", "-b", "-m", "-e");

    public static void main(String[] args) throws IOException {
        Communicator communicator = new Communicator(new Channel(), new Encoder(new ReedMullerCodeGenerator()), new Decoder());

        Map<String, String> arguments = parseArgs(args);

        if(arguments.size() < 3) {
            System.out.println("Not enough arguments, should contain -m and -e flags and something to encode");
            return;
        }
        if(!arguments.containsKey("-m") || !arguments.containsKey("-e")) {
            System.out.println("Should contain -m and -e flags");
            return;
        }

        int m;
        double errorRate;
        try {
            m = Integer.parseInt(arguments.get("-m"));
            errorRate = Double.parseDouble(arguments.get("-e"));
        }
        catch (NumberFormatException e) {
            System.out.println(String.format("Incorrect flags, \"-m\" should be integer, but is %s%n" +
                    "\"-e\" should be decimal number between 0 and 100, but is %s%n",arguments.get("-m"), arguments.get("-e")));
            return;
        }

        if(arguments.containsKey("-f")) {
            String sourcePath = arguments.get("-f");
            File fi = new File(sourcePath);
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            byte[] receiveBytes = communicator.transmitAndReceiveBytes(fileContent, m, errorRate);
            OutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(sourcePath + ".jpg"));
                out.write(receiveBytes);
            } finally {
                if (out != null) out.close();
            }
        }
        else if(arguments.containsKey("-b")) {
            String input = arguments.get("-b");
            if(Pattern.matches("^[0,1]+$", input)) {
                List<Boolean> vector = new ArrayList(input.length());
                for(int i = 0; i < input.length(); i++) {
                    vector.add(input.charAt(i) == '1');
                }
                List<Boolean> decoded = communicator.transmitAndReceiveBits(vector, m, errorRate);
                for (Boolean aDecoded : decoded) {
                    System.out.print(aDecoded ? 1 : 0);
                }
                System.out.println();
            }
        }
        else {
            byte[] decoded = communicator.transmitAndReceiveBytes(arguments.get("args").getBytes(), m, errorRate);
            System.out.println(new String(decoded));
        }
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();

        List<String> argList = new ArrayList<>(Arrays.asList(args));
        for (String argFlag: argumentFlags) {
            int index = argList.indexOf(argFlag);
            if (index >= 0 && argList.size() > index + 1) {
                String argValue = argList.get(index + 1);
                map.put(argFlag, argValue);
                argList.remove(argFlag);
                argList.remove(argValue);
            }
        }
        map.put("args", StringUtils.join(argList, " "));
        return map;
    }
}
