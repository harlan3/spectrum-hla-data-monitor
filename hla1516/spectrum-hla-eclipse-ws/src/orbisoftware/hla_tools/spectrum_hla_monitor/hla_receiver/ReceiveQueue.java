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

package orbisoftware.hla_tools.spectrum_hla_monitor.hla_receiver;

import java.util.ArrayDeque;

import orbisoftware.hla_tools.spectrum_hla_monitor.HLASamples;

public class ReceiveQueue {
	
    private ArrayDeque<HLASamples> deque;

    public ReceiveQueue() {
        deque = new ArrayDeque<>();
    }
    
    public int size() {
    	return deque.size();
    }

    public HLASamples front() {
    	return deque.peekFirst();
    }
    
    public HLASamples back() {
    	return deque.peekLast();
    }
    
    public void pop_front() {
    	deque.pollFirst();
    }
    
    public void pop_back() {
    	deque.pollLast();
    }
    
    public HLASamples get_index(int i) {

    	java.util.Iterator<HLASamples> it = deque.iterator();
    	HLASamples returnVal = null;
    	int count = 0;
    	
    	if (i < deque.size()) {
    		
    		while (it.hasNext()) {
    		
    			if (count == i) {
    				
    				returnVal = it.next();
    				break;
    			}
    			
    			it.next();
    			count++;
    		}
    	}
    	
    	return returnVal;
    }
    
    public void pushBack(HLASamples value) {
        
        deque.addLast(value);
    }
    
    public void clear() {
       
       deque.clear();
    }
}
