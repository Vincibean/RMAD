package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "RMAD_cluster_optimal" Node.
 * cluster.optimal will search for the optimal k-clustering of the dataset.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi
 */
public class RMAD_cluster_optimalNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the RMAD_cluster_optimal node.
	 */
	protected RMAD_cluster_optimalNodeDialog() {

		setDefaultTabTitle("Standard Options");
		
		createNewGroup("Filter the desired columns:");
		addDialogComponent(new DialogComponentColumnFilter(
				new SettingsModelFilterString(
						RMAD_cluster_optimalNodeModel.m_defaultTabTitle, 
						RMAD_cluster_optimalNodeModel.m_inclList, 
						RMAD_cluster_optimalNodeModel.m_exclList),
						RMAD_cluster_optimalNodeModel.IN_PORT, true));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_K,
						RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_K,
						RMAD_cluster_optimalNodeModel.K_MIN_VALUE, 
						RMAD_cluster_optimalNodeModel.K_MAX_VALUE),
						"Clusters:", /*step*/ 1));

		createNewTab("Advanced options");

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
						RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_NSIM, 
						RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_NSIM),
						"nsim: ", /*stepSize*/ 1));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_AR,
				RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_AR,
				RMAD_cluster_optimalNodeModel.AR_MIN_VALUE, 
				RMAD_cluster_optimalNodeModel.AR_MAX_VALUE),
				"aR: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_A,
				RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_A,
				RMAD_cluster_optimalNodeModel.A_MIN_VALUE, 
				RMAD_cluster_optimalNodeModel.A_MAX_VALUE),
				"a: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_B,
				RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_B,
				RMAD_cluster_optimalNodeModel.B_MIN_VALUE, 
				RMAD_cluster_optimalNodeModel.B_MAX_VALUE),
				"b: ", /*stepSize */ 0.000001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_TAU2,
				RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_TAU2,
				RMAD_cluster_optimalNodeModel.TAU2_MIN_VALUE, 
				RMAD_cluster_optimalNodeModel.TAU2_MAX_VALUE),
				"tau2: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_MCS,
				RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_MCS,
				RMAD_cluster_optimalNodeModel.MCS_MIN_VALUE, 
				RMAD_cluster_optimalNodeModel.MCS_MAX_VALUE),
				"mcs: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_cluster_optimalNodeModel.CFGKEY_COUNT_KEEP,
						RMAD_cluster_optimalNodeModel.DEFAULT_COUNT_KEEP,
						RMAD_cluster_optimalNodeModel.KEEP_MIN_VALUE, 
						RMAD_cluster_optimalNodeModel.KEEP_MAX_VALUE),
						"Keep: ", /*step*/ 1));
		

	}
}

