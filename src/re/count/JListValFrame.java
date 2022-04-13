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

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import re.count.interfaces.GetTableModel;
import re.count.interfaces.SetTableFocus;
import static re.count.data.Data.*;

/**
 *
 * @author xavetar
 */

public class JListValFrame implements GetTableModel{
    
    private final int row;
    private final String Text;
    private final JFrame JListF;
    private final JList<Object> List;
    private final JScrollPane pane;
    private final DefaultListModel<Object> JListFModel;
    
    private static DefaultTableModel JTableModel;
    private static final List<SetTableFocus> focus = new ArrayList<>();
    
    JListValFrame(){
        row = 0;
        Text = null;
        List = null;
        JListF = null;
        pane = null;
        JListFModel = null;
    }
    
    JListValFrame(String Text, int row){
        this.Text = Text;
        this.row = row;
        List = new JList<>();
        pane =new JScrollPane(List);
        JListF = new JFrame("Select S/N");
        JListFModel = new  DefaultListModel<>();
        
        
        List.setModel(JListFModel);
        pane.setBounds(0, 30, 351, 360);
        JListF.add(pane);
        JListF.setLayout(null);
        JListF.setResizable(false);
        JListF.setSize(357,400);

        List.addKeyListener(new java.awt.event.KeyAdapter() {
             @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                List_keyPressed(e);
            }
        });
        
        JListF.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e){
                JListFClose();
            }
        });
        
        InitList();
        JListF.setVisible(true);
    }
    
    private void InitList(){
        for(int i = 0; i < GetSizeDuplicateVENDOR(Text); i++){
            JListFModel.addElement(JTableModel.getValueAt(row + i, SERIALNUMBER));
        }
        List.setSelectedIndex(0);
    }
    
    //need add atention after value > remains + 1
    //need add searcg to this windows in panel
    
    private void JListFClose(){
        SaySetTableFocus();
    }
    
    private void List_keyPressed(java.awt.event.KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(CheckCountRemains() == 0){
                new Search((String)JListFModel.getElementAt(List.getSelectedIndex()));
                JListF.setVisible(false);
            }
        }
    }
    
    private int CheckCountRemains(){
        if((int)JTableModel.getValueAt(row, JTableModel.getColumnCount()-1) >= (int)JTableModel.getValueAt(row, REMAINS)){
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to increase the value? Current > Remains", null, JOptionPane.YES_NO_OPTION);
            if(reply ==  JOptionPane.YES_OPTION){
                new Search((String)JListFModel.getElementAt(List.getSelectedIndex()));
                JListF.setVisible(false);
                return 1;
            }else{
                JListF.setVisible(false);
                JListFClose();
                return 2;
            }
        }
        return 0;
    }
    
    public void addListenerFocus(SetTableFocus toAdd){
        focus.add(toAdd);
    }
    
    private  void SaySetTableFocus(){
        for(SetTableFocus h1 : focus){
            h1.SetTableFocus();
        }
    }
    
    @Override
    public void GetTableModel(DefaultTableModel JTableModel){
        JListValFrame.JTableModel = JTableModel;
    }
}
