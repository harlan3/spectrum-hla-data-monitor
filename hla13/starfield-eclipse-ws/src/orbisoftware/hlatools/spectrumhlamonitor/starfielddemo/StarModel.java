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

package orbisoftware.hlatools.spectrumhlamonitor.starfielddemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import orbisoftware.hla13_containers.Interactions.StarHasLeftStarField_bd6b9371adddafc6_Cont.StarHasLeftStarField_bd6b9371adddafc6_Cont;
import orbisoftware.hlatools.spectrumhlamonitor.hla_sender.PublishStarHasLeftStarField;

public class StarModel {

	public Star hlaStar;

	public StarModel(int index, String properName, int screenWidth, int screenHeight) {

		hlaStar = new Star();
		Random rand = new Random();
		this.hlaStar.starID = index;
		this.hlaStar.properName = properName;
		this.hlaStar.projX = rand.nextInt(screenWidth) - screenWidth / 2.0;
		this.hlaStar.projY = rand.nextInt(screenHeight) - screenHeight / 2.0;
		this.hlaStar.projZ = rand.nextInt(screenWidth);

		this.hlaStar.speed = rand.nextDouble() * 5 + 1;
		this.hlaStar.baseSize = rand.nextInt(6) + 1; // 1-4 for background, 5-8 for large stars
		this.hlaStar.blinkSpeed = rand.nextDouble() * 0.1 + 0.05;
		this.hlaStar.blinkPhase = rand.nextDouble() * Math.PI * 2;
	}

	public void update(int screenWidth, int screenHeight) {

		this.hlaStar.projZ -= this.hlaStar.speed;

		if (this.hlaStar.projZ <= 1) {

			PublishStarHasLeftStarField publishStarHasLeftStarField = PublishStarHasLeftStarField.getInstance();
			StarHasLeftStarField_bd6b9371adddafc6_Cont container = new StarHasLeftStarField_bd6b9371adddafc6_Cont();
			
			this.hlaStar.projZ = screenWidth;
			Random rand = new Random();
			this.hlaStar.projX = rand.nextInt(screenWidth) - screenWidth / 2.0;
			this.hlaStar.projY = rand.nextInt(screenHeight) - screenHeight / 2.0;
			
			container.starID = this.hlaStar.starID;
			container.starName.value = this.hlaStar.properName;
			publishStarHasLeftStarField.sideLoadPublishSample(container);
		}
		this.hlaStar.blinkPhase += this.hlaStar.blinkSpeed;
	}

	public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {

		// Compute 3D projection
		this.hlaStar.xPos = (this.hlaStar.projX / this.hlaStar.projZ) * (screenWidth / 2.0) + (screenWidth / 2.0);
		this.hlaStar.yPos = (this.hlaStar.projY / this.hlaStar.projZ) * (screenHeight / 2.0) + (screenHeight / 2.0);

		// Stars closer to the screen appear larger
		this.hlaStar.size = (1 - this.hlaStar.projZ / screenWidth) * this.hlaStar.baseSize;

		if (this.hlaStar.size < 0.5)
			this.hlaStar.size = 0.5;

		// Twinkling effect: Map sine wave to opacity (Alpha channel)
		this.hlaStar.alpha = ((Math.sin(this.hlaStar.blinkPhase) + 1.0) / 2.0);

		// Ensure some stars don't fade completely out
		this.hlaStar.alpha = Math.max(0.2f, this.hlaStar.alpha * 0.9f);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(1f, 1f, 1f, (float) this.hlaStar.alpha));

		// Draw larger stars slightly differently
		g2d.fillOval((int) this.hlaStar.xPos, (int) this.hlaStar.yPos, (int) this.hlaStar.size,
				(int) this.hlaStar.size);
	}
}