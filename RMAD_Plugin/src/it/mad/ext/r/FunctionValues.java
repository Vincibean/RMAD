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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to store a set of values of a R function that the R environment
 * can process.
 */
public class FunctionValues{

	/** Holds the actual values */
	private HashMap<String, Object> mValues;

	/** The name of the library to be used with the node / function. */
	private String library;

	/** The name of the function. */
	private String functionName;

	/** The list of (names of) columns / fields to process */
	private List<String> includeList;

	/** The (eventual) suffix of the R function */
	private String suffix;

	/** The (eventual) number of columns for turning the output in a matrix. */
	private String columnsOfOutputMatrix;


	public FunctionValues(String library, String functionName,
			List<String> includeList, String suffix, String columnsOfOutputMatrix) {
		super();
		this.library = library;
		this.functionName = functionName;
		this.includeList = includeList;
		this.suffix = suffix;
		this.columnsOfOutputMatrix = columnsOfOutputMatrix;
		// Choosing a default minimum size of 10 based on analysis of typical 
		// consumption.
		mValues = new HashMap<String, Object>(10);
	}

	public FunctionValues(String library, String functionName,
			List<String> includeList, String suffix) {
		super();
		this.library = library;
		this.functionName = functionName;
		this.includeList = includeList;
		this.suffix = suffix;
		this.columnsOfOutputMatrix = null;
		// Choosing a default size of 10 based on analysis of typical 
		// consumption.
		mValues = new HashMap<String, Object>(10);
	}

	public FunctionValues(String library, String functionName,
			List<String> includeList) {
		super();
		this.library = library;
		this.functionName = functionName;
		this.includeList = includeList;
		this.columnsOfOutputMatrix = null;
		// Choosing a default size of 10 based on analysis of typical 
		// consumption.
		mValues = new HashMap<String, Object>(10);
	}

	/**
	public FunctionValues(String library, String functionName,
			List<String> includeList, String columnsOfOutputMatrix) {
		super();
		this.library = library;
		this.functionName = functionName;
		this.includeList = includeList;
		this.columnsOfOutputMatrix = columnsOfOutputMatrix;
		// Choosing a default size of 10 based on analysis of typical 
		// consumption.
		mValues = new HashMap<String, Object>(10);
	}
	 */








	/**
	 * Creates an empty set of values using the default initial size

	public FunctionValues() {
		// Choosing a default size of 10 based on analysis of typical 
		// consumption.
		mValues = new HashMap<String, Object>(10);
	}
	 */



	/**
	 * Creates an empty set of values using the given initial size
	 * 
	 * @param size the initial size of the set of values

	public FunctionValues(int size) {
		mValues = new HashMap<String, Object>(size, 1.0f);
	}
	 */

	/**
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof FunctionValues)) {
			return false;
		}
		return mValues.equals(((FunctionValues) object).mValues);
	}
	 */

	@Override
	public int hashCode() {
		return mValues.hashCode();
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, String value) {
		mValues.put(key, value);
	}

