/*
 * ------------------------------------------------------------------
 * Copyright, 2003 - 2011
 * University of Konstanz, Germany.
 * Chair for Bioinformatics and Information Mining
 * Prof. Dr. Michael R. Berthold
 *
 * This file is part of the R integration plugin for KNIME.
 *
 * The R integration plugin is free software; you can redistribute 
 * it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St., Fifth Floor, Boston, MA 02110-1301, USA.
 * Or contact us: contact@knime.org.
 * ---------------------------------------------------------------------
 *
 */
package it.mad.ext.r;

import org.knime.base.node.util.exttool.ExtToolOutputNodeView;
import org.knime.base.node.util.exttool.ExtToolStderrNodeView;
import org.knime.base.node.util.exttool.ExtToolStdoutNodeView;
import org.knime.core.node.NodeDialogPane;
import it.mad.ext.r.preferences.PreferenceInitializer;

/**
 * Factory for the <code>RMAD_claraNodeFactory</code> node.
 *
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMAD_claraNodeFactory extends RMADCoreNodeFactory<RMAD_claraNodeModel> {

	/**
	 * Empty default constructor.
	 */
	public RMAD_claraNodeFactory() {
		super(PreferenceInitializer.getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected NodeDialogPane createNodeDialogPane() {
		return new RMAD_claraNodeDialog();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RMAD_claraNodeModel createNodeModel() {
		return new RMAD_claraNodeModel(getRProvider());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExtToolOutputNodeView<RMAD_claraNodeModel> createNodeView(
			final int viewIndex,
			final RMAD_claraNodeModel nodeModel) {
		if (viewIndex == 0) {
			return
					new ExtToolStdoutNodeView<RMAD_claraNodeModel>(nodeModel);
		} else if (viewIndex == 1) {
			return
					new ExtToolStderrNodeView<RMAD_claraNodeModel>(nodeModel);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	 @Override
	 protected int getNrNodeViews() {
		 return 2;
	 }

	 /**
	  * {@inheritDoc}
	  */
	 @Override
	 protected boolean hasDialog() {
		 return true;
	 }
}
