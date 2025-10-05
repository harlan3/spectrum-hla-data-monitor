/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2024 Harlan Murphy
 *  Orbis Software - orbisoftware@gmail.com
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */

package orbisoftware.hla_tools.spectrum_hla_monitor.sampleviewer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

import orbisoftware.hla_tools.spectrum_hla_monitor.HLATopic;
import orbisoftware.hla_tools.spectrum_hla_monitor.TopicManager;
import orbisoftware.oricrdsm.OricSymbolMap;

import org.netbeans.swing.outline.DefaultOutlineModel;

public class SampleViewerGUI implements ActionListener {

   private int oricSymMapCount;
   private org.netbeans.swing.outline.Outline outline;
   private SampleTextList sampleTextList;
   private SymbolRowModel symbolRowModel;
   private JFrame frame;
   private boolean treeTableInitialized;
   private JButton publishButton;
   private JButton injectButton;
   private JButton resizeButton;
   private JButton clearTextListButton;
   private HLATopic hlaTopic;
   private String resizeNodeSymbol;
   private JPanel receivedHLAPanel;
   private JRadioButtonMenuItem collapseMenuItem;
   private JRadioButtonMenuItem expandMenuItem;
   private JRadioButtonMenuItem treeMenuItem;
   private JRadioButtonMenuItem listMenuItem;
   private JLabel autoUpdateLabel;
   private JCheckBox autoUpdateCheckBox;

   private ListSelectionListener listSelectionListener = null;

   private JPanel treeTablePanel;

   private JPanel cardSampleViewerPanel;

