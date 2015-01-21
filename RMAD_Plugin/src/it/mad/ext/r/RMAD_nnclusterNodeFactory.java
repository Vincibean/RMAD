package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;
import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;

/**
 * <code>NodeFactory</code> for the "RMAD_nncluster" Node.
 * Uses Prim’s algorithm to build a minimum spanning tree for each cluster, stopping when the nearest- * nneighbour distance rises above a specified threshold. Returns a set of clusters and a set of ’outliers’ * nnot in any cluster.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_nnclusterNodeFactory extends RMADCoreNodeFactory<RMAD_nnclusterNodeModel> {

	public RMAD_nnclusterNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RMAD_nnclusterNodeModel createNodeModel() {
		return new RMAD_nnclusterNodeModel(getRProvider());
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
	public ExtToolOutputNodeView<RMAD_nnclusterNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_nnclusterNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_nnclusterNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_nnclusterNodeModel>(nodeModel);
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
		return new RMAD_nnclusterNodeDialog();
	}

}

