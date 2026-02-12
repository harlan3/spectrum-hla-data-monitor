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

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AboutBox extends javax.swing.JDialog implements ActionListener {

   private String softwareVersion = new String("1.0");
   private JButton okButton;

   public AboutBox() {
      initComponents();
   }

   private void initComponents() {

      JLabel appTitleLabel = new javax.swing.JLabel();
      JLabel versionLabel = new javax.swing.JLabel();
      JLabel vendorLabel = new javax.swing.JLabel();
      JLabel copyRightLabel = new javax.swing.JLabel();
      JLabel appDescLabel = new javax.swing.JLabel();

      Container pane = getContentPane();
      pane.setLayout(new GridBagLayout());

      setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
      setTitle("About");
      setModal(true);
      setResizable(false);

      appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(
            appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD,
            appTitleLabel.getFont().getSize() + 4));
      appTitleLabel.setText("Spectrum HLA Data Monitor");

      appDescLabel.setText("HLA data monitor and debug tool");

      versionLabel.setText("Version " + softwareVersion);

      vendorLabel.setText("Orbis Software");
      copyRightLabel.setText("Copyright (C) 2026 Harlan Murphy");

      GridBagConstraints c = new GridBagConstraints();
      c.weightx = 1.0;
      c.weighty = 0.05;
      c.fill = GridBagConstraints.BOTH;

      c.gridx = 0;
      c.gridy = 0;
      c.insets = new Insets(10, 20, 0, 0);
      pane.add(appTitleLabel, c);

      c.gridx = 0;
      c.gridy = 1;
      c.insets = new Insets(0, 20, 0, 0);
      pane.add(appDescLabel, c);

      c.gridx = 0;
      c.gridy = 2;
      c.weighty = 0.5;
      pane.add(versionLabel, c);

      c.gridx = 0;
      c.gridy = 3;
      c.weighty = 0.05;
      pane.add(vendorLabel, c);

      c.gridx = 0;
      c.gridy = 4;
      pane.add(copyRightLabel, c);

      JPanel panel = new JPanel(new FlowLayout());

      okButton = new JButton("Ok");
      panel.add(okButton);
      okButton.addActionListener(this);

      c.gridx = 0;
      c.gridy = 6;
      pane.add(panel, c);

      setSize(400, 220);
      setVisible(true);
   }

   public void actionPerformed(ActionEvent e) {

      if (e.getSource() == okButton)
         this.dispose();
   }
}
