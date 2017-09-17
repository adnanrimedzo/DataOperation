package com.sentiance.service;

import com.sentiance.model.DatasetModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatasetServiceTest {
    private static String dataFolder = "./src/test/resources/data";
    private static String dataBackUpFolder = "./src/test/resources/backup";

    private DatasetService datasetService = new DatasetService();

    @BeforeClass
    public static void setUp() throws Exception {
        new File(dataFolder).mkdir();
        new File(dataBackUpFolder).mkdir();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        deleteDir(new File(dataFolder));
        deleteDir(new File(dataBackUpFolder));
    }

    @Test
    public void AgenerateDataset() throws Exception {
        List<DatasetModel> datasetModels = new ArrayList<>(0);
        DatasetModel datasetModel = new DatasetModel();
        datasetModel.setDirectory(dataFolder);
        datasetModel.setName("locations");
        datasetModel.setSize(32);
        datasetModels.add(datasetModel);

        datasetService.generateDataset(datasetModel,2,0);

        assertEquals(datasetModel.getSize(),datasetService.getFolderSize(new File(dataFolder)));
    }

    @Test
    public void BupdateDataset() throws Exception {
        List<DatasetModel> datasetModels = new ArrayList<>(0);
        DatasetModel datasetModel = new DatasetModel();
        datasetModel.setDirectory(dataFolder);
        datasetModel.setName("locations");
        datasetModel.setSize(10);
        datasetModels.add(datasetModel);

        int currentSize = datasetService.getFolderSize(new File(dataFolder));
        datasetService.updateDataset(datasetModel);

        assertEquals(datasetModel.getSize(),datasetService.getFolderSize(new File(dataFolder))-currentSize);
    }

    @Test
    public void CmoveDatasetFolder() throws Exception {
        File sourceFolder=new File(dataFolder);
        File targetFolder=new File(dataBackUpFolder);
        datasetService.moveDatasetFolder(sourceFolder,targetFolder);
        assertEquals(datasetService.getFolderSize(sourceFolder),
                datasetService.getFolderSize(targetFolder));
    }

    static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }

}