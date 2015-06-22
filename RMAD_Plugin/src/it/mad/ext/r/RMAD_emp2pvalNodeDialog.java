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

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "RMAD_emp2pval" Node.
 * Converts the Empirical Posterior Probability (EPP) computed by cluster.test into a frequentist * np-value, which can then be used to assess the significance of the alternative hypothesis.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi
 */
public class RMAD_emp2pvalNodeDialog extends DefaultNodeSettingsPane {

	/**
	 * New pane for configuring RMAD_emp2pval node dialog.
	 */
	protected RMAD_emp2pvalNodeDialog() {

		setDefaultTabTitle("Standard Options");

		createNewGroup("Filter the desired columns:");
		addDialogComponent(new DialogComponentColumnFilter(
				new SettingsModelFilterString(
						RMAD_emp2pvalNodeModel.m_defaultTabTitle, 
						RMAD_emp2pvalNodeModel.m_inclList, 
						RMAD_emp2pvalNodeModel.m_exclList),
						RMAD_emp2pvalNodeModel.IN_PORT, true));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_emp2pvalNodeModel.CFGKEY_COUNT_K,
						RMAD_emp2pvalNodeModel.DEFAULT_COUNT_K,
						RMAD_emp2pvalNodeModel.K_MIN_VALUE, 
						RMAD_emp2pvalNodeModel.K_MAX_VALUE),
						"k:", /*step*/ 1));

		createNewTab("Advanced options");

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
						RMAD_emp2pvalNodeModel.CFGKEY_COUNT_CLUSTERTEST_NSIM, 
						RMAD_emp2pvalNodeModel.DEFAULT_COUNT_CLUSTERTEST_NSIM),
						"nsim (cluster.test): ", /*stepSize*/ 1));
		
		addDialogComponent(new DialogComponentNumber(
				new SettingsModelInteger(
						RMAD_emp2pvalNodeModel.CFGKEY_COUNT_NULLDENSITY_NSIM, 
						RMAD_emp2pvalNodeModel.DEFAULT_COUNT_NULLDENSITY_NSIM),
						"nsim (nulldensity): ", /*stepSize*/ 1));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_AR,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_AR,
				RMAD_emp2pvalNodeModel.AR_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.AR_MAX_VALUE),
				"aR: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_A,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_A,
				RMAD_emp2pvalNodeModel.A_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.A_MAX_VALUE),
				"a: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_B,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_B,
				RMAD_emp2pvalNodeModel.B_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.B_MAX_VALUE),
				"b: ", /*stepSize */ 0.000001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_TAU2,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_TAU2,
				RMAD_emp2pvalNodeModel.TAU2_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.TAU2_MAX_VALUE),
				"tau2: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_MCS,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_MCS,
				RMAD_emp2pvalNodeModel.MCS_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.MCS_MAX_VALUE),
				"mcs: ", /*stepSize */ 0.001));

		addDialogComponent(new DialogComponentNumber(
				new SettingsModelIntegerBounded(
						RMAD_emp2pvalNodeModel.CFGKEY_COUNT_REPLICATIONS,
						RMAD_emp2pvalNodeModel.DEFAULT_COUNT_REPLICATIONS,
						RMAD_emp2pvalNodeModel.REPLICATIONS_MIN_VALUE, 
						RMAD_emp2pvalNodeModel.REPLICATIONS_MAX_VALUE),
						"Replications (cluster.test): ", /*step*/ 1));
		
		addDialogComponent(new DialogComponentNumber(new SettingsModelDoubleBounded(
				RMAD_emp2pvalNodeModel.CFGKEY_COUNT_PROP,
				RMAD_emp2pvalNodeModel.DEFAULT_COUNT_PROP,
				RMAD_emp2pvalNodeModel.PROP_MIN_VALUE, 
				RMAD_emp2pvalNodeModel.PROP_MAX_VALUE),
				"prop: ", /*stepSize */ 0.001));

	}
}

