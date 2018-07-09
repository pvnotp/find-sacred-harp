public class singingEvent{

	int year, month, day, duration;
	String dateText, name, location, link;
	String[] books = new String[3];

	public singingEvent(){
		this.year = 2018;
		this.duration = 1;
	}
	
	public String getDate(){
		return this.year + "-" + this.month + "-" + this.day;
	}
	
}