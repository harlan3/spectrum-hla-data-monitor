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
import javax.swing.DefaultListModel;
import javax.swing.JList;

import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;
import orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver.ReceiveQueue;

public class ReceiveListGUI implements PropertyChangeListener, ActionListener {

	public ReceiveQueue receiveQueue = new ReceiveQueue();

	private DefaultListModel<String> model = new DefaultListModel<>();
	public JList<String> displayList = new JList<String>(model);

	public ReceiveListGUI() {

	}

   public void propertyChange(PropertyChangeEvent evt) {

      if (evt.getPropertyName().toString().equals("hlaReadEvent") && (model.size() < 25)) {

         synchronized (receiveQueue) {

            receiveQueue.pushBack((HLASamples) evt.getNewValue());
            receiveQueue.notify();

            HLASamples samples = (HLASamples) evt.getNewValue();

            model.addElement("TimeStamp: " + Long.toString(samples.sampleReadTime));
         }
      }
   }
	
	@Override
	public void actionPerformed(ActionEvent e) {
	   
	   receiveQueue.clear();
	   model.clear();
	}
}
