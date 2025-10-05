/*
 *  Solar System Demo
 *
 *  Copyright (C) 2011 Harlan Murphy
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

package orbisoftware.hlatools.spectrumhlamonitor.solarsystemdemo;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import orbisoftware.hlatools.spectrumhlamonitor.hla_sender.PublishSolarSystem;

@SuppressWarnings("serial")
public class SolarSystem extends JFrame {

   private static final int width = 485;
   private static final int height = 500;

   private boolean firstPaint;

   private ArrayList<PlanetModel> planetList = new ArrayList<PlanetModel>();
   private BufferedImage bufferedImage;
   private Graphics2D graphics2D;
   
   public void init() {

      PlanetModel.setScreenDimensions(width, height);
      setBackground(Color.black);
      firstPaint = true;

      // Initialize Solar System
      initSolarSystem();
   }

   public void paint(Graphics g) {

      PublishSolarSystem publishSolarSystem = PublishSolarSystem.getInstance();
      Graphics2D g2d = (Graphics2D) g;

      if (firstPaint) {

         bufferedImage = (BufferedImage) createImage(width, height);
         graphics2D = bufferedImage.createGraphics();

         firstPaint = false;
      }

      for (PlanetModel planet : planetList) {
         
         planet.draw(graphics2D);
      }
      
      // Publish the Solar System to HLA
      publishSolarSystem.sideLoadPublishSample(planetList);

      g2d.drawImage(bufferedImage, 0, 0, this);
   }

   private void initSolarSystem() {

      planetList.add(new PlanetModel(0, "Sun", 0, 40, Color.yellow));
      planetList.add(new PlanetModel(1, "Mercury", 30, 6, Color.red));
      planetList.add(new PlanetModel(2, "Venus", 45, 12, Color.pink));
      planetList.add(new PlanetModel(3, "Earth", 65, 15, Color.blue));
      planetList.add(new PlanetModel(4, "Mars", 85, 12, Color.orange));
      planetList.add(new PlanetModel(5, "Jupiter", 105, 25, Color.gray));
      planetList.add(new PlanetModel(6, "Saturn", 130, 22, Color.lightGray));
      planetList.add(new PlanetModel(7, "Uranus", 155, 12, Color.cyan));
      planetList.add(new PlanetModel(8, "Neptune", 180, 11, Color.green));
      planetList.add(new PlanetModel(9, "Pluto", 200, 5, Color.magenta));
   }

   public void startSimulation() {

      SolarSystem solarSystem = new SolarSystem();
      
      solarSystem.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            System.exit(0);
         }
      });

      solarSystem.setSize(new Dimension(width - 0, height - 20));
      solarSystem.setVisible(true);

      solarSystem.init();

      while (true) {

         solarSystem.repaint();

         try {
            Thread.sleep(30);
         } catch (InterruptedException e1) {
         }
      }
   }
}
