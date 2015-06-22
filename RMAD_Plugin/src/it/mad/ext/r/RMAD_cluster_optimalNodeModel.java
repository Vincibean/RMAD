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

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import it.mad.ext.r.preferences.RMADPreferenceProvider;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.util.FileUtil;

/**
 * This is the model implementation of RMAD_cluster_optimal.
 * cluster.optimal will search for the optimal k-clustering of the dataset.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_cluster_optimalNodeModel extends RMADCoreNodeModel {

	/***********************************/
	/** Constant for the inport index. */
	/***********************************/
	public static final int IN_PORT = 0;


	/***********************************/
	/** Constants for the ColumnFilter */
	/***********************************/

	/** the identifier the values are stored with in the NodeSettings object*/
	static final String m_defaultTabTitle = "cluster_optimal_ColumnFilter_Name";

	static final Collection<String> m_inclList = new ArrayList<String>();

	static final Collection<String> m_exclList = new ArrayList<String>();

	private final SettingsModelFilterString m_filter = 
			new SettingsModelFilterString(
					RMAD_cluster_optimalNodeModel.m_defaultTabTitle, 
					RMAD_cluster_optimalNodeModel.m_inclList, 
					RMAD_cluster_optimalNodeModel.m_exclList);

	/***************************************************/
	/** Constants for the DialogComponentNumber (nsim) */
	/***************************************************/

	static final String CFGKEY_COUNT_NSIM = "nsim_Number";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_NSIM = 1000000;

	private final SettingsModelInteger m_nsim = new SettingsModelInteger(
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_NSIM, 
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_NSIM);

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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_AR,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_AR,
			RMAD_cluster_optimalNodeModel.AR_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.AR_MAX_VALUE);



	// Initial (default) count value for the P parameter. This should not be chosen by the user.
	private int DEFAULT_COUNT_P;



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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_K,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_K,
			RMAD_cluster_optimalNodeModel.K_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.K_MAX_VALUE);


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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_A,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_A,
			RMAD_cluster_optimalNodeModel.A_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.A_MAX_VALUE);

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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_B,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_B,
			RMAD_cluster_optimalNodeModel.B_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.B_MAX_VALUE);

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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_TAU2,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_TAU2,
			RMAD_cluster_optimalNodeModel.TAU2_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.TAU2_MAX_VALUE);

	/***************************************************/
	/** Constants for the DialogComponentNumber (keep) */
	/***************************************************/

	static final String CFGKEY_COUNT_KEEP = "keep_size";

	/** Initial default count value. */
	static final int DEFAULT_COUNT_KEEP = 4;

	/** Minimum value. */
	static final int KEEP_MIN_VALUE = 1;

	/** Maximum value. */
	static final int KEEP_MAX_VALUE = Integer.MAX_VALUE;

	private final SettingsModelIntegerBounded m_keep = new SettingsModelIntegerBounded(
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_KEEP,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_KEEP,
			RMAD_cluster_optimalNodeModel.KEEP_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.KEEP_MAX_VALUE);

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
			RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_MCS,
			RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_MCS,
			RMAD_cluster_optimalNodeModel.MCS_MIN_VALUE, 
			RMAD_cluster_optimalNodeModel.MCS_MAX_VALUE);





	/************************************/
	/** UTILITY PARAMETERS FOR THE PLOT */
	/************************************/

	private String m_filename;

	/**
	 * Default image size.
	 */
	public static final int IMG_DEF_SIZE = 640;

	/**
	 * Minimum image width.
	 */
	public static final int IMG_MIN_WIDTH = 50;

	/**
	 * Maximum image width.
	 */
	public static final int IMG_MAX_WIDTH = Integer.MAX_VALUE;

	/**
	 * Minimum image height.
	 */
	public static final int IMG_MIN_HEIGHT = 50;

	/**
	 * Maximum image height.
	 */
	public static final int IMG_MAX_HEIGHT = Integer.MAX_VALUE;

	private static final String INTERNAL_FILE_NAME = "Rplot";

	private final SettingsModelIntegerBounded m_heightModel =
			RMADViewsPngDialogPanel.createHeightModel();

	private final SettingsModelIntegerBounded m_widthModel =
			RMADViewsPngDialogPanel.createWidthModel();

	private final SettingsModelString m_resolutionModel =
			RMADViewsPngDialogPanel.createResolutionModel();

	private final SettingsModelIntegerBounded m_pointSizeModel =
			RMADViewsPngDialogPanel.createPointSizeModel();

	private final SettingsModelString m_bgModel =
			RMADViewsPngDialogPanel.createBgModel();

	private Image m_resultImage;






	public RMAD_cluster_optimalNodeModel(RMADPreferenceProvider pref) {
		super(pref);
		m_resultImage = null;
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

		// Sets the value of p.
		DEFAULT_COUNT_P = m_filter.getIncludeList().size();

		firstConfigure = false;

		return new DataTableSpec[1];
	}


	@Override
	protected String getCommand() {

		/**
		FunctionValues fv = new FunctionValues("bayesclust", "cluster.optimal", m_filter.getIncludeList(), m_keep.getIntValue());
		fv.put("nsim", m_nsim.getIntValue());
		fv.put("aR", m_aR.getDoubleValue());
		fv.put("p", DEFAULT_COUNT_P);
		fv.put("k", m_cluster.getIntValue());
		fv.put("a", m_a.getDoubleValue());
		fv.put("b", m_b.getDoubleValue());
		fv.put("tau2", m_tau2.getDoubleValue());
		fv.put("mcs", m_mcs.getDoubleValue());
		fv.putNull("file");
		fv.put("label", "data");
		fv.put("keep", m_keep.getIntValue());
		//return fv.toString();
		 * */

		StringBuilder cbind = new StringBuilder("cbind(");
		for(String columnName : m_filter.getIncludeList()){
			if (cbind.length() > 6) 
				cbind.append(", ");
			cbind.append("R$\"" + RMADAbstractNodeModel.formatColumn(columnName) + "\"");
		}
		cbind.append(")");

		StringBuilder params = new StringBuilder();
		params.append("nsim = " + m_nsim.getIntValue() + ", ");
		params.append("aR = " + m_aR.getDoubleValue() + ", ");
		params.append("p = " + DEFAULT_COUNT_P + ", ");
		params.append("k = " + m_cluster.getIntValue() + ", ");
		params.append("a = " + m_a.getDoubleValue() + ", ");
		params.append("b = " + m_b.getDoubleValue() + ", ");
		params.append("tau2 = " + m_tau2.getDoubleValue() + ", ");
		params.append("mcs = " + m_mcs.getDoubleValue() + ", ");
		params.append("file = \"\"" + ", ");
		params.append("label = \"data\"" + ", ");
		params.append("keep = " + m_keep.getIntValue());


		StringBuffer command = new StringBuffer("R <- R \n " +
				"library(\"bayesclust\")\n");
		command.append("cluster.optimal <- cluster.optimal(" + cbind + ", " + params + ")\n");
		command.append("R <- cbind(R, matrix(cluster.optimal$data1$cluster, ncol = " + m_keep.getIntValue() + "))\n");
		command.append("plot(cluster.optimal)");


		return "png(\"" + m_filename + "\""
		+ ", width=" + m_widthModel.getIntValue()
		+ ", height=" + m_heightModel.getIntValue()
		+ ", pointsize=" + m_pointSizeModel.getIntValue()
		+ ", bg=\"" + m_bgModel.getStringValue() + "\""
		+ ", res=" + m_resolutionModel.getStringValue() + ");\n"
		+ command
		+ "\ndev.off();";
	}

	@Override
	protected void postConfigure(PortObject[] inData, ExecutionContext exec)
			throws InvalidSettingsException {
		// Nothing to do here

	}


	@Override
	protected PortObject[] preprocessDataTable(PortObject[] inData,
			ExecutionContext exec) throws Exception {
		m_filename =
				FileUtil.createTempDir("R_").getAbsolutePath().replace('\\', '/')
				+ "/" + "R-View-" + System.identityHashCode(inData) + ".png";

		return inData;
	}


	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.loadSettingsFrom(settings);
		m_nsim.loadSettingsFrom(settings);
		m_aR.loadSettingsFrom(settings);
		m_cluster.loadSettingsFrom(settings);
		m_a.loadSettingsFrom(settings);
		m_b.loadSettingsFrom(settings);
		m_tau2.loadSettingsFrom(settings);
		m_keep.loadSettingsFrom(settings);
		m_mcs.loadSettingsFrom(settings);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_filter.saveSettingsTo(settings);
		m_nsim.saveSettingsTo(settings);
		m_aR.saveSettingsTo(settings);
		m_cluster.saveSettingsTo(settings);
		m_a.saveSettingsTo(settings);
		m_b.saveSettingsTo(settings);
		m_tau2.saveSettingsTo(settings);
		m_keep.saveSettingsTo(settings);
		m_mcs.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_filter.validateSettings(settings);
		m_nsim.validateSettings(settings);
		m_aR.validateSettings(settings);
		m_cluster.validateSettings(settings);
		m_a.validateSettings(settings);
		m_b.validateSettings(settings);
		m_tau2.validateSettings(settings);
		m_keep.validateSettings(settings);
		m_mcs.validateSettings(settings);
	}


	@Override
	protected PortObject[] execute(PortObject[] inData, ExecutionContext exec)
			throws CanceledExecutionException, Exception {
		PortObject[] p = new PortObject[0];
		try{
			p = super.execute(inData, exec);
			// create image after execution.
			FileInputStream fis = new FileInputStream(new File(m_filename));
			PNGImageContent content = new PNGImageContent(fis);
			fis.close();
			m_resultImage = content.getImage();
		}catch(InvalidSettingsException e){
			throw new InvalidSettingsException(e.getMessage() + "In Cluster Optimal, this may be due to the input table having too few rows."); // Input table may be too small for the function to work properly.");
		}
		return p;
	}

	/**
	 * @return result image for the view, only available after successful
	 *         execution of the node model.
	 */
	Image getResultImage() {
		return m_resultImage;
	}

	/**
	 * The saved image is loaded.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File nodeInternDir,
			final ExecutionMonitor exec)
					throws IOException, CanceledExecutionException {
		super.loadInternals(nodeInternDir, exec);

		File file = new File(nodeInternDir, INTERNAL_FILE_NAME + ".png");
		if (file.exists() && file.canRead()) {
			File pngFile = File.createTempFile(INTERNAL_FILE_NAME, ".png");
			FileUtil.copy(file, pngFile);
			InputStream is = new FileInputStream(pngFile);
			m_resultImage = new PNGImageContent(is).getImage();
			is.close();
		}
	}

	/**
	 * The created image is saved.
	 *
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File nodeInternDir,
			final ExecutionMonitor exec)
					throws IOException, CanceledExecutionException {
		super.saveInternals(nodeInternDir, exec);

		File imgFile = new File(m_filename);
		if (imgFile.exists() && imgFile.canWrite()) {
			File file = new File(nodeInternDir, INTERNAL_FILE_NAME + ".png");
			FileUtil.copy(imgFile, file);
		}
	}


}

