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
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of RMAD_HDDC.
 * HDDC(High Dimensional Data Clustering) is a model-based clustering method. It is based on the Gaussian Mixture Model and on * nthe idea that the data lives in subspaces with a lower dimension than the dimension of the original * nspace. It uses the Expectation - Maximisation algorithm to estimate the parameters of the model.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_HDDCNodeModel extends RMADCoreNodeModel {

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
					RMAD_HDDCNodeModel.m_defaultTabTitle, 
					RMAD_HDDCNodeModel.m_inclList, 
					RMAD_HDDCNodeModel.m_exclList);

	/********************/
	/** Constants for k */
	/********************/

	static final String CFGKEY_STRING_K = "Stand";

	static final String DEFAULT_STRING_K = "1;2;3;4;5;6;7;8;9;10";

	private final SettingsModelString m_k = new SettingsModelString(
			RMAD_HDDCNodeModel.CFGKEY_STRING_K,
			RMAD_HDDCNodeModel.DEFAULT_STRING_K);

	private Integer[] K_ARRAY;

	/************************/
	/** Constants for model */
	/************************/

	static final String CFGKEY_STRING_MODEL = "Model";

	static final ArrayList<String> m_models = new ArrayList<>();

	/** The available models are: "AkjBkQkDk", "AkBkQkDk", "ABkQkDk", "AkjBQkDk",
	 * "AkBQkDk", "ABQkDk", "AkjBkQkD", "AkBkQkD", "ABkQkD", "AkjBQkD",
	 * "AkBQkD", "ABQkD", "AjBQD", "ABQD". The default is model="AkjBkQkDk"
	 */
	static{
		m_models.add(0, "AkjBkQkDk");
		m_models.add(1, "AkBkQkDk");
		m_models.add(2, "ABkQkDk");
		m_models.add(3, "AkjBQkDk");
		m_models.add(4, "AkBQkDk");
		m_models.add(5, "ABQkDk");
		m_models.add(6, "AkjBkQkD");
		m_models.add(7, "AkBkQkD");
		m_models.add(8, "ABkQkD");
		m_models.add(9, "AkjBQkD");
		m_models.add(10, "AkBQkD");
		m_models.add(11, "ABQkD");
		m_models.add(12, "AjBQD");
		m_models.add(13, "ABQD");
	}

	private final SettingsModelStringArray m_models_chooser = new SettingsModelStringArray(
			RMAD_HDDCNodeModel.CFGKEY_STRING_MODEL, 
			RMAD_HDDCNodeModel.MODEL_DEFAULT_VALUE);

	static final String[] MODEL_DEFAULT_VALUE = {m_models.get(0)};

	/********************************************************/
	/** Constants for the DialogComponentNumber (threshold) */
	/********************************************************/

	static final String CFGKEY_COUNT_THRESHOLD = "threshold";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_THRESHOLD = 0.2;

	/** Minimum value. */
	static final double THRESHOLD_MIN_VALUE = 0;

	/** Maximum value. */
	static final double THRESHOLD_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_threshold = new SettingsModelDoubleBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_THRESHOLD,
			RMAD_HDDCNodeModel.DEFAULT_COUNT_THRESHOLD,
			RMAD_HDDCNodeModel.THRESHOLD_MIN_VALUE, 
			RMAD_HDDCNodeModel.THRESHOLD_MAX_VALUE);

	/**************************/
	/** Constants for com_dim */
	/**************************/
	/**

	static final String CFGKEY_STRING_COM_DIM = "com_dim";

	static final String DEFAULT_STRING_COM_DIM = "NULL";

	private final SettingsModelString m_com_dim = new SettingsModelString(
			RMAD_HDDCNodeModel.CFGKEY_STRING_COM_DIM,
			RMAD_HDDCNodeModel.DEFAULT_STRING_COM_DIM);

	private int COM_DIM;
	 */


	/******************************/
	/** Constants for com_dim v.2 */
	/******************************/

	static final String CFGKEY_STRING_COM_DIM2 = "com_dim2";

	private final SettingsModelDouble m_com_dim2 = new SettingsModelDouble(
			RMAD_HDDCNodeModel.CFGKEY_STRING_COM_DIM2,
			Double.NaN);

	/******************************************************/
	/** Constants for the DialogComponentNumber (itermax) */
	/******************************************************/

	static final String CFGKEY_COUNT_ITERMAX = "itermax";

	/** Initial default value. */
	static final int DEFAULT_COUNT_ITERMAX = 60;

	/** Minimum value. */
	static final int ITERMAX_MIN_VALUE = 0;

	/** Maximum value. */
	static final int ITERMAX_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_itermax = new SettingsModelIntegerBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_ITERMAX,
			RMAD_HDDCNodeModel.DEFAULT_COUNT_ITERMAX,
			RMAD_HDDCNodeModel.ITERMAX_MIN_VALUE, 
			RMAD_HDDCNodeModel.ITERMAX_MAX_VALUE);

	/**************************************************/
	/** Constants for the DialogComponentNumber (eps) */
	/**************************************************/


	// 1e-3 = 0.0001

	static final String CFGKEY_COUNT_EPS = "eps";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_EPS = 0.001;

	/** Minimum value. */
	static final double EPS_MIN_VALUE = 0;

	/** Maximum value. */
	static final double EPS_MAX_VALUE = Double.MAX_VALUE;

	private final SettingsModelDoubleBounded m_eps = new SettingsModelDoubleBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_EPS,
			RMAD_HDDCNodeModel.DEFAULT_COUNT_EPS,
			RMAD_HDDCNodeModel.EPS_MIN_VALUE, 
			RMAD_HDDCNodeModel.EPS_MAX_VALUE);

	/***************************************************/
	/** Constants for the SettingsModelBoolean (graph) */
	/***************************************************/

	static final String CFGKEY_BOOLEAN_GRAPH = "graph";

	private final SettingsModelBoolean m_graph = new SettingsModelBoolean(
			RMAD_HDDCNodeModel.CFGKEY_BOOLEAN_GRAPH, 
			false);

	/***********************/
	/** Constants for algo */
	/***********************/

	static final String CFGKEY_STRING_ALGO = "algo";

	static final ArrayList<String> m_algo = new ArrayList<>();

	/** The available algorithms are the 
	 * Expectation-Maximisation ("EM"), the Classification E-M ("CEM") and
	 * the Stochastic E-M ("SEM"). The default algorithm is the "EM".
	 */
	static{
		m_algo.add(0, "EM");
		m_algo.add(1, "CEM");
		m_algo.add(2, "SEM");
	}

	private final SettingsModelString m_algo_chooser = new SettingsModelString(
			RMAD_HDDCNodeModel.CFGKEY_STRING_ALGO, 
			RMAD_HDDCNodeModel.m_algo.get(0));

	/********************/
	/** Constants for d */
	/********************/

	static final String CFGKEY_STRING_D = "d_value";

	static final ArrayList<String> m_d = new ArrayList<>();

	/** The available algorithms are the 
	 * Expectation-Maximisation ("EM"), the Classification E-M ("CEM") and
	 * the Stochastic E-M ("SEM"). The default algorithm is the "EM".
	 */
	static{
		m_d.add(0, "Cattell");
		m_d.add(1, "BIC");
	}

	private final SettingsModelString m_d_chooser = new SettingsModelString(
			RMAD_HDDCNodeModel.CFGKEY_STRING_D, 
			RMAD_HDDCNodeModel.m_d.get(0));

	/***********************/
	/** Constants for init */
	/***********************/

	static final String CFGKEY_STRING_INIT = "init";

	static final ArrayList<String> m_init = new ArrayList<>();

	/** The available algorithms are the 
	 * Expectation-Maximisation ("EM"), the Classification E-M ("CEM") and
	 * the Stochastic E-M ("SEM"). The default algorithm is the "EM".
	 */
	static{
		m_init.add(0, "param");
		m_init.add(1, "mini-em");
		m_init.add(2, "random");
		m_init.add(3, "kmeans");
	}

	static final String INIT_DEFAULT_VALUE = m_init.get(3);

	private final SettingsModelString m_init_chooser = new SettingsModelString(
			RMAD_HDDCNodeModel.CFGKEY_STRING_INIT, 
			RMAD_HDDCNodeModel.INIT_DEFAULT_VALUE);

	/**********************/
	/** Constant for show */
	/**********************/

	private static final boolean DEFAULT_SHOW = true;


	/********************************************************/
	/** Constants for the DialogComponentNumber (mini.nb-1) */
	/********************************************************/

	// mini.nb=c(5,10)

	static final String CFGKEY_COUNT_MINI_NB_1 = "mini.nb-1";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MINI_NB_1 = 5;

	/** Minimum value. */
	static final int MINI_NB_1_MIN_VALUE = 0;

	/** Maximum value. */
	static final int MINI_NB_1_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_mini_nb_1 = new SettingsModelIntegerBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_MINI_NB_1, 
			RMAD_HDDCNodeModel.DEFAULT_COUNT_MINI_NB_1, 
			RMAD_HDDCNodeModel.MINI_NB_1_MIN_VALUE, 
			RMAD_HDDCNodeModel.MINI_NB_1_MAX_VALUE);

	/********************************************************/
	/** Constants for the DialogComponentNumber (mini.nb-2) */
	/********************************************************/

	// mini.nb=c(5,10)

	static final String CFGKEY_COUNT_MINI_NB_2 = "mini.nb-2";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MINI_NB_2 = 10;

	/** Minimum value. */
	static final int MINI_NB_2_MIN_VALUE = 0;

	/** Maximum value. */
	static final int MINI_NB_2_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_mini_nb_2 = new SettingsModelIntegerBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_MINI_NB_2, 
			RMAD_HDDCNodeModel.DEFAULT_COUNT_MINI_NB_2, 
			RMAD_HDDCNodeModel.MINI_NB_2_MIN_VALUE, 
			RMAD_HDDCNodeModel.MINI_NB_2_MAX_VALUE);

	/*****************************************************/
	/** Constants for the SettingsModelBoolean (scaling) */
	/*****************************************************/

	static final String CFGKEY_BOOLEAN_SCALING = "scaling";

	private final SettingsModelBoolean m_scaling = new SettingsModelBoolean(
			RMAD_HDDCNodeModel.CFGKEY_BOOLEAN_SCALING, 
			false);

	/**************************************************************/
	/** Constants for the DialogComponentNumber (min.individuals) */
	/**************************************************************/

	// mini.nb=c(5,10)

	static final String CFGKEY_COUNT_MIN_INDIVIDUALS = "min.individuals";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MIN_INDIVIDUALS = 2;

	/** Minimum value. */
	static final int MIN_INDIVIDUALS_MIN_VALUE = 2;

	/** Maximum value. */
	static final int MIN_INDIVIDUALS_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_min_individuals = new SettingsModelIntegerBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_MIN_INDIVIDUALS, 
			RMAD_HDDCNodeModel.DEFAULT_COUNT_MIN_INDIVIDUALS, 
			RMAD_HDDCNodeModel.MIN_INDIVIDUALS_MIN_VALUE, 
			RMAD_HDDCNodeModel.MIN_INDIVIDUALS_MAX_VALUE);

	/*********************************************************/
	/** Constants for the DialogComponentNumber (noise.ctrl) */
	/*********************************************************/

	// !!!!!! N.B.: IL VALORE DEL PARAMETRO È STATO LASCIATO TRA 0 E 1. MA È GIUSTO??????

	// 1e-8 = 0.00000001

	static final String CFGKEY_COUNT_NOISE_CTRL = "noise.ctrl";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_NOISE_CTRL = 0.00000001;

	/** Minimum value. */
	static final double NOISE_CTRL_MIN_VALUE = 0;

	/** Maximum value. */
	static final double NOISE_CTRL_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_noise_ctrl = new SettingsModelDoubleBounded(
			RMAD_HDDCNodeModel.CFGKEY_COUNT_NOISE_CTRL,
			RMAD_HDDCNodeModel.DEFAULT_COUNT_NOISE_CTRL,
			RMAD_HDDCNodeModel.NOISE_CTRL_MIN_VALUE, 
			RMAD_HDDCNodeModel.NOISE_CTRL_MAX_VALUE);












	public RMAD_HDDCNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {

		FunctionValues fv = new FunctionValues("HDclassif", "hddc", m_filter.getIncludeList(), "posterior", "hddc$K");
		fv.put("K", K_ARRAY);
		if(m_models_chooser.getStringArrayValue().length == 1)
			fv.put("model", m_models_chooser.getStringArrayValue()[0]);
		else
			fv.put("model", m_models_chooser.getStringArrayValue());
		fv.put("threshold", m_threshold.getDoubleValue());

		if(Double.isNaN(m_com_dim2.getDoubleValue()))
			fv.put("com_dim", "NULL");
		else{
			double cd = m_com_dim2.getDoubleValue();
			if(cd - (int)cd != 0)
				getLogger().warn("Value of type double in field \"com_dim\" has been cast to integer.");
			fv.put("com_dim", (int)cd);
		}
		fv.put("itermax", m_itermax.getIntValue());
		fv.put("eps", m_eps.getDoubleValue());
		fv.put("graph", m_graph.getBooleanValue());
		fv.put("algo", m_algo_chooser.getStringValue());
		fv.put("d", m_d_chooser.getStringValue());
		fv.put("init", m_init_chooser.getStringValue());

		fv.put("show", DEFAULT_SHOW);
		Integer[] i = {m_mini_nb_1.getIntValue(), m_mini_nb_2.getIntValue()};
		fv.put("mini.nb", i);
		fv.put("scaling", m_scaling.getBooleanValue());
		fv.put("min.individuals", m_min_individuals.getIntValue());
		fv.put("noise.ctrl", m_noise_ctrl.getDoubleValue());
		return fv.toString() + "\n"
		+ "R <- cbind(R, hddc$class)";
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


		K_ARRAY = string2intArray(m_k.getStringValue());

		/**
		String cd = m_com_dim.getStringValue();
		if (!cd.equalsIgnoreCase("NULL"))
			COM_DIM = parseIntWithException(m_com_dim.getStringValue());
		 */




		return new DataTableSpec[1];
	}

	@Override
	protected void postConfigure(PortObject[] inData, ExecutionContext exec)
			throws InvalidSettingsException {
		// Nothing to do here.

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_k.saveSettingsTo(settings);
		m_models_chooser.saveSettingsTo(settings);
		m_threshold.saveSettingsTo(settings);
		/** m_com_dim.saveSettingsTo(settings);  */
		m_com_dim2.saveSettingsTo(settings);
		m_itermax.saveSettingsTo(settings);
		m_eps.saveSettingsTo(settings);
		m_graph.saveSettingsTo(settings);
		m_algo_chooser.saveSettingsTo(settings);
		m_d_chooser.saveSettingsTo(settings);
		m_init_chooser.saveSettingsTo(settings);
		m_mini_nb_1.saveSettingsTo(settings);
		m_mini_nb_2.saveSettingsTo(settings);
		m_scaling.saveSettingsTo(settings);
		m_min_individuals.saveSettingsTo(settings);
		m_noise_ctrl.saveSettingsTo(settings);

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_k.validateSettings(settings);
		m_models_chooser.validateSettings(settings);
		m_threshold.validateSettings(settings);
		/**m_com_dim.validateSettings(settings); */
		m_com_dim2.validateSettings(settings);
		m_itermax.validateSettings(settings);
		m_eps.validateSettings(settings);
		m_graph.validateSettings(settings);
		m_algo_chooser.validateSettings(settings);
		m_d_chooser.validateSettings(settings);
		m_init_chooser.validateSettings(settings);
		m_mini_nb_1.validateSettings(settings);
		m_mini_nb_2.validateSettings(settings);
		m_scaling.validateSettings(settings);
		m_min_individuals.validateSettings(settings);
		m_noise_ctrl.validateSettings(settings);

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_k.loadSettingsFrom(settings);
		m_models_chooser.loadSettingsFrom(settings);
		m_threshold.loadSettingsFrom(settings);
		/** m_com_dim.loadSettingsFrom(settings); */
		m_com_dim2.loadSettingsFrom(settings);
		m_itermax.loadSettingsFrom(settings);
		m_eps.loadSettingsFrom(settings);
		m_graph.loadSettingsFrom(settings);
		m_algo_chooser.loadSettingsFrom(settings);
		m_d_chooser.loadSettingsFrom(settings);
		m_init_chooser.loadSettingsFrom(settings);
		m_mini_nb_1.loadSettingsFrom(settings);
		m_mini_nb_2.loadSettingsFrom(settings);
		m_scaling.loadSettingsFrom(settings);
		m_min_individuals.loadSettingsFrom(settings);
		m_noise_ctrl.loadSettingsFrom(settings);

	}


}

