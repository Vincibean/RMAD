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
 * --------------------------------------------------------------------- *
 *
 */
package it.mad.ext.r;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

import org.knime.base.node.util.exttool.CommandExecution;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import it.mad.ext.r.preferences.RMADPreferenceProvider;

/**
 * <code>RLocalNodeModel</code> is an abstract
 * <code>StdOutBufferedNodeModel</code> which can be extended to implement a R
 * node using a local R installation. This abstract node model provides
 * functionality to write incoming data of a <code>DataTable</code> into a csv
 * file and read this data into R. The R variable containing the data is named
 * "R". Additional R commands to modify and process this data can be specified
 * by a class extending <code>RLocalNodeModel</code>. To access i.e. the
 * first three columns of a table and reference them by another variable "a" the
 * R command "a <- R[1:3]" can be used.<br/>
 * Further, this class writes the data
 * referenced by the R variable "R" after execution of the additional commands
 * into a csv file and generates an outgoing <code>DataTable</code> out of it,
 * which is returned by this node. This means, the user has to take care that
 * the processed data have to be referenced by the variable "R".
 * <br />
 * Note that the number of input data tables is one and can not be modified when
 * extending this node model. The number of output data tables can be specified
 * but only one or zero will make any sense since only the data referenced by
 * the "R" variable will be exported as output data table if the number of
 * output tables is greater than zero.
 * <br/>
 * Additionally this class provides a preprocessing method
 * {@link RMADLocalNodeModel#preprocessDataTable(PortObject[], ExecutionContext)}
 * which can be overwritten to preprocess that input data, as well as a
 * postprocess method
 * {@link RMADLocalNodeModel#postprocessDataTable(
 * BufferedDataTable[], ExecutionContext)}
 * which can be overwritten to process that data after the R script execution.
 * If these methods are not overwritten the data will not be modified.
 *
 * @author Kilian Thiel, University of Konstanz
 * @author Thomas Gabriel, University of Konstanz
 * @author Andre Bessi, University of Milan Bicocca
 */
public abstract class RMADLocalNodeModel extends RMADAbstractNodeModel {
    
    private static final NodeLogger LOGGER =
        NodeLogger.getLogger(RMADLocalNodeModel.class);
    
    private boolean m_hasDataTableOutPort = false;

    /**
     * Constructor of <code>RLocalNodeModel</code> creating a model with one
     * data in port and one data out port if and only if <code>hasOutput</code>
     * is set <code>true</code>. Otherwise the node will not have any
     * data out port.
     * @param outPorts array of out-port types
     * @param pref R preference provider
     */
    public RMADLocalNodeModel(final PortType[] outPorts, 
            final RMADPreferenceProvider pref) {
        super(new PortType[]{BufferedDataTable.TYPE}, outPorts, pref);
        // check for data table out ports
        if (outPorts != null) {
            for (int i = 0; i < outPorts.length; i++) {
                if (outPorts[i].equals(BufferedDataTable.TYPE)) {
                    m_hasDataTableOutPort = true;
                }
            }
        }
    }
    
    /**
     * Implement this method to specify certain R code to run. Be aware that
     * this R code has to be valid, otherwise the node will not execute
     * properly. To access the input data of the node via R use the variable
     * "R". To access i.e. the first three columns of a table and reference
     * them by another variable "a" the R command "a <- R[1:3];" can be used.
     * End all R command lines with a semicolon and a line break. The data
     * which has to be returned be the node as out data has to be stored in
     * the "R" variable again, so take care to reference your data by "R".
     *
     * @return The R command to execute.
     */
    protected abstract String getCommand();

