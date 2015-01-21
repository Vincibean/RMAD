package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;

import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RMAD_emp2pval" Node.
 * Converts the Empirical Posterior Probability (EPP) computed by cluster.test into a frequentist * np-value, which can then be used to assess the significance of the alternative hypothesis.
 *
 * @author Andre Bessi
 */
public class RMAD_emp2pvalNodeFactory 
extends RMADCoreNodeFactory<RMAD_emp2pvalNodeModel> {

	public RMAD_emp2pvalNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	 @Override
	 public RMAD_emp2pvalNodeModel createNodeModel() {
		 return new RMAD_emp2pvalNodeModel(getRProvider());
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
	 public NodeView<RMAD_emp2pvalNodeModel> createNodeView(final int viewIndex,
			 final RMAD_emp2pvalNodeModel nodeModel) {
		 if (viewIndex == 0) {
				return
						new ExtToolStdoutNodeView<RMAD_emp2pvalNodeModel>(nodeModel);
			} else if (viewIndex == 1) {
				return
						new ExtToolStderrNodeView<RMAD_emp2pvalNodeModel>(nodeModel);
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
		 return new RMAD_emp2pvalNodeDialog();
	 }

}