	/**
	 * Adds all values from the passed in FunctionValues.
	 *
	 * @param other the FunctionValues from which to copy
	 */
	public void putAll(FunctionValues other) {
		mValues.putAll(other.mValues);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Byte value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Short value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Integer value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Long value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Float value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Double value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Boolean value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, byte[] value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Integer[] value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, Double[] value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a value to the set.
	 *
	 * @param key the name of the value to put
	 * @param value the data for the value to put
	 */
	public void put(String key, String[] value) {
		mValues.put(key, value);
	}

	/**
	 * Adds a null value to the set.
	 * 
	 * @param key the name of the value to make null
	 */
	public void putNull(String key) {
		mValues.put(key, null);
	}

	/**
	 * Returns the number of values.
	 * 
	 * @return the number of values
	 */
	public int size() {
		return mValues.size();
	}

	/**
	 * Remove a single value.
	 *
	 * @param key the name of the value to remove
	 */
	public void remove(String key) {
		mValues.remove(key);
	}

	/**
	 * Removes all values.
	 */
	public void clear() {
		mValues.clear();
	}

	/**
	 * Returns true if this object has the named value.
	 *
	 * @param key the value to check for
	 * @return {@code true} if the value is present, {@code false} otherwise 
	 */
	public boolean containsKey(String key) {
		return mValues.containsKey(key);
	}

	/**
	 * Gets a value. Valid value types are {@link String}, {@link Boolean}, and
	 * {@link Number} implementations.
	 *
	 * @param key the value to get
	 * @return the data for the value
	 */
	public Object get(String key) {
		return mValues.get(key);
	}

	/**
	 * Gets a value and converts it to a String.
	 * 
	 * @param key the value to get
	 * @return the String for the value
	 */
	public String getAsString(String key) {
		Object value = mValues.get(key);
		// return value != null ? mValues.get(key).toString() : null;
		String s = null;
		if(value == null)
			s = "NULL";
		else if(value instanceof Integer)
			s = Integer.toString((int) value);
		else if(value instanceof Double)
			s = Double.toString((double) value);
		else if(value instanceof Boolean)
			s = Boolean.toString((boolean) value).toUpperCase();
		else if(value instanceof String)
			s = "\"" + value + "\"";
		else if(value instanceof Integer[]){
			s = "c(";
			for(Integer i : (Integer[])value){
				if (s.length() > 2) 
					s += ", ";
				s += i;
			}
			s += ")";
		}
		else if(value instanceof Double[]){
			s = "c(";
			for(Double i : (Double[])value){
				if (s.length() > 2) 
					s += ", ";
				s += i;
			}
			s += ")";
		}
		else if(value instanceof String[]){
			s = "c(";
			for(String i : (String[])value){
				if (s.length() > 2) 
					s += ", ";
				s += "\"" + i + "\"";
			}
			s += ")";
		}
		return s;
	}

	/**
	 * Gets a value and converts it to a Long.
	 * 
	 * @param key the value to get
	 * @return the Long value, or null if the value is missing or cannot be converted
	 */
	public Long getAsLong(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).longValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Long.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Long value for "value" at key "key"
					return null;
				}
			} else {
				// "Cannot cast value for "key" to a Long
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to an Integer.
	 * 
	 * @param key the value to get
	 * @return the Integer value, or null if the value is missing or cannot be converted
	 */
	public Integer getAsInteger(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).intValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Integer.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Integer value for "value" at key "key"
					return null;
				}
			} else {
				// Cannot cast value for "key" to a Integer
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to a Short.
	 * 
	 * @param key the value to get
	 * @return the Short value, or null if the value is missing or cannot be converted
	 */
	public Short getAsShort(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).shortValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Short.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Short value for "value" at key "key"
					return null;
				}
			} else {
				// Cannot cast value for "key" to a Short
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to a Byte.
	 * 
	 * @param key the value to get
	 * @return the Byte value, or null if the value is missing or cannot be converted
	 */
	public Byte getAsByte(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).byteValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Byte.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Byte value for "value" at key "key"
					return null;
				}
			} else {
				// "Cannot cast value for "key" to a Byte
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to a Double.
	 * 
	 * @param key the value to get
	 * @return the Double value, or null if the value is missing or cannot be converted
	 */
	public Double getAsDouble(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).doubleValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Double.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Double value for "value" at key "key"
					return null;
				}
			} else {
				// Cannot cast value for "key" to a Double
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to a Float.
	 * 
	 * @param key the value to get
	 * @return the Float value, or null if the value is missing or cannot be converted
	 */
	public Float getAsFloat(String key) {
		Object value = mValues.get(key);
		try {
			return value != null ? ((Number) value).floatValue() : null;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				try {
					return Float.valueOf(value.toString());
				} catch (NumberFormatException e2) {
					// Cannot parse Float value for "value" at key "key"
					return null;
				}
			} else {
				// Cannot cast value for "key" to a Float
				return null;
			}
		}
	}

	/**
	 * Gets a value and converts it to a Boolean.
	 * 
	 * @param key the value to get
	 * @return the Boolean value, or null if the value is missing or cannot be converted
	 */
	public Boolean getAsBoolean(String key) {
		Object value = mValues.get(key);
		try {
			return (Boolean) value;
		} catch (ClassCastException e) {
			if (value instanceof CharSequence) {
				return Boolean.valueOf(value.toString());
			} else {
				// Cannot cast value for "key" to a Boolean
				return null;
			}
		}
	}

	/**
	 * Gets a value that is a byte array. Note that this method will not convert
	 * any other types to byte arrays.
	 * 
	 * @param key the value to get
	 * @return the byte[] value, or null is the value is missing or not a byte[]
	 */
	public byte[] getAsByteArray(String key) {
		Object value = mValues.get(key);
		if (value instanceof byte[]) {
			return (byte[]) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns a set of all of the keys and values
	 * 
	 * @return a set of all of the keys and values
	 */
	public Set<Map.Entry<String, Object>> valueSet() {
		return mValues.entrySet();
	}

	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {

		// Creates a matrix of incoming data, combining them by columns.
		StringBuilder cbind = new StringBuilder("cbind(");
		for(String columnName : includeList){
			if (cbind.length() > 6) 	// 6 = number of characters in String "cbind("
				cbind.append(", ");
			cbind.append("R$\"" + RMADAbstractNodeModel.formatColumn(columnName) + "\"");
		}
		cbind.append(")");


		StringBuilder params = new StringBuilder();
		for (String name : mValues.keySet()) {
			String value = getAsString(name);
			if (params.length() > 0) 
				params.append(", ");
			params.append(name + " = " + value);
		}

		// Assemble the function string command.
		StringBuilder function = new StringBuilder(functionName + "(" + cbind + ", " + params + ")");

		// Assemble and return the command:
		// First, the "R <- R" default command, load the right library, then get the desired vector / matrix, storing
		// it in a variable with the same name of the function (for explanatory purposes).
		StringBuilder command = new StringBuilder("R <- R \n"
				+ "library(\"" + library + "\")\n"
				+ functionName + " <- " + function + "\n");

		// Second, if the output of the function needs to be transposed, then turn it into a matrix
		// by declaring of how many columns it will be composed (the number of columns is easier to get
		// than the number of rows). 
		if(columnsOfOutputMatrix != null){
			if(suffix != null)
				command.append(functionName + ".m" + " <- matrix(" + functionName + "$" + suffix + ", ncol = " + columnsOfOutputMatrix + ")\n");
			else
				command.append(functionName + ".m" + " <- matrix(" + functionName + ", ncol = " + columnsOfOutputMatrix + ")\n");
			// Finally, bind the output of your function with R (the input data of the node).
			command.append("R <- cbind(R, " + functionName + ".m" + ")\n");
			return command.toString();
		}
		if(suffix != null)
			command.append("R <- cbind(R, " + functionName + "$" + suffix + ")\n");
		else
			command.append("R <- cbind(R, " + functionName + ")\n");
		return command.toString();
	}

	
	public String toStringExceptLastLine() {

		// Creates a matrix of incoming data, combining them by columns.
		StringBuilder cbind = new StringBuilder("cbind(");
		for(String columnName : includeList){
			if (cbind.length() > 6) 
				cbind.append(", ");
			cbind.append("R$\"" + RMADAbstractNodeModel.formatColumn(columnName) + "\"");
		}
		cbind.append(")");


		StringBuilder params = new StringBuilder();
		for (String name : mValues.keySet()) {
			String value = getAsString(name);
			if (params.length() > 0) 
				params.append(", ");
			params.append(name + " = " + value);
		}

		// Assemble the function string command.
		StringBuilder function = new StringBuilder(functionName + "(" + cbind + ", " + params + ")");

		// Assemble and return the command:
		// First, the "R <- R" default command, load the right library, then get the desired vector / matrix, storing
		// it in a variable with the same name of the function (for explanatory purposes).
		StringBuilder command = new StringBuilder("R <- R \n"
				+ "library(\"" + library + "\")\n"
				+ functionName + " <- " + function + "\n");

		// Second, if the output of the function needs to be transposed, then turn it into a matrix
		// by declaring of how many columns it will be composed (the number of columns is easier to get
		// than the number of rows). 
		if(columnsOfOutputMatrix != null){
			if(suffix != null)
				command.append(functionName + ".m" + " <- matrix(" + functionName + "$" + suffix + ", ncol = " + columnsOfOutputMatrix + ")\n");
			else
				command.append(functionName + ".m" + " <- matrix(" + functionName + ", ncol = " + columnsOfOutputMatrix + ")\n");

			return command.toString();
		}

		return command.toString();
	}
}