   public SampleViewerGUI(String keyName) {

      Map<String, HLATopic> topicMap = TopicManager.getInstance().topicMap;

      hlaTopic = topicMap.get(keyName);

      // Create JFrame
      frame = new JFrame();
      frame.setBounds(20, 20, 950, 600);
      frame.setTitle(keyName);
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            shutdownReq();
         }
      });

      receivedHLAPanel = new JPanel();
      initSampleViewerGUI();
   }

   public void initSampleViewerGUI() {

      JMenuBar menuBar;
      JMenu menu;
      JMenuItem openObjectRDSM, saveObjectRDSM, exitMenuItem;
      Path currentWorkingDir = Paths.get("").toAbsolutePath().getParent();
      
      // Create the menu bar.
      menuBar = new JMenuBar();

      // Build the first menu.
      menu = new JMenu("File");
      menuBar.add(menu);
      frame.setJMenuBar(menuBar);
      
      openObjectRDSM = new JMenuItem("Open Object rdsm");
      menu.add(openObjectRDSM);
      openObjectRDSM.addActionListener(new ActionListener() {
         
         public void actionPerformed(ActionEvent ae) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Recursive Descent Symbol Map", "rdsm");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(currentWorkingDir.toString()));
            int result = fileChooser.showOpenDialog(fileChooser);
            if (result == JFileChooser.APPROVE_OPTION) {
               File selectedFile = fileChooser.getSelectedFile();
               //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
               
               OricSymbolMap oricSymbolMap = new OricSymbolMap();
               StringBuffer buffer = new StringBuffer();
               
               try (BufferedReader br = new BufferedReader(new FileReader(selectedFile.getAbsolutePath()))) {
                  
                  String line;
                  
                  while ((line = br.readLine()) != null) {
                     buffer.append(line);
                  }
               } catch (Exception e) {
                  
                  e.printStackTrace();
               }
              
               oricSymbolMap.bufferToSymMap(buffer);
               createOutlineTreeTable(oricSymbolMap);
               publishButton.setEnabled(true);
            }
         }
      });

      saveObjectRDSM = new JMenuItem("Save Object rdsm");
      menu.add(saveObjectRDSM);
      saveObjectRDSM.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Recursive Descent Symbol Map", "rdsm");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(currentWorkingDir.toString()));
            int result = fileChooser.showSaveDialog(fileChooser);
            if (result == JFileChooser.APPROVE_OPTION) {
               String selectedFile = fileChooser.getSelectedFile().getAbsolutePath().toString();
               if (!selectedFile.endsWith(".rdsm"))
                  selectedFile += ".rdsm";
               //System.out.println("Selected file: " + selectedFile);
               
               OricSymbolMap oricSymbolMap = new OricSymbolMap();
               StringBuffer buffer = new StringBuffer();
               Object objInstance;
               
               objInstance = symbolRowModel.getOricSymbolMap().symMapToObj();
               oricSymbolMap.objToSymMapBuffer(objInstance, buffer);
               
               try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
                  
                  writer.write(buffer.toString());
                  
               } catch (Exception e) {
                  e.printStackTrace();
               }
            }
         }
      });

      menu.addSeparator();
      exitMenuItem = new JMenuItem("Exit");
      menu.add(exitMenuItem);
      exitMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            System.exit(0);
         }
      });
      
      Container pane = frame.getContentPane();
      pane.setLayout(new GridBagLayout());

      JPanel mainControlPanel = new JPanel();
      mainControlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

      JPanel nodeStatePanel = new JPanel();
      JPanel viewModePanel = new JPanel();
      JPanel sampleControlPanel = new JPanel();
      
      JButton clearEntriesButton  = new JButton("Clear All");
      clearEntriesButton.setEnabled(true);
      sampleControlPanel.add(clearEntriesButton);
      clearEntriesButton.addActionListener(hlaTopic.readEventHandler.receiveListGUI);
      
      receivedHLAPanel.setLayout(new BoxLayout(receivedHLAPanel, BoxLayout.Y_AXIS));

      TitledBorder title1 = BorderFactory.createTitledBorder(receivedHLAPanel.getBorder(), "Received Samples");
      title1.setTitleJustification(TitledBorder.CENTER);
      receivedHLAPanel.setBorder(title1);

      receivedHLAPanel.add(Box.createVerticalStrut(10));
      
      clearEntriesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      receivedHLAPanel.add(clearEntriesButton);

      receivedHLAPanel.add(Box.createVerticalStrut(10));
      
      receivedHLAPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
      JScrollPane listScrollPane = new JScrollPane(hlaTopic.readEventHandler.receiveListGUI.displayList);
      receivedHLAPanel.add(listScrollPane);

      // Used fixed size for sample list
      Dimension fixedSize = new Dimension(150, 150);
      receivedHLAPanel.setMinimumSize(fixedSize);
      receivedHLAPanel.setPreferredSize(fixedSize);
      receivedHLAPanel.setMaximumSize(fixedSize);
      
      // Sample view with outline tree table
      treeTablePanel = new JPanel();
      treeTablePanel.setLayout(new BorderLayout());
      treeTablePanel.add(new JScrollPane(outline));

      // Sample view with text list
      JPanel textListPanel = new JPanel();
      textListPanel.setLayout(new BorderLayout());
      sampleTextList = new SampleTextList();
      textListPanel.add(new JScrollPane(sampleTextList));

      cardSampleViewerPanel = new JPanel(new CardLayout());
      cardSampleViewerPanel.add(treeTablePanel, "TREE_TABLE");
      cardSampleViewerPanel.add(textListPanel, "TEXT_LIST");
      cardSampleViewerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
      
      createCommonControlPanels(nodeStatePanel, viewModePanel,
            sampleControlPanel);

      mainControlPanel.add(nodeStatePanel);
      mainControlPanel.add(viewModePanel);
      mainControlPanel.add(sampleControlPanel);

      GridBagConstraints c = new GridBagConstraints();
      c.gridx = 0;
      c.gridy = 0;
      c.weightx = 1.0;
      c.weighty = 1;
      c.fill = GridBagConstraints.BOTH;
      c.insets = new Insets(0, 15, 10, 0);
      pane.add(mainControlPanel, c);
      
      c.gridx = 0;
      c.gridy = 1;
      c.weightx = 50.0;
      c.weighty = 50.0;
      c.fill = GridBagConstraints.BOTH;
      pane.add((cardSampleViewerPanel), c);
     
      c.gridx = 1;
      c.gridy = 0;
      c.weightx = 1.0;
      c.weighty = 15.0;
      c.fill = GridBagConstraints.VERTICAL;
      c.gridheight = GridBagConstraints.REMAINDER;
      pane.add(receivedHLAPanel, c);

      switch (hlaTopic.topicQos) {
         
      case HLA_REGION_PROFILE_1:
      case HLA_REGION_PROFILE_2:
      case HLA_REGION_PROFILE_3:
      default:
         
         hlaTopic.viewMode = HLATopic.ViewMode.TREE_TABLE;
         treeMenuItem.setSelected(true);
         collapseMenuItem.setSelected(true);
         injectButton.setVisible(true);
         break;
      }
      
      frame.setVisible(true);

      hlaTopic.autoUpdate = false;
      treeTableInitialized = false;
   }

   private void createCommonControlPanels(JPanel nodeStatePanel,
         JPanel viewModePanel, JPanel sampleControlPanel) {

      // Create radio button menu items
      treeMenuItem = new JRadioButtonMenuItem();
      listMenuItem = new JRadioButtonMenuItem();
      collapseMenuItem = new JRadioButtonMenuItem();
      expandMenuItem = new JRadioButtonMenuItem();

      // Create NodeState button group
      ButtonGroup nodeStateButtonGroup = new ButtonGroup();
      Border nodeStateBorder = BorderFactory
            .createTitledBorder(
                  BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                  "Node State");
      
      // Populate ViewMode panel
      nodeStatePanel.setBorder(nodeStateBorder);
      nodeStatePanel.setLayout(new BoxLayout(nodeStatePanel, BoxLayout.Y_AXIS));
      
      collapseMenuItem.setBorder(BorderFactory.createEmptyBorder());
      collapseMenuItem.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
            if (selected && outline != null) {
               collapseNodes();
            }
         }
      });
      
      collapseMenuItem.setText(" Collapse");
      nodeStateButtonGroup.add(collapseMenuItem);
      nodeStatePanel.add(collapseMenuItem);
      
      expandMenuItem.setBorder(BorderFactory.createEmptyBorder());
      expandMenuItem.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
            if (selected && outline != null) {
               expandNodes();
            }
         }
      });   
      expandMenuItem.setText(" Expand");
      nodeStateButtonGroup.add(expandMenuItem);
      nodeStatePanel.add(expandMenuItem);
      
      // Create ViewMode button group
      ButtonGroup viewModeButtonGroup = new ButtonGroup();
      Border viewModeBorder = BorderFactory
            .createTitledBorder(
                  BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                  "View Mode");

      // Populate ViewMode panel
      viewModePanel.setBorder(viewModeBorder);
      viewModePanel.setLayout(new BoxLayout(viewModePanel, BoxLayout.Y_AXIS));
      treeMenuItem.setBorder(BorderFactory.createEmptyBorder());
      treeMenuItem.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
            if (selected) {
               hlaTopic.viewMode = HLATopic.ViewMode.TREE_TABLE;
               CardLayout cardLayout = (CardLayout) (cardSampleViewerPanel
                     .getLayout());
               cardLayout.show(cardSampleViewerPanel, "TREE_TABLE");
               treeMenuItem.setSelected(true);
               nodeStatePanel.setVisible(true);
               injectButton.setVisible(true);
               resizeButton.setVisible(true);
               publishButton.setVisible(true);
               clearTextListButton.setVisible(false);
               autoUpdateLabel.setVisible(true);
               autoUpdateCheckBox.setVisible(true);
               receivedHLAPanel.setVisible(true);
            }
         }
      });
      treeMenuItem.setText(" Decomposition");
      viewModeButtonGroup.add(treeMenuItem);

      listMenuItem.setBorder(BorderFactory.createEmptyBorder());
      listMenuItem.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
            if (selected) {
               hlaTopic.viewMode = HLATopic.ViewMode.TEXT_LIST;
               CardLayout cardLayout = (CardLayout) (cardSampleViewerPanel
                     .getLayout());
               cardLayout.show(cardSampleViewerPanel, "TEXT_LIST");
               listMenuItem.setSelected(true);
               nodeStatePanel.setVisible(false);
               injectButton.setVisible(false);
               resizeButton.setVisible(false);
               publishButton.setVisible(false);
               clearTextListButton.setVisible(true);
               autoUpdateLabel.setVisible(true);
               autoUpdateCheckBox.setVisible(true);
               receivedHLAPanel.setVisible(false);
            }
         }
      });
      listMenuItem.setText(" Serialization");
      viewModeButtonGroup.add(listMenuItem);
      viewModePanel.add(treeMenuItem);
      viewModePanel.add(listMenuItem);

      // Populate Sample Control panel region
      // Auto Update checkbox
      autoUpdateLabel = new JLabel("Auto Update");
      autoUpdateCheckBox = new JCheckBox();
      autoUpdateCheckBox.setSelected(false);
      autoUpdateCheckBox.addItemListener(new ItemListener() {
         
         public void itemStateChanged(ItemEvent e) {
            
            boolean selected = (e.getStateChange() == ItemEvent.SELECTED);

            try {

               Class<?> singletonClass = Class.forName(hlaTopic.receiverType);
               Method getInstanceMethod = singletonClass
                     .getMethod("getInstance");
               Object singletonInstance = getInstanceMethod.invoke(null);

               Field declaredField = singletonClass
                     .getDeclaredField("autoUpdateEnabled");
               declaredField.setAccessible(true);
               declaredField.set(singletonInstance, selected);

            } catch (Exception e1) {
               e1.printStackTrace();
            }
         }
      });
      
      sampleControlPanel.add(Box.createRigidArea(new Dimension(5, 0)));
      sampleControlPanel.add(autoUpdateCheckBox);
      autoUpdateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
      sampleControlPanel.add(autoUpdateLabel);

      // Inject button
      injectButton = new JButton("Inject");
      injectButton.setEnabled(true);
      sampleControlPanel.add(injectButton);
      injectButton.addActionListener(this);
      
      // Publish button
      publishButton = new JButton("Publish");
      publishButton.setEnabled(false);
      sampleControlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      sampleControlPanel.add(publishButton);
      publishButton.addActionListener(this);

      // Resize button
      resizeButton = new JButton("Resize Array");
      resizeButton.setEnabled(false);
      sampleControlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      sampleControlPanel.add(resizeButton);
      resizeButton.addActionListener(this);
      
      // Clear text list  button
      clearTextListButton = new JButton("Clear All");
      clearTextListButton.setEnabled(true);
      sampleControlPanel.add(clearTextListButton);
      clearTextListButton.addActionListener(this);
   }

   private void createOutlineTreeTable(OricSymbolMap oricSymMap) {

      SymbolMapTreeModel treeMdl = new SymbolMapTreeModel(oricSymMap);
      org.netbeans.swing.outline.OutlineModel mdl;

      if ((outline != null) && (listSelectionListener != null)) {

         outline.getSelectionModel().removeListSelectionListener(
               listSelectionListener);
      }

      treeTablePanel.removeAll();

      outline = new org.netbeans.swing.outline.Outline();
      outline.setRenderDataProvider(new RenderSymbolMap(oricSymMap));
      outline.getTableHeader().setReorderingAllowed(false);
      outline.setColumnHidingAllowed(false);
      outline.setSelectionMode(0);
      
      listSelectionListener = new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {

            int row = outline.getSelectedRow();
            if (!e.getValueIsAdjusting() && (row > -1)
                  && ((String) outline.getValueAt(row, 1)).equals("ARRAY")) {
               resizeNodeSymbol = (String) outline.getValueAt(row, 0);
               if (!resizeButton.isEnabled())
                  resizeButton.setEnabled(true);
            } else {
               if (resizeButton.isEnabled())
                  resizeButton.setEnabled(false);
            }
         }
      };

      outline.getSelectionModel().addListSelectionListener(
            listSelectionListener);

      symbolRowModel = new SymbolRowModel();
      symbolRowModel.setSymbolMap(oricSymMap);
      oricSymMapCount = oricSymMap.getSymbolCount();

      mdl = DefaultOutlineModel.createOutlineModel(treeMdl, symbolRowModel,
            true);
      outline.setRootVisible(false);
      outline.setModel(mdl);
      outline.setCellSelectionEnabled(true);
      
      treeTablePanel.add(new JScrollPane(outline));
      treeTablePanel.revalidate();
      
      treeTableInitialized = true;
   }

   private void expandNodes() {
      
      int prevRowCount = 0;
      int currentRowCount = 0;
      boolean expansionCompleted = false;
      
      while (!expansionCompleted) {
         
         currentRowCount = outline.getRowCount();
         
         if (currentRowCount == prevRowCount)
            expansionCompleted = true;
         else
            prevRowCount = currentRowCount;
         
         int pathIndex = 0;
         
         while (!expansionCompleted && (pathIndex < currentRowCount)) {
          
            TreePath selectedPath = outline.getLayoutCache().getPathForRow(pathIndex);
            
            if (selectedPath != null) {

               outline.expandPath(selectedPath);
            }
            
            pathIndex++;               
         }
      }
   }
   
   private void collapseNodes() {
      
      int prevRowCount = 0;
      int currentRowCount = 0;
      boolean collapsionComplete = false;
      
      while (!collapsionComplete) {
         
         currentRowCount = outline.getRowCount();
         
         if (currentRowCount == prevRowCount)
            collapsionComplete = true;
         else
            prevRowCount = currentRowCount;
         
         int pathIndex = 0;
         
         while (!collapsionComplete && (pathIndex < currentRowCount)) {
          
            TreePath selectedPath = outline.getLayoutCache().getPathForRow(pathIndex);
            
            if (selectedPath != null) {

               outline.collapsePath(selectedPath);
            }
            
            pathIndex++;               
         }
      }
   }
   
   public void updateTreeTable(OricSymbolMap newOricSymMap) {

      // Create tree table on first call
      if (!treeTableInitialized) {
         createOutlineTreeTable(newOricSymMap);
         return;
      }

      // If the number of symbols has changed, the entire tree table
      // needs to be regenerated.
      if (newOricSymMap.getSymbolCount() != oricSymMapCount) {
         createOutlineTreeTable(newOricSymMap);
      } else {
         // Update model to new symbol map
         symbolRowModel.setSymbolMap(newOricSymMap);

         // Generate a table model event to refresh table
         TableModel tableModel = outline.getModel();
         TableModelEvent tableModelEvent = new TableModelEvent(tableModel);
         outline.tableChanged(tableModelEvent);
      }
   }

   public void updateTextTable(String sampleXml) {

      sampleTextList.writeXML(sampleXml);
   }

   public void shutdownReq() {

      hlaTopic.subscriberAttached = false;

      // Start the shutdown sequence for the read event handler thread
      hlaTopic.readEventHandler.shutdownReq();
      
      // Start the shutdown sequence for the data logger thread
      if (hlaTopic.dataLogger != null)
         hlaTopic.dataLogger.shutdownReq();

      while ( !hlaTopic.readEventHandlerShutdownComplete
           || !hlaTopic.dataLoggerShutdownComplete) {
         
         try {
            Thread.sleep(200);
         } catch (InterruptedException e) {
         }
      }

      frame.dispose();
   }

   @Override
   public void actionPerformed(ActionEvent event) {

      if ((JButton) event.getSource() == publishButton) {

         try {
            
            Object objectRef = symbolRowModel.getOricSymbolMap().symMapToObj();
            Class<?> singletonClass = Class.forName(hlaTopic.publisherType);
            Method getInstanceMethod = singletonClass.getMethod("getInstance"); 
            Object singletonInstance = getInstanceMethod.invoke(null);

            Method targetMethod = singletonClass.getMethod("sideLoadPublishSample", objectRef.getClass());
            Object[] methodArgs = {objectRef};
            targetMethod.invoke(singletonInstance, methodArgs);
            
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else if ((JButton) event.getSource() == injectButton) {

         Class<?> hlaObjectClass = null;
         Object objInstance = null;
         OricSymbolMap oricSymbolMap = new OricSymbolMap();

         try {
            hlaObjectClass = Class.forName(hlaTopic.dataContainer);
            objInstance = hlaObjectClass.newInstance();

            oricSymbolMap.instantiateObj(objInstance);
            oricSymbolMap.objToSymMap(objInstance);
         } catch (Exception e) {
            System.err.println("ERROR: SequenceHolder instantiation failed");
         }
         updateTreeTable(oricSymbolMap);
         publishButton.setEnabled(true);
         
      } else if ((JButton) event.getSource() == resizeButton) {

         ResizeBox resizeBox = new ResizeBox();
         int newArraySize = resizeBox.ShowDialog();

         // Resize array in symbol map and instantiate new object
         OricSymbolMap oricSymbolMap = symbolRowModel.getOricSymbolMap();
         oricSymbolMap.setFieldNumChildren(resizeNodeSymbol,
               Integer.toString(newArraySize));
         Object newObject = oricSymbolMap.symMapToObj();
         oricSymbolMap.instantiateObj(newObject);

         // Create new symbol map based on resized object
         OricSymbolMap newOricSymbolMap = new OricSymbolMap();
         newOricSymbolMap.objToSymMap(newObject);
         updateTreeTable(newOricSymbolMap);
      } else if ((JButton) event.getSource() == clearTextListButton) {
         
         sampleTextList.clearAll();
      }
   }
}