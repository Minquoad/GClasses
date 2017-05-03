package gClasses;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DataAssociator {
	private File file;
	private ArrayList<byte[]> keys;
	private ArrayList<byte[]> values;

	private int masque = 113;

	private void construct(File file) {
		this.file = file;
		keys = new ArrayList<byte[]>();
		values = new ArrayList<byte[]>();
	}

	public DataAssociator() {
		this.construct(null);
	}

	public DataAssociator(byte[] ba) {
		this.construct(null);

		extractData(ba);
	}

	public DataAssociator(File file) throws Exception {
		this.construct(file);

		if (file.exists()) {

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

			if (bis.available() != 0) {
				byte[] ba = new byte[bis.available()];
				bis.read(ba);

				for (int i = 0; i < ba.length; i++) {
					ba[i] = (byte) (ba[i] - masque);
				}

				extractData(ba);
			}

			bis.close();
		} else {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addValue(byte[] key, byte[] value) {
		this.keys.add(cloner(key));
		this.values.add(cloner(value));
	}

	public byte[] getValue(byte[] key) {
		byte[] value = null;

		int i = 0;
		while (value == null && i < this.keys.size()) {
			if (equals(this.keys.get(i), key)) {
				value = this.values.get(i);
			}
			i++;
		}

		return cloner(value);
	}

	public ArrayList<byte[]> getValues(byte[] key) {
		ArrayList<byte[]> valuesReturned = new ArrayList<byte[]>();

		int i = 0;
		while (i < this.keys.size()) {
			if (equals(this.keys.get(i), key)) {
				valuesReturned.add(cloner(this.values.get(i)));
			}
			i++;
		}

		return valuesReturned;
	}

	public int deleteValues(byte[] key) {
		int k = 0;

		int i = 0;
		while (i < this.keys.size()) {
			if (equals(this.keys.get(i), key)) {
				this.keys.remove(i);
				this.values.remove(i);
				k++;
			} else {
				i++;
			}
		}

		return k;
	}

	public int setValue(byte[] key, byte[] value) {
		int k = this.deleteValues(key);
		this.addValue(key, value);
		return k;
	}

	public boolean exists(byte[] key) {
		boolean finded = false;

		int i = 0;
		while (finded == false && i < this.keys.size()) {
			if (equals(this.keys.get(i), key)) {
				finded = true;
			}
			i++;
		}

		return finded;
	}

	public byte[] getData() {
		byte[] data = new byte[0];
		for (int i = 0; i < this.keys.size(); i++) {
			data = concate(data, intToByteArray(this.keys.get(i).length));
			data = concate(data, this.keys.get(i));
			data = concate(data, intToByteArray(this.values.get(i).length));
			data = concate(data, this.values.get(i));
		}
		return data;
	}

	public void resetData() {
		keys = new ArrayList<byte[]>();
		values = new ArrayList<byte[]>();
	}

	public boolean save(File file) {
		boolean fait;

		try {

			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

			byte[] ba = this.getData();

			for (int i = 0; i < ba.length; i++) {
				ba[i] = (byte) (ba[i] + masque);
			}

			bos.write(ba);

			bos.close();

			fait = true;
		} catch (IOException e) {
			e.printStackTrace();

			fait = false;
		}

		return fait;
	}

	// utilitaires

	private int longeurChamp(byte[] data, int i) {
		byte[] longeurChampByteTable = new byte[4];

		int j = 0;
		while (j < 4) {
			longeurChampByteTable[j] = data[i];
			i++;
			j++;
		}

		return byteArrayToInt(longeurChampByteTable);
	}

	private void extractData(byte[] data) {
		try {
			int i = 0;
			while (i < data.length) {
				int l = longeurChamp(data, i);
				i += 4;

				this.keys.add(subArray(data, i, i + l));
				i += l;

				l = longeurChamp(data, i);
				i += 4;

				this.values.add(subArray(data, i, i + l));
				i += l;
			}
		} catch (Exception e) {
			e.printStackTrace();
			resetData();
		}
	}

	// utilitaires de byte[]

	private byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
	}

	private int byteArrayToInt(byte[] bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}

	private boolean equals(byte[] ba1, byte[] ba2) {
		if (ba1.length != ba2.length) {
			return false;
		} else {
			int i = 0;
			while (i < ba1.length && ba1[i] == ba2[i]) {
				i++;
			}
			return i == ba1.length;
		}
	}

	private byte[] subArray(byte[] baOriginal, int begin, int end) {
		int length = end - begin;
		if (length == 0) {
			return new byte[0];
		} else {
			byte[] baNew = new byte[length];

			for (int i = 0; i < length; i++) {
				baNew[i] = baOriginal[i + begin];
			}

			return baNew;
		}
	}

	private byte[] concate(byte[] ba1, byte[] ba2) {
		if (ba1 == null && ba2 == null) {
			return null;
		} else {
			if (ba1 == null) {
				return ba2;
			} else {
				if (ba2 == null) {
					return ba1;
				} else {
					byte[] ba = new byte[ba1.length + ba2.length];

					for (int i = 0; i < ba1.length; i++) {
						ba[i] = ba1[i];
					}
					for (int i = 0; i < ba2.length; i++) {
						ba[i + ba1.length] = ba2[i];
					}

					return ba;
				}
			}
		}
	}

	private byte[] cloner(byte[] ba) {
		if (ba == null) {
			return null;
		} else {
			byte[] ba2 = new byte[ba.length];
			for (int i = 0; i < ba.length; i++) {
				ba2[i] = ba[i];
			}
			return ba2;
		}
	}

	// interface

	public boolean save() {
		return this.save(this.file);
	}

	public ArrayList<byte[]> getAllKeys() {
		ArrayList<byte[]> list = new ArrayList<byte[]>();

		for (int i = 0; i < this.keys.size(); i++) {
			list.add(cloner(this.keys.get(i)));
		}

		return list;
	}

	public void addValue(int key, int value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(int key, String value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(String key, int value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(String key, String value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(int key, byte[] value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(String key, byte[] value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(byte[] key, DataAssociator value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(int key, DataAssociator value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public void addValue(String key, DataAssociator value) {
		this.addValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(int key, int value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(int key, String value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(String key, int value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(String key, String value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(int key, byte[] value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(String key, byte[] value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(byte[] key, DataAssociator value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(int key, DataAssociator value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int setValue(String key, DataAssociator value) {
		return this.setValue(convertToByteArray(key), convertToByteArray(value));
	}

	public int deleteValues(int key) {
		return this.deleteValues(intToByteArray(key));
	}

	public int deleteValues(String key) {
		return this.deleteValues(key.getBytes());
	}

	public int getValueInt(int key) {
		return byteArrayToInt(this.getValue(convertToByteArray(key)));
	}

	public int getValueInt(String key) {
		return byteArrayToInt(this.getValue(convertToByteArray(key)));
	}

	public int getValueInt(byte[] key) {
		return byteArrayToInt(this.getValue(convertToByteArray(key)));
	}

	public ArrayList<Integer> getValuesInt(int key) {
		ArrayList<Integer> returnedList = new ArrayList<Integer>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(byteArrayToInt(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<Integer> getValuesInt(String key) {
		ArrayList<Integer> returnedList = new ArrayList<Integer>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(byteArrayToInt(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<Integer> getValuesInt(byte[] key) {
		ArrayList<Integer> returnedList = new ArrayList<Integer>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(byteArrayToInt(list.get(i)));
		}
		return returnedList;
	}

	public String getValueString(int key) {
		byte[] r = this.getValue(convertToByteArray(key));
		if (r == null) {
			return null;
		} else {
			return new String(r);
		}
	}

	public String getValueString(String key) {
		byte[] r = this.getValue(convertToByteArray(key));
		if (r == null) {
			return null;
		} else {
			return new String(r);
		}
	}

	public String getValueString(byte[] key) {
		byte[] r = this.getValue(convertToByteArray(key));
		if (r == null) {
			return null;
		} else {
			return new String(r);
		}
	}

	public ArrayList<String> getValuesString(int key) {
		ArrayList<String> returnedList = new ArrayList<String>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new String(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<String> getValuesString(String key) {
		ArrayList<String> returnedList = new ArrayList<String>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new String(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<String> getValuesString(byte[] key) {
		ArrayList<String> returnedList = new ArrayList<String>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new String(list.get(i)));
		}
		return returnedList;
	}

	public byte[] getValueData(int key) {
		return this.getValue(convertToByteArray(key));
	}

	public byte[] getValueData(String key) {
		return this.getValue(convertToByteArray(key));
	}

	public byte[] getValueData(byte[] key) {
		return this.getValue(convertToByteArray(key));
	}

	public ArrayList<byte[]> getValuesData(int key) {
		return this.getValues(convertToByteArray(key));
	}

	public ArrayList<byte[]> getValuesData(String key) {
		return this.getValues(convertToByteArray(key));
	}

	public ArrayList<byte[]> getValuesData(byte[] key) {
		return this.getValues(convertToByteArray(key));
	}

	public DataAssociator getValueDataAssociator(int key) {
		return new DataAssociator(this.getValue(convertToByteArray(key)));
	}

	public DataAssociator getValueDataAssociator(String key) {
		return new DataAssociator(this.getValue(convertToByteArray(key)));
	}

	public DataAssociator getValueDataAssociator(byte[] key) {
		return new DataAssociator(this.getValue(convertToByteArray(key)));
	}

	public ArrayList<DataAssociator> getValuesDataAssociator(int key) {
		ArrayList<DataAssociator> returnedList = new ArrayList<DataAssociator>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new DataAssociator(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<DataAssociator> getValuesDataAssociator(String key) {
		ArrayList<DataAssociator> returnedList = new ArrayList<DataAssociator>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new DataAssociator(list.get(i)));
		}
		return returnedList;
	}

	public ArrayList<DataAssociator> getValuesDataAssociator(byte[] key) {
		ArrayList<DataAssociator> returnedList = new ArrayList<DataAssociator>();
		ArrayList<byte[]> list = this.getValues(convertToByteArray(key));
		for (int i = 0; i < list.size(); i++) {
			returnedList.add(new DataAssociator(list.get(i)));
		}
		return returnedList;
	}

	public boolean exists(int key) {
		return this.exists(convertToByteArray(key));
	}

	public boolean exists(String key) {
		return this.exists(convertToByteArray(key));
	}

	// interface utilitaires

	private byte[] convertToByteArray(String var) {
		return var.getBytes();
	}

	private byte[] convertToByteArray(int var) {
		return intToByteArray(var);
	}

	private byte[] convertToByteArray(byte[] var) {
		return var;
	}

	private byte[] convertToByteArray(DataAssociator var) {
		return var.getData();
	}

}
