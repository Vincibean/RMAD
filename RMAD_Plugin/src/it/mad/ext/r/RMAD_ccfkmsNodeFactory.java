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
import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;

/**
 * <code>NodeFactory</code> for the "RMAD_ccfkms" Node.
 * Partition a data set into convex sets using conjugate convex functions.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_ccfkmsNodeFactory extends RMADCoreNodeFactory<RMAD_ccfkmsNodeModel> {

	/**
	 * Empty default constructor.
	 */

	public RMAD_ccfkmsNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RMAD_ccfkmsNodeModel createNodeModel() {
		return new RMAD_ccfkmsNodeModel(getRProvider());
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
	public ExtToolOutputNodeView<RMAD_ccfkmsNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_ccfkmsNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_ccfkmsNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_ccfkmsNodeModel>(nodeModel);
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
		return new RMAD_ccfkmsNodeDialog();
	}

}

