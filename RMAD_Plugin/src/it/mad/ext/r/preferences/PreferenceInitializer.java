/*
 * ------------------------------------------------------------------------
 *
 *  Copyright (C) 2003 - 2011
 *  University of Konstanz, Germany and
 *  KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, version 2, as 
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ------------------------------------------------------------------------
 *
 */
package it.mad.ext.r.preferences;

import java.io.File;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.knime.core.node.NodeLogger;
import it.mad.ext.r.RMADCoreNodePlugin;

/**
 *
 * @author Kilian Thiel, University of Konstanz
 * @author Andre Bessi, University of Milan Bicocca
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    
    private static final NodeLogger LOGGER = 
        NodeLogger.getLogger(PreferenceInitializer.class); 

    /** Preference key for the path to the R executable setting. */
    //public static final String PREF_R_PATH = "knime.r.path";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = RMADCoreNodePlugin.getDefault().getPreferenceStore();
        File rPath = RMADCoreNodePlugin.getRExecutable();
        if (rPath != null) {
            LOGGER.info("Default R executable: " + rPath.getAbsolutePath());
            store.setDefault(PreferenceConstants.RMAD_P_PATH, rPath.getAbsolutePath());
        } else {
            store.setDefault(PreferenceConstants.RMAD_P_PATH, "");
        }
    }

    /**
     * Returns a provider for the R executable.
     * @return provider to the path to the R executable
     */
    public static final RMADPreferenceProvider getRProvider() {
        return new RMADPreferenceProvider() {
            @Override
            /** {@inheritDoc} */
            public String getRPath() {
                final IPreferenceStore pStore =
                		RMADCoreNodePlugin.getDefault().getPreferenceStore();
                return pStore.getString(PreferenceConstants.RMAD_P_PATH);
            }
        };
    }
}
