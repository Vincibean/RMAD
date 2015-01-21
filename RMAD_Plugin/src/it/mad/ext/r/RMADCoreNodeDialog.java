package it.mad.ext.r;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

/**
 * <code>NodeDialog</code> for the "RMADCore" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Andre Bessi
 */
public class RMADCoreNodeDialog extends DefaultNodeSettingsPane {

    /**
     * New pane for configuring the RMADCore node.
     */
    protected RMADCoreNodeDialog() {

    }
}

