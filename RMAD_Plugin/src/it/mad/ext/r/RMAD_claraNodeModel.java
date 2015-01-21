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
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of RMAD_clara.
 * Computes a "clara" object, a list representing a clustering of the data into k clusters. 
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_claraNodeModel extends RMADCoreNodeModel {

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
					RMAD_claraNodeModel.m_defaultTabTitle, 
					RMAD_claraNodeModel.m_inclList, 
					RMAD_claraNodeModel.m_exclList);


	/*****************************************************************/
	/** Constants for the DialogComponentNumber (number of clusters) */
	/*****************************************************************/

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
			new SettingsModelIntegerBounded(RMAD_claraNodeModel.CFGKEY_COUNT_CLUSTERS,
					RMAD_claraNodeModel.DEFAULT_COUNT_CLUSTERS,
					RMAD_claraNodeModel.K_MIN_VALUE, 
					RMAD_claraNodeModel.K_MAX_VALUE);

	/********************************************/
	/** Constants for the Button Group (metric) */
	/********************************************/

	static final String CFGKEY_STRING = "Metric";

	static final String EUCLIDEAN_VALUE = "euclidean";

	static final String MANHATTAN_VALUE = "manhattan";

	static final String[] METRIC_ELEMENTS = {EUCLIDEAN_VALUE, MANHATTAN_VALUE} ;

	private final SettingsModelString m_metric =
			new SettingsModelString(RMAD_claraNodeModel.CFGKEY_STRING,
					RMAD_claraNodeModel.EUCLIDEAN_VALUE);


	/***************************************************/
	/** Constants for the SettingsModelBoolean (stand) */
	/***************************************************/

	static final String CFGKEY_BOOLEAN_STAND = "Stand";

	private final SettingsModelBoolean m_stand = new SettingsModelBoolean(
			RMAD_claraNodeModel.CFGKEY_BOOLEAN_STAND, 
			false);


	/******************************************************/
	/** Constants for the DialogComponentNumber (samples) */
	/******************************************************/

	static final String CFGKEY_COUNT_SAMPLES = "Samples_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_SAMPLES = 5;

	private final SettingsModelInteger m_samples = new SettingsModelInteger(
			RMAD_claraNodeModel.CFGKEY_COUNT_SAMPLES, 
			RMAD_claraNodeModel.DEFAULT_COUNT_SAMPLES);


	/*******************************************************/
	/** Constants for the DialogComponentNumber (sampsize) */
	/*******************************************************/

	static final String CFGKEY_COUNT_SAMPSIZE = "Sampsize";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_SAMPSIZE = 40 + 2*DEFAULT_COUNT_CLUSTERS;

	/** Minimum value. */
	static final int SAMPSIZE_MIN_VALUE = 0;

	/** Maximum value. */
	static final int SAMPSIZE_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_sampsize = new SettingsModelIntegerBounded(
			RMAD_claraNodeModel.CFGKEY_COUNT_SAMPSIZE,
			RMAD_claraNodeModel.DEFAULT_COUNT_SAMPSIZE,
			RMAD_claraNodeModel.SAMPSIZE_MIN_VALUE, 
			RMAD_claraNodeModel.SAMPSIZE_MAX_VALUE);


	/****************************************************/
	/** Constants for the DialogComponentNumber (trace) */
	/****************************************************/

	static final String CFGKEY_COUNT_TRACE = "Trace_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_TRACE = 0;

	private final SettingsModelInteger m_trace = new SettingsModelInteger(
			RMAD_claraNodeModel.CFGKEY_COUNT_TRACE, 
			RMAD_claraNodeModel.DEFAULT_COUNT_TRACE);

	/*******************************************************/
	/** Constants for the SettingsModelBoolean (medoids.x) */
	/*******************************************************/

	static final String CFGKEY_BOOLEAN_MEDOIDS = "medoids.x";

	private final SettingsModelBoolean m_medoids = new SettingsModelBoolean(
			RMAD_claraNodeModel.CFGKEY_BOOLEAN_MEDOIDS, 
			true);

	/*******************************************************/
	/** Constants for the SettingsModelBoolean (keep.data) */
	/*******************************************************/

	static final String CFGKEY_BOOLEAN_KEEP_DATA = "keep.data";

	private final SettingsModelBoolean m_keepData = new SettingsModelBoolean(
			RMAD_claraNodeModel.CFGKEY_BOOLEAN_KEEP_DATA, 
			false);

	/**************************************************/
	/** Constants for the SettingsModelBoolean (rngR) */
	/**************************************************/

	static final String CFGKEY_BOOLEAN_RNGR = "rngR";

	private final SettingsModelBoolean m_rngR = new SettingsModelBoolean(
			RMAD_claraNodeModel.CFGKEY_BOOLEAN_RNGR, 
			false);

	/*****************************************************/
	/** Constants for the SettingsModelBoolean (pamLike) */
	/*****************************************************/

	static final String CFGKEY_BOOLEAN_PAMLIKE = "pamLike";

	private final SettingsModelBoolean m_pamLike = new SettingsModelBoolean(
			RMAD_claraNodeModel.CFGKEY_BOOLEAN_PAMLIKE, 
			false);



	/**
	 * Creates new instance of <code>RMAD_claraNodeModel</code> with one
	 * data in and data one out port.
	 * @param pref R preference provider
	 */
	public RMAD_claraNodeModel(final RMADPreferenceProvider pref) {
		super(pref);
	}
	
	@Override
	protected String getCommand() {
		FunctionValues fv = new FunctionValues("cluster", "clara", m_filter.getIncludeList(), "clustering");
		fv.put("k", m_count.getIntValue());
		fv.put("metric", m_metric.getStringValue());
		fv.put("stand", m_stand.getBooleanValue());
		fv.put("samples", m_samples.getIntValue());
		fv.put("sampsize", m_sampsize.getIntValue());
		fv.put("trace", m_trace.getIntValue());
		fv.put("medoids.x", m_medoids.getBooleanValue());
		fv.put("keep.data", m_keepData.getBooleanValue());
		fv.put("rngR", m_rngR.getBooleanValue());
		fv.put("pamLike", m_pamLike.getBooleanValue());
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

		// If medoids.x is set to FALSE, keep.data must be set to FALSE as well.
		if(m_medoids.getBooleanValue() == false){
			if(m_keepData.getBooleanValue() != false)
				throw new InvalidSettingsException("If medoids.x is set to FALSE, keep.data must be set to FALSE as well.");
		}

		return new DataTableSpec[1];
	}

	/**
	 * {@inheritDoc}
	 */ 
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_count.saveSettingsTo(settings);
		m_metric.saveSettingsTo(settings);
		m_stand.saveSettingsTo(settings);
		m_samples.saveSettingsTo(settings);
		m_sampsize.saveSettingsTo(settings);
		m_trace.saveSettingsTo(settings);
		m_medoids.saveSettingsTo(settings);
		m_keepData.saveSettingsTo(settings);
		m_rngR.saveSettingsTo(settings);
		m_pamLike.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		m_filter.loadSettingsFrom(settings);
		m_count.loadSettingsFrom(settings);
		m_metric.loadSettingsFrom(settings);
		m_stand.loadSettingsFrom(settings);
		m_samples.loadSettingsFrom(settings);
		m_sampsize.loadSettingsFrom(settings);
		m_trace.loadSettingsFrom(settings);
		m_medoids.loadSettingsFrom(settings);
		m_keepData.loadSettingsFrom(settings);
		m_rngR.loadSettingsFrom(settings);
		m_pamLike.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */ 
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		// Simply checks if the keys are available.
		m_filter.validateSettings(settings);
		m_count.validateSettings(settings);
		m_metric.validateSettings(settings);
		m_stand.validateSettings(settings);
		m_samples.validateSettings(settings);
		m_sampsize.validateSettings(settings);
		m_trace.validateSettings(settings);
		m_medoids.validateSettings(settings);
		m_keepData.validateSettings(settings);
		m_rngR.validateSettings(settings);
		m_pamLike.validateSettings(settings);
	}



	@Override
	protected void postConfigure(PortObject[] inData, ExecutionContext exec) throws InvalidSettingsException {
		// The number of clusters should be lower than the number of observations
		int numData = ((BufferedDataTable) inData[IN_PORT]).getRowCount();
		int clusters = m_count.getIntValue();
		if(clusters > numData)
			throw new InvalidSettingsException("The number of clusters should be lower than the number of observations.");

		// sampsize should be higher than the number of clusters (k) and at most the number of observations
		int sampsize = m_sampsize.getIntValue();
		if(sampsize <= clusters)
			throw new InvalidSettingsException("'sampsize' should be higher than the number of clusters");
		if(sampsize > numData)
			throw new InvalidSettingsException("'sampsize' should be at most the number of observations");
	}

}

