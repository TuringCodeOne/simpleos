import java.io.*;
import java.util.ArrayList;


public class OperatingSystem {
    private ArrayList<Integer> imgByteToWrite = new ArrayList<Integer>();
    private ArrayList<String> readKernelFromFile(String fileName) {
        File file = new File(fileName);
        ArrayList<String> sourceFileInfo = new ArrayList<>();
        sourceFileInfo.add(file.getParent());
        sourceFileInfo.add(file.getName());
        InputStream in = null;

        try {
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                imgByteToWrite.add(tempbyte);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

//        imgByteToWrite.add(0x55);
//        imgByteToWrite.add(0xaa);
        int leftBytes = 512 - imgByteToWrite.size();
        for (int i = 0; i < leftBytes; i++) {
            imgByteToWrite.add(0x00);
        }
        System.out.println(imgByteToWrite.size());
        return sourceFileInfo;
    }

    public OperatingSystem(String s) {

        ArrayList<String> sourceFileInfo = readKernelFromFile(s);
        makeFllopy(sourceFileInfo.get(0), sourceFileInfo.get(1));
    }

    public void makeFllopy(String targetFilePath, String targetFileName)   {
        try {
            DataOutputStream out =
                    new DataOutputStream(
                            new FileOutputStream(targetFilePath
                    + File.separator
                    + targetFileName
                    + ".img"));
            for (int i = 0; i < imgByteToWrite.size(); i++) {
                out.writeByte(imgByteToWrite.get(i).byteValue());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("You should give a file name!");
        }
        OperatingSystem op = new OperatingSystem(args[0]);
    }
}