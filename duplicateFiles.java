import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;

public class duplicateFiles {
    private static MessageDigest md;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please supply a directory path");
            return;
        }
        File dir = new File(args[0]);
        if (!dir.isDirectory()) {
            System.out.println("Supplied directory does not exist.");
            return;
        }
        Map<String, List<String>> lists = new HashMap<String, List<String>>();
        duplicateFiles.findDuplicates(lists, dir);
        for (List<String> list : lists.values()) {
            if (list.size() > 1) {
                System.out.println("\n");
                for (String file : list) {
                    System.out.println(file);
                }
            }
        }
        System.out.println("\n");
    }
    
    static {
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("cannot initialize SHA-512 hash function", e);
        }
    }
    
    public static void findDuplicates(Map<String, List<String>> files, File directory) {
        for(File subfile : directory.listFiles()) {
            if(subfile.isDirectory()) {
                findDuplicates(files, subfile);
            } else {
                try {
                    FileInputStream input = new FileInputStream(subfile);
                    byte data[] = new byte[(int) subfile.length()];
                    input.read(data);
                    input.close();
                    String hashes = new BigInteger(1, md.digest(data)).toString(16);
                    List<String> list = files.get(hashes);
                    if(list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(subfile.getAbsolutePath());
                    files.put(hashes, list);
                } catch (IOException e) {
                    throw new RuntimeException("cannot read file " + subfile.getAbsolutePath(), e);
                }
            }
        }
    }
    
    
}    
    