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
 * <code>NodeView</code> for the "RMAD_emp2pval" Node.
 * Converts the Empirical Posterior Probability (EPP) computed by cluster.test into a frequentist * np-value, which can then be used to assess the significance of the alternative hypothesis.
 *
 * @author Andre Bessi
 */
public class RMAD_emp2pvalNodeView extends NodeView<RMAD_emp2pvalNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link RMAD_emp2pvalNodeModel})
     */
    protected RMAD_emp2pvalNodeView(final RMAD_emp2pvalNodeModel nodeModel) {
        super(nodeModel);

        // TODO instantiate the components of the view here.

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {

        // TODO retrieve the new model from your nodemodel and 
        // update the view.
        RMAD_emp2pvalNodeModel nodeModel = 
            (RMAD_emp2pvalNodeModel)getNodeModel();
        assert nodeModel != null;
        
        // be aware of a possibly not executed nodeModel! The data you retrieve
        // from your nodemodel could be null, emtpy, or invalid in any kind.
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
    
        // TODO things to do when closing the view
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {

        // TODO things to do when opening the view
    }

}

