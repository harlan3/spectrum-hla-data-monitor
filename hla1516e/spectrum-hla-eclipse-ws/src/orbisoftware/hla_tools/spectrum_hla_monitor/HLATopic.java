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

import orbisoftware.hla_tools.spectrum_hla_monitor.sampleviewer.SampleViewerGUI;

public class HLATopic {

   public enum QoSProfile {
      HLA_REGION_PROFILE_1, HLA_REGION_PROFILE_2, HLA_REGION_PROFILE_3
   };

   public enum ViewMode {
      TREE_TABLE, TEXT_LIST
   };
   
   // Topic Attributes
   public String name;
   public String dataContainer;
   public String publisherType;
   public String receiverType;
   public QoSProfile topicQos;
   
   // View mode
   public ViewMode viewMode;
   
   // Auto Update
   public boolean autoUpdate;

   // Subscriber
   public boolean subscriberAttached;

   // Topic Subscriber Thread
   public boolean topicReaderWriterFinished;
   public HLATopicReaderWriter topicReaderWriter;

   // Read Event Handler Thread
   public boolean readEventHandlerShutdownComplete;
   public HLAReadEventHandler readEventHandler;
   
   // Data Logger Thread
   public boolean dataLoggerShutdownComplete;
   public HLADataLogger dataLogger;

   // Sample Viewer GUI
   public SampleViewerGUI sampleViewerGUI;
}
