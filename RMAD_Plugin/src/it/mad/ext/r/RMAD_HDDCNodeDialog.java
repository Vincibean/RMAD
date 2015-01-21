package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentColumnFilter;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentString;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelFilterString;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;

/**
 * <code>NodeDialog</code> for the "RMAD_HDDC" Node.
 * HDDC(High Dimensional Data Clustering) is a model-based clustering method. It is based on the Gaussian Mixture Model and on * nthe idea that the data lives in subspaces with a lower dimension than the dimension of the original * nspace. It uses the Expectation - Maximisation algorithm to estimate the parameters of the model.
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_HDDCNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMAD_HDDC node.
     */
    protected RMAD_HDDCNodeDialog() {
    	super();

    	setDefaultTabTitle("Standard Options");
        createNewGroup("Filter the desired columns:");
        addDialogComponent(new DialogComponentColumnFilter(
        		new SettingsModelFilterString(
        				RMAD_HDDCNodeModel.m_defaultTabTitle, 
        				RMAD_HDDCNodeModel.m_inclList, 
        				RMAD_HDDCNodeModel.m_exclList),
        				RMAD_HDDCNodeModel.IN_PORT, true));
        
        DialogComponentString clusters = new DialogComponentString(
        		new SettingsModelString(
        				RMAD_HDDCNodeModel.CFGKEY_STRING_K, 
        				RMAD_HDDCNodeModel.DEFAULT_STRING_K), 
        				"K (clusters):");
        clusters.setToolTipText("Choose a vector of up to 20 integer clusters" +
        		" separating them with a semicolon (\";\")");
        addDialogComponent(clusters);
        
        
        createNewTab("Advanced options");
        addDialogComponent(new DialogComponentStringListSelection(
        		new SettingsModelStringArray(
        				RMAD_HDDCNodeModel.CFGKEY_STRING_MODEL, 
        				RMAD_HDDCNodeModel.MODEL_DEFAULT_VALUE), 
        				"Model:", RMAD_HDDCNodeModel.m_models, true, 4));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_THRESHOLD, 
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_THRESHOLD, 
        				RMAD_HDDCNodeModel.THRESHOLD_MIN_VALUE, 
        				RMAD_HDDCNodeModel.THRESHOLD_MAX_VALUE), 
        				"Threshold:", /*stepSize */ 0.001));
        
        DialogComponentNumber d_com_dim = new DialogComponentNumber(new SettingsModelDouble(
    			RMAD_HDDCNodeModel.CFGKEY_STRING_COM_DIM2, Double.NaN),
    			"com_dim:", 1);
        d_com_dim.setToolTipText("Default value is set to \"NULL\"");
        addDialogComponent(d_com_dim);
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_ITERMAX,
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_ITERMAX,
        				RMAD_HDDCNodeModel.ITERMAX_MIN_VALUE, 
        				RMAD_HDDCNodeModel.ITERMAX_MAX_VALUE),
        				"Maximum number of iterations:", /*stepSize */ 1));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_EPS, 
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_EPS, 
        				RMAD_HDDCNodeModel.EPS_MIN_VALUE, 
        				RMAD_HDDCNodeModel.EPS_MAX_VALUE), 
        				"eps:", /*stepSize */ 0.0001));
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_HDDCNodeModel.CFGKEY_BOOLEAN_GRAPH, 
        				false), "Graph"));
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_HDDCNodeModel.CFGKEY_STRING_ALGO, 
        				RMAD_HDDCNodeModel.m_algo.get(0)), 
        				"Algorithm:", RMAD_HDDCNodeModel.m_algo));
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_HDDCNodeModel.CFGKEY_STRING_D, 
        				RMAD_HDDCNodeModel.m_d.get(0)), 
        				"D:", RMAD_HDDCNodeModel.m_d));
        
        addDialogComponent(new DialogComponentStringSelection(
        		new SettingsModelString(
        				RMAD_HDDCNodeModel.CFGKEY_STRING_INIT, 
        				RMAD_HDDCNodeModel.INIT_DEFAULT_VALUE), 
        		"Initialization:", RMAD_HDDCNodeModel.m_init));

        setHorizontalPlacement(true);
        createNewGroup("mini.nb");
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_MINI_NB_1, 
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_MINI_NB_1, 
        				RMAD_HDDCNodeModel.MINI_NB_1_MIN_VALUE, 
        				RMAD_HDDCNodeModel.MINI_NB_1_MAX_VALUE), 
        				"1st value:", /*stepSize */ 1));
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_MINI_NB_2, 
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_MINI_NB_2, 
        				RMAD_HDDCNodeModel.MINI_NB_2_MIN_VALUE, 
        				RMAD_HDDCNodeModel.MINI_NB_2_MAX_VALUE), 
        				"2nd value:", /*stepSize */ 1));
        closeCurrentGroup();
        setHorizontalPlacement(false);
        
        addDialogComponent(new DialogComponentBoolean(
        		new SettingsModelBoolean(
        				RMAD_HDDCNodeModel.CFGKEY_BOOLEAN_SCALING, 
        				false), "Scaling"));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelIntegerBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_MIN_INDIVIDUALS, 
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_MIN_INDIVIDUALS, 
        				RMAD_HDDCNodeModel.MIN_INDIVIDUALS_MIN_VALUE, 
        				RMAD_HDDCNodeModel.MIN_INDIVIDUALS_MAX_VALUE), 
        				"Min. Individuals: ", /*stepSize */ 1));
        
        addDialogComponent(new DialogComponentNumber(
        		new SettingsModelDoubleBounded(
        				RMAD_HDDCNodeModel.CFGKEY_COUNT_NOISE_CTRL,
        				RMAD_HDDCNodeModel.DEFAULT_COUNT_NOISE_CTRL,
        				RMAD_HDDCNodeModel.NOISE_CTRL_MIN_VALUE, 
        				RMAD_HDDCNodeModel.NOISE_CTRL_MAX_VALUE), 
        				"Noise control:", /*stepSize */ 0.00000001, 8));
        
        
        
        

    }
}

