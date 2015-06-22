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

import java.util.ArrayList;
import java.util.Collection;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of RMAD_nncluster.
 * Uses Prim’s algorithm to build a minimum spanning tree for each cluster, stopping when the nearest- * nneighbour distance rises above a specified threshold. Returns a set of clusters and a set of ’outliers’ * nnot in any cluster.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_nnclusterNodeModel extends RMADCoreNodeModel {

	/***********************************/
	/** Constant for the inport index. */
	/***********************************/
	public static final int IN_PORT = 0;


	/***********************************/
	/** Constants for the ColumnFilter */
	/***********************************/

	/** the identifier the values are stored with in the NodeSettings object*/
	static final String m_defaultTabTitle = "configName";

	static final Collection<String> m_inclList = new ArrayList<String>();

	static final Collection<String> m_exclList = new ArrayList<String>();

	private final SettingsModelFilterString m_filter = 
			new SettingsModelFilterString(
					RMAD_nnclusterNodeModel.m_defaultTabTitle, 
					RMAD_nnclusterNodeModel.m_inclList, 
					RMAD_nnclusterNodeModel.m_exclList);

	/********************************************************/
	/** Constants for the DialogComponentNumber (threshold) */
	/********************************************************/

	static final String CFGKEY_COUNT_THRESHOLD = "Threshold";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_THRESHOLD = 0.2;

	/** Minimum value. */
	static final double THRESHOLD_MIN_VALUE = 0;

	/** Maximum value. */
	static final double THRESHOLD_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_threshold = new SettingsModelDoubleBounded(
			RMAD_nnclusterNodeModel.CFGKEY_COUNT_THRESHOLD,
			RMAD_nnclusterNodeModel.DEFAULT_COUNT_THRESHOLD,
			RMAD_nnclusterNodeModel.THRESHOLD_MIN_VALUE, 
			RMAD_nnclusterNodeModel.THRESHOLD_MAX_VALUE);


	/***************************************************/
	/** Constants for the DialogComponentNumber (fill) */
	/***************************************************/

	static final String CFGKEY_COUNT_FILL = "Fill";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_FILL = 0.95;

	/** Minimum value. */
	static final double FILL_MIN_VALUE = 0;

	/** Maximum value. */
	static final double FILL_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_fill = new SettingsModelDoubleBounded(
			RMAD_nnclusterNodeModel.CFGKEY_COUNT_FILL,
			RMAD_nnclusterNodeModel.DEFAULT_COUNT_FILL,
			RMAD_nnclusterNodeModel.FILL_MIN_VALUE, 
			RMAD_nnclusterNodeModel.FILL_MAX_VALUE);


	/*******************************************************/
	/** Constants for the DialogComponentNumber (maxclust) */
	/*******************************************************/

	/** The settings key which is used to retrieve and 
    store the settings (from the dialog or from a settings file)    
   (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT_MAXCLUST = "Maxclust";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MAXCLUST = 20;

	/** Minimum value. */
	static final int MAXCLUST_MIN_VALUE = 0;

	/** Maximum value. */
	static final int MAXCLUST_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_maxclust = new SettingsModelIntegerBounded(
			RMAD_nnclusterNodeModel.CFGKEY_COUNT_MAXCLUST,
			RMAD_nnclusterNodeModel.DEFAULT_COUNT_MAXCLUST,
			RMAD_nnclusterNodeModel.MAXCLUST_MIN_VALUE, 
			RMAD_nnclusterNodeModel.MAXCLUST_MAX_VALUE);


	/******************************************************/
	/** Constants for the DialogComponentNumber (give.up) */
	/******************************************************/

	/** The settings key which is used to retrieve and 
    store the settings (from the dialog or from a settings file)    
   (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT_GIVE_UP = "Give_Up";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_GIVE_UP = 500;

	/** Minimum value. */
	static final int GIVE_UP_MIN_VALUE = 0;

	/** Maximum value. */
	static final int GIVE_UP_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_give_up =
			new SettingsModelIntegerBounded(RMAD_nnclusterNodeModel.CFGKEY_COUNT_GIVE_UP,
					RMAD_nnclusterNodeModel.DEFAULT_COUNT_GIVE_UP,
					RMAD_nnclusterNodeModel.GIVE_UP_MIN_VALUE, 
					RMAD_nnclusterNodeModel.GIVE_UP_MAX_VALUE);


	/**************************/
	/** Constants for verbose */
	/**************************/

	private final Boolean DEFAULT_VERBOSE = true;


	/************************/
	/** Constants for start */
	/************************/

	static final String CFGKEY_STRING_START = "Start";

	static final int DEFAULT_COUNT_START = 0;

	/** Minimum value. */
	static final int START_MIN_VALUE = 0;

	/** Maximum value. */
	static final int START_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_start = new SettingsModelIntegerBounded(
			RMAD_nnclusterNodeModel.CFGKEY_STRING_START, 
			RMAD_nnclusterNodeModel.DEFAULT_COUNT_START, 
			RMAD_nnclusterNodeModel.START_MIN_VALUE, 
			RMAD_nnclusterNodeModel.START_MAX_VALUE);



	/*****************************************************/
	/** Constants for the SettingsModelBoolean (outlier) */
	/*****************************************************/

	static final String CFGKEY_BOOLEAN_OUTLIER = "Outlier";

	private final SettingsModelBoolean m_outlier = new SettingsModelBoolean(
			RMAD_nnclusterNodeModel.CFGKEY_BOOLEAN_OUTLIER, 
			true);

















	public RMAD_nnclusterNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		FunctionValues fv = new FunctionValues("nnclust", "nncluster", m_filter.getIncludeList());
		fv.put("threshold", m_threshold.getDoubleValue());
		fv.put("fill", m_fill.getDoubleValue());
		fv.put("maxclust", m_maxclust.getIntValue());
		fv.put("give.up", m_give_up.getIntValue());
		fv.put("verbose", DEFAULT_VERBOSE);
		if(m_start.getIntValue() == 0)
			fv.putNull("start");
		else
			fv.put("start", m_start.getIntValue());
		String command = fv.toStringExceptLastLine();
		command += "clusterMember <- clusterMember(nncluster, outlier = " + Boolean.toString(m_outlier.getBooleanValue()).toUpperCase() + ")\n";
		command += "R <- cbind(R, clusterMember)\n";

		return command;
	}


	private boolean firstConfigure = true;
	/**
	 * {@inheritDoc}
	 */ 
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// Checks if there are no columns in the incoming DataTableSpec.
		if(inSpecs[IN_PORT].getNumColumns() == 0)
			throw new InvalidSettingsException("Input table contains no columns.");

		// Checks if the user included at least one column, and if there is a non-numerical column between the numerical columns selected by the user.
		if(!firstConfigure){
			if(m_filter.getIncludeList().isEmpty())
				throw new InvalidSettingsException("At least one column must be included for the node to work.");
			for(String filteredColumnName : m_filter.getIncludeList()){
				if(!inSpecs[IN_PORT].getColumnSpec(filteredColumnName).getType().isCompatible(DoubleValue.class))
					throw new InvalidSettingsException("All filtered columns must be numerical.");
			}
		}

		// Checks if there are (no) numerical values in the incoming DataTableSpec.
		if(!inSpecs[IN_PORT].containsCompatibleType(DoubleValue.class))
			throw new InvalidSettingsException("Input table contains no numerical values for classification.");

		// Automatically include all numerical columns if no column is included.
		// *** N.B.: DA VERIFICARE SE, IN CASO DI INCLUDELIST VUOTA, 
		// IL METODO EXECUTE PARTE CON LA VUOTA (SCELTA DALL'UTENTE)(BENE?) 
		// O PARTE CON QUELLA OTTIMIZATA DEL CONFIGURE (MALE?)
		if(firstConfigure){
			Collection<String> local_exclList = new ArrayList<String>();
			Collection<String> local_inclList = new ArrayList<String>();
			for(String columnName : inSpecs[IN_PORT].getColumnNames()){
				if(!inSpecs[IN_PORT].getColumnSpec((String)columnName).getType().isCompatible(DoubleValue.class))
					local_exclList.add(columnName);
				else
					local_inclList.add(columnName);
			}
			m_filter.setExcludeList(local_exclList);
			m_filter.setIncludeList(local_inclList);
		}

		firstConfigure = false;

		return new DataTableSpec[1];
	}


	@Override
	protected void postConfigure(PortObject[] inData, ExecutionContext exec)
			throws InvalidSettingsException {
		// The number of clusters should be lower than the number of observations
		int numData = ((BufferedDataTable) inData[IN_PORT]).getRowCount();
		int maxClusters = m_maxclust.getIntValue();
		if(maxClusters > numData)
			throw new InvalidSettingsException("The maximum number of clusters should be lower than the number of observations.");

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_threshold.saveSettingsTo(settings);
		m_fill.saveSettingsTo(settings);
		m_maxclust.saveSettingsTo(settings);
		m_give_up.saveSettingsTo(settings);
		m_start.saveSettingsTo(settings);
		m_outlier.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_threshold.validateSettings(settings);
		m_fill.validateSettings(settings);
		m_maxclust.validateSettings(settings);
		m_give_up.validateSettings(settings);
		m_start.validateSettings(settings);
		m_outlier.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_threshold.loadSettingsFrom(settings);
		m_fill.loadSettingsFrom(settings);
		m_maxclust.loadSettingsFrom(settings);
		m_give_up.loadSettingsFrom(settings);
		m_start.loadSettingsFrom(settings);
		m_outlier.loadSettingsFrom(settings);
	}

}

