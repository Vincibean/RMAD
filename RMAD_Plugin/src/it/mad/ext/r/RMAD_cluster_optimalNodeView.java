package it.mad.ext.r;

import java.awt.Image;

import javax.swing.JScrollPane;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_cluster_optimal" Node.
 * cluster.optimal will search for the optimal k-clustering of the dataset.
 *
 * @author Andre Bessi
 */
public class RMAD_cluster_optimalNodeView extends NodeView<RMAD_cluster_optimalNodeModel> {

	private final RMADPlotterViewPanel m_panel;


	/**
	 * Creates a new instance of <code>RMAD_cluster_optimalNodeView</code> which displays
	 * a certain image.
	 * 
	 * @param nodeModel The model (class: {@link RMAD_cluster_optimalNodeModel})
	 */
	protected RMAD_cluster_optimalNodeView(final RMAD_cluster_optimalNodeModel nodeModel) {
		super(nodeModel);
		m_panel = new RMADPlotterViewPanel();
		super.setComponent(new JScrollPane(m_panel));
	}

	/**
	 * Updates the image to display.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
		RMAD_cluster_optimalNodeModel model = super.getNodeModel();
        Image image = model.getResultImage();
        m_panel.update(image);
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

