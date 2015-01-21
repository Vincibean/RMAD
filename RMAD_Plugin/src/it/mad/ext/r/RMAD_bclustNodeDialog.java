package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "RMAD_bclust" Node.
 * The function clusters data saved in a matrix using an additive linear model with disappearing random effects. The model has built-in spike-and-slab components which quantifies important variables for clustering and can be extracted using the imp function.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_bclustNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMAD_bclust node.
     */
    protected RMAD_bclustNodeDialog() {
    	super();

    	// setDefaultTabTitle("Standard Options");
        createNewGroup("Filter the desired columns:");
        addDialogComponent(new DialogComponentColumnFilter(
        		new SettingsModelFilterString(
        				RMAD_bclustNodeModel.m_defaultTabTitle, 
        				RMAD_bclustNodeModel.m_inclList, 
        				RMAD_bclustNodeModel.m_exclList),
        				RMAD_bclustNodeModel.IN_PORT, true));
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_bclustNodeModel.CFGKEY_STRING_EFFECT_FAMILY, 
        				RMAD_bclustNodeModel.EFFECT_FAMILY_DEFAULT_VALUE), 
        		"Distribution Family:", RMAD_bclustNodeModel.m_effect_family));
        
        addDialogComponent(new DialogComponentBoolean(new SettingsModelBoolean(
    			RMAD_bclustNodeModel.CFGKEY_BOOLEAN_VAR_SELECT, 
    			true), 
    			"Variable selection"));
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_bclustNodeModel.CFGKEY_STRING_METHOD, 
        				RMAD_bclustNodeModel.METHOD_DEFAULT_VALUE), 
        		"Method:", RMAD_bclustNodeModel.m_method));
   
    }
}

