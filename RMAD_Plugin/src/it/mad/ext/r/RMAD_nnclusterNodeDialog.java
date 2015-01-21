package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "RMAD_nncluster" Node.
 * Uses Prim’s algorithm to build a minimum spanning tree for each cluster, stopping when the nearest- * nneighbour distance rises above a specified threshold. Returns a set of clusters and a set of ’outliers’ * nnot in any cluster.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_nnclusterNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the RMAD_nncluster node.
	 */
	protected RMAD_nnclusterNodeDialog() {
		super();

		setDefaultTabTitle("Standard Options");

		createNewGroup("Filter the desired columns:");
		addDialogComponent(new DialogComponentColumnFilter(
				new SettingsModelFilterString(
						RMAD_nnclusterNodeModel.m_defaultTabTitle, 
						RMAD_nnclusterNodeModel.m_inclList, 
						RMAD_nnclusterNodeModel.m_exclList),
						RMAD_nnclusterNodeModel.IN_PORT, true)); 

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelDoubleBounded(
						RMAD_nnclusterNodeModel.CFGKEY_COUNT_THRESHOLD,
						RMAD_nnclusterNodeModel.DEFAULT_COUNT_THRESHOLD,
						RMAD_nnclusterNodeModel.THRESHOLD_MIN_VALUE, 
						RMAD_nnclusterNodeModel.THRESHOLD_MAX_VALUE), 
						"Threshold:", /* Stepsize */ 0.0001, 10));


		createNewTab("Advanced options");

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelDoubleBounded(
						RMAD_nnclusterNodeModel.CFGKEY_COUNT_FILL,
						RMAD_nnclusterNodeModel.DEFAULT_COUNT_FILL,
						RMAD_nnclusterNodeModel.FILL_MIN_VALUE, 
						RMAD_nnclusterNodeModel.FILL_MAX_VALUE),
						"Fill:", /* stepSize */ 0.0001, 10));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_nnclusterNodeModel.CFGKEY_COUNT_MAXCLUST,
						RMAD_nnclusterNodeModel.DEFAULT_COUNT_MAXCLUST,
						RMAD_nnclusterNodeModel.MAXCLUST_MIN_VALUE, 
						RMAD_nnclusterNodeModel.MAXCLUST_MAX_VALUE),
						"Maximum number of clusters:", /*step*/ 1));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(RMAD_nnclusterNodeModel.CFGKEY_COUNT_GIVE_UP,
						RMAD_nnclusterNodeModel.DEFAULT_COUNT_GIVE_UP,
						RMAD_nnclusterNodeModel.GIVE_UP_MIN_VALUE, 
						RMAD_nnclusterNodeModel.GIVE_UP_MAX_VALUE),
						"Give up:", /* stepSize */ 1));

		DialogComponentNumber startComponent = new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_nnclusterNodeModel.CFGKEY_STRING_START, 
						RMAD_nnclusterNodeModel.DEFAULT_COUNT_START, 
						RMAD_nnclusterNodeModel.START_MIN_VALUE, 
						RMAD_nnclusterNodeModel.START_MAX_VALUE), 
						"Start:", /* Stepsize */ 1);
		startComponent.setToolTipText("0 will be read as NULL.");
		addDialogComponent(startComponent);

		addDialogComponent(new DialogComponentBoolean(
				new SettingsModelBoolean(
						RMAD_nnclusterNodeModel.CFGKEY_BOOLEAN_OUTLIER, 
						true), 
				"Outlier"));

	}
}

