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
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of Pmclust.
 * 
 *
 * @author Andre Bessi, Univerity of Milan Bicocca
 */
public class RMAD_pmclustNodeModel extends RMADCoreNodeModel {

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
					RMAD_pmclustNodeModel.m_defaultTabTitle, 
					RMAD_pmclustNodeModel.m_inclList, 
					RMAD_pmclustNodeModel.m_exclList);


	/*****************************************************************/
	/** Constants for the DialogComponentNumber (number of clusters) */
	/*****************************************************************/

	/** The settings key which is used to retrieve and 
    store the settings (from the dialog or from a settings file)    
   (package visibility to be usable from the dialog). */
	static final String CFGKEY_COUNT_CLUSTERS = "Clusters_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_CLUSTERS = 2;

	/** Minimum value. */
	static final int K_MIN_VALUE = 0;

	/** Maximum value. */
	static final int K_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_count =
			new SettingsModelIntegerBounded(RMAD_pmclustNodeModel.CFGKEY_COUNT_CLUSTERS,
					RMAD_pmclustNodeModel.DEFAULT_COUNT_CLUSTERS,
					RMAD_pmclustNodeModel.K_MIN_VALUE, 
					RMAD_pmclustNodeModel.K_MAX_VALUE);


	/****************************/
	/** Constants for algorithm */
	/****************************/

	static final String CFGKEY_STRING_ALGORITHM = "algorithm";

	static final ArrayList<String> m_algorithm = new ArrayList<>();

	/** The available algorithms are "em", "aecm", "apecm", "apecma", "kmeans", "em.dmat", "kmeans.dmat"
	 */
	static{
		m_algorithm.add(0, "em");
		m_algorithm.add(1, "aecm");
		m_algorithm.add(2, "apecm");
		m_algorithm.add(3, "apecma");
		m_algorithm.add(4, "kmeans");
		m_algorithm.add(5, "em.dmat");
		m_algorithm.add(6, "kmeans.dmat");
	}

	private final SettingsModelString m_algorithm_chooser = new SettingsModelString(
			RMAD_pmclustNodeModel.CFGKEY_STRING_ALGORITHM, 
			RMAD_pmclustNodeModel.m_algorithm.get(0));

	/*****************************/
	/** Constants for RndEM.iter */
	/*****************************/

	static final String CFGKEY_COUNT_RNDEM_ITER = "RndEM_iter";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_RNDEM_ITER = 10;

	private final SettingsModelDouble m_RndEM_iter = new SettingsModelDouble(
			RMAD_pmclustNodeModel.CFGKEY_COUNT_RNDEM_ITER, 
			RMAD_pmclustNodeModel.DEFAULT_COUNT_RNDEM_ITER);


	/*******************************/
	/** Constants for method.own.X */
	/*******************************/

	static final String CFGKEY_STRING_METHOD_OWN_X = "Method_own_x";

	static final ArrayList<String> m_method_own_x = new ArrayList<>();

	/** The available algorithms are "gbdr", "spmdr", "common", "single", "ddmatrix"
	 */
	static{
		m_method_own_x.add(0, "gbdr");
		m_method_own_x.add(1, "spmdr");
		m_method_own_x.add(2, "common");
		m_method_own_x.add(3, "single");
		m_method_own_x.add(4, "ddmatrix");
	}

	private final SettingsModelString m_method_own_x_chooser = new SettingsModelString(
			RMAD_pmclustNodeModel.CFGKEY_STRING_METHOD_OWN_X, 
			RMAD_pmclustNodeModel.m_method_own_x.get(0));

	/*****************************/
	/** Constants for rank.own.X */
	/*****************************/

	static final String CFGKEY_COUNT_RANK_OWN_X = "Rank_own_x";

	static final int DEFAULT_COUNT_RANK_OWN_X = 0;

	/** Minimum value. */
	static final int RANK_OWN_X_MIN_VALUE = 0;

	/** Maximum value. */
	static final int RANK_OWN_X_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_rank_own_x = new SettingsModelIntegerBounded(
			RMAD_pmclustNodeModel.CFGKEY_COUNT_RANK_OWN_X,
			RMAD_pmclustNodeModel.DEFAULT_COUNT_RANK_OWN_X, 
			RMAD_pmclustNodeModel.RANK_OWN_X_MIN_VALUE, 
			RMAD_pmclustNodeModel.RANK_OWN_X_MAX_VALUE);






	public RMAD_pmclustNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		FunctionValues fv = new FunctionValues("pmclust", "pmclust", m_filter.getIncludeList(), "class");
		fv.put("K", m_count.getIntValue());
		fv.put("algorithm", m_algorithm_chooser.getStringValue());
		fv.put("method.own.X", m_method_own_x_chooser.getStringValue());
		fv.put("rank.own.X", m_rank_own_x.getIntValue());
		return fv.toString();
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
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_count.saveSettingsTo(settings);
		m_algorithm_chooser.saveSettingsTo(settings);
		m_RndEM_iter.saveSettingsTo(settings);
		m_method_own_x_chooser.saveSettingsTo(settings);
		m_rank_own_x.saveSettingsTo(settings);

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_count.validateSettings(settings);
		m_algorithm_chooser.validateSettings(settings);
		m_RndEM_iter.validateSettings(settings);
		m_method_own_x_chooser.validateSettings(settings);
		m_rank_own_x.validateSettings(settings);

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_count.loadSettingsFrom(settings);
		m_algorithm_chooser.loadSettingsFrom(settings);
		m_RndEM_iter.loadSettingsFrom(settings);
		m_method_own_x_chooser.loadSettingsFrom(settings);
		m_rank_own_x.loadSettingsFrom(settings);

	}

}

