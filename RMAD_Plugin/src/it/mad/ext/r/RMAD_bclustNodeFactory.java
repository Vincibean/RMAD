package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;
import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;

/**
 * <code>NodeFactory</code> for the "RMAD_bclust" Node.
 * The function clusters data saved in a matrix using an additive linear model with disappearing random effects. The model has built-in spike-and-slab components which quantifies important variables for clustering and can be extracted using the imp function.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_bclustNodeFactory extends RMADCoreNodeFactory<RMAD_bclustNodeModel> {

	public RMAD_bclustNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RMAD_bclustNodeModel createNodeModel() {
		return new RMAD_bclustNodeModel(getRProvider());
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
	public ExtToolOutputNodeView<RMAD_bclustNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_bclustNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_bclustNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_bclustNodeModel>(nodeModel);
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
		return new RMAD_bclustNodeDialog();
	}

}

