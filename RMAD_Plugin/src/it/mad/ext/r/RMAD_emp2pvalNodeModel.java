package it.mad.ext.r;

import java.util.ArrayList;
import java.util.Collection;

import it.mad.ext.r.preferences.RMADPreferenceProvider;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;


/**
 * This is the model implementation of RMAD_emp2pval.
 * Converts the Empirical Posterior Probability (EPP) computed by cluster.test into a frequentist * np-value, which can then be used to assess the significance of the alternative hypothesis.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_emp2pvalNodeModel extends RMADCoreNodeModel {
	

	/***********************************/
	/** Constant for the inport index. */
	/***********************************/
	public static final int IN_PORT = 0;


	/******************************************/
	/** Constants for the ColumnFilter (data) */
	/******************************************/

	/** the identifier the values are stored with in the NodeSettings object*/
	static final String m_defaultTabTitle = "emp2pval_ColumnFilter_Name";

	static final Collection<String> m_inclList = new ArrayList<String>();

	static final Collection<String> m_exclList = new ArrayList<String>();

	private final SettingsModelFilterString m_filter = 
			new SettingsModelFilterString(
					RMAD_emp2pvalNodeModel.m_defaultTabTitle, 
					RMAD_emp2pvalNodeModel.m_inclList, 
					RMAD_emp2pvalNodeModel.m_exclList);

	
	/******************************************************************/
	/** Constants for the DialogComponentNumber (nsim - cluster.test) */
	/******************************************************************/

	static final String CFGKEY_COUNT_CLUSTERTEST_NSIM = "cluster.test_nsim_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_CLUSTERTEST_NSIM = 500000;

	private final SettingsModelInteger clustertest_nsim = new SettingsModelInteger(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_CLUSTERTEST_NSIM, 
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_CLUSTERTEST_NSIM);

	
	/******************************************************************/
	/** Constants for the DialogComponentNumber (nsim - nulldensity) */
	/******************************************************************/

	static final String CFGKEY_COUNT_NULLDENSITY_NSIM = "nulldensity_nsim_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_NULLDENSITY_NSIM = 8000;

	private final SettingsModelInteger nulldensity_nsim = new SettingsModelInteger(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_NULLDENSITY_NSIM, 
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_NULLDENSITY_NSIM);

	
	/*************************************************/
	/** Constants for the DialogComponentNumber (aR) */
	/*************************************************/

	static final String CFGKEY_COUNT_AR = "aR";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_AR = 0.4;

	/** Minimum value. */
	static final double AR_MIN_VALUE = 0;

	/** Maximum value. */
	static final double AR_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_aR = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_AR,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_AR,
			RMAD_emp2pvalNodeModel.AR_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.AR_MAX_VALUE);


	/********************/
	/** Constants for P */
	/********************/

	// Initial (default) count value for the P parameter. This should not be chosen by the user.
	private int DEFAULT_COUNT_P;
	
	
	/********************/
	/** Constants for N */
	/********************/

	// Initial (default) count value for the n parameter. This should not be chosen by the user.
	private int NULLDENSITY_N;



	/**********************************************************/
	/** Constants for the DialogComponentNumber (k, clusters) */
	/**********************************************************/

	static final String CFGKEY_COUNT_K = "cluster_size";

	// *** N.B.: ricordarsi di inserire il metodo getNumberOfColumns()!!!
	/** Initial default count value. */
	static final int DEFAULT_COUNT_K = 2;

	/** Minimum value. */
	static final int K_MIN_VALUE = 1;

	/** Maximum value. */
	static final int K_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_cluster = new SettingsModelIntegerBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_K,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_K,
			RMAD_emp2pvalNodeModel.K_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.K_MAX_VALUE);


	/************************************************/
	/** Constants for the DialogComponentNumber (a) */
	/************************************************/
	// a, b and tau2 have to be non-negative

	static final String CFGKEY_COUNT_A = "a_size";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_A = 2.01;

	/** Minimum value. */
	static final double A_MIN_VALUE = 0;

	/** Maximum value. */
	static final double A_MAX_VALUE = Double.MAX_VALUE;

	private final SettingsModelDoubleBounded m_a = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_A,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_A,
			RMAD_emp2pvalNodeModel.A_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.A_MAX_VALUE);

	
	/************************************************/
	/** Constants for the DialogComponentNumber (b) */
	/************************************************/
	// a, b and tau2 have to be non-negative

	static final String CFGKEY_COUNT_B = "b_size";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_B = 0.990099;

	/** Minimum value. */
	static final double B_MIN_VALUE = 0;

	/** Maximum value. */
	static final double B_MAX_VALUE = Double.MAX_VALUE;

	private final SettingsModelDoubleBounded m_b = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_B,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_B,
			RMAD_emp2pvalNodeModel.B_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.B_MAX_VALUE);

	/****************************************************/
	/** Constants for the DialogComponentNumber (tau2) */
	/****************************************************/
	// a, b and tau2 must be non-negative

	static final String CFGKEY_COUNT_TAU2 = "tau2_size";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_TAU2 = 1;

	/** Minimum value. */
	static final double TAU2_MIN_VALUE = 0;

	/** Maximum value. */
	static final double TAU2_MAX_VALUE = Double.MAX_VALUE;

	private final SettingsModelDoubleBounded m_tau2 = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_TAU2,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_TAU2,
			RMAD_emp2pvalNodeModel.TAU2_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.TAU2_MAX_VALUE);

	/**************************************************************************/
	/** Constants for the DialogComponentNumber (replications - cluster.test) */
	/**************************************************************************/

	static final String CFGKEY_COUNT_REPLICATIONS = "replications";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_REPLICATIONS = 1;

	/** Minimum value. */
	static final int REPLICATIONS_MIN_VALUE = 1;

	/** Maximum value. */
	static final int REPLICATIONS_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_replications = new SettingsModelIntegerBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_REPLICATIONS,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_REPLICATIONS,
			RMAD_emp2pvalNodeModel.REPLICATIONS_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.REPLICATIONS_MAX_VALUE);

	/**************************************************/
	/** Constants for the DialogComponentNumber (mcs) */
	/**************************************************/
	// a, b and tau2 have to be non-negative

	static final String CFGKEY_COUNT_MCS = "mcs_size";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_MCS = 0.1;

	/** Minimum value. */
	static final double MCS_MIN_VALUE = 0;

	/** Maximum value. */
	static final double MCS_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_mcs = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_MCS,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_MCS,
			RMAD_emp2pvalNodeModel.MCS_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.MCS_MAX_VALUE);

	/**************************************************/
	/** Constants for the DialogComponentNumber (prop - nulldensity) */
	/**************************************************/
	// a, b and tau2 have to be non-negative

	static final String CFGKEY_COUNT_PROP = "prop_size";

	/** Initial default count value. */
	static final double DEFAULT_COUNT_PROP = 0.25;

	/** Minimum value. */
	static final double PROP_MIN_VALUE = 0;

	/** Maximum value. */
	static final double PROP_MAX_VALUE = 1;

	private final SettingsModelDoubleBounded m_prop = new SettingsModelDoubleBounded(
			RMAD_emp2pvalNodeModel.CFGKEY_COUNT_PROP,
			RMAD_emp2pvalNodeModel.DEFAULT_COUNT_PROP,
			RMAD_emp2pvalNodeModel.PROP_MIN_VALUE, 
			RMAD_emp2pvalNodeModel.PROP_MAX_VALUE);














	/**
	 * Creates new instance of <code>RMAD_emp2pvaplNodeModel</code> with one
	 * data in and data one out port.
	 * @param pref R preference provider
	 */
	public RMAD_emp2pvalNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		/**
		 * R <- R 
		 * library("bayesclust")
		 * cluster.optimal <- cluster.optimal(cbind(R$"Universe.0.0", R$"Universe.0.1", R$"Universe.1.0", R$"Universe.1.1"), 1000, 0.4, 4, 2, 2.01, 0.990099, 1.0, 0.1, , , 4)$data1$cluster
		 * cluster.optimal <- matrix(cluster.optimal, ncol = 4)
		 * R <- cbind(R, cluster.optimal)
		 */

		String cbind = "cbind(";
		for(String columnName : m_filter.getIncludeList()){
			cbind = cbind + "R$\"" + formatColumn(columnName) + "\", ";
		}
		cbind = cbind.substring(0, cbind.length() - 2) + ")";

		int clustertestNsim = clustertest_nsim.getIntValue();
		double aR = m_aR.getDoubleValue();
		int k = m_cluster.getIntValue();
		double a = m_a.getDoubleValue();
		double b = m_b.getDoubleValue();
		double tau2 = m_tau2.getDoubleValue();
		double mcs = m_mcs.getDoubleValue();
		int replications = m_replications.getIntValue();
		String cluster_test = "test1 <- cluster.test(" + cbind 
				+ ", nsim = " + clustertestNsim + ", aR = " + aR + ", p = " + DEFAULT_COUNT_P + ", k = " + k 
				+ ", a = " + a + ", b = " + b + ", tau2 = " + tau2 + ", mcs = " + mcs 
				+ ",,, replications = " + replications + ") \n";
		int nulldensityNsim = nulldensity_nsim.getIntValue();
		double prop = m_prop.getDoubleValue();
		// null1 <- nulldensity(nsim=100, n=12, p=2, k=2)
		String nulldensity = "null1 <- nulldensity(nsim = " + nulldensityNsim + ", n = " + NULLDENSITY_N 
				+ ", p = " + DEFAULT_COUNT_P + ", k = " + k + ", a = " + a + ", b = " + b + ", tau2 = " + tau2 
				+ ", mcs = " + mcs + ", prop = " + prop + ", ) \n";
		String emp2pval = "R <- emp2pval(test1, null1)$pvals \n";

		String Command = "R <- R \n"
				+ "library(\"bayesclust\") \n"
				+ cluster_test
				+ nulldensity
				+ emp2pval;

		return Command;
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

		// Sets the value of p.
		DEFAULT_COUNT_P = m_filter.getIncludeList().size();;

		return new DataTableSpec[1];
	}

	@Override
	protected void postConfigure(PortObject[] inData, ExecutionContext exec)
			throws InvalidSettingsException {
		NULLDENSITY_N = ((BufferedDataTable) inData[IN_PORT]).getRowCount();	
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		clustertest_nsim.saveSettingsTo(settings);
		nulldensity_nsim.saveSettingsTo(settings);
		m_aR.saveSettingsTo(settings);
		m_cluster.saveSettingsTo(settings);
		m_a.saveSettingsTo(settings);
		m_b.saveSettingsTo(settings);
		m_tau2.saveSettingsTo(settings);
		m_replications.saveSettingsTo(settings);
		m_mcs.saveSettingsTo(settings);
		m_prop.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		clustertest_nsim.validateSettings(settings);
		nulldensity_nsim.validateSettings(settings);
		m_aR.validateSettings(settings);
		m_cluster.validateSettings(settings);
		m_a.validateSettings(settings);
		m_b.validateSettings(settings);
		m_tau2.validateSettings(settings);
		m_replications.validateSettings(settings);
		m_mcs.validateSettings(settings);
		m_prop.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		clustertest_nsim.loadSettingsFrom(settings);
		nulldensity_nsim.loadSettingsFrom(settings);
		m_aR.loadSettingsFrom(settings);
		m_cluster.loadSettingsFrom(settings);
		m_a.loadSettingsFrom(settings);
		m_b.loadSettingsFrom(settings);
		m_tau2.loadSettingsFrom(settings);
		m_replications.loadSettingsFrom(settings);
		m_mcs.loadSettingsFrom(settings);
		m_prop.loadSettingsFrom(settings);
	}

}

