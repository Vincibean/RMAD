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

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "RMAD_nncluster" Node.
 * Uses Prim’s algorithm to build a minimum spanning tree for each cluster, stopping when the nearest- * nneighbour distance rises above a specified threshold. Returns a set of clusters and a set of ’outliers’ * nnot in any cluster.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_nnclusterNodeView extends NodeView<RMAD_nnclusterNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_nnclusterNodeModel})
     */
    protected RMAD_nnclusterNodeView(final RMAD_nnclusterNodeModel nodeModel) {
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

