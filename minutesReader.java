import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;  
import java.util.Scanner;
import java.lang.Integer;
import java.util.*;


public class minutesReader{
	
	public static void main(String[] args) throws Exception{
		String month = "";
		String weekday = "";
		String day;
		int n = 0;
		
		File minutes = new File("annualMinutes.txt");
		Scanner fileScan = new Scanner(minutes);
		
		File json = new File("annualSingings.json");
		Scanner jsonScan = new Scanner(json).useDelimiter("\\Z");
		String jsonFileString = jsonScan.next();
		
		singingEvent event = null;
		String eventString = "";
		
		String jsonString;
		PrintWriter jsonWriter = new PrintWriter(new FileOutputStream(json, true));
		
		do{
			//Scan the Minutes a line a time.
			String line = fileScan.nextLine();
			Scanner lineScan = new Scanner(line);
			n++;
			
			if(isMonth(line)){
				month = line;  //If the line starts a new month, set the month value
			}else if(isGarbage(line)){  //Throw away "Third Sunday and the Saturday before" etc.
			}else{
				if(lineScan.hasNext("...\\.,") && n >3){
					//If a new event is starting, let's process the last one
					
					//Create an event
					event = new singingEvent();
					Scanner eventScan = new Scanner(eventString);
					
					//Store the date
					event.month = monthNum(month);
					eventScan.useDelimiter("\\., ");
					event.dateText = eventScan.next().trim();
					eventScan.useDelimiter("-"); 
					day = eventScan.next().replaceAll("\\D","");
					event.day = Integer.parseInt(day);
					
					//Store the name
					event.name = eventScan.next();
					
					//If that name is in the database, skip to the next entry
					if(jsonFileString.contains(event.name)){
					}else{ //If it's new, keep processing it.
					
						//Store the location
						event.location = eventScan.next();
						
						//Check if a different book is mentioned
						event.books = books(eventString);

						//Check that previous event listing has been parsed 
						//correctly and make necessary corrections.
						if(event != null){
							System.out.println();
							System.out.print("Date: ");
							System.out.print(event.getDate() + " (" + event.dateText + ") | ");
							Scanner scanIn = new Scanner(System.in);
							String correction = scanIn.nextLine();
							while(!correction.isEmpty()){
								event.day = Integer.parseInt(correction);
								System.out.print("Duration: ");
								correction = scanIn.nextLine();
								event.duration = Integer.parseInt(correction);
								System.out.print("Date: ");
								System.out.print(event.getDate() + " (" + event.dateText + ") | ");
								if(event.duration > 1)
									System.out.print(event.duration + " day event");
								correction = scanIn.nextLine();
							}
							
							System.out.print("Name: ");
							System.out.print(event.name+ " | ");
							scanIn = new Scanner(System.in);
							correction = scanIn.nextLine();
							while(!correction.isEmpty()){
								event.name = correction;
								System.out.print("Name: ");
								System.out.print(event.name + " | ");
								correction = scanIn.nextLine();
							}
							
							System.out.print("Location: ");
							System.out.print(event.location+ " | ");
							scanIn = new Scanner(System.in);
							correction = scanIn.nextLine();
							while(!correction.isEmpty()){
								event.location = correction;
								System.out.print("Location: ");
								System.out.print(event.location + " | ");
								correction = scanIn.nextLine();
							}
							
							if(!event.books[0].equals("Denson")){
								System.out.print("Book: ");
								for(String book : event.books){
									if(book!=null)
										System.out.print(book + " | ");
								}
								scanIn = new Scanner(System.in);
								correction = scanIn.nextLine();
								while(!correction.isEmpty()){
									event.books = books(correction);
									System.out.print("Book: ");
									for(String book : event.books){
										if(book!=null)
											System.out.print(book + " | ");
									}
									correction = scanIn.nextLine();
								}
							}
							
							if(eventString.contains(".com")||eventString.contains(".org")){
								System.out.print("Link: ");
								scanIn = new Scanner(System.in);
								correction = scanIn.nextLine();
								event.link = correction;
							}
								
							
							//Compose string for the json database
							jsonString = "[\"" + event.name +  "\", \"";
							jsonString = jsonString + event.location + "\", \"";
							jsonString = jsonString + event.getDate() + "\", \"";
							jsonString = jsonString + event.duration + "\", \"";
							jsonString = jsonString + event.books[0] + "\", \"";
							jsonString = jsonString + event.books[1] + "\", \"";
							jsonString = jsonString + event.books[2] + "\", \"";
							jsonString = jsonString + event.link + "\"],";
							
							jsonString = jsonString.replace("null","");
						
							jsonWriter.println(jsonString);
							jsonWriter.flush();
						}
					}
					//Now that we've processed the last event,
					//start a new eventString from the new line.
					eventString = line;
					System.out.println();
					System.out.println("************************");
				}else{
					//As more lines get added without starting a new event,
					//add them to the event string
					eventString = eventString.concat(" ");
					eventString = eventString.concat(line);
				}
				//Show the actual lines so we can make corrections
				System.out.println(line);				
			}
						
		}while(fileScan.hasNext());
	}
	
	public static boolean isMonth(String string){
		if(string.equals("January") ||
			string.equals("February") ||
			string.equals("March") ||
			string.equals("April") ||
			string.equals("May") ||
			string.equals("June") ||
			string.equals("July") ||
			string.equals("August") ||
			string.equals("September") ||
			string.equals("October") ||
			string.equals("November") ||
			string.equals("December"))
			return true;
		else
			return false;
	}
	
	public static int monthNum(String string){
		int month;
		switch(string){
			case "January": month = 1; break;
			case "February": month = 2; break;
			case "March": month = 3; break;
			case "April": month = 4; break;
			case "May": month = 5; break;
			case "June": month = 6; break;
			case "July": month = 7; break;
			case "August": month = 8; break;
			case "September": month = 9; break;
			case "October": month = 10; break;
			case "November": month = 11; break;
			case "December": month = 12; break;
			default: month = 0; break;
		}
		return month;
	}
	
	public static boolean isGarbage(String string){
		if(string.startsWith("First") ||
			string.startsWith("Second") ||
			string.startsWith("Third") ||
			string.startsWith("Fourth") ||
			string.startsWith("Fifth"))
			return true;
		else
			return false;
	}
	
	public static String[] books(String string){
		List<String> bookList = new ArrayList<String>();
		String[] books = new String[3];
		if(string.contains("Cooper"))
			bookList.add("Cooper");
		if(string.contains("White"))
			bookList.add("White");
		if(string.contains("Southern Harmony"))
			bookList.add("Southern Harmony");
		if(string.contains("Georgian Harmony"))
			bookList.add("Georgian Harmony");
		if(string.contains("Colored Sacred Harp"))
			bookList.add("Colored Sacred Harp");
		if(string.contains("Christian Harmony"))
			bookList.add("Christian Harmony");
		if(string.contains("Shenandoah Harmony"))
			bookList.add("Shenandoah Harmony");
		if(bookList.size() == 0 || string.contains("1991 Edition") || string.contains("Denson"))
			bookList.add("Denson");
		
		if(bookList.size() > 3)
			System.out.println("Warning: More than three books.");
		else
			bookList.toArray(books);
		return books;
	}
		
}