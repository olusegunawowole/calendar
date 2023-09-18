package application;

import java.util.ArrayList;

public class Decade {
	private int startYear;
	private int endYear;
	private int index;
	private static final int FIRST_DECADE = 1930;
	private static final int LAST_DECADE = 2110;
	private static final int LAST_YEAR = 2123;
	private static final int[] RANGE_STARTS = { 1923, 1939, 1947, 1959, 1967, 1979, 1987, 1999, 2007, 2019, 2027, 2039,
			2047, 2059, 2067, 2079, 2087, 2099, 2107, 2111 };

	public Decade(int index) {
		if (index < 0) {
			index = 0;
			this.startYear = FIRST_DECADE;
		} else if (index >= 19) {
			startYear = LAST_DECADE;
			this.index = 19;
		} else {
			this.index = index;
			startYear = FIRST_DECADE + index * 10;
		}
		endYear = startYear + 9;
	}
/**
 * 
 * @return years to be displayed when the object is selected
 */
	public ArrayList<Integer> getDisplayRange() {
		ArrayList<Integer> years = new ArrayList<>();
		int year = RANGE_STARTS[index];
		while (years.size() < 16 && year <= LAST_YEAR) {
			years.add(year);
			year++;
		}
		return years;
	}

	public String toString() {
		return this.startYear + " - " + endYear;
	}

	/**
	 * 
	 * @param year the year which we want to find its decades
	 * @return string containing the decade of the year
	 */
	public static String toString(int year) {
		int decade = year / 10 * 10;// year/10 will return int (ignoring remainders). Multiplying the result by 10
		// will give the beginning of a decade.
		return decade + " - " + (decade + 9);
	}
	/**
	 * This function finds decade index of a given year
	 * @param year the year to find its decade index.
	 * @return the decade index
	 */
	public static int getIndex(int year) {
		if(year < FIRST_DECADE)
			return 0;
		if(year > LAST_DECADE)
			return 19;
		
		int runner = FIRST_DECADE;
		int index = -1;
		
		while(runner <= year) {
			runner+=10;
			index++;
		}
		return index;
	}
	
	public int getIndex() {
		return index;
	}

	/**
	 * @return the startYear
	 */
	public int getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the endYear
	 */
	public int getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear the endYear to set
	 */
	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	/**
	 * This method checks if a given year is within the decade
	 * 
	 * @param year the year to check
	 * @return true if year is within the decade or false if otherwise.
	 */
	public boolean inDecade(int year) {
		return year >= startYear && year <= endYear;
	}
}