    /**
     * First the
     * {@link RMADLocalNodeModel#preprocessDataTable(PortObject[],
     * ExecutionContext)}
     * method is called to preprocess that input data. Further a csv file is
     * written containing the input data. Next a R script is created consisting
     * of R commands to import the data of the csv file, the R code specified in
     * the command string and the export of the modified data into a output
     * table. This table is returned at the end.
     *
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws CanceledExecutionException,
            Exception {
    	
    	postConfigure(inData, exec);

        // blow away the output of any previous (failed) runs
        setFailedExternalErrorOutput(new LinkedList<String>());
        setFailedExternalOutput(new LinkedList<String>());

        // preprocess data in inDataTable.
        PortObject[] inDataTables = preprocessDataTable(inData, exec);

        File tempOutData = null;
        File inDataCsvFile = null;
        File rCommandFile = null;
        File rOutFile = null;
        BufferedDataTable[] dts = null;

        try {
            // write data to csv
            inDataCsvFile = writeInDataCsvFile(
                    (BufferedDataTable)inDataTables[0], exec);

            // execute R cmd
            StringBuilder completeCmd = new StringBuilder();
            completeCmd.append(SET_WORKINGDIR_CMD);
            completeCmd.append(READ_DATA_CMD_PREFIX);
            completeCmd.append(inDataCsvFile.getAbsolutePath().replace('\\',
                    '/'));
            completeCmd.append(READ_DATA_CMD_SUFFIX);

            completeCmd.append(getCommand().trim());
            completeCmd.append("\n");

            // write R data only into out file if data table out port exists
            if (m_hasDataTableOutPort) {
                tempOutData = File.createTempFile("R-outDataTempFile-", ".csv",
                        new File(TEMP_PATH));
                tempOutData.deleteOnExit();
                completeCmd.append(WRITE_DATA_CMD_PREFIX);
                completeCmd.append(
                        tempOutData.getAbsolutePath().replace('\\', '/'));
                completeCmd.append(WRITE_DATA_CMD_SUFFIX);
            }

            // write R command
            String rCmd = ExpressionResolver.parseCommand(
                    completeCmd.toString(), this);
            LOGGER.debug("R Command: \n" + rCmd);
            rCommandFile = writeRcommandFile(rCmd);
            rOutFile = File.createTempFile("R-outDataTempFile-", ".Rout",
                    rCommandFile.getParentFile());
            rOutFile.deleteOnExit();

            // create shell command
            StringBuilder shellCmd = new StringBuilder();

            final String rBinaryFile = getRBinaryPathAndArguments();
            shellCmd.append(rBinaryFile);
            shellCmd.append(" " + rCommandFile.getName());
            shellCmd.append(" " + rOutFile.getName());

            // execute shell command
            String shcmd = shellCmd.toString();
            LOGGER.debug("Shell command: \n" + shcmd);

            CommandExecution cmdExec = new CommandExecution(shcmd);
            cmdExec.addObserver(this);
            cmdExec.setExecutionDir(rCommandFile.getParentFile());
            int exitVal = cmdExec.execute(exec);

            setExternalErrorOutput(new LinkedList<String>(cmdExec.getStdErr()));
            setExternalOutput(new LinkedList<String>(cmdExec.getStdOutput()));

            String rErr = "";

            if (exitVal != 0) {
                // before we return, we save the output in the failing list
                synchronized (cmdExec) {
                    setFailedExternalOutput(new LinkedList<String>(
                            cmdExec.getStdOutput()));
                }
            }
            synchronized (cmdExec) {

                // save error description of the Rout file to the ErrorOut
                LinkedList<String> list =
                        new LinkedList<String>(cmdExec.getStdErr());

                list.add("#############################################");
                list.add("#");
                list.add("# Content of .Rout file: ");
                list.add("#");
                list.add("#############################################");
                list.add(" ");
                BufferedReader bfr =
                        new BufferedReader(new FileReader(rOutFile));
                String line;
                while ((line = bfr.readLine()) != null) {
                    list.add(line);
                }
                bfr.close();

                // use row before last as R error.
                int index = list.size() - 2;
                if (index >= 0) {
                    rErr = list.get(index);
                }
                
                if (exitVal != 0) {
                    setFailedExternalErrorOutput(list);
                    LOGGER.debug("Execution of R Script failed with exit code: "
                            + exitVal);
                    throw new IllegalStateException(
                            "Execution of R script failed: " + rErr);
                } else {
                    setExternalOutput(list);
                }                
            }

            // read out data only if data table out port exists
            if (m_hasDataTableOutPort) {
                // read data from R output csv into a buffered data table.
                ExecutionContext subExecCon = 
                    exec.createSubExecutionContext(1.0);
                BufferedDataTable dt = readOutData(tempOutData, subExecCon);

                // postprocess data in out DataTable.
                dts = postprocessDataTable(new BufferedDataTable[]{dt}, exec);
            }

        } finally {
            // delete all temp files
            deleteFile(inDataCsvFile);
            deleteFile(tempOutData);
            deleteFile(rCommandFile);
            deleteFile(rOutFile);
        }

        // return this table
        return dts;
    }
    
    /**
     * Implement this method if you want to validate complex settings 
     * specifical of your node.
     * @throws InvalidSettingsException 
     */
	protected abstract void postConfigure(PortObject[] inData, ExecutionContext exec) throws InvalidSettingsException;

}
