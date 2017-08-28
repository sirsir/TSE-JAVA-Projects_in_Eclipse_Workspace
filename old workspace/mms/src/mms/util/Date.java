package mms.util;

import java.util.Calendar;

public class Date extends java.util.Date {

	private static final long serialVersionUID = 405658517143213607L;

	@SuppressWarnings("deprecation")
	public Date(String value) {
		if(value != null && value.length() > 8) {
			String[] values = value.split(" ");
			String[] dateValues = values[0].split("-");
			
			this.setYear(Integer.valueOf(dateValues[0])-1900);
			this.setMonth(Integer.valueOf(dateValues[1])-1);
			this.setDate(Integer.valueOf(dateValues[2]));
			
			if(values.length > 1) {
				int idx = values[1].indexOf(".");
				if(idx > 0)
					values[1] = values[1].substring(0, idx);
				
				String[] timeValues = values[1].split(":");
				
				this.setHours(Integer.valueOf(timeValues[0]));
				this.setMinutes(Integer.valueOf(timeValues[1]));
				if(timeValues.length == 3)
					this.setSeconds(Integer.valueOf(timeValues[2]));
				else this.setSeconds(0);
			}
		}
	}
	
	public Date(String value, String format) {
		if(value != null && value.length() > 8) {
			String[] values = value.split(" ");
			
			if("mmddyyyy".equals(format)) {
				String[] dateValues = values[0].split("/");
				this.setYear(Integer.valueOf(dateValues[1])-1900);
				this.setMonth(Integer.valueOf(dateValues[0])-1);
				this.setDate(Integer.valueOf(dateValues[2]));
			}
			else {
				String[] dateValues = values[0].split("-");
				this.setYear(Integer.valueOf(dateValues[0])-1900);
				this.setMonth(Integer.valueOf(dateValues[1])-1);
				this.setDate(Integer.valueOf(dateValues[2]));
			}
			
			if(values.length > 1) {
				int idx = values[1].indexOf(".");
				if(idx > 0)
					values[1] = values[1].substring(0, idx);
				
				String[] timeValues = values[1].split(":");
				
				this.setHours(Integer.valueOf(timeValues[0]));
				this.setMinutes(Integer.valueOf(timeValues[1]));
				this.setSeconds(Integer.valueOf(timeValues[2]));
			}
		}
	}
	
	public Date() {
		super();
	}
	
	public Date(long time) {
		super(time);
	}
	
	@SuppressWarnings("deprecation")
	public String getDateString() {
		String month = String.valueOf(this.getMonth()+1);
		if(this.getMonth()+1 < 10)
			month = "0" + month;
		
		String day = String.valueOf(this.getDate());
		if(this.getDate() < 10)
			day = "0" + day;
		
		return (this.getYear()+1900) + "-" + month + "-" + day;
	}
	
	public String getDMY() {
		String month = String.valueOf(this.getMonth()+1);
		if(this.getMonth()+1 < 10)
			month = "0" + month;
		
		String day = String.valueOf(this.getDate());
		if(this.getDate() < 10)
			day = "0" + day;
		return day + "/" + month + "/" + (this.getYear()+1900);
	}
	
	public String getMDYHM() {
		return getMDY() + " " + getTimeString();
	}
	
	public String getMDY() {
		String month = String.valueOf(this.getMonth()+1);
		if(this.getMonth()+1 < 10)
			month = "0" + month;
		
		String day = String.valueOf(this.getDate());
		if(this.getDate() < 10)
			day = "0" + day;
		return month + "/" + day + "/" + (this.getYear()+1900);
	}
	
	public String getDMYHM() {
		return getDMY() + " " + getTimeString();
	}
	
	public String getYMDHM() {
		return getDateString() + " " + getTimeString();
	}
	
	public String getShortDate() {
		String day = String.valueOf(this.getDate());
		String month = String.valueOf(this.getMonth()+1);
		String year = String.valueOf(this.getYear()+1900);
		year = year.substring(2, 4);
		return day + "/" + month + "/" + year;
	}
	
	public String getFirstShiftTime() {
		Date tmpDate = new Date();
		tmpDate.setDate(this.getDate() + 1);
		
		return tmpDate.getDateString() + " " + "06:00:00"; 
	}
	
	public void setSpecificMinus() {
		int minutes = this.getMinutes();
		if(minutes == 0) {
			this.setMinutes(0);
			this.setSeconds(0);
		}
		else if(minutes > 0 && minutes < 15) {
			this.setMinutes(0);
			this.setSeconds(0);
		}
		else if(minutes > 15 && minutes < 30) {
			this.setMinutes(15);
			this.setSeconds(0);
		}
		else if(minutes > 30 && minutes < 45) {
			this.setMinutes(30);
			this.setSeconds(0);
		}
		else if(minutes > 45 && minutes <= 59) {
			this.setMinutes(45);
			this.setSeconds(0);
		}
	}
	
	public String getFullDateTimeString() {
		return getDateString() + " " + getTimeString();
	}
	
	@SuppressWarnings("deprecation")
	public String getTimeString() {
		String hours = String.valueOf(this.getHours());
		if(this.getHours() < 10) 
			hours = "0" + hours;
		
		String minus = String.valueOf(this.getMinutes());
		if(this.getMinutes() < 10)
			minus = "0" + minus;
		
		return hours + ":" + minus;
	}
	
	public String getFullTimeString() {
		String hours = String.valueOf(this.getHours());
		if(this.getHours() < 10) 
			hours = "0" + hours;
		
		String minus = String.valueOf(this.getMinutes());
		if(this.getMinutes() < 10)
			minus = "0" + minus;
		
		String seconds = String.valueOf(this.getSeconds());
		if(this.getSeconds() < 10)
			seconds = "0" + seconds;
		
		return hours + ":" + minus + ":" + seconds;
	}
	
	public String toString() {
		return getDateString() + " " + getTimeString();
	}
	
	public static boolean isTimeValid(String time) {
		boolean valid = true;
		String[] split = time.split(":");
		try {
			if(split.length == 2) {
				int hour = Integer.valueOf(split[0]);
				int minus = Integer.valueOf(split[1]);
				
				if(hour < 0 || hour > 23)
					valid = false;
				else if(minus < 0 || minus > 59)
					valid = false;
			}
			else valid = false;
		}
		catch(NumberFormatException e) {
			valid = false;
		}
		
		return valid;
	}
	
	public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Date(cal.getTime().getTime());
    }
	
	public String getCalendarFormat() {
		Calendar cal = Calendar.getInstance();
        cal.setTime(this);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        String value = String.valueOf(this.getDate());
        switch(dayOfWeek) {
	        case 1:
	        	value += " (Sun)";
	        	break;
	        case 2:
	        	value += " (Mon)";
	        	break;
	        case 3:
	        	value += " (Tue)";
	        	break;
	        case 4:
	        	value += " (Wed)";
	        	break;
	        case 5:
	        	value += " (Thu)";
	        	break;
	        case 6:
	        	value += " (Fri)";
	        	break;
	        case 7:
	        	value += " (Sat)";
	        	break;
        }
        return value;
	}
	
	public static int diffInDays(Date newerDate, Date olderDate) {
		return (int)( (newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24) );
	}
	
	public static int getDayOfMonth(int year, int month) {
		int day = 31;
		
		switch(month) {
			case 1: 
				if((year%4) == 0)
					day = 29;
				else day = 28;
				break;
			case 3: 
				day = 30;
				break;
			case 5: 
				day = 30;
				break;
			case 8: 
				day = 30;
				break;
			case 10: 
				day = 30;
				break;
		}
		
		return day;
	}
}
