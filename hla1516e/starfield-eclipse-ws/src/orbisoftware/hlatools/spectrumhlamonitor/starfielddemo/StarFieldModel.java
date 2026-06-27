/*
 *  Spectrum HLA data monitor tool
 *
 *  Copyright (C) 2026 Harlan Murphy
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

import javax.swing.JPanel;
import javax.swing.Timer;

import orbisoftware.hlatools.spectrumhlamonitor.hla_sender.PublishStarField;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StarFieldModel extends JPanel implements ActionListener, KeyListener {

	private final ArrayList<StarModel> stars;
	private final Timer timer;
	private int numStars = 0; // star database contains 571 stars
	private List<String> starDb;
	private int frameCounter = 0;
	
	public StarFieldModel() {

		try {
			starDb = Files.readAllLines(Paths.get("star_db.txt"));
			numStars = starDb.size();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setBackground(Color.BLACK);
		stars = new ArrayList<>();
		
		// Wait for component to get actual size, then initialize stars
		javax.swing.SwingUtilities.invokeLater(() -> {
			int w = getWidth();
			int h = getHeight();
			for (int i = 0; i < numStars; i++) {
				stars.add(new StarModel(i, starDb.get(i), w, h));
			}
		});
		
		// 60 FPS animation timer
		timer = new Timer(16, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {

		PublishStarField publishStarField = PublishStarField.getInstance();

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Clear and draw space background
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		// Draw all stars
		synchronized (stars) {
			for (StarModel star : stars) {

				star.draw(g2d, getWidth(), getHeight());

				if (MainSharedData.showLabels) {

					g2d.setColor(Color.LIGHT_GRAY);
					g2d.drawString(Integer.toString(star.hlaStar.starID) + " : " + star.hlaStar.properName,
							(int) star.hlaStar.xPos + 5 + 2, (int) star.hlaStar.yPos - 2);
				}
			}
			
			frameCounter++;

			if (frameCounter == 10) {
				publishStarField.sideLoadPublishSample(stars);
				frameCounter = 0;
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_N) {
			MainSharedData.showLabels = !MainSharedData.showLabels;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		int w = getWidth();
		int h = getHeight();
		if (w == 0 || h == 0)
			return;

		synchronized (stars) {
			for (StarModel star : stars) {
				star.update(w, h);
			}
		}
		repaint();
	}
}