
public class Record {
	private String id;
	private String sale;
	private String rent;
	private String area;
	private String district;
	private String price;
	private String rent_price;
	private String sa;
	private String gfa;
	private String address;
	private String bedroom;
	private String living_room;
	private String release_date;
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		id = id.replace("[", "");
		this.id = id;
	}
	
	public String getSale(){
		return sale;
	}
	public void setSale(String sale){
		this.sale = sale;
	}
	
	public String getRent(){
		return rent;
	}
	public void setRent(String rent){
		this.rent = rent;
	}
	
	public String getArea(){
		return area;
	}
	public void setArea(String area){
		this.area = area;
	}
	
	public String getDistrict(){
		return district;
	}
	public void setDistrict(String district){
		this.district = district;
	}
	
	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}
	
	public String getRent_price(){
		return rent_price;
	}
	public void setRent_price(String rent_price){
		this.rent_price = rent_price;
	}
	
	public String getSa(){
		return sa;
	}
	public void setSa(String sa){
		this.sa = sa;
	}
	
	public String getGfa(){
		return gfa;
	}
	public void setGfa(String gfa){
		this.gfa = gfa;
	}
	
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	
	public String getBedroom(){
		return bedroom;
	}
	public void setBedroom(String bedroom){
		this.bedroom = bedroom;
	}
	
	public String getLiving_room(){
		return living_room;
	}
	public void setLiving_room(String living_room){
		this.living_room = living_room;
	}
	
	public String getReleaseDate(){
		return release_date;
	}
	public void setReleaseDate(String release_date){
		release_date = release_date.replace("]", "");
		this.release_date = release_date;
	}
}
