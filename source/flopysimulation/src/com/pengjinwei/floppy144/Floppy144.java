package com.pengjinwei.floppy144;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Peng Jinwei
 * @since 1.0
 */

public class Floppy144 {

    private static Floppy144 floppy144;

    /**
     * 磁头定义
     */
    public enum MAGNETIC_HEAD {
        /**
         * 磁头0
         */
        MAGNETIC_HEAD_0,
        /**
         * 磁头1
         */
        MAGNETIC_HEAD_1
    }

    /**
     * 扇区大小
     */
    private static final int SECTOR_SIZE = 512;

    /**
     * 磁道数
     */
    private static final int CYLINDER_COUNT = 80;

    /**
     * 扇区数
     */
    private static final int SECTOR_COUNT = 18;

    /**
     * 软盘定义
     */
    private HashMap<Integer, ArrayList<ArrayList<byte[]>>> floppy;

    /**
     * 初始化软盘存储结构，2个盘面
     */
    private Floppy144() {
        floppy = new HashMap<>();
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_0.ordinal(), initFloppyDisk());
        floppy.put(MAGNETIC_HEAD.MAGNETIC_HEAD_1.ordinal(), initFloppyDisk());
    }

    public static Floppy144 getInstance() {
        if (floppy144 == null) {
            floppy144 = new Floppy144();
        }
        return floppy144;
    }

    /**
     * 一个盘面80个磁道初始化
     */
    private ArrayList<ArrayList<byte[]>> initFloppyDisk() {
        ArrayList<ArrayList<byte[]>> floppyDisk = new ArrayList<>();
        for (int i = 0; i < CYLINDER_COUNT; i++) {
            floppyDisk.add(initCylinder());
        }
        return floppyDisk;
    }

    /**
     * 一个磁道18个扇区初始化
     */
    private ArrayList<byte[]> initCylinder() {
        ArrayList<byte[]> cylinder = new ArrayList<>();
        for (int i = 0; i < SECTOR_COUNT; i++) {
            cylinder.add(new byte[SECTOR_SIZE]);
        }
        return cylinder;
    }

    /**
     * 读取软盘数据
     */
    private byte[] readFloppy(MAGNETIC_HEAD head, int cylinderNum, int sectorNum) {
        return floppy.get(head.ordinal())
                .get(cylinderNum)
                .get(sectorNum);
    }

    /**
     * 向软盘写入数据
     */
    public void write(MAGNETIC_HEAD head, int cylinderNum, int sectorNum, byte[] buf) {
        byte[] floppyBuf = readFloppy(head, cylinderNum, sectorNum - 1);
        System.arraycopy(buf, 0, floppyBuf, 0, buf.length);
    }

    /**
     *
     * @param filePath 文件输出路径
     * @param fileName 不需要.img扩展名
     */
    public void makeFloppy(String filePath, String fileName) {
        try {
            String fullPathFile = filePath + File.separator + fileName + ".img";
            DataOutputStream out =
                    new DataOutputStream(new FileOutputStream(fullPathFile));
            for (int cylinder = 0; cylinder < CYLINDER_COUNT; cylinder++) {
                for (int header = 0; header <= 1; header++) {
                    for (int sector = 1; sector <= SECTOR_COUNT; sector++) {
                        byte[] buffer = readFloppy(MAGNETIC_HEAD.values()[header], cylinder, sector - 1);
                        out.write(buffer);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
