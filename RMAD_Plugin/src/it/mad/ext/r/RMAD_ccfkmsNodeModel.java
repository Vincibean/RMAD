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

import java.util.ArrayList;
import java.util.Collection;

import it.mad.ext.r.preferences.RMADPreferenceProvider;

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
 * This is the model implementation of RMAD_ccfkms.
 * Partition a data set into convex sets using conjugate convex functions.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_ccfkmsNodeModel extends RMADCoreNodeModel {


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
					RMAD_ccfkmsNodeModel.m_defaultTabTitle, 
					RMAD_ccfkmsNodeModel.m_inclList, 
					RMAD_ccfkmsNodeModel.m_exclList);


	/*******************************************************************/
	/** Constants for the DialogComponentNumber (number of prototypes) */
	/*******************************************************************/

	/** The settings key which is used to retrieve and 
    store the settings (from the dialog or from a settings file)    
   (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT_CLUSTERS = "Clusters_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_CLUSTERS = 5;

	/** Minimum value. */
	static final int K_MIN_VALUE = 0;

	/** Maximum value. */
	static final int K_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_count =
			new SettingsModelIntegerBounded(RMAD_ccfkmsNodeModel.CFGKEY_COUNT_CLUSTERS,
					RMAD_ccfkmsNodeModel.DEFAULT_COUNT_CLUSTERS,
					RMAD_ccfkmsNodeModel.K_MIN_VALUE, 
					RMAD_ccfkmsNodeModel.K_MAX_VALUE);


	/**************************************************/
	/** Constants for the DialogComponentNumber (par) */
	/**************************************************/

	static final String CFGKEY_COUNT_PAR = "Par";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_PAR = 2;

	/** Minimum value. */
	static final double PAR_MIN_VALUE = 0;

	/** Maximum value. */
	static final double PAR_MAX_VALUE = Double.MAX_VALUE;

	private final SettingsModelDoubleBounded m_par = new SettingsModelDoubleBounded(
			RMAD_ccfkmsNodeModel.CFGKEY_COUNT_PAR, 
			RMAD_ccfkmsNodeModel.DEFAULT_COUNT_PAR, 
			RMAD_ccfkmsNodeModel.PAR_MIN_VALUE, 
			RMAD_ccfkmsNodeModel.PAR_MAX_VALUE);


	/*******************************************************/
	/** Constants for the DialogComponentNumber (max.iter) */
	/*******************************************************/

	static final String CFGKEY_COUNT_MAX_ITER = "Maximum_iterations";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MAX_ITER = 100;

	/** Minimum value. */
	static final int MAX_ITER_MIN_VALUE = 0;

	/** Maximum value. */
	static final int MAX_ITER_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_max_iter = new SettingsModelIntegerBounded(
			RMAD_ccfkmsNodeModel.CFGKEY_COUNT_MAX_ITER,
			RMAD_ccfkmsNodeModel.DEFAULT_COUNT_MAX_ITER,
			RMAD_ccfkmsNodeModel.MAX_ITER_MIN_VALUE, 
			RMAD_ccfkmsNodeModel.MAX_ITER_MAX_VALUE);


	/*****************************************************/
	/** Constants for the SettingsModelBoolean (opt.std) */
	/*****************************************************/

	static final String CFGKEY_BOOLEAN_OPT_STD = "opt.std";

	private final SettingsModelBoolean m_opt_std = new SettingsModelBoolean(
			RMAD_ccfkmsNodeModel.CFGKEY_BOOLEAN_OPT_STD, 
			false);


	/*******************************************************/
	/** Constants for the DialogComponentNumber (opt.retry) */
	/*******************************************************/

	static final String CFGKEY_COUNT_OPT_RETRY = "Retries";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_OPT_RETRY = 0;

	/** Minimum value. */
	static final int OPT_RETRY_MIN_VALUE = 0;

	/** Maximum value. */
	static final int OPT_RETRY_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_opt_retry = new SettingsModelIntegerBounded(
			RMAD_ccfkmsNodeModel.CFGKEY_COUNT_OPT_RETRY,
			RMAD_ccfkmsNodeModel.DEFAULT_COUNT_OPT_RETRY,
			RMAD_ccfkmsNodeModel.OPT_RETRY_MIN_VALUE, 
			RMAD_ccfkmsNodeModel.OPT_RETRY_MAX_VALUE);


	/************************/
	/** Constants for debug */
	/************************/

	private final Boolean DEFAULT_DEBUG = true;


















	public RMAD_ccfkmsNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		FunctionValues fv = new FunctionValues("cba", "ccfkms", m_filter.getIncludeList(), "size");
		fv.put("n", m_count.getIntValue());
		fv.putNull("p");
		fv.put("par", m_par.getDoubleValue());
		fv.put("max.iter", m_max_iter.getIntValue());
		fv.put("opt.std", m_opt_std.getBooleanValue());
		fv.put("opt.retry", m_opt_retry.getIntValue());
		fv.put("debug", DEFAULT_DEBUG);
		return fv.toString();
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
		int clusters = m_count.getIntValue();
		if(clusters > numData)
			throw new InvalidSettingsException("The number of clusters should be lower than the number of observations.");
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_count.saveSettingsTo(settings);
		m_par.saveSettingsTo(settings);
		m_max_iter.saveSettingsTo(settings);
		m_opt_std.saveSettingsTo(settings);
		m_opt_retry.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_count.validateSettings(settings);
		m_par.validateSettings(settings);
		m_max_iter.validateSettings(settings);
		m_opt_std.validateSettings(settings);
		m_opt_retry.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_count.loadSettingsFrom(settings);
		m_par.loadSettingsFrom(settings);
		m_max_iter.loadSettingsFrom(settings);
		m_opt_std.loadSettingsFrom(settings);
		m_opt_retry.loadSettingsFrom(settings);
	}


}

