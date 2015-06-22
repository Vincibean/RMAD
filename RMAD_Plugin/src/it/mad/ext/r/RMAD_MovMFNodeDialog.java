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
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "RMAD_MovMF" Node.
 * Fit mixtures of von Mises-Fisher Distributions
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_MovMFNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMAD_MovMF node.
     */
    protected RMAD_MovMFNodeDialog() {
    	super();

    	setDefaultTabTitle("Standard Options");
        createNewGroup("Filter the desired columns:");
        addDialogComponent(new DialogComponentColumnFilter(
        		new SettingsModelFilterString(
        				RMAD_MovMFNodeModel.m_defaultTabTitle, 
        				RMAD_MovMFNodeModel.m_inclList, 
        				RMAD_MovMFNodeModel.m_exclList),
        				RMAD_MovMFNodeModel.IN_PORT, true));
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    RMAD_MovMFNodeModel.CFGKEY_COUNT_CLUSTERS,
                    RMAD_MovMFNodeModel.DEFAULT_COUNT_CLUSTERS,
                    RMAD_MovMFNodeModel.K_MIN_VALUE, 
                    RMAD_MovMFNodeModel.K_MAX_VALUE),
                    "Clusters:", /*step*/ 1));
        
        createNewTab("Advanced options");
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_MovMFNodeModel.CFGKEY_COUNT_MAXITER,
        				RMAD_MovMFNodeModel.DEFAULT_COUNT_MAXITER,
        				RMAD_MovMFNodeModel.MAXITER_MIN_VALUE, 
        				RMAD_MovMFNodeModel.MAXITER_MAX_VALUE),
                    "Max. iter:", /*step*/ 1));
        
        DialogComponentNumber d_reltol = new DialogComponentNumber(
        		new SettingsModelDouble(
        				RMAD_MovMFNodeModel.CFGKEY_COUNT_RELTOL, 
        				RMAD_MovMFNodeModel.DEFAULT_COUNT_RELTOL), 
        				"reltol:", /* stepSize*/ 0.00000001);
        d_reltol.setToolTipText("Default value will be set depending on the numerical characteristics of the machine R is running on.");
        addDialogComponent(d_reltol);
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_MovMFNodeModel.CFGKEY_STRING_START, 
        				RMAD_MovMFNodeModel.m_start.get(1)), 
        				"Start:", RMAD_MovMFNodeModel.m_start));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_MovMFNodeModel.CFGKEY_COUNT_NRUNS,
        				RMAD_MovMFNodeModel.DEFAULT_COUNT_NRUNS,
        				RMAD_MovMFNodeModel.NRUNS_MIN_VALUE, 
        				RMAD_MovMFNodeModel.NRUNS_MAX_VALUE), 
        				"nruns:", /* step */ 1));
        
        addDialogComponent(new DialogComponentNumber(new SettingsModelDouble(
    			RMAD_MovMFNodeModel.CFGKEY_COUNT_MINALPHA, 
    			RMAD_MovMFNodeModel.DEFAULT_COUNT_MINALPHA), 
        		"Min. alpha:", /* stepSize*/ 0.000001, 10));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_MovMFNodeModel.CFGKEY_BOOLEAN_CONVERGE, 
        				true), "Converge"));
        
        

    }
}

