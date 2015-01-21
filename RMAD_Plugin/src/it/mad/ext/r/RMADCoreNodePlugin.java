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
package it.mad.ext.r;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author Thomas Gabriel, University of Konstanz
 * @author Andre Bessi, University of Milan Bicocca
 */
public class RMADCoreNodePlugin extends AbstractUIPlugin {

    // The shared instance.
    private static RMADCoreNodePlugin plugin;

    private static File rExecutable;
    
    private static final NodeLogger LOGGER = 
        NodeLogger.getLogger(RMADCoreNodePlugin.class);

    /**
     * The constructor.
     */
    public RMADCoreNodePlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation.
     * @param context The bundle context.
     * @throws Exception If cause by super class.
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        Bundle bundle = context.getBundle();
        Enumeration<URL> e = bundle.findEntries("/R-Inst/bin", "R.exe", true);
        URL url = null;
        if ((e != null) && e.hasMoreElements()) {
            url = e.nextElement();
        } else {
            e = bundle.findEntries("/R-Inst/bin", "R", true);
            if ((e != null) && e.hasMoreElements()) {
                url = e.nextElement();
            }
        }
        // default path on linux systems (no R binary plugin available) 
        String[] searchPaths = {"/usr/bin/R", "/usr/local/bin/R"};
        if (url == null) {
            for (String s : searchPaths) {
                File f = new File(s);
                if (f.canExecute()) {
                    url = f.toURI().toURL();
                    break;
                }
            }
        }
        if (url != null) {
            try {
                rExecutable = new File(FileLocator.toFileURL(url).getFile());
            } catch (Exception ex) {
                LOGGER.info("Could not locate default R executable", ex);
            }
        }
    }

    /**
     * This method is called when the plug-in is stopped.
     * @param context The bundle context.
     * @throws Exception If cause by super class.
     */
    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     *
     * @return The shared instance
     */
    public static RMADCoreNodePlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.knime.ext.r",
                path);
    }

    /**
     * @return R executable
     */
    public static File getRExecutable() {
        return rExecutable;
    }
}
