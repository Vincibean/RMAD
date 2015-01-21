package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "Pmclust" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, Univerity of Milan Bicocca
 */
public class RMAD_pmclustNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring the Pmclust node.
	 */
	protected RMAD_pmclustNodeDialog() {
		super();

		setDefaultTabTitle("Standard Options");
		createNewGroup("Filter the desired columns:");
		addDialogComponent(new DialogComponentColumnFilter(
				new SettingsModelFilterString(
						RMAD_pmclustNodeModel.m_defaultTabTitle, 
						RMAD_pmclustNodeModel.m_inclList, 
						RMAD_pmclustNodeModel.m_exclList),
						RMAD_pmclustNodeModel.IN_PORT, true));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_pmclustNodeModel.CFGKEY_COUNT_CLUSTERS,
						RMAD_pmclustNodeModel.DEFAULT_COUNT_CLUSTERS,
						RMAD_pmclustNodeModel.K_MIN_VALUE, 
						RMAD_pmclustNodeModel.K_MAX_VALUE),
						"Clusters:", /*step*/ 1));

		createNewTab("Advanced options");
		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(
						RMAD_pmclustNodeModel.CFGKEY_STRING_ALGORITHM, 
						RMAD_pmclustNodeModel.m_algorithm.get(0)), 
				"Algorithm:", RMAD_pmclustNodeModel.m_algorithm));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelDouble(
						RMAD_pmclustNodeModel.CFGKEY_COUNT_RNDEM_ITER, 
						RMAD_pmclustNodeModel.DEFAULT_COUNT_RNDEM_ITER), 
						"RndEM.iter:", /* stepSize*/ 0.00001, /* Component width */10));

		addDialogComponent(new DialogComponentStringSelection(
				new SettingsModelString(
						RMAD_pmclustNodeModel.CFGKEY_STRING_METHOD_OWN_X, 
						RMAD_pmclustNodeModel.m_method_own_x.get(0)), 
				"Method own x:", RMAD_pmclustNodeModel.m_method_own_x));

		addDialogComponent(new DialogComponentNumber(new SettingsModelIntegerBounded(
				RMAD_pmclustNodeModel.CFGKEY_COUNT_RANK_OWN_X,
				RMAD_pmclustNodeModel.DEFAULT_COUNT_RANK_OWN_X, 
				RMAD_pmclustNodeModel.RANK_OWN_X_MIN_VALUE, 
				RMAD_pmclustNodeModel.RANK_OWN_X_MAX_VALUE), 
				"Rank own x:", /* stepSize*/ 1));

	}
}

