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
package re.count;

import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import re.count.interfaces.GetTableModel;
import re.count.interfaces.SetTableFocus;
import static re.count.data.Data.*;

/**
 *
 * @author xavetar
 */

public class Search implements GetTableModel{
    
    private int row = 0;
    private final String Text;
    private boolean find = false;
    
    private static DefaultTableModel JTableModel;
    private static final List<SetTableFocus> focus = new ArrayList<>();
    
    Search(){
        this.Text = null;
    }
    
    Search(String Text){
        this.Text = Text;
        CheckInput();
    }
    
//    Search(String SIM){
//        
//    }
    
    private void CheckInput(){//optimize with search barcode first then S/N
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                //need add search SIM-card
                if(CheckDuplicate(Text) == 1){
                    return;
                }
                if(Text.length() == 6){
                    Searcher(VENDOR);
                    if(find == true){
                        return;
                    }
                }else{ //BARCODE Search
                    Searcher(SERIALNUMBER);
                    if(find == true){
                        return;
                    }
                }
                BARCODESearch();//BARCODE Search
                SaySetTableFocus();
            }
        });
    }
    
    private int CheckDuplicate(String value){
        if(GetDuplicate_ContainsKey(value)){
            GetIndex(value);
            new JListValFrame(value, row);
            return 1;
        }
        return 0;
    }
    
    private void GetIndex(String value){
        for(int i = 0; i < JTableModel.getRowCount(); i++){ //not good work search algorithm
            if(NotNULL(Text) == 1){//if value == null break
                break;
            }
            for(int j = VENDOR; j == VENDOR; j++){
                if((value).equals(JTableModel.getValueAt(i, j).toString())){//search value in table model
                    row = i;
                    return;
                }
            }
        }
    }
    
    private void Searcher(int T){
        for(int i = 0; i < JTableModel.getRowCount(); i++){ //not good work search algorithm
            if(NotNULL(Text) == 1){//if value == null break
                break;
            }
            for(int j = T; j == T; j++){
                if((Text).equals(JTableModel.getValueAt(i, j).toString())){//search value in table model
                    SetValue(i);
                    return;
                }
            }
        }
    }
    
    private void BARCODESearch(){
        for(int i = 0; i < JTableModel.getRowCount(); i++){
            for(int j = VENDOR; j == VENDOR; j++){
                if(JTableModel.getValueAt(i, j).toString().equals(GetBARCODE(Text))){
                    if(GetDuplicate_ContainsKey(GetBARCODE(Text))){
                        CheckDuplicate(GetBARCODE(Text));
                        return;
                    }
                    SetValue(i);
                    return;
                }
            }
        }
    }
    
    private void SetValue(int i){
        JTableModel.setValueAt((((int)JTableModel.getValueAt(i, (JTableModel.getColumnCount()-1)))+1), i, (JTableModel.getColumnCount()-1));
        SaySetTableFocus(i);
        find = true;
    }
    
    private int NotNULL(Object a){
        if(a == null || a.equals("") || a.equals(" ")){
            return 1;
        }else{
            return 0;
        }
    }
    
    public void addListenerFocus(SetTableFocus toAdd){
        focus.add(toAdd);
    }
    
    private  void SaySetTableFocus(){
        for(SetTableFocus h1 : focus){
            h1.SetTableFocus();
        }
    }
        
    private  void SaySetTableFocus(int i){
        for(SetTableFocus h1 : focus){
            h1.SetTableFocus(i);
        }
    }
    
    @Override
    public void GetTableModel(DefaultTableModel JTableModel){
        Search.JTableModel = JTableModel;
    }
}