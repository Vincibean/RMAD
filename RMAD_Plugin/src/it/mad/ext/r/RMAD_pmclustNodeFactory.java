package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;
import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;

/**
 * <code>NodeFactory</code> for the "Pmclust" Node.
 * 
 *
 * @author Andre Bessi, Univerity of Milan Bicocca
 */
public class RMAD_pmclustNodeFactory 
        extends RMADCoreNodeFactory<RMAD_pmclustNodeModel> {

    public RMAD_pmclustNodeFactory() {
    	super(PreferenceInitializer.getRProvider());
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public RMAD_pmclustNodeModel createNodeModel() {
        return new RMAD_pmclustNodeModel(getRProvider());
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
	public ExtToolOutputNodeView<RMAD_pmclustNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_pmclustNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_pmclustNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_pmclustNodeModel>(nodeModel);
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
        return new RMAD_pmclustNodeDialog();
    }

}

