
import java.util.ArrayList;
import java.util.List;
import java.io.*;


public class CsvToRecord {

	private static final List<Record> recordList = new ArrayList<Record>();
	private static final List<Record> searchRecordList = new ArrayList<Record>();
	
	public void clearAllList(){
		searchRecordList.clear();
		recordList.clear();
	}
	
    public void setRecord() { 
    	try {  
	        String fileName = "DB_Big5.csv";
	        FileInputStream fis = new FileInputStream(fileName);
	        InputStreamReader isr = new InputStreamReader(fis, "BIG5");
	        BufferedReader br = new BufferedReader(isr);
        	String line = "";
        	String splitBy = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        	
        	while ((line = br.readLine()) != null) {
        		String[] tokens = line.split(splitBy);
        		Record record = new Record();
        		int i;
                for(i = 0; i<tokens.length; i++){
                	String temp = trimDoubleQuotes(tokens[i]);
                	tokens[i] = temp;
                }
                record.setId(tokens[0]);
        		record.setSale(tokens[1]);
        		record.setRent(tokens[2]);
        		record.setArea(tokens[3]);
        		record.setDistrict(tokens[4]);
        		record.setPrice(tokens[5]);
        		record.setRent_price(tokens[6]);
        		record.setSa(tokens[7]);
        		record.setGfa(tokens[8]);
        		record.setAddress(tokens[9]);
        		record.setBedroom(tokens[10]);
        		record.setLiving_room(tokens[11]);
        		record.setReleaseDate(tokens[12]);
        		recordList.add(record);                
        	}
        	br.close();        
    	}catch (Exception e) {
        	e.printStackTrace();
        } 
    }
    
    /*
     * Convert Big5 to UTF8 CSV
     */
    
    public List<Record> getRecord(){
    	//this.printRecordList(recordList);//For DelBug
    	return recordList;
    }
    
    /*
     * For Debug & Test in Console
     */
    public void printRecordList(List<Record>recordListToPrint){
    	for (int i=0; i < recordListToPrint.size(); i++){
    		System.out.println("Record "+
    				recordListToPrint.get(i).getId()+"|"+
    				recordListToPrint.get(i).getSale()+"|"+
    				recordListToPrint.get(i).getRent()+"|"+
    				recordListToPrint.get(i).getArea()+"|"+
    				recordListToPrint.get(i).getDistrict()+"|"+
    				recordListToPrint.get(i).getPrice()+"|"+
    				recordListToPrint.get(i).getRent_price()+"|"+
    				recordListToPrint.get(i).getSa()+"|"+
    				recordListToPrint.get(i).getGfa()+"|"+
    				recordListToPrint.get(i).getAddress()+"|"+
    				recordListToPrint.get(i).getBedroom()+"|"+
    				recordListToPrint.get(i).getLiving_room()+"|"+
    				recordListToPrint.get(i).getReleaseDate()+
    				"");
    	}
    }
    
    public List<Record> searchRecord(String[] filter){
    	for (int i=0; i < recordList.size(); i++){
    		Record searchRecord = new Record();
    		searchRecord.setId(recordList.get(i).getId());
    		searchRecord.setSale(recordList.get(i).getSale());
    		searchRecord.setRent(recordList.get(i).getRent());
    		searchRecord.setArea(recordList.get(i).getArea());
    		searchRecord.setDistrict(recordList.get(i).getDistrict());
    		searchRecord.setPrice(recordList.get(i).getPrice());
    		searchRecord.setRent_price(recordList.get(i).getRent_price());
    		searchRecord.setSa(recordList.get(i).getSa());
    		searchRecord.setGfa(recordList.get(i).getGfa());
    		searchRecord.setAddress(recordList.get(i).getAddress());
    		searchRecord.setBedroom(recordList.get(i).getBedroom());
    		searchRecord.setLiving_room(recordList.get(i).getLiving_room());
    		searchRecord.setReleaseDate(recordList.get(i).getReleaseDate());
    		
    		/*
    		 * Refer. Number
    		 */
    		if(filter[0]!=null){
    			if(!this.checkInString(filter[0].toString(), recordList.get(i).getId().toString())){
    				continue;
    			}
    		}
    		/*
    		 * Drop down menu
    		 */
    		if(filter[1]!=null && !recordList.get(i).getSale().toString().equals(new String(filter[1]))){
    			continue;
    		}
    		if(filter[2]!=null && !recordList.get(i).getRent().toString().equals(new String(filter[2]))){
    			continue;
    		}
    		if(filter[3]!=null && !recordList.get(i).getArea().toString().equals(new String(filter[3]))){
    			continue;
    		}
    		if(filter[4]!=null && !recordList.get(i).getDistrict().toString().equals(new String(filter[4]))){
    			continue;
    		}
    		if(filter[5]!=null){
    			if(recordList.get(i).getPrice().toString().length()==0 || !this.checkInRange(filter[5], recordList.get(i).getPrice().toString())){
    				continue;
    			}
    		}
    		if(filter[6]!=null){
    			if(recordList.get(i).getRent_price().toString().length()==0 || !this.checkInRange(filter[6], recordList.get(i).getRent_price().toString())){
    				continue;
    			}
    		}
    		if(filter[7]!=null){
    			if(recordList.get(i).getSa().toString().length()==0 || !this.checkInRange(filter[7], recordList.get(i).getSa().toString())){
    				continue;
    			}
    		}
    		if(filter[8]!=null){
    			if(recordList.get(i).getGfa().toString().length()==0 || !this.checkInRange(filter[8], recordList.get(i).getGfa().toString())){
    				continue;
    			}
    		}
    		
    		searchRecordList.add(searchRecord);
    	}
//    	printRecordList(searchRecordList);//For DelBug
    	return searchRecordList;
    }
    
    private static String trimDoubleQuotes(String text) {
        int textLength = text.length();
        if (textLength >= 2 && text.charAt(0) == '"' && text.charAt(textLength - 1) == '"') {
          return text.substring(1, textLength - 1);
        }
        return text;
      }
    
    private boolean checkInRange(String filter,String target){
    	String temp = filter;
		String[] range = temp.split(",");
		double lower = Double.parseDouble(range[0]);
		double upper = Double.parseDouble(range[1]);
		double value = Double.parseDouble(target);
		
		if(value>=lower && value<=upper){
			return true;//in range
		}else{
			return false;//not in range
		}
    }
    
    /*
     * Make sure filter match target with AND
     */
    private boolean checkInString(String key,String record){
		return(record.contains(key));
    }
}