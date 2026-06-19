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

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class SampleTextList extends JTextPane {

   enum XmlToken {
      NONE, LEFT_TOKEN, RIGHT_TOKEN, TEXT
   }
   
   // Maximum length of buffer
   private int MAX_BUFFER = 102400;
   
   // Remove first 20% of buffer when MAX_BUFFER is reached
   private double BUFFER_REDUCTION = 0.2;

   // Text pane update interval in milliseconds
   private static final int UPDATE_INTERVAL_MS = 100;

   // Buffered styled text waiting to be appended to the JTextPane
   private final List<TextFragment> pendingFragments = new ArrayList<TextFragment>();

   // Flushes buffered text to the JTextPane on the Swing event dispatch thread
   private final Timer updateTimer;

   public SampleTextList() {

      updateTimer = new Timer(UPDATE_INTERVAL_MS, e -> flushPendingFragments());
      updateTimer.setCoalesce(true);
      updateTimer.start();
   }

   private static final java.util.concurrent.atomic.AtomicLong last = new java.util.concurrent.atomic.AtomicLong();

   public static long nextUniqueMillis() {
      while (true) {
         long current = System.currentTimeMillis();
         long previous = last.get();
         long next = Math.max(current, previous + 5);

         if (last.compareAndSet(previous, next)) {
            return next;
         }
      }
   }

   public void writeXML(String xmlContent) {

      if (xmlContent == null)
         return;

      char xmlContentCharArray[] = xmlContent.toCharArray();
      Color tagColor = new Color(24, 147, 145);

      int cursor = 0;
      int startMarker = 0;
      XmlToken xmlToken = XmlToken.NONE;

      bufferAppend(Color.BLUE, " Sample Timestamp: " + new Timestamp(nextUniqueMillis()) + " ");
      
      while (cursor < xmlContentCharArray.length) {

         if (xmlContentCharArray[cursor] == '<') {

            if (xmlToken == XmlToken.TEXT) {
               bufferAppend(Color.BLACK, xmlContent.substring(startMarker, cursor));
            }

            xmlToken = XmlToken.LEFT_TOKEN;
            startMarker = cursor;

         } else if (xmlContentCharArray[cursor] == '>') {

            bufferAppend(tagColor, xmlContent.substring(startMarker, cursor + 1));
            xmlToken = XmlToken.NONE;

         } else if (xmlToken == XmlToken.NONE) {

            startMarker = cursor;
            xmlToken = XmlToken.TEXT;
         }
         cursor++;
      }

      bufferAppend(Color.BLACK, "\n");
   }

   public void clearAll() {

      synchronized (pendingFragments) {
         pendingFragments.clear();
      }

      Runnable clearTask = () -> {
         StyledDocument doc = getStyledDocument();
         try {
            doc.remove(0, doc.getLength());
         } catch (Exception e) {
            e.printStackTrace();
         }
      };

      if (SwingUtilities.isEventDispatchThread())
         clearTask.run();
      else
         SwingUtilities.invokeLater(clearTask);
   }
   
   private void bufferAppend(Color color, String string) {

      if (string == null || string.length() == 0)
         return;

      synchronized (pendingFragments) {
         pendingFragments.add(new TextFragment(color, string));
      }
   }

   private void flushPendingFragments() {

      List<TextFragment> fragmentsToAppend;

      synchronized (pendingFragments) {
         if (pendingFragments.isEmpty())
            return;

         fragmentsToAppend = new ArrayList<TextFragment>(pendingFragments);
         pendingFragments.clear();
      }

      StyledDocument doc = getStyledDocument();

      try {
         for (TextFragment fragment : fragmentsToAppend) {
            doc.insertString(doc.getLength(), fragment.text, getAttributeSet(fragment.color));
         }

         setCaretPosition(doc.getLength());

         if (doc.getLength() > MAX_BUFFER) {
            doc.remove(0, (int)(MAX_BUFFER * BUFFER_REDUCTION));
         }
      } catch (BadLocationException e) {
         e.printStackTrace();
      }
   }

   private AttributeSet getAttributeSet(Color color) {

      StyleContext styleContext = StyleContext.getDefaultStyleContext();
      return styleContext.addAttribute(
            SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
   }

   private static class TextFragment {

      private final Color color;
      private final String text;

      private TextFragment(Color color, String text) {
         this.color = color;
         this.text = text;
      }
   }
}
