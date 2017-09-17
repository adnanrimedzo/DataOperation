package com.sentiance;

import com.sentiance.service.DataOperationService;

public class MainApplication {

    public static void main(String[] args) {
        try {
            DataOperationService dataOperationService = new DataOperationService();
            dataOperationService.dataOperation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
