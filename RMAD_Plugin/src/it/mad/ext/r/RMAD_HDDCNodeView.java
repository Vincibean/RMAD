package it.mad.ext.r;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_HDDC" Node.
 * HDDC(High Dimensional Data Clustering) is a model-based clustering method. It is based on the Gaussian Mixture Model and on * nthe idea that the data lives in subspaces with a lower dimension than the dimension of the original * nspace. It uses the Expectation - Maximisation algorithm to estimate the parameters of the model.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_HDDCNodeView extends NodeView<RMAD_HDDCNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_HDDCNodeModel})
     */
    protected RMAD_HDDCNodeView(final RMAD_HDDCNodeModel nodeModel) {
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

