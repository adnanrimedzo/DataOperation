package com.sentiance.service;

import com.sentiance.model.DatasetModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DataOperationService {

    private DatasetService datasetService = new DatasetService();

    public void dataOperation() {

        System.out.print("1 - Data generation\n" + "2 - Data update\n" + "3 - Data backup\n" + "4 - Exit\n" +
                "Please enter a Dataset Operation: ");

        String input = null;
        int operationNum = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            input = bufferedReader.readLine();
            operationNum = Integer.parseInt(input);
            if (!(operationNum >= 1 || operationNum <= 4)) {
                System.out.print("Invalid operation Number: ");
                dataOperation();
            }
        } catch (Exception e) {
            System.out.print("Invalid operation Number: ");
            dataOperation();
        }

        File masterFolder = null;
        int fileSize;
        List<DatasetModel> datasetModels = null;

        switch (operationNum) {
            case 1:
                masterFolder = getMasterFolder();
                fileSize = getFileSize();
                datasetModels = getSubDirectories(masterFolder.getAbsolutePath());
                datasetModels.forEach(d -> {
                    try {
                        datasetService.generateDataset(d, fileSize, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                dataOperation();
                break;
            case 2:
                masterFolder = getMasterFolder();
                datasetModels = getSubDirectories(masterFolder.getAbsolutePath());
                datasetModels.forEach(d -> {
                    try {
                        datasetService.updateDataset(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                dataOperation();
                break;
            case 3:
                File[] backUpFolders = getBackupFolderDirectories();
                datasetService.moveDatasetFolder(backUpFolders[0], backUpFolders[1]);
                dataOperation();
                break;
            case 4:
                return;
            default:
                dataOperation();
                break;
        }

    }

    private File getMasterFolder() {
        System.out.print("Please enter a Master Dataset Folder: ");
        String input = null;
        File srcFolder = getFolder();
        return srcFolder;
    }

    private int getFileSize() {
        System.out.print("Please enter an Individual File Size (MB): ");
        String input = null;
        int srcFolderSize = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            input = bufferedReader.readLine();
            srcFolderSize = Integer.parseInt(input);
            if (!(srcFolderSize > 0)) {
                System.out.print("Invalid Size: ");
                return getFileSize();
            }
        } catch (IOException e) {
            System.out.print("Invalid Size: ");
            return getFileSize();
        }
        return srcFolderSize;
    }

    private List<DatasetModel> getSubDirectories(String directory) {
        System.out.print("Please enter Subdirectories (locations,64,sensors,138,devices,24,...): ");
        String input[] = null;
        int srcFolderSize = 0;
        List<DatasetModel> datasetModels = new ArrayList<DatasetModel>(0);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            input = bufferedReader.readLine().split(",");

            for (int i = 0; i < input.length; i = i + 2) {
                DatasetModel datasetModel = new DatasetModel();
                datasetModel.setName(input[i]);
                datasetModel.setSize(Integer.parseInt(input[i + 1]));
                datasetModel.setDirectory(directory);
                datasetModels.add(datasetModel);
            }
        } catch (IOException e) {
            System.out.print("Invalid Input: ");
            return getSubDirectories(directory);
        }
        return datasetModels;
    }

    private File[] getBackupFolderDirectories() {
        File[] backupFolder = new File[2];
        System.out.print("Please enter a Source Folder: ");
        backupFolder[0] = getFolder();
        System.out.print("Please enter a target Folder: ");
        backupFolder[1] = new File(getFolder().getAbsoluteFile() + "/" + new Date().toString());
        return backupFolder;
    }

    private File getFolder() {
        String input = null;
        File srcFolder = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            input = bufferedReader.readLine();
            srcFolder = new File(input);
            if (!srcFolder.isDirectory()) {
                System.out.print("Invalid Folder please enter again: ");
                return getFolder();
            }
        } catch (IOException e) {
            System.out.print("Invalid Folder please enter again: ");
            return getFolder();
        }
        return srcFolder;
    }

}
