package com.pengjinwei.floppy144;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Peng Jinwei
 * @since 1.0
 */

public class MakeFloppy14 {
    public static void main(String args[]) {
        if (args.length == 0) {
            System.err.println("Can't find a file to make floppy!");
            System.exit(0);
        }
        String fullFilePath = args[0];
        File file = new File(fullFilePath);
        String fileParentPath = file.getParent();
        String fileName = file.getName();
        try {
            byte[] buffer = Files.readAllBytes(Paths.get(fullFilePath));
            Floppy144 floppy144 = Floppy144.getInstance();
            floppy144.write(Floppy144.MAGNETIC_HEAD.MAGNETIC_HEAD_0, 0, 1, buffer);
            floppy144.makeFloppy(fileParentPath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
