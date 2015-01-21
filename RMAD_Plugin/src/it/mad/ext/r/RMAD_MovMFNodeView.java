package it.mad.ext.r;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_MovMF" Node.
 * Fit mixtures of von Mises-Fisher Distributions
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_MovMFNodeView extends NodeView<RMAD_MovMFNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_MovMFNodeModel})
     */
    protected RMAD_MovMFNodeView(final RMAD_MovMFNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

