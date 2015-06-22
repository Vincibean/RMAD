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

