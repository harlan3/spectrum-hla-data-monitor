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

package orbisoftware.hla_tools.spectrum_hla_monitor;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import orbisoftware.hla_tools.spectrum_hla_monitor.sampleviewer.SampleViewerGUI;


@SuppressWarnings("serial")
class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

   CheckBoxRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
   }

   public Component getTableCellRendererComponent(JTable table, Object value,
         boolean isSelected, boolean hasFocus, int row, int column) {
      if (isSelected) {
         setForeground(table.getSelectionForeground());
         setBackground(table.getSelectionBackground());
      } else {
         setForeground(table.getForeground());
         setBackground(table.getBackground());
      }
      setSelected((value != null && ((Boolean) value).booleanValue()));
      return this;
   }
}

class ColumnListener extends MouseAdapter {
   private JTable table;
   private static boolean allTopicsSelected = false;

   public ColumnListener(JTable t) {
      table = t;
   }

   public void mouseClicked(MouseEvent e) {

      TableColumnModel colModel = table.getColumnModel();
      int columnModelIndex = colModel.getColumnIndexAtX(e.getX());

      if (columnModelIndex == 0) {

         int rowCount = table.getModel().getRowCount();

         if (!allTopicsSelected) {
            for (int i = 0; i < rowCount; i++) {
               table.getModel().setValueAt(true, i, 0);
            }
            allTopicsSelected = true;
         } else {

            for (int i = 0; i < rowCount; i++) {
               table.getModel().setValueAt(false, i, 0);
            }
            allTopicsSelected = false;
         }
      }
   }
}

@SuppressWarnings("serial")
public class TopicSubscriberGUI implements ItemListener, ActionListener {

   private int SELECTED_COL = 0;
   private int TOPIC_COL = 1;
   private int CONTAINER_COL = 2;
   private int PUBLISHER_COL = 3;
   private int RECEIVER_COL = 4;
   private int QOS_COL = 5;

   private JFrame frame;
   private JTable table;
   private JButton subscriberButton;
   private JButton unSubscribeButton;
   private JMenuItem openManifestMenuItem;
   private JMenuItem exitMenuItem;
   private JMenuItem aboutMenuItem;
   private CustomFileFilter filter;
   private JFileChooser fileChooser;

   public static long sessionStartTime;

   public TopicSubscriberGUI() {

      frame = new JFrame();

      initComponents();
      frame.setVisible(true);
   }

