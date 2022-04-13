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

import re.count.initialization.InitData;
import java.util.*;
import javax.swing.*;
import java.awt.Frame;
import javax.swing.table.*;
import java.awt.FileDialog;
import java.io.IOException;
import static re.count.data.Data.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import static javax.swing.WindowConstants.*;
import re.count.interfaces.EndInit;
import re.count.interfaces.GetTableModel;
import re.count.interfaces.SetTableFocus;
import re.count.export.Export;

/**
 *
 * @author xavetar
 */

public class JTableFrame extends JTable implements EndInit, SetTableFocus{
    
    private final JTable table;
    private final JMenuBar jBar;
    private final JFrame JTableF;
    private final JMenu jMenuFile;
    private final JMenu jMenuEdit;
    private final JScrollPane pane;
    private final JTextField TextField;
    private final FileDialog remainsFile;
    private final FileDialog barcodeFile;
    private final FileDialog exportFile;
    private final JProgressBar progressBar;
    private final JMenuItem jMenuFile_Export;
    private final JMenuItem jMenuFile_Open;
    private final DefaultTableModel JTableModel;
    
    private final List<String> column;
    private final List<Integer> currentRemain;
    
    private static final long serialVersionUID = 1L;
    private static final List<GetTableModel> retModel = new ArrayList<>();
    
    JTableFrame() throws IOException{
        table = new JTable();
        jBar = new JMenuBar();
        jMenuFile = new JMenu();
        jMenuEdit = new JMenu();
        TextField = new JTextField();
        pane = new JScrollPane(table);
        JTableF = new JFrame("Recount");
        progressBar  = new JProgressBar();
        jMenuFile_Export = new JMenuItem();
        jMenuFile_Open = new JMenuItem();
        barcodeFile = new FileDialog((Frame)null, "Select Barcode File to Open");
        remainsFile = new FileDialog((Frame)null, "Select Remains File to Open");
        exportFile = new FileDialog((Frame)null, "Select Export path to Save");
        JTableModel = new DefaultTableModel(new Object [][] {},new String [] {});
        column = new ArrayList<>();
        currentRemain = new ArrayList<>();
        
        initCopmonent();
        SetFrameVisible();
    }
    
