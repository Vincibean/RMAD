package it.mad.ext.r;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_clara" Node.
 * Computes a "clara" object, a list representing a clustering of the data into k clusters. 
 *
 * @author Andre Bessi
 */
public class RMAD_claraNodeView extends NodeView<RMAD_claraNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_claraNodeModel})
     */
    protected RMAD_claraNodeView(final RMAD_claraNodeModel nodeModel) {
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

