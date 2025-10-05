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

import orbisoftware.hla_1516e_encoding.Interactions.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode.PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode;
import orbisoftware.hla_1516e_encoding.Objects.SolarSystem_124a7dc86c25491f_Encode.SolarSystem_124a7dc86c25491f_Encode;

public class ClassInstanceReference {

	private static ClassInstanceReference single_instance = null;
	
	// Objects
	public SolarSystem_124a7dc86c25491f_Encode solarSystem = new SolarSystem_124a7dc86c25491f_Encode();
	
	// Interactions
	public PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode planetHasCompletedAnOrbit = new PlanetHasCompletedAnOrbit_3a3be83c4403ab7d_Encode();
	
    public static synchronized ClassInstanceReference getInstance()
    {
        if (single_instance == null)
            single_instance = new ClassInstanceReference();
 
        return single_instance;
    }
}
