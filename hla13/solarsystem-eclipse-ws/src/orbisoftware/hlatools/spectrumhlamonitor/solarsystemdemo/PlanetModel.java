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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class PlanetModel {

   static int width;
   static int height;

   private Planet hlaPlanet;

   public PlanetModel(int index, String planetName, double orbitalRadius,
         double planetSize, Color planetColor) {

      hlaPlanet = new Planet();

      hlaPlanet.planetID = index;
      hlaPlanet.planetOrdinalValue = index;
      hlaPlanet.planetSize = planetSize;
      hlaPlanet.orbitalRadius = orbitalRadius; 
      
      if (hlaPlanet.orbitalRadius == 0.0)
         hlaPlanet.orbitalVelocity = 0.0;
      else
         hlaPlanet.orbitalVelocity = Math.sqrt(5000.0 / Math.pow(orbitalRadius, 2));
      
      hlaPlanet.planetColor = planetColor;
   }

   public static void setScreenDimensions(int newWidth, int newHeight) {
      
      width = newWidth;
      height = newHeight;
   }

   public Planet getHLAPlanet() {
      
      return hlaPlanet;
   }

   public void draw(Graphics2D g2) {

      // Erase previous
      g2.setPaint(Color.black);
      drawOrbit(g2);
      drawPlanet(g2);

      // Update Orbit
      updateOrbit();

      // Draw
      g2.setPaint(Color.white);
      drawOrbit(g2);
      g2.setPaint(hlaPlanet.planetColor);
      drawPlanet(g2);
   }

   private void drawPlanet(Graphics2D g2) {

      double xOffset = hlaPlanet.xPos - hlaPlanet.planetSize / 2.0;
      double yOffset = hlaPlanet.yPos - hlaPlanet.planetSize / 2.0;

      Ellipse2D myEllipse = new Ellipse2D.Double(xOffset, yOffset,
            hlaPlanet.planetSize, hlaPlanet.planetSize);
      g2.fill(myEllipse);
   }

   private void drawOrbit(Graphics2D g2) {

      double orbitalDiameter = hlaPlanet.orbitalRadius * 2.0;
      int offsetX = (int) (width / 2.0 - hlaPlanet.orbitalRadius);
      int offsetY = (int) (height / 2.0 - hlaPlanet.orbitalRadius);

      Ellipse2D orbit = new Ellipse2D.Double(offsetX, offsetY, orbitalDiameter,
            orbitalDiameter);

      g2.setStroke(new BasicStroke(0));
      g2.draw(orbit);
   }

   private void updateOrbit() {

      if (hlaPlanet.orbitalRadius == 0) {
         
         hlaPlanet.theta = 0;
         hlaPlanet.xPos = width / 2;
         hlaPlanet.yPos = height / 2;
      } else {
         
         hlaPlanet.theta = hlaPlanet.theta + hlaPlanet.orbitalVelocity
               / hlaPlanet.orbitalRadius;
         hlaPlanet.xPos = width / 2 - hlaPlanet.orbitalRadius
               * Math.sin(hlaPlanet.theta);
         hlaPlanet.yPos = height / 2 - hlaPlanet.orbitalRadius
               * Math.cos(hlaPlanet.theta);
      }
   }
}
