package com.counter.utility;

class CountComparable implements Comparable<CountComparable> {
	public String wordFromFile;
	public int numberOfOccurrence;

	public CountComparable(String wordFromFile, int numberOfOccurrence) {
		super();
		this.wordFromFile = wordFromFile;
		this.numberOfOccurrence = numberOfOccurrence;
	}

	@Override
	public int compareTo(CountComparable arg0) {
		int countCompare = Integer.compare(arg0.numberOfOccurrence, this.numberOfOccurrence);
		return countCompare != 0 ? countCompare : wordFromFile.compareTo(arg0.wordFromFile);
	}

	@Override
	public int hashCode() {
		final int uniqueNumber = 19;
		int countResult = 9;
		countResult = uniqueNumber * countResult + numberOfOccurrence;
		countResult = uniqueNumber * countResult + ((wordFromFile == null) ? 0 : wordFromFile.hashCode());
		return countResult;
	}

	@Override
	public boolean equals(Object countObj) {
		if (this == countObj)
			return true;
		if (countObj == null)
			return false;
		if (getClass() != countObj.getClass())
			return false;
		CountComparable other = (CountComparable) countObj;
		if (numberOfOccurrence != other.numberOfOccurrence)
			return false;
		if (wordFromFile == null) {
			if (other.wordFromFile != null)
				return false;
		} else if (!wordFromFile.equals(other.wordFromFile))
			return false;
		return true;
	}
}
