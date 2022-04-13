/*
 * The MIT License
 *
 * Copyright 2022 xavetar.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package re.count.initialization;

import java.io.*;
import java.util.*;
import static re.count.data.Data.*;
import javax.swing.SwingUtilities;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import re.count.interfaces.EndInit;

/**
 *
 * @author xavetar
 */

public class InitData{
    
    private static final List<EndInit> listeners = new ArrayList<>();
    
    List<String> Vendor_barcode = new ArrayList<>();
    List<String> Barcodes_barcode = new ArrayList<>();
    
    public InitData() {
        
    }
    
    public InitData(final String fileRemains, final String fileBarcode) throws FileNotFoundException, IOException {
        InputStream fileRemainsStream = new FileInputStream(fileRemains);
        InputStream fileBarcodeStream = new FileInputStream(fileBarcode);

        XSSFWorkbook remains = new XSSFWorkbook(fileRemainsStream);
        XSSFWorkbook barcode = new XSSFWorkbook(fileBarcodeStream);

        XSSFSheet remains_Sheet = remains.getSheetAt(0);
        XSSFSheet barcode_Sheet = barcode.getSheetAt(0);
        
        initCopmonent(remains_Sheet, barcode_Sheet);
    }

    public void addListener(EndInit toAdd){
        listeners.add(toAdd);
    }
    
    private void initCopmonent(XSSFSheet remains_Sheet, XSSFSheet barcode_Sheet){
        GetRemainsToArray(remains_Sheet);
        GetBarcodeToArray(barcode_Sheet);
        DestructGetData();
        
        Thread Duplicate = new Thread(){
            @Override
            public void run(){
                InitDuplicates();
            }
        };
        Duplicate.start();
        
        sayEnd();
    }
    
    private void GetRemainsToArray(XSSFSheet remains_Sheet){
        for (int columnNumber = 0; columnNumber < 6; columnNumber++) { //processing and adding values to arrays
            for (Row r : remains_Sheet) {
                Cell c = r.getCell(columnNumber);
                if (c != null) {
                    switch (c.getCellType()) {
                        case NUMERIC:
                            if (columnNumber == 0) {
                                SetVendor(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                            if (columnNumber == 2) {
                                SetSR(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                            if (columnNumber == 3) {
                                SetRemains((int)c.getNumericCellValue());
                            }
                            if (columnNumber == 4) {
                                SetReplace((int)c.getNumericCellValue());
                            }
                            if (columnNumber == 5) {
                                SetSumm(c.getNumericCellValue());
                            }
                            break;
                        case STRING:
                            if (columnNumber == 1) {
                                SetNM(c.getStringCellValue());
                            }
                            if (columnNumber == 2) {
                                SetSR(c.getStringCellValue());
                            }
                            break;
                        case BLANK:
                            //System.out.print("null" + " ");
                            if (columnNumber == 0) {
                                SetVendor("null");
                            }
                            if (columnNumber == 1) {
                                SetNM("null");
                            }
                            if (columnNumber == 2) {
                                SetSR("");
                            }
                            if (columnNumber == 3) {
                                SetRemains(0);
                            }
                            if (columnNumber == 4) {
                                SetReplace(0);
                            }
                            if (columnNumber == 5) {
                                SetSumm(0.0);
                            }
                            break;
                    }
                }
            }
        }
    }
    private void GetBarcodeToArray(XSSFSheet barcode_Sheet){
        for (int columnNumber = 0; columnNumber < 3; columnNumber++) { //processing and adding values to arrays from barcode file
            if (columnNumber == 1) {
                ++columnNumber;
            }
            for (Row r : barcode_Sheet) {
                Cell c = r.getCell(columnNumber);
                if (c != null) {
                    switch (c.getCellType()) {
                        case NUMERIC:
                            if (columnNumber == 0) {
                                Vendor_barcode.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                            if (columnNumber == 2) {
                                Barcodes_barcode.add(NumberToTextConverter.toText(c.getNumericCellValue()));
                            }
                            break;
                        case STRING:
                            if (columnNumber == 0) {
                                Vendor_barcode.add(c.getStringCellValue());
                            }
                            if (columnNumber == 2) {
                                Barcodes_barcode.add(c.getStringCellValue());
                            }
                            break;
                        case BLANK:
                            //System.out.print("null" + " ");
                            Barcodes_barcode.add("null");
                            break;
                    }
                }
            }
        }

        for (int i = 0; i < GetSizeVendor(); i++) { //merging barcodes with residuals/remains
            for (int j = 0; j < Vendor_barcode.size(); j++) {
                if (GetVendor(i).equals(Vendor_barcode.get(j))) {
                    PutBarcode(Barcodes_barcode.get(j), Vendor_barcode.get(j));
                }
            }
        }
    }
    
    private  void sayEnd(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                for(EndInit h1 : listeners){
                    h1.SomeoneSayEnd();
                }
            }
        });
    }
    
    private void InitDuplicates(){
        for(int i = 0; i < GetSizeVendor(); i++){
            if(GetVendor(i).equals(GetVendor(i))){
                PutDuplicateVendor(GetVendor(i), 0);
            }
        }
        SetDuplicates();
    }
    
    private void SetDuplicates(){ //Crutch
        int lastDuplicatedIndex = 0;
        for(int i = 0; i < GetSizeVendor(); i++){
            if(i > 0){
                i = lastDuplicatedIndex + 1;
            }
            for(int j = i; j < GetSizeVendor(); j++){
                if(GetVendor(i).equals(GetVendor(j))){
                    PutDuplicateVendor(GetVendor(i), GetSizeDuplicateVENDOR(GetVendor(i)) + 1);
                    lastDuplicatedIndex = j; //NE IZMENYAEM i INACHE NAM PIZDA
                }
            }
        }
        RemoveSoleVal();
        
    }
    
    private void RemoveSoleVal(){
        List<String> temp = new ArrayList<>();
        
        for(Map.Entry<String, Integer> entry : GetEntrySetDuplicateVen()) {
            if((int)entry.getValue() == 1){
                temp.add(entry.getKey());
            }
        }
        
        for(int i = 0; i < temp.size(); i++) {
            EraseDupVen_Val(temp.get(i));
        }
        
        PrintDuplicates();
    }
    
    private void PrintDuplicates(){
        for(Map.Entry<String, Integer> entry : GetEntrySetDuplicateVen()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }

    
    private void DestructGetData(){
        Vendor_barcode.clear();
        Barcodes_barcode.clear();
    }
}
