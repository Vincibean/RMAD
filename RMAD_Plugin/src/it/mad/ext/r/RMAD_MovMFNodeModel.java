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
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of RMAD_MovMF.
 * Fit mixtures of von Mises-Fisher Distributions
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_MovMFNodeModel extends RMADCoreNodeModel {

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
					RMAD_MovMFNodeModel.m_defaultTabTitle, 
					RMAD_MovMFNodeModel.m_inclList, 
					RMAD_MovMFNodeModel.m_exclList);


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
			new SettingsModelIntegerBounded(RMAD_MovMFNodeModel.CFGKEY_COUNT_CLUSTERS,
					RMAD_MovMFNodeModel.DEFAULT_COUNT_CLUSTERS,
					RMAD_MovMFNodeModel.K_MIN_VALUE, 
					RMAD_MovMFNodeModel.K_MAX_VALUE);

	/*******************************************************/
	/** Constants for the DialogComponentNumber (maxiter) */
	/*******************************************************/

	static final String CFGKEY_COUNT_MAXITER = "Max_iter";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MAXITER = 100;

	/** Minimum value. */
	static final int MAXITER_MIN_VALUE = 1;

	/** Maximum value. */
	static final int MAXITER_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_maxiter = new SettingsModelIntegerBounded(
			RMAD_MovMFNodeModel.CFGKEY_COUNT_MAXITER,
			RMAD_MovMFNodeModel.DEFAULT_COUNT_MAXITER,
			RMAD_MovMFNodeModel.MAXITER_MIN_VALUE, 
			RMAD_MovMFNodeModel.MAXITER_MAX_VALUE);

	/*****************************************************/
	/** Constants for the DialogComponentNumber (reltol) */
	/*****************************************************/

	static final String CFGKEY_COUNT_RELTOL = "Reltol";

	/** Initial default count value. */
	static final Double DEFAULT_COUNT_RELTOL = Double.NaN;

	private final SettingsModelDouble m_reltol = new SettingsModelDouble(
			RMAD_MovMFNodeModel.CFGKEY_COUNT_RELTOL, 
			RMAD_MovMFNodeModel.DEFAULT_COUNT_RELTOL);

	/************************/
	/** Constants for start */
	/************************/

	static final String CFGKEY_STRING_START = "Start";

	static final ArrayList<String> m_start = new ArrayList<>();

	/** A character vector with elements "i"(randomly pick component ids for the observations),
	 *  or one of "p", "S" or "s". By default, initialization method "p" is used.
	 */
	static{
		m_start.add(0, "i");
		m_start.add(1, "p");
		m_start.add(2, "S");
		m_start.add(3, "s");
	}

	private final SettingsModelString m_start_chooser = new SettingsModelString(
			RMAD_MovMFNodeModel.CFGKEY_STRING_START, 
			RMAD_MovMFNodeModel.m_start.get(1));

	/****************************************************/
	/** Constants for the DialogComponentNumber (nruns) */
	/****************************************************/

	static final String CFGKEY_COUNT_NRUNS = "Nruns";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_NRUNS = 1;

	/** Minimum value. */
	static final int NRUNS_MIN_VALUE = 1;

	/** Maximum value. */
	static final int NRUNS_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_nruns = new SettingsModelIntegerBounded(
			RMAD_MovMFNodeModel.CFGKEY_COUNT_NRUNS,
			RMAD_MovMFNodeModel.DEFAULT_COUNT_NRUNS,
			RMAD_MovMFNodeModel.NRUNS_MIN_VALUE, 
			RMAD_MovMFNodeModel.NRUNS_MAX_VALUE);

	/*******************************************************/
	/** Constants for the DialogComponentNumber (minalpha) */
	/*******************************************************/

	static final String CFGKEY_COUNT_MINALPHA = "Minalpha";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_MINALPHA = 0;

	private final SettingsModelDouble m_minalpha= new SettingsModelDouble(
			RMAD_MovMFNodeModel.CFGKEY_COUNT_MINALPHA, 
			RMAD_MovMFNodeModel.DEFAULT_COUNT_MINALPHA);

	/***************************************************/
	/** Constants for the SettingsModelBoolean (converge) */
	/***************************************************/

	static final String CFGKEY_BOOLEAN_CONVERGE = "Converge";

	private final SettingsModelBoolean m_converge = new SettingsModelBoolean(
			RMAD_MovMFNodeModel.CFGKEY_BOOLEAN_CONVERGE, 
			true);


	/**************************************************/
	/** Constants for the DialogComponentNumber (verbose) */
	/**************************************************/

	private static final Boolean DEFAULT_VERBOSE = true;








	/**
	 * Creates new instance of <code>RMAD_MovMFNodeModel</code> with one
	 * data in and data one out port.
	 * @param pref R preference provider
	 */
	public RMAD_MovMFNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		FunctionValues fv = new FunctionValues("movMF", "movMF", m_filter.getIncludeList());
		fv.put("k", m_count.getIntValue());
		fv.put("maxiter", m_maxiter.getIntValue());

		if(Double.isNaN(m_reltol.getDoubleValue()))
			fv.put("com_dim", "sqrt(.Machine$double.eps)");
		else
			fv.put("reltol", m_reltol.getDoubleValue());
		fv.put("start", m_start_chooser.getStringValue());
		fv.put("nruns", m_nruns.getIntValue());
		fv.put("minalpha", m_minalpha.getDoubleValue());
		fv.put("converge", m_converge.getBooleanValue());
		fv.put("verbose", DEFAULT_VERBOSE);
		String command = fv.toString();

		// movMF)\n = 8	
		command = command.substring(0, command.length() - 7);
		command += "predict(movMF))\n";

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
		int clusters = m_count.getIntValue();
		if(clusters > numData)
			throw new InvalidSettingsException("The number of clusters should be lower than the number of observations.");

	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_count.saveSettingsTo(settings);
		m_maxiter.saveSettingsTo(settings);
		m_reltol.saveSettingsTo(settings);
		m_start_chooser.saveSettingsTo(settings);
		m_nruns.saveSettingsTo(settings);
		m_minalpha.saveSettingsTo(settings);
		m_converge.saveSettingsTo(settings);

	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_count.validateSettings(settings);
		m_maxiter.validateSettings(settings);
		m_reltol.validateSettings(settings);
		m_start_chooser.validateSettings(settings);
		m_nruns.validateSettings(settings);
		m_minalpha.validateSettings(settings);
		m_converge.validateSettings(settings);

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_count.loadSettingsFrom(settings);
		m_maxiter.loadSettingsFrom(settings);
		m_reltol.loadSettingsFrom(settings);
		m_start_chooser.loadSettingsFrom(settings);
		m_nruns.loadSettingsFrom(settings);
		m_minalpha.loadSettingsFrom(settings);
		m_converge.loadSettingsFrom(settings);		

	}

}

