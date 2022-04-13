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
package re.count.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author xavetar
 */

public class Data {
    public static final int VENDOR = 0;
    public static final int NOMENCKLATURE = 1;
    public static final int SERIALNUMBER = 2;
    public static final int SUMM = 3;
    public static final int REMAINS = 4;
    public static final int REPLACE = 5;
    
    
    private static final List<String> Vendor = new ArrayList<>();//vendor_code
    private static final List<String> NM = new ArrayList<>();//Nomenklature
    private static final List<String> SR = new ArrayList<>(); //serial number
    private static final List<Integer> Remains = new ArrayList<>();//remaings
    private static final List<Integer> Replace = new ArrayList<>();//Replace
    private static final List<Double> Summ = new ArrayList<>();//summ_vendor
    
//    private static final List<Integer> duplicates = new ArrayList<>();//Replace
    private static final Map<String, String> BARCODE = new HashMap<>();
    private static final Map<String, Integer> DupVENDOR = new HashMap<>();
    
    public static String GetVendor(int i){
        return Vendor.get(i);
    }
    public static String GetNM(int i){
        return NM.get(i);
    }
    public static String GetSR(int i){
        return SR.get(i);
    }
    public static Integer GetRemains(int i){
        return Remains.get(i);
    }
    public static Integer GetReplace(int i){
        return Replace.get(i);
    }
    public static Double GetSumm(int i){
        return Summ.get(i);
    }
    public static String GetBARCODE(String key){
        return BARCODE.get(key);
    }
    public static Integer GetSizeDuplicateVENDOR(String key){
        return DupVENDOR.get(key);
    }
    
    public static void SetVendor(String value){
        Vendor.add(value);
    }
    public static void SetNM(String value){
        NM.add(value);
    }
    public static void SetSR(String value){
        SR.add(value);
    }
    public static void SetRemains(Integer value){
        Remains.add(value);
    }
    public static void SetReplace(Integer value){
        Replace.add(value);
    }
    public static void SetSumm(Double value){
        Summ.add(value);
    }
    public static void PutBarcode(String key, String value){
        BARCODE.put(key, value);
    }
    public static void PutDuplicateVendor(String key, Integer value){
        DupVENDOR.put(key, value);
    }
    
    public static int GetSizeVendor(){
        return Vendor.size();
    }
    public static int GetSizeNM(){
        return NM.size();
    }
    public static int GetSizeSR(){
        return SR.size();
    }
    public static int GetSizeRemains(){
        return Remains.size();
    }
    public static int GetSizeReplace(){
        return Replace.size();
    }
    public static int GetSizeSumm(){
        return Summ.size();
    }
    
    public static boolean GetBarcode_ContainsKey(String key){
        return BARCODE.containsKey(key);
    }
    public static boolean GetDuplicate_ContainsKey(String key){
        return DupVENDOR.containsKey(key);
    }
    
    public static void EraseBarcode_Val(String key){
        BARCODE.remove(key);
    }
    
    public static void EraseDupVen_Val(String key){
        DupVENDOR.remove(key);
    }
    
    public static Set<Map.Entry<String, String>> GetEntrySetBarcode(){
        return BARCODE.entrySet();
    }
    public static Set<Map.Entry<String, Integer>> GetEntrySetDuplicateVen(){
        return DupVENDOR.entrySet();
    }
    
    public static void DestructData(){
        Vendor.clear();
        NM.clear();
        SR.clear();
        Remains.clear();
        Replace.clear();
        Summ.clear();
        BARCODE.clear();
        DupVENDOR.clear();
    }
}
