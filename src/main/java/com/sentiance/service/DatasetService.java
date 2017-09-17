package com.sentiance.service;

import com.sentiance.model.DatasetModel;
import lombok.extern.slf4j.Slf4j;
import org.fluttercode.datafactory.impl.DataFactory;
import org.apache.commons.io.FileUtils;

import java.io.*;

@Slf4j
public class DatasetService {

    private static final int numBytesKB = 1024;
    private static final int numBytesMB = numBytesKB * numBytesKB;
    private static int fileSize = 0;

    public void generateDataset(DatasetModel datasetModel, int fileSize, int index) throws IOException {

        log.info("Data generation for  "
                + datasetModel.getName());

        this.fileSize = fileSize;

        DataFactory df = new DataFactory();

        for (int remainingSize = datasetModel.getSize();
             remainingSize > 0; remainingSize = remainingSize - fileSize) {

            File outputFile = new File(datasetModel.getPath(index++));
            outputFile.getParentFile().mkdirs();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));

            int subFileSize = remainingSize < fileSize ? remainingSize : fileSize;

            for (int i = 0; i < subFileSize*numBytesKB*32; i++) {
                byte[] buf = df.getNumberText(numBytesKB/32).getBytes();
                out.write(buf, 0, numBytesKB/32);
            }

            out.flush();
            out.close();
        }

    }

    public void updateDataset(DatasetModel datasetModel) throws IOException {
        int currentFolderSize = getFolderSize(new File(datasetModel.getPath()));
        generateDataset(datasetModel, fileSize, currentFolderSize/fileSize);
    }

    public void moveDatasetFolder(File srcFolder, File destFolder) {

        if (!srcFolder.exists()) {
            log.info("Directory does not exist.");
            return;
        } else {
            try {
                copyFolder(srcFolder, destFolder);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        log.info("Done");
    }


    private void copyFolder(File src, File dest)
            throws IOException {

        if (src.isDirectory()) {

            if (!dest.exists()) {
                dest.mkdir();
                log.info("Directory copied from "
                        + src + "  to " + dest);
            }

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                copyFolder(srcFile, destFile);
            }

        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[numBytesKB];

            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            log.info("File copied from " + src + " to " + dest);
        }
    }


    public int getFolderSize(File folder) {
        long size = FileUtils.sizeOfDirectory(folder);
        return (int) (size / numBytesMB);
    }


}
