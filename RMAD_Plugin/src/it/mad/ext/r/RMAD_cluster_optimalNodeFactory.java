/*
 * Copyright (C) 2015  Vincibean <Andre Bessi>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.mad.ext.r;

import it.mad.ext.r.preferences.PreferenceInitializer;

import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "RMAD_cluster_optimal" Node.
 * cluster.optimal will search for the optimal k-clustering of the dataset.
 *
 * @author Andre Bessi
 */
public class RMAD_cluster_optimalNodeFactory 
extends RMADCoreNodeFactory<RMAD_cluster_optimalNodeModel> {

	public RMAD_cluster_optimalNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	@Override
	public RMAD_cluster_optimalNodeModel createNodeModel() {
		return new RMAD_cluster_optimalNodeModel(getRProvider());
	}

	@Override
	protected int getNrNodeViews() {
		return 3;
	}

	@Override
	public NodeView<RMAD_cluster_optimalNodeModel> createNodeView(
			int viewIndex, RMAD_cluster_optimalNodeModel nodeModel) {
		if (viewIndex == 0) {
			return new RMAD_cluster_optimalNodeView(nodeModel);
		}
		else if (viewIndex == 1) {
			return new ExtToolStdoutNodeView<RMAD_cluster_optimalNodeModel>(nodeModel);
		} else if (viewIndex == 2) {
			return new ExtToolStderrNodeView<RMAD_cluster_optimalNodeModel>(nodeModel);
		}
		return null;
	}

	@Override
	protected boolean hasDialog() {
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new RMAD_cluster_optimalNodeDialog();
	}


}