   private void initComponents() {

      frame.setTitle("Spectrum HLA Monitor");
      frame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            shutdown();
         }
      });
      frame.setSize(700, 400);

      JScrollPane tableScrollPane;

      // Application menubar
      JMenuBar menuBar = new JMenuBar();
      JMenu fileMenu = new JMenu();
      JMenu helpMenu = new JMenu();

      // File Menu
      openManifestMenuItem = new JMenuItem();
      openManifestMenuItem.setText("Open URI Manifest");
      openManifestMenuItem.addActionListener(this);
      fileMenu.add(openManifestMenuItem);
      exitMenuItem = new JMenuItem();
      fileMenu.setText("File");
      exitMenuItem.setText("Exit");
      exitMenuItem.addActionListener(this);
      fileMenu.add(exitMenuItem);

      // Help Menu
      helpMenu.setText("Help");
      aboutMenuItem = new JMenuItem();
      aboutMenuItem.setText("About");
      aboutMenuItem.addActionListener(this);
      helpMenu.add(aboutMenuItem);

      menuBar.add(fileMenu);
      menuBar.add(helpMenu);

      filter = new CustomFileFilter();

      // URI Manifest file chooser
      fileChooser = new JFileChooser(System.getProperty("user.dir"));

      filter.addExtension("txt");
      filter.setDescription("URI Manifest Files");

      fileChooser.setFileFilter(filter);

      populateTopicSubscriberTable();
      tableScrollPane = new JScrollPane(table);

      Container pane = frame.getContentPane();
      pane.setLayout(new GridBagLayout());

      GridBagConstraints c = new GridBagConstraints();
      c.weightx = 1.0;
      c.weighty = 0.2;
      c.fill = GridBagConstraints.BOTH;
      c.gridx = 0;
      c.gridy = 0;
      pane.add(menuBar, c);

      c.weightx = 1.0;
      c.weighty = 2.0;
      c.fill = GridBagConstraints.BOTH;
      c.gridx = 0;
      c.gridy = 1;
      c.insets = new Insets(10, 10, 10, 10);
      pane.add(tableScrollPane, c);

      JPanel panel = new JPanel(new FlowLayout());

      subscriberButton = new JButton("Subscribe");
      panel.add(subscriberButton);
      subscriberButton.addActionListener(this);

      panel.add(Box.createRigidArea(new Dimension(5, 0)));

      unSubscribeButton = new JButton("UnSubscribe");
      panel.add(unSubscribeButton);
      unSubscribeButton.addActionListener(this);

      c.weightx = 1.0;
      c.weighty = 0.1;
      c.gridx = 0;
      c.gridy = 2;
      pane.add(panel, c);
   }

   private void populateTopicSubscriberTable() {

      // Topic Subscriber Table
      DefaultTableModel dm = new DefaultTableModel() {
         public boolean isCellEditable(int row, int column) {
            if ((column == 0) || (column == 1))
               return true;
            else
               return false;
         }
      };

      dm.setColumnIdentifiers(new Object[] { "Select", "Topic",
            "Container Data Type", "Publisher Type", "Receiver Type", "HLA Region Profile" });

      table = new JTable(dm);

      JCheckBox topicCheckBox = new JCheckBox();
      topicCheckBox.addItemListener(this);
      topicCheckBox.setHorizontalAlignment(JLabel.CENTER);

      CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer();
      DefaultCellEditor checkBoxEditor = new DefaultCellEditor(topicCheckBox);

      table.getColumn("Select").setCellRenderer(checkBoxRenderer);
      table.getColumn("Select").setCellEditor(checkBoxEditor);

      Map<String, HLATopic> topicMap = TopicManager.getInstance().topicMap;
      Iterator<Map.Entry<String, HLATopic>> it = topicMap.entrySet().iterator();
      int row = 0;

      while (it.hasNext()) {
         Map.Entry<String, HLATopic> pairs = it.next();
         String key = pairs.getKey();

         dm.addRow(new Object[] { new Boolean(false),
               topicMap.get(key).name,
               topicMap.get(key).dataContainer,
               topicMap.get(key).publisherType,
               topicMap.get(key).receiverType,
               topicMap.get(key).topicQos.toString() });
         row++;
      }

      table = autoResizeColWidth(table, dm);
      table.getTableHeader().addMouseListener(new ColumnListener(table));
   }

   private JTable autoResizeColWidth(JTable table, DefaultTableModel model) {

      table.setModel(model);

      int margin = 5;

      for (int i = 0; i < table.getColumnCount(); i++) {

         int vColIndex = i;
         DefaultTableColumnModel colModel = (DefaultTableColumnModel) table
               .getColumnModel();
         TableColumn col = colModel.getColumn(vColIndex);
         int width = 0;

         // Get width of column header
         TableCellRenderer renderer = col.getHeaderRenderer();

         if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
         }

         Component comp = renderer.getTableCellRendererComponent(table,
               col.getHeaderValue(), false, false, 0, 0);

         width = comp.getPreferredSize().width;

         // Get maximum width of column data
         for (int r = 0; r < table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, vColIndex);
            comp = renderer.getTableCellRendererComponent(table,
                  table.getValueAt(r, vColIndex), false, false, r, vColIndex);
            width = Math.max(width, comp.getPreferredSize().width);
         }

         // Add margin
         width += 2 * margin;
         
         // Force column widths for selected columns
         if ((i==1) || (i==5))
            width *= 10;
         
         // Set the width
         col.setPreferredWidth(width);
      }

      table.setAutoCreateRowSorter(true);
      table.getRowSorter().toggleSortOrder(TOPIC_COL);

      return table;
   }

   public void itemStateChanged(ItemEvent e) {

      int row = table.getSelectionModel().getAnchorSelectionIndex();

      @SuppressWarnings("unused")
      String topicName = (String) table.getModel().getValueAt(
            table.convertRowIndexToModel(row), TOPIC_COL);

      if (e.getStateChange() == ItemEvent.SELECTED) {
      }
   }

   public void actionPerformed(ActionEvent e) {
      
      if (e.getSource() == subscriberButton) {

         for (int i = 0; i < table.getModel().getRowCount(); i++) {

            boolean selected = (Boolean) table.getModel().getValueAt(
                  table.convertRowIndexToModel(i), SELECTED_COL);

            // Topic is selected
            if (selected) {
               String topicName = (String) table.getModel().getValueAt(
                     table.convertRowIndexToModel(i), TOPIC_COL);
               String keyName = topicName;

               Map<String, HLATopic> topicMap = TopicManager.getInstance().topicMap;

               HLATopic hlaTopic = topicMap.get(keyName);

               // the hla partition must have been changed by the user,
               // resulting in a new keyName which needs to be put
               // into topic manager
               if (hlaTopic == null) {
                  hlaTopic = new HLATopic();

                  hlaTopic.name = topicName;
                  hlaTopic.dataContainer = (String) table.getModel().getValueAt(
                        table.convertRowIndexToModel(i), CONTAINER_COL);
                  hlaTopic.publisherType = (String) table.getModel().getValueAt(
                        table.convertRowIndexToModel(i), PUBLISHER_COL);
                  hlaTopic.receiverType = (String) table.getModel().getValueAt(
                        table.convertRowIndexToModel(i), RECEIVER_COL);
                  hlaTopic.topicQos = HLATopic.QoSProfile
                        .valueOf((String) table.getModel().getValueAt(
                              table.convertRowIndexToModel(i), QOS_COL));

                  TopicManager.getInstance().topicMap.put(keyName, hlaTopic);
               }

               if (!hlaTopic.subscriberAttached) {

                  // Create topic subscriber
                  hlaTopic.topicReaderWriter = new HLATopicReaderWriter(
                        hlaTopic);
                  boolean connected = false;

                  try {
                     connected = hlaTopic.topicReaderWriter.HLAConnect();
                     
                  } catch (Exception e2) {
                     System.out
                           .println("Exception encountered when connecting to HLA service.");
                     System.out
                           .println("Please verify that HLA service is running.");
                  }
                  
                  if (connected) {
                     hlaTopic.subscriberAttached = true;

                     // Create topic GUI
                     hlaTopic.sampleViewerGUI = new SampleViewerGUI(keyName);

                     // Start subscriber thread
                     hlaTopic.topicReaderWriter.start();
                  }
               }
            }
         }
      } else if (e.getSource() == unSubscribeButton) {

         for (int i = 0; i < table.getModel().getRowCount(); i++) {

            boolean selected = (Boolean) table.getModel().getValueAt(
                  table.convertRowIndexToModel(i), SELECTED_COL);

            // Topic is selected
            if (selected) {

               String topicName = (String) table.getModel().getValueAt(
                     table.convertRowIndexToModel(i), TOPIC_COL);
               String keyName = topicName;

               Map<String, HLATopic> topicMap = TopicManager.getInstance().topicMap;
               HLATopic hlaTopic = topicMap.get(keyName);

               if (hlaTopic.subscriberAttached) {
                  hlaTopic.sampleViewerGUI.shutdownReq();
               }
            }
         }
      } else if (e.getSource() == openManifestMenuItem) {

         int returnVal = fileChooser.showOpenDialog(null);

         if (returnVal == JFileChooser.APPROVE_OPTION) {

            // Close all existing topics
            HashMap<String, HLATopic> topicMap = TopicManager.getInstance().topicMap;

            Iterator<Map.Entry<String, HLATopic>> it = topicMap.entrySet()
                  .iterator();
            while (it.hasNext()) {
               Map.Entry<String, HLATopic> pairs = it.next();
               HLATopic topic = topicMap.get(pairs.getKey());

               if (topic.subscriberAttached)
                  topic.sampleViewerGUI.shutdownReq();
            }

            // Parse new manifest file
            SpectrumHLAMonitor.parseURIManifest(fileChooser.getSelectedFile()
                  .getPath());

            frame.getContentPane().removeAll();
            populateTopicSubscriberTable();
            initComponents();
            frame.setVisible(true);
         }

      } else if (e.getSource() == exitMenuItem) {
         shutdown();
      } else if (e.getSource() == aboutMenuItem) {
         @SuppressWarnings("unused")
         AboutBox aboutBox = new AboutBox();
      }
   }

   public void shutdown() {

      TopicManager topicManager = TopicManager.getInstance();
      HashMap<String, HLATopic> topicMap = topicManager.topicMap;

      Iterator<Map.Entry<String, HLATopic>> it = topicMap.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry<String, HLATopic> pairs = it.next();
         HLATopic topic = topicMap.get(pairs.getKey());

         if (topic.subscriberAttached)
            topic.sampleViewerGUI.shutdownReq();
      }

      // Start the shutdown sequence for the receiver hla federate thread
      orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.HLAFederateThread.shutdownReq();

      // Start the shutdown sequence for the sender hla federate thread
      orbisoftware.hla_tools.spectrum_hla_monitor.hla_sender.HLAFederateThread.shutdownReq();

      while ( !orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.HLAFederateThread.shutdownComplete
           || !orbisoftware.hla_tools.spectrum_hla_monitor.hla_sender.HLAFederateThread.shutdownComplete) {
         try {
            Thread.sleep(200);
         } catch (InterruptedException e) {
         }
      }

      System.exit(0);
   }
}
