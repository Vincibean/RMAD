package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;
import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;

/**
 * <code>NodeFactory</code> for the "RMAD_MovMF" Node.
 * Fit mixtures of von Mises-Fisher Distributions
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_MovMFNodeFactory 
        extends RMADCoreNodeFactory<RMAD_MovMFNodeModel> {

    public RMAD_MovMFNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public RMAD_MovMFNodeModel createNodeModel() {
        return new RMAD_MovMFNodeModel(getRProvider());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 2;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	public ExtToolOutputNodeView<RMAD_MovMFNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_MovMFNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_MovMFNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_MovMFNodeModel>(nodeModel);
		}
		return null;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new RMAD_MovMFNodeDialog();
    }

}

