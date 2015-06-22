/*
 * Copyright (C) 2015  Vincibean <Andre Bessi>
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
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.mad.ext.r;

import it.mad.ext.r.preferences.RMADPreferenceProvider;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

/**
 * This is a middle layer for the (eventual)
 * definition of abstract methods common to the RMAD nodes.
 * 
 * @author Andre Bessi
 */
public abstract class RMADCoreNodeModel extends RMADLocalNodeModel {

	public RMADCoreNodeModel(RMADPreferenceProvider pref) {
		super(new PortType[]{BufferedDataTable.TYPE}, pref);
	}

	/**
	protected String getRFunctionParams(String library, String functionName, List<String> includeList, String suffix, List<Object> params){
		return getRFunctionParams(library, functionName, includeList, suffix, 0, params);
	}

	protected String getRFunctionParams(String library, String functionName, List<String> includeList, List<Object> params){
		return getRFunctionParams(library, functionName, includeList, null, 0, params);
	}

	protected String getRFunctionParams(String library, String functionName, List<String> includeList, String suffix, int columnsOfTransposedMatrix, List<Object> params) {		

		// Create a matrix of incoming data, combining them by columns.
		String cbind = "cbind(";
		for(String columnName : includeList){
			cbind = cbind + "R$\"" + formatColumn(columnName) + "\", ";
		}
		cbind = cbind.substring(0, cbind.length() - 2) + ")";

		// Get (in the proper order) all the parameters necessary for the function to work and create a String with them (in the proper order).
		String parameters = "";
		for(Object param : params){
			if(param instanceof Integer)
				parameters = parameters + Integer.toString((int) param) + ", ";
			else if(param instanceof Double)
				parameters = parameters + Double.toString((double) param) + ", ";
			else if(param instanceof Boolean)
				parameters = parameters + Boolean.toString((boolean) param).toUpperCase() + ", ";
			else if(param instanceof String)
				parameters = parameters + "\"" + param + "\", ";
			else if(param == null)
				parameters = parameters + ", ";

		}
		parameters = parameters.substring(0, parameters.length() - 2);

		// Assemble the function string command.
		String function = functionName + "(" + cbind + ", " + parameters + ")";
		// Add the proper suffix for getting the desired vector / matrix.
		if(suffix != null)
			function = function + "$" + suffix;

		// Assemble and return the command:
		// First, the "R <- R" default command, load the right library, then get the desired vector / matrix, storing
		// it in a variable with the same name of the function (for explanatory purposes).
		String Command = "R <- R \n"
				+ "library(\"" + library + "\")\n"
				+ functionName + " <- " + function + "\n";
		// Second, if the output of the function needs to be transposed, then turn it into a matrix
		// by declaring of how many columns it will be composed (the number of columns is easier to get
		// than the number of rows). 
		if(columnsOfTransposedMatrix != 0)
			Command = Command + functionName + " <- matrix(" + functionName + ", ncol = " + columnsOfTransposedMatrix + ")\n";
		// Finally, bind the output of your function with R (the input data of the node).
		Command = Command + "R <- cbind(R, " + functionName + ")\n";

		return Command;

	}

	protected String getRFunctionParams(FunctionValues fv) {
		return fv.toString();
	}
	 */

	@Override
	protected PortObject[] execute(PortObject[] inData, ExecutionContext exec)
			throws CanceledExecutionException, Exception {
		PortObject[] p = new PortObject[0];
		try{
			p = super.execute(inData, exec);
		}catch(IllegalStateException e){
			throw new InvalidSettingsException("Error in the R output. Check the \"R MAD Err Output\" Node View for further informations."); // Input table may be too small for the function to work properly.");
		}
		return p;
	}

	protected Integer[] string2intArray(String s) throws InvalidSettingsException{
		String[] s_array = s.split(";");
		Integer[] m_int = new Integer[s_array.length];
		//ArrayList<Integer> intArrayList = new ArrayList<Integer>();
		for(int i = 0; i < s_array.length; i++){
			int e = parseIntWithException(s_array[i].trim());
			m_int[i] = e;
		}
		if (m_int.length > 20)
			throw new InvalidSettingsException("K array must not be larger than 20.");
		return m_int;
	}

	protected int parseIntWithException(String str) throws InvalidSettingsException {
		int t = 0;
		try {
			t = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			throw new InvalidSettingsException("\"" + str + "\" value is not a number.");
		}
		return t;   
	}



}

