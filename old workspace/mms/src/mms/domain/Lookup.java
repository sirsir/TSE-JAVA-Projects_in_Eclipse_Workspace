package mms.domain;

public abstract class Lookup {
	abstract public int store();
	abstract public boolean update();
	abstract public boolean delete();

	protected String message;
	public String getMessage() {return message;}

	public void setMessage(String message) {this.message = message;}
}
