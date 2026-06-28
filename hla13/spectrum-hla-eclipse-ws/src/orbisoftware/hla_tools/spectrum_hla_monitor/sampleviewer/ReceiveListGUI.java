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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.Timer;

import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.ReceiveQueue;

public class ReceiveListGUI implements PropertyChangeListener, ActionListener {

   private static final int MAX_DISPLAY_ENTRIES = 25;

   public ReceiveQueue receiveQueue = new ReceiveQueue();

   private final DefaultListModel<String> model = new DefaultListModel<>();
   public final JList<String> displayList = new JList<>(model);

   /*
    * Property-change events may arrive from an HLA receiver thread.  Do not
    * modify DefaultListModel from that thread: Swing components and their
    * models must be changed only on the Event Dispatch Thread (EDT).
    */
   private final Queue<String> pendingDisplayEntries = new ConcurrentLinkedQueue<>();
   private final AtomicInteger reservedDisplayEntries = new AtomicInteger();
   private final Timer displayListRefreshTimer;

   public ReceiveListGUI() {

      // Swing Timer callbacks execute on the EDT every 100 ms.
      displayListRefreshTimer = new Timer(100, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            refreshDisplayList();
         }
      });
      displayListRefreshTimer.setCoalesce(true);
      displayListRefreshTimer.start();
   }

   public void propertyChange(PropertyChangeEvent evt) {

      if (!"hlaReadEvent".equals(evt.getPropertyName())) {
         return;
      }

      /*
       * Reserve one of the 25 display slots before modifying either queue.
       * This preserves the original limit even when receiver events arrive
       * faster than the 100 ms UI refresh timer.
       */
      while (true) {
         int entryCount = reservedDisplayEntries.get();

         if (entryCount >= MAX_DISPLAY_ENTRIES) {
            return;
         }

         if (reservedDisplayEntries.compareAndSet(entryCount, entryCount + 1)) {
            break;
         }
      }

      HLASamples samples = (HLASamples) evt.getNewValue();

      synchronized (receiveQueue) {
         receiveQueue.pushBack(samples);
         receiveQueue.notify();
      }

      // Queue the text only. The timer updates the Swing list model on the EDT.
      pendingDisplayEntries.offer(
            "TimeStamp: " + Long.toString(samples.sampleReadTime));
   }

   /**
    * Runs on the EDT, draining received entries into the displayed list.
    */
   private void refreshDisplayList() {

      while (model.getSize() < MAX_DISPLAY_ENTRIES) {
         String entry = pendingDisplayEntries.poll();

         if (entry == null) {
            break;
         }

         model.addElement(entry);
      }

      displayList.revalidate();
      displayList.repaint();
   }

   @Override
   public void actionPerformed(ActionEvent e) {

      synchronized (receiveQueue) {
         receiveQueue.clear();
      }

      pendingDisplayEntries.clear();
      reservedDisplayEntries.set(0);
      model.clear();
      displayList.revalidate();
      displayList.repaint();
   }
}