    private void initCopmonent(){
        column.add("Vendor Code");
        column.add("Name");
        column.add("S/N");
        column.add("Summ");
        column.add("Remains");
        column.add("Replace");
        column.add("Current");
        
        progressBar.setBounds(1289, 751, 200, 15);
        TextField.setBounds(550,550,400,25);
        pane.setBounds(0,24,1491,450);
        jBar.setBounds(0,0,1500,25);
        
        jMenuFile.setText("File");
        jMenuEdit.setText("Edit");
        jMenuFile_Export.setText("Export");
        jMenuFile_Open.setText("Open files");
        
        jMenuFile.add(jMenuFile_Open);
        jMenuFile.add(jMenuFile_Export);
        
        jBar.add(jMenuFile);
        jBar.add(jMenuEdit);
        
        remainsFile.setMode(FileDialog.LOAD);
        barcodeFile.setMode(FileDialog.LOAD);
        exportFile.setMode(FileDialog.SAVE);
        
        progressBar.setIndeterminate(true);
        
        JTableF.add(pane);
        JTableF.add(TextField);
        JTableF.add(jBar);
        JTableF.setLayout(null);
        JTableF.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTableF.setResizable(false);
        JTableF.setSize(1500,800);
        
        jMenuFile_Open.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    jMenuOpen_actionPerformed(e);
                } catch (IOException ex) {
                    Logger.getLogger(JTableFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        jMenuFile_Export.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                jMenuExport_actionPerformed(e);
            }
        });
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                tableMouseClicked(e);
            }
        });
        
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                table_KeyPressed(e);
            }
        });
        
        TextField.addActionListener(new AbstractAction() {
            @Override
               public void actionPerformed(ActionEvent e){
                       TextField_actionPerformed(e);
                  }
            });
    }
    
    private void loadData(final String remainsFile, final String barcodeFile){
        Thread start = new Thread(){
            @Override
                public void run(){
                    try {
                        InitData loadData = new InitData(remainsFile, barcodeFile);
                    } catch (IOException ex) {
                        Logger.getLogger(JTableFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            start.start();
    }
    
    private void SetVisibleProgressBar(int i){
        if(i == 1){
            JTableF.add(progressBar);
            JTableF.repaint();
        }else{
            JTableF.remove(progressBar);
            JTableF.repaint();
        }
    }
    
    private void ReInitTable(){
        for (int i = 0; i < GetSizeVendor(); i++){ //init current size
            currentRemain.add(0);
        }
        SetTableColumns();
        SetTableModel();
        table.setModel(JTableModel);
    }
    
    private void SetTableColumns(){
        for(int i = 0; i < column.size(); i++){
            JTableModel.addColumn(column.get(i));
        }
    }
    
    private void SetTableModel(){
        Object rowData[] = new Object[7];
        for(int i = 0; i < GetSizeVendor(); i++){
            rowData[VENDOR] = GetVendor(i);
            rowData[NOMENCKLATURE] = GetNM(i);
            rowData[SERIALNUMBER] = GetSR(i);
            rowData[SUMM] = GetSumm(i);
            rowData[REMAINS] = GetRemains(i);
            rowData[REPLACE] = GetReplace(i);
            rowData[6] = currentRemain.get(i);
            JTableModel.addRow(rowData);
        }
    }
    
    private void CleanTableModel(){
        table.removeAll();
        DestructData();
        if(JTableModel.getRowCount() > 0) {
            for (int i = JTableModel.getRowCount() - 1; i > -1; i--) {
                JTableModel.removeRow(i);
            }
            JTableModel.setRowCount(0);
        }
        if(JTableModel.getColumnCount() > 0){
            for (int i = JTableModel.getColumnCount() - 1; i > -1; i--) {
                JTableModel.setColumnCount(0);
            }
        }
    }
    
    private void SetFrameVisible(){
        JTableF.setVisible(true);
    }
    
    private void TextField_actionPerformed(ActionEvent e){
        Thread search = new Thread(){
            @Override
            public void run(){
                Search search = new Search(TextField.getText().trim());
            }
        };
        search.start();
    }
    
    private void tableMouseClicked(java.awt.event.MouseEvent e){
        boolean a = table.isEditing();
        if(a == false){
            JOptionPane.showMessageDialog(null, "You can't change this.");
        }
    }
    
    private void table_KeyPressed(KeyEvent e) {
        JOptionPane.showMessageDialog(null, "You can't change this.");
    }
    
    private void jMenuOpen_actionPerformed(ActionEvent e) throws IOException{
        if(JTableModel.getRowCount()>=1){//CHANGE
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure? All data will be wipe.", null, JOptionPane.YES_NO_OPTION);
            if(reply ==  JOptionPane.YES_OPTION){
                CleanTableModel();
            }else{
                return;
            }
        }
        remainsFile.setVisible(true);
        if(remainsFile.getFile() == null){
            JOptionPane.showMessageDialog(null, "You have not selected a file.");
            return;
        }
        barcodeFile.setVisible(true);
        if(barcodeFile.getFile() == null){
            JOptionPane.showMessageDialog(null, "You have not selected a file.");
            return;
        }
        SetVisibleProgressBar(1);
        jMenuFile_Open.setEnabled(false);
        loadData(remainsFile.getDirectory() + remainsFile.getFile(), barcodeFile.getDirectory() + barcodeFile.getFile());
    }
    
    public void addListener(GetTableModel toAdd){
        retModel.add(toAdd);
    }
    
    private void jMenuExport_actionPerformed(ActionEvent e) {
        if(JTableModel.getRowCount() < 1){
            JOptionPane.showMessageDialog(null, "The table is empty.");
            return;
        }
        exportFile.setVisible(true);
        if(exportFile.getFile() == null){
            JOptionPane.showMessageDialog(null, "Need select save path.");
            return;
        }else{
            Thread export = new Thread(){ //Warning Swing
                @Override
                public void run(){
                    try {
                        Export export = new Export(exportFile.getDirectory() + exportFile.getFile(), JTableModel);
                    } catch (IOException ex) {
                        Logger.getLogger(JTableFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            export.start();
        }
    }

   
    
    public  void SentTableModel(){
        for(GetTableModel h1 : retModel){
            h1.GetTableModel(JTableModel);
        }
    }
    
    @Override
    public void SomeoneSayEnd(){
        ReInitTable();
        SetVisibleProgressBar(0);
        jMenuFile_Open.setEnabled(true);
    }
    
    @Override
    public void SetTableFocus() {
        TextField.setText(null);
        TextField.requestFocus();
    }
    
    @Override
    public void SetTableFocus(int i) {
        table.changeSelection(i, 0, false, false);
        TextField.setText(null);
        TextField.requestFocus();
    }
}
