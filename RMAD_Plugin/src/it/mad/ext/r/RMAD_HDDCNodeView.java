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
 * <code>NodeView</code> for the "RMAD_HDDC" Node.
 * HDDC(High Dimensional Data Clustering) is a model-based clustering method. It is based on the Gaussian Mixture Model and on * nthe idea that the data lives in subspaces with a lower dimension than the dimension of the original * nspace. It uses the Expectation - Maximisation algorithm to estimate the parameters of the model.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_HDDCNodeView extends NodeView<RMAD_HDDCNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_HDDCNodeModel})
     */
    protected RMAD_HDDCNodeView(final RMAD_HDDCNodeModel nodeModel) {
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

