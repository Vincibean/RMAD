package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "RMAD_clara" Node.
 * Computes a "clara" object, a list representing a clustering of the data into k clusters. 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi
 */
public class RMAD_claraNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMAD_clara node.
     */
    protected RMAD_claraNodeDialog() {
    	super();

    	setDefaultTabTitle("Standard Options");
        createNewGroup("Filter the desired columns:");
        addDialogComponent(new DialogComponentColumnFilter(
        		new SettingsModelFilterString(
        				RMAD_claraNodeModel.m_defaultTabTitle, 
        				RMAD_claraNodeModel.m_inclList, 
        				RMAD_claraNodeModel.m_exclList),
        				RMAD_claraNodeModel.IN_PORT, true));
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    RMAD_claraNodeModel.CFGKEY_COUNT_CLUSTERS,
                    RMAD_claraNodeModel.DEFAULT_COUNT_CLUSTERS,
                    RMAD_claraNodeModel.K_MIN_VALUE, 
                    RMAD_claraNodeModel.K_MAX_VALUE),
                    "Clusters:", /*step*/ 1));
        
        createNewTab("Advanced options");
        
        addDialogComponent(new DialogComponentButtonGroup(
        		new SettingsModelString(RMAD_claraNodeModel.CFGKEY_STRING, RMAD_claraNodeModel.EUCLIDEAN_VALUE),
        		false, "Metric: ", RMAD_claraNodeModel.METRIC_ELEMENTS));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_claraNodeModel.CFGKEY_BOOLEAN_STAND, 
        				false), "Stand"));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelInteger(
        				RMAD_claraNodeModel.CFGKEY_COUNT_SAMPLES, 
        				RMAD_claraNodeModel.DEFAULT_COUNT_SAMPLES),
        				"Samples: ", /*stepSize*/ 1));
        
        addDialogComponent(new DialogComponentNumber(
                new SettingsModelIntegerBounded(
                    RMAD_claraNodeModel.CFGKEY_COUNT_SAMPSIZE,
                    RMAD_claraNodeModel.DEFAULT_COUNT_SAMPSIZE,
                    RMAD_claraNodeModel.SAMPSIZE_MIN_VALUE, 
                    RMAD_claraNodeModel.SAMPSIZE_MAX_VALUE),
                    "Sampsize:", /*step*/ 1));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelInteger(
        				RMAD_claraNodeModel.CFGKEY_COUNT_TRACE, 
        				RMAD_claraNodeModel.DEFAULT_COUNT_TRACE),
        				"Trace: ", /*stepSize*/ 1));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_claraNodeModel.CFGKEY_BOOLEAN_MEDOIDS, 
        				true), "Medoids.x"));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_claraNodeModel.CFGKEY_BOOLEAN_KEEP_DATA, 
        				false), "Keep data"));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_claraNodeModel.CFGKEY_BOOLEAN_RNGR, 
        				false), "rngR"));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_claraNodeModel.CFGKEY_BOOLEAN_PAMLIKE, 
        				false), "pamLike"));
        
        
                    
    }
}

