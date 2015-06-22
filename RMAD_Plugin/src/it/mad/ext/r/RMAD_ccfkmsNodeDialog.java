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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

/**
 * <code>NodeDialog</code> for the "RMAD_ccfkms" Node.
 * Partition a data set into convex sets using conjugate convex functions.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_ccfkmsNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMAD_ccfkms node.
     */
    protected RMAD_ccfkmsNodeDialog() {
    	super();

    	setDefaultTabTitle("Standard Options");
    	
        createNewGroup("Filter the desired columns:");
        addDialogComponent(new DialogComponentColumnFilter(
        		new SettingsModelFilterString(
        				RMAD_ccfkmsNodeModel.m_defaultTabTitle, 
        				RMAD_ccfkmsNodeModel.m_inclList, 
        				RMAD_ccfkmsNodeModel.m_exclList),
        				RMAD_ccfkmsNodeModel.IN_PORT, true));
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    RMAD_ccfkmsNodeModel.CFGKEY_COUNT_CLUSTERS,
                    RMAD_ccfkmsNodeModel.DEFAULT_COUNT_CLUSTERS,
                    RMAD_ccfkmsNodeModel.K_MIN_VALUE, 
                    RMAD_ccfkmsNodeModel.K_MAX_VALUE),
                    "Prototypes:", /*step*/ 1));
        
        
        createNewTab("Advanced options");
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				RMAD_ccfkmsNodeModel.CFGKEY_COUNT_PAR, 
        				RMAD_ccfkmsNodeModel.DEFAULT_COUNT_PAR, 
        				RMAD_ccfkmsNodeModel.PAR_MIN_VALUE, 
        				RMAD_ccfkmsNodeModel.PAR_MAX_VALUE), 
        		"Par:", /*step*/ 0.0001, 10));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_ccfkmsNodeModel.CFGKEY_COUNT_MAX_ITER,
        				RMAD_ccfkmsNodeModel.DEFAULT_COUNT_MAX_ITER,
        				RMAD_ccfkmsNodeModel.MAX_ITER_MIN_VALUE, 
        				RMAD_ccfkmsNodeModel.MAX_ITER_MAX_VALUE), 
        		"Maximum number of iterations:", /*step*/ 1));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_ccfkmsNodeModel.CFGKEY_BOOLEAN_OPT_STD, 
        				false), 
        		"Standardize"));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_ccfkmsNodeModel.CFGKEY_COUNT_OPT_RETRY,
        				RMAD_ccfkmsNodeModel.DEFAULT_COUNT_OPT_RETRY,
        				RMAD_ccfkmsNodeModel.OPT_RETRY_MIN_VALUE, 
        				RMAD_ccfkmsNodeModel.OPT_RETRY_MAX_VALUE), 
        		"Retries:", /*step*/ 1));
        

    }
}

