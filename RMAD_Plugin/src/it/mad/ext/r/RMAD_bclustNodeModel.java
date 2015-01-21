package it.mad.ext.r;

import it.mad.ext.r.preferences.RMADPreferenceProvider;

import java.util.ArrayList;
import java.util.Collection;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;

/**
 * This is the model implementation of RMAD_bclust.
 * The function clusters data saved in a matrix using an additive linear model with disappearing random effects. The model has built-in spike-and-slab components which quantifies important variables for clustering and can be extracted using the imp function.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_bclustNodeModel extends RMADCoreNodeModel {

	/***********************************/
	/** Constant for the inport index. */
	/***********************************/

	public static final int IN_PORT = 0;


	/* CONSTANTS FOR BCLUST - LOGLIKELIHOOD */

	/***********************************/
	/** Constants for the ColumnFilter */
	/***********************************/

	/** the identifier the values are stored with in the NodeSettings object*/
	static final String m_defaultTabTitle = "configName";

	static final Collection<String> m_inclList = new ArrayList<String>();

	static final Collection<String> m_exclList = new ArrayList<String>();

	private final SettingsModelFilterString m_filter = 
			new SettingsModelFilterString(
					RMAD_bclustNodeModel.m_defaultTabTitle, 
					RMAD_bclustNodeModel.m_inclList, 
					RMAD_bclustNodeModel.m_exclList);

	/********************************/
	/** Constants for effect.family */
	/********************************/

	static final String CFGKEY_STRING_EFFECT_FAMILY = "Effect_Family";

	static final ArrayList<String> m_effect_family = new ArrayList<>();

	/** 
	 * The choices are "gaussian" or "alaplace" allowing Gaussian 
	 * or asymmetric Laplace family, respectively.
	 */
	static{
		m_effect_family.add(0, "gaussian");
		m_effect_family.add(1, "alaplace");
	}

	static final String EFFECT_FAMILY_DEFAULT_VALUE = m_effect_family.get(0);

	private final SettingsModelString m_effect_family_chooser = new SettingsModelString(
			RMAD_bclustNodeModel.CFGKEY_STRING_EFFECT_FAMILY, 
			RMAD_bclustNodeModel.EFFECT_FAMILY_DEFAULT_VALUE);

	/********************************************************/
	/** Constants for the SettingsModelBoolean (var.select) */
	/********************************************************/

	static final String CFGKEY_BOOLEAN_VAR_SELECT = "Var_Select";

	private final SettingsModelBoolean m_var_select = new SettingsModelBoolean(
			RMAD_bclustNodeModel.CFGKEY_BOOLEAN_VAR_SELECT, 
			true);

	/* CONSTANTS FOR OPTIM */

	/*************************/
	/** Constants for method */
	/*************************/

	static final String CFGKEY_STRING_METHOD = "method";

	static final ArrayList<String> m_method = new ArrayList<>();

	/** 
	 * method = c("Nelder-Mead", "BFGS", "CG", "L-BFGS-B", "SANN", "Brent")		// Brent has been discarded, since it works for the "optimize" function
	 */
	static{
		m_method.add(0, "Nelder-Mead");
		m_method.add(1, "BFGS");
		m_method.add(2, "CG");
		m_method.add(3, "L-BFGS-B");
		m_method.add(4, "SANN");
	}

	static final String METHOD_DEFAULT_VALUE = m_method.get(0);

	private final SettingsModelString m_method_chooser = new SettingsModelString(
			RMAD_bclustNodeModel.CFGKEY_STRING_METHOD, 
			RMAD_bclustNodeModel.METHOD_DEFAULT_VALUE);

	
	
	
	
	
	
	
	
	/**
	 * LOGLIKELIHOOD:
	 * The transformed.par is a vector of length 6 
	 * when using effect.family = "gaussian" and var.select=TRUE, and is 
	 * a vector of length 7 for effect.family="alaplace" and var.select=TRUE. 
	 * When var.select=FALSE the q parameter is dropped, yielding a vector of length 5 
	 * for effect.family="gaussian" and a vector of length 6 for effect.family="alaplace". 	 
	 */

	/**			____________________________________
	 * _________|___GAUSSIAN___	|______	ALAPLACE___	|
	 *|__TRUE__	|_______6_______|__________7________|
	 *|__FALSE__|_______5_______|__________6________|
	 */










	/**
	 * Creates new instance of <code>RMAD_bclustNodeModel</code> with one
	 * data in and data one out port.
	 * @param pref R preference provider
	 */
	public RMAD_bclustNodeModel(RMADPreferenceProvider pref) {
		super(pref);
	}

	@Override
	protected String getCommand() {
		// Creates a matrix of incoming data, combining them by columns.
		//StringBuilder cbind = new StringBuilder("cbind(");
		StringBuilder cbindMatrix = new StringBuilder("X <- cbind(");
		for(String columnName : m_filter.getIncludeList()){
			if (cbindMatrix.length() > 11)  	// 11 = number of characters in String "X <- cbind("
				cbindMatrix.append(", ");
			cbindMatrix.append("R$\"" + RMADAbstractNodeModel.formatColumn(columnName) + "\"");
		}
		cbindMatrix.append(")\n");

		String mean = "mean <- meancss(X)\n";

		String optimfunc = "optimfunc<-function(theta){-loglikelihood(x.mean=mean$mean, x.css=mean$css, repno=mean$repno, transformed.par=theta, effect.family=" + "\"" + m_effect_family_chooser.getStringValue() + "\"" +  ", var.select=" + Boolean.toString(m_var_select.getBooleanValue()).toUpperCase() + ")}\n";

		int paramNumber;
		// "gaussian", "alaplace"
		switch(m_effect_family_chooser.getStringValue()){
		case "gaussian":
			if (m_var_select.getBooleanValue() == true)
				paramNumber = 6;
			else
				paramNumber = 5;
			break;
		case "alaplace":
			if (m_var_select.getBooleanValue() == true)
				paramNumber = 7;
			else
				paramNumber = 6;
			break;
		default:
			paramNumber = 6;	// Arbitrary value, since this case will never be met.
			break;
		}

		String optim = "transpar<-optim(rep(0," + paramNumber + "), optimfunc, method=" + "\"" + m_method_chooser.getStringValue() + "\"" + ")\n";

		String bclust = "bc <- bclust(X, effect.family=" + "\"" + m_effect_family_chooser.getStringValue() + "\"" +  ", var.select=" + Boolean.toString(m_var_select.getBooleanValue()).toUpperCase() + ", transformed.par=transpar$par)\n";

		String rFinal = "R <- cbind(R, bc$optim.alloc)\n";

		StringBuilder command = new StringBuilder("R <- R \n"
				+ "library(\"bclust\")\n"
				+ cbindMatrix
				+ mean
				+ optimfunc
				+ optim
				+ bclust
				+ rFinal);

		return command.toString();
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
		// Nothing to do here.

	}
	
	
	@Override
	protected PortObject[] execute(PortObject[] inData, ExecutionContext exec)
			throws CanceledExecutionException, Exception {
		PortObject[] p = new PortObject[0];
		try{
			p = super.execute(inData, exec);
		}catch(InvalidSettingsException e){
			throw new InvalidSettingsException(e.getMessage() + " In Bayesian Agglomerative Clustering," +
					"the C-stack of the system may overflow if you have a large dataset. " +
					"You can try to adjust the stack before running R using your operation system command line. " +
					"If you use Linux, try opening a console and typing > ulimit -s unlimited and then run R in the same console. " +
					"The Microsoft Windows users should not need to increase the stack size.");
		}
		return p;
	}
	
	

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_effect_family_chooser.saveSettingsTo(settings);
		m_var_select.saveSettingsTo(settings);
		m_method_chooser.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_effect_family_chooser.validateSettings(settings);
		m_var_select.validateSettings(settings);
		m_method_chooser.validateSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_effect_family_chooser.loadSettingsFrom(settings);
		m_var_select.loadSettingsFrom(settings);
		m_method_chooser.loadSettingsFrom(settings);
	}

}

