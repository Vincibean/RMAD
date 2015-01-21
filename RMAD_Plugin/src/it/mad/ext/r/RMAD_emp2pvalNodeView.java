package it.mad.ext.r;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_emp2pval" Node.
 * Converts the Empirical Posterior Probability (EPP) computed by cluster.test into a frequentist * np-value, which can then be used to assess the significance of the alternative hypothesis.
 *
 * @author Andre Bessi
 */
public class RMAD_emp2pvalNodeView extends NodeView<RMAD_emp2pvalNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_emp2pvalNodeModel})
     */
    protected RMAD_emp2pvalNodeView(final RMAD_emp2pvalNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        RMAD_emp2pvalNodeModel nodeModel = 
            (RMAD_emp2pvalNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

