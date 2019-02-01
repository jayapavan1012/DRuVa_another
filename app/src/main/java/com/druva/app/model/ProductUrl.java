package com.druva.app.model;

import java.io.Serializable;


/**
 * Created by PT on 2/9/2017.
 */

public class ProductUrl implements Serializable {
    private String productNo ;
    private String productBarcodeNo ;


    public String getProductBarcodeNo() {
        return productBarcodeNo;
    }

    public void setProductBarcodeNo(String productBarcodeNo) {
        this.productBarcodeNo = productBarcodeNo;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }



    public ProductUrl(String productBarcodeNo, String scanTime, String scanDate) {
        this.productBarcodeNo = productBarcodeNo;
    }

    public ProductUrl(){

    }
}
