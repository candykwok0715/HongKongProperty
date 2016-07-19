
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class SeekHome extends javax.swing.JDialog {
    public static String location  ; //UK, CN, HK
    public static String characterSet;  //en, tc, sc 
    public static String Locale_region ; 
    public static String Locale_language ; 
    public static String measurement; // si (CN), ft (UK,HK)
    public static double ChangeRate_RNB = 1.266;
    public static double ChangeRate_GBP = 12.3038;
    public static double ChangeRate_measurement = 0.0929030;
    
    
    public SeekHome(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
    }                    
    public static void main(String args[]) {
    	//Starting Point
    	if (args.length == 1) {
    		location  = new String(args[0]);
    		if ("CN".equals(location)){
    			characterSet = new String("sc");
    			measurement= new String("si");
    		}
    		if ("HK".equals(location)){
    			characterSet = new String("tc");
    			measurement= new String("ft");
    		}
    		if ("UK".equals(location)){
    			characterSet = new String("en");
    			measurement= new String("ft");
    		}
    	}
    	else if (args.length == 2) {
    		location  = new String(args[0]);
    		characterSet = new String(args[1]);
    		
    		// set Measurement
    		if ("CN".equals(location))
    			measurement= new String("si");
    		else 
    			measurement= new String("ft");
    	}
    	else if (args.length == 3) {
    		location = new String(args[0]);
    		characterSet = new String(args[1]);
    		measurement = new String(args[2]);
    	}
    	else{
    		location = new String("HK");
        	characterSet = new String("tc");
        	measurement= new String("ft");
    	}
    	// Set Locale_region and Locale_language
    	if ("en".equals(characterSet)){
    		Locale_region = new String("en"); 
    		Locale_language = new String("UK"); 
    	}
    	if ("tc".equals(characterSet)){
    		Locale_region = new String("zh"); 
    		Locale_language = new String("HK"); 
    	}
    	if ("sc".equals(characterSet)){
    		Locale_region = new String("zh"); 
    		Locale_language = new String("CN"); 
    	}
    	
          try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SeekHome dialog = new SeekHome(new javax.swing.JFrame(), true);
                dialog.setVisible(true);
            }
        });
    }
   
    
    public String getProgerties(String name){
		try{
			Locale  currentLocale = new Locale(Locale_region, Locale_language);
			ResourceBundle messages = ResourceBundle.getBundle("Seekhome",currentLocale);	     
	    	return new String(messages.getString(name).getBytes("ISO8859-1"),"UTF-8");
	     } catch (Exception e){
	    	 e.printStackTrace();
	    	 return null;
	     }
	}
    public String getChineseProgerties(String name){
		try{
			Locale  currentLocale = new Locale("zh", "HK");
			ResourceBundle messages = ResourceBundle.getBundle("Seekhome",currentLocale);	     
	    	return new String(messages.getString(name).getBytes("ISO8859-1"),"UTF-8");
	     } catch (Exception e){
	    	 e.printStackTrace();
	    	 return null;
	     }
	}
    
	public static String getKeyFormValue(String matchvalue,Boolean DBorNot){
	 	String matchedKey ="";
	 	String fileName="";
		try {
			if (DBorNot){// true= value from DB 
				fileName ="Seekhome_zh_HK.properties";
			}
			else{ // false = value from properties
				fileName = "Seekhome_"+Locale_region+"_"+Locale_language+".properties";
			}
			File file = new File(fileName);
			FileInputStream fileInput = new FileInputStream(file);
			
	        InputStreamReader isr = new InputStreamReader(fileInput, "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
			
			Properties properties = new Properties();
			properties.load(br);

			Enumeration enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				if (matchvalue.equals(value)){
					matchedKey=key;
					break;
				}
			}
			br.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return matchedKey;
	}
	

	public String[] setSearchCriteria() {
		String[] filter;
		filter = new String[9];
		for (int i = 0; i < filter.length; i++) {
			filter[i] = null;
		}
		
		//Refer. Number
		if (!"".equals(jTextField1.getText())){
			filter[0] = jTextField1.getText();
		}
	
		//set sale and rent
		if (!this.jCheckBox1.isSelected() && !this.jCheckBox2.isSelected()) { //no sale and rent
			filter[1]=null;
			filter[2]=null;
		}
		
		if (this.jCheckBox1.isSelected() && !this.jCheckBox2.isSelected()){ // only sale
			filter[1] = "TRUE";
			filter[2]=null;
		}

		if (!this.jCheckBox1.isSelected() && this.jCheckBox2.isSelected()){ // only rent
			filter[1]=null;
			filter[2] = "TRUE";
		}
		if (this.jCheckBox1.isSelected() && this.jCheckBox2.isSelected()) { //check sale and rent
			filter[1]=null;
			filter[2]=null;
		}

		// Pass area
		if (this.jComboBox1.getSelectedItem() != null) {
			if ("tc".equals(characterSet))
				filter[3] = jComboBox1.getSelectedItem().toString();
			else
				filter[3] = getChineseProgerties(getKeyFormValue(jComboBox1.getSelectedItem().toString(),false));
		}

		// Pass district
		if (this.jComboBox2.getSelectedItem() != null) {
			if ("tc".equals(characterSet))
				filter[4] = jComboBox2.getSelectedItem().toString();
			else
				filter[4] = getChineseProgerties(getKeyFormValue(jComboBox2.getSelectedItem().toString(),false));
		}

		// price
		if (this.jComboBox5.getSelectedItem() != null || this.jComboBox8.getSelectedItem() != null) {
			if (this.jComboBox5.getSelectedItem() != null && this.jComboBox8.getSelectedItem() == null) {
				filter[5] = CNY_BGPtoHKD(this.jComboBox5.getSelectedItem().toString())+",100000000000000000000000000";					
			}
			if (this.jComboBox5.getSelectedItem() == null && this.jComboBox8.getSelectedItem() != null) {
				if ((this.jComboBox8.getSelectedItem().toString()).contains(">")){
					filter[5] = "0,100000000000000000000000000";
				}else{
					filter[5] = "0,"+CNY_BGPtoHKD(this.jComboBox8.getSelectedItem().toString());
				}
			}
			if (this.jComboBox5.getSelectedItem() != null && this.jComboBox8.getSelectedItem() != null) {
				if ((this.jComboBox8.getSelectedItem().toString()).contains(">")){
					filter[5] = CNY_BGPtoHKD(jComboBox5.getSelectedItem().toString())+",100000000000000000000000000";
				}else{
					if (Integer.parseInt(this.jComboBox5.getSelectedItem().toString())
						<= Integer.parseInt(this.jComboBox8.getSelectedItem().toString())){
						filter[5] = CNY_BGPtoHKD(jComboBox5.getSelectedItem().toString())+","+CNY_BGPtoHKD(this.jComboBox8.getSelectedItem().toString());
					}
				}
			}
//			System.out.println("filter[5]:"+filter[5]);
		}
		
		// rent_price
		if (this.jComboBox10.getSelectedItem() != null || this.jComboBox11.getSelectedItem() != null) {
			if (this.jComboBox10.getSelectedItem() != null && this.jComboBox11.getSelectedItem() == null) {
					filter[6] = CNY_BGPtoHKD(this.jComboBox10.getSelectedItem().toString())+",100000000000000000000000000";
			}
			if (this.jComboBox10.getSelectedItem() == null && this.jComboBox11.getSelectedItem() != null) {
				if ((this.jComboBox11.getSelectedItem().toString()).contains(">")){
					filter[6] = "0,100000000000000000000000000";
				}else{
						filter[6] = "0,"+CNY_BGPtoHKD(this.jComboBox11.getSelectedItem().toString());
				}	
			}
			if (this.jComboBox10.getSelectedItem() != null && this.jComboBox11.getSelectedItem() != null) {
				if ((this.jComboBox11.getSelectedItem().toString()).contains(">")){
						filter[6] = CNY_BGPtoHKD(jComboBox10.getSelectedItem().toString())+",100000000000000000000000000";
				}else{
					if (Integer.parseInt(this.jComboBox10.getSelectedItem().toString())
						<= Integer.parseInt(this.jComboBox11.getSelectedItem().toString())){
							filter[6] = CNY_BGPtoHKD(jComboBox10.getSelectedItem().toString())+","+CNY_BGPtoHKD(this.jComboBox11.getSelectedItem().toString());
					}
				}
			}
//			System.out.println("filter[6]:"+filter[6]);
		}
		
		//sa
		if (this.jComboBox3.getSelectedItem() != null || this.jComboBox7.getSelectedItem() != null) {
			if (this.jComboBox3.getSelectedItem() != null && this.jComboBox7.getSelectedItem() == null) {
				filter[7] = SiToFt(this.jComboBox3.getSelectedItem().toString())+",1000000000000000000";
			}
			if (this.jComboBox3.getSelectedItem() == null && this.jComboBox7.getSelectedItem() != null) {
				if (this.jComboBox7.getSelectedItem().toString().contains(">")){
					filter[7] = "0,1000000000000000000";
				}else{
					filter[7] = "0,"+SiToFt(this.jComboBox7.getSelectedItem().toString());
				}
			}
			if (this.jComboBox3.getSelectedItem() != null && this.jComboBox7.getSelectedItem() != null) {
				if (this.jComboBox7.getSelectedItem().toString().contains(">")){
					filter[7] = SiToFt(jComboBox3.getSelectedItem().toString())+",1000000000000000000";	
				}else{
					if (Integer.parseInt(this.jComboBox3.getSelectedItem().toString())
							<= Integer.parseInt(this.jComboBox7.getSelectedItem().toString())){
						filter[7] = SiToFt(jComboBox3.getSelectedItem().toString())+","+SiToFt(this.jComboBox7.getSelectedItem().toString());
					}
				}
			}
//			System.out.println("filter[7]:"+filter[7]);
		}
		
		//dfa
		if (this.jComboBox4.getSelectedItem() != null || this.jComboBox9.getSelectedItem() != null) {
			if (this.jComboBox4.getSelectedItem() != null && this.jComboBox9.getSelectedItem() == null) {
					filter[8] = SiToFt(this.jComboBox4.getSelectedItem().toString())+",1000000000000000000";					
			}
			if (this.jComboBox4.getSelectedItem() == null && this.jComboBox9.getSelectedItem() != null) {
				if (this.jComboBox9.getSelectedItem().toString().contains(">")){
					filter[8] = "0,1000000000000000000";
				}else{
					filter[8] = "0,"+SiToFt(this.jComboBox9.getSelectedItem().toString());
				}
			}
			if (this.jComboBox4.getSelectedItem() != null && this.jComboBox9.getSelectedItem() != null) {
				if (this.jComboBox9.getSelectedItem().toString().contains(">")){
					filter[8] = SiToFt(jComboBox4.getSelectedItem().toString())+",1000000000000000000";
				}else{
					if (Integer.parseInt(this.jComboBox4.getSelectedItem().toString())
						<= Integer.parseInt(this.jComboBox9.getSelectedItem().toString())){
						filter[8] = SiToFt(jComboBox4.getSelectedItem().toString())+","+SiToFt(this.jComboBox9.getSelectedItem().toString());
					}
				}
			}
//			System.out.println("filter[8]:"+filter[8]);
		}	
		return filter;
	}
	
    public String writeRecord(List<Record>recordListToPrint){
    	String result="";
   	for (int i=0; i < recordListToPrint.size(); i++){
   		
    		result+=getProgerties("ReferenceNo")+": ";
    		result+=recordListToPrint.get(i).getId()+"\n";
    		result+=getProgerties("area")+": ";
        	result+=getProgerties(getKeyFormValue(recordListToPrint.get(i).getArea(),true))+"\n";
    		result+=getProgerties("district")+": ";
        	result+=getProgerties(getKeyFormValue(recordListToPrint.get(i).getDistrict(),true))+"\n";
    		result+=getProgerties("address")+": ";
        	result+=getProgerties(getKeyFormValue(recordListToPrint.get(i).getAddress(),true))+"\n";
       		
			if (!"".equals(recordListToPrint.get(i).getPrice())) {
				result += getProgerties(location+"_price") + ": ";
				result += HKDtoCNY_BGP(recordListToPrint.get(i).getPrice())+ "\n";
			}
        	
			if (!"".equals(recordListToPrint.get(i).getRent_price())) {
				result += getProgerties(location+"_rent_price") + ": ";
				result += HKDtoCNY_BGP(recordListToPrint.get(i).getRent_price())+"\n";
			}
			
    		result+=getProgerties("sa")+getProgerties(measurement)+": ";
    	   	result+=FtToSi(recordListToPrint.get(i).getSa())+"\n";
			
    		result+=getProgerties("dfa")+getProgerties(measurement)+": ";
        	result+=FtToSi(recordListToPrint.get(i).getGfa())+"\n";

    		result+=getProgerties("bedroom")+": ";
    		result+=recordListToPrint.get(i).getBedroom()+"\n";
    		result+=getProgerties("livingroom")+": ";
    		result+=recordListToPrint.get(i).getLiving_room()+"\n";
    		result+=getProgerties(location+"_releaseDate")+": ";
    		if ("UK".equals(location))
    			result+=HKDateTimeToUK(recordListToPrint.get(i).getReleaseDate())+"\n";
    		else
    			result+=recordListToPrint.get(i).getReleaseDate()+"\n";
    		
    		if (i!= recordListToPrint.size()-1){
    			result += "_________________________________________________\n";
    		}
    	}
    	
    	if ("".equals(result)){
    		result=getProgerties("NoResult");
    	}
    	return result;
    }
    
    public String FtToSi(String area) {
    	if ("si".endsWith(measurement)){
    		area= String.valueOf(Double.parseDouble(area)*ChangeRate_measurement); // ft -->si
    	}
        return format0(area);  
    }
    
    public String SiToFt(String area) {  
    	if ("si".endsWith(measurement)){
    		area= String.valueOf(Double.parseDouble(area)/ChangeRate_measurement); // si -->ft
    	}
        return format0(area);  
    }

    public String format0(String input) {  
    	double value = Double.parseDouble(input);
        DecimalFormat df = new DecimalFormat("0");  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        return df.format(value);  
    }
    
    public String format2(String input) {  
    	double value = Double.parseDouble(input);
        DecimalFormat df = new DecimalFormat("0.00");  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        return df.format(value);  
    }
    
    public String HKDtoCNY_BGP (String HKD){
    	if ("CN".equals(location))
    		HKD=  String.valueOf(Double.parseDouble(HKD)/ChangeRate_RNB); //HKD ->RNB
    	if ("UK".equals(location))
    		HKD = String.valueOf(Double.parseDouble(HKD)/ChangeRate_GBP); //HKD ->GBP
    	return format2(HKD);
    }
    
    public String CNY_BGPtoHKD (String CNY_BGP){
    	if ("CN".equals(location))
    		CNY_BGP= String.valueOf(Double.parseDouble(CNY_BGP)*ChangeRate_RNB); //RNB -> HKD
    	if ("UK".equals(location))
    		CNY_BGP= String.valueOf(Double.parseDouble(CNY_BGP)*ChangeRate_GBP); //GBP -> HKD
    	return CNY_BGP;
    }
	public String HKDateTimeToUK(String HKDateTime){
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String CN_UK_DateTime ="";
		try {
			Date date = ft.parse(HKDateTime);
//			System.out.println(ft.format(date));
			ft.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));
			CN_UK_DateTime = ft.format(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return CN_UK_DateTime;
	}
	
	// submit Event Listener.
    private void submit(java.awt.event.ActionEvent evt) {                                           
		CsvToRecord csvToRecord = new CsvToRecord();
		csvToRecord.setRecord();//Must use this function to save CSV to Record
		
		// pass search criteria
		String[] filter = setSearchCriteria();
		
		//Do the searching
		List<Record> resultList =csvToRecord.searchRecord(filter);

		//Show result in Text Area
		String result =writeRecord(resultList);
		 jTextArea1.setText(result); 
		 jTextArea1.setLineWrap(true);
		 jTextArea1.setWrapStyleWord(true);
		 // clear all records 
		csvToRecord.clearAllList();

    }
    
    // reset Event Listener
    private void resetAllValue(java.awt.event.ActionEvent evt) {                                           
        jComboBox1.setSelectedItem(null);  // area
        jComboBox2.setSelectedItem(null);  // district
        jComboBox3.setSelectedItem(null);  //sa_1
        jComboBox4.setSelectedItem(null);  //dfa_1 
        jComboBox5.setSelectedItem(null);  //price_1
        jComboBox7.setSelectedItem(null);  //sa_2
        jComboBox8.setSelectedItem(null);  //price_2
        jComboBox9.setSelectedItem(null);  //dfa_2
        jComboBox10.setSelectedItem(null); //rent_1
        jComboBox11.setSelectedItem(null); //rent_2
        jTextField1.setText(null);         //Refer No.
        jCheckBox1.setSelected(false);     //buy
        jCheckBox2.setSelected(false);     //rent
        jTextArea1.setText("");            //Result
    }     

	//Set UI
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jComboBox11 = new javax.swing.JComboBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(getProgerties("search")));

        jLabel1.setText(getProgerties("area"));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties("hk") ,getProgerties("kln") ,getProgerties("nt") ,getProgerties("is") }));

        jLabel2.setText(getProgerties("district"));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null,getProgerties("district_1") ,getProgerties("district_2") ,getProgerties("district_3") ,getProgerties("district_4") ,getProgerties("district_5") ,getProgerties("district_6") ,getProgerties("district_7") ,getProgerties("district_8") ,getProgerties("district_9") ,getProgerties("district_10") ,getProgerties("district_11") ,getProgerties("district_12") ,getProgerties("district_13") ,getProgerties("district_14") ,getProgerties("district_15") ,getProgerties("district_16") ,getProgerties("district_17") ,getProgerties("district_18") ,getProgerties("district_19") ,getProgerties("district_20") ,getProgerties("district_21") ,getProgerties("district_22") ,getProgerties("district_23") ,getProgerties("district_24") ,getProgerties("district_25") ,getProgerties("district_26") ,getProgerties("district_27") ,getProgerties("district_28") ,getProgerties("district_29") ,getProgerties("district_30") ,getProgerties("district_31") ,getProgerties("district_32") ,getProgerties("district_33") ,getProgerties("district_34") ,getProgerties("district_35") ,getProgerties("district_36") ,getProgerties("district_37") ,getProgerties("district_38") ,getProgerties("district_39") ,getProgerties("district_40") ,getProgerties("district_41") ,getProgerties("district_42") ,getProgerties("district_43") ,getProgerties("district_44") ,getProgerties("district_45") ,getProgerties("district_46") ,getProgerties("district_47") ,getProgerties("district_48") ,getProgerties("district_49") ,getProgerties("district_50") ,getProgerties("district_51") ,getProgerties("district_52") ,getProgerties("district_53") ,getProgerties("district_54") ,getProgerties("district_55") ,getProgerties("district_56") ,getProgerties("district_57") ,getProgerties("district_58") ,getProgerties("district_59") ,getProgerties("district_60") ,getProgerties("district_61") ,getProgerties("district_62") ,getProgerties("district_63") ,getProgerties("district_64") ,getProgerties("district_65") ,getProgerties("district_66") ,getProgerties("district_67") ,getProgerties("district_68") ,getProgerties("district_69") ,getProgerties("district_70") ,getProgerties("district_71") ,getProgerties("district_72") ,getProgerties("district_73") ,getProgerties("district_74") ,getProgerties("district_75") ,getProgerties("district_76") ,getProgerties("district_77") ,getProgerties("district_78") ,getProgerties("district_79") ,getProgerties("district_80") ,getProgerties("district_81") ,getProgerties("district_82") ,getProgerties("district_83") ,getProgerties("district_84") ,getProgerties("district_85") ,getProgerties("district_86") ,getProgerties("district_87") ,getProgerties("district_88") ,getProgerties("district_89") ,getProgerties("district_90") ,getProgerties("district_91") ,getProgerties("district_92") ,getProgerties("district_93") ,getProgerties("district_94") ,getProgerties("district_95")  }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties(measurement+"_sa_1"),getProgerties(measurement+"_sa_2"),getProgerties(measurement+"_sa_3"),getProgerties(measurement+"_sa_4"),getProgerties(measurement+"_sa_5"),getProgerties(measurement+"_sa_6"),getProgerties(measurement+"_sa_7"),getProgerties(measurement+"_sa_8"),getProgerties(measurement+"_sa_9"),getProgerties(measurement+"_sa_10") }));

        jLabel3.setText(getProgerties("sa")+getProgerties(measurement));

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties(measurement+"_dfa_1"),getProgerties(measurement+"_dfa_2"),getProgerties(measurement+"_dfa_3"),getProgerties(measurement+"_dfa_4"),getProgerties(measurement+"_dfa_5"),getProgerties(measurement+"_dfa_6"),getProgerties(measurement+"_dfa_7"),getProgerties(measurement+"_dfa_8"),getProgerties(measurement+"_dfa_9"),getProgerties(measurement+"_dfa_10")}));

        jLabel4.setText(getProgerties("dfa")+getProgerties(measurement));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null,getProgerties(location+"_price_1"),getProgerties(location+"_price_2"),getProgerties(location+"_price_3"),getProgerties(location+"_price_4"),getProgerties(location+"_price_5"),getProgerties(location+"_price_6"),getProgerties(location+"_price_7"),getProgerties(location+"_price_8"),getProgerties(location+"_price_9"),getProgerties(location+"_price_10"),getProgerties(location+"_price_11"),getProgerties(location+"_price_12"),getProgerties(location+"_price_13") }));

        jLabel5.setText(getProgerties(location+"_price"));

        jTextField1.setText(null);

        jLabel6.setText(getProgerties("refNo"));

        jButton2.setText(getProgerties("reset"));

        jButton1.setText(getProgerties("search"));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] {null,getProgerties(measurement+"_sa_2"),getProgerties(measurement+"_sa_3"),getProgerties(measurement+"_sa_4"),getProgerties(measurement+"_sa_5"),getProgerties(measurement+"_sa_6"),getProgerties(measurement+"_sa_7"),getProgerties(measurement+"_sa_8"),getProgerties(measurement+"_sa_9"),getProgerties(measurement+"_sa_10"),getProgerties(measurement+"_sa_11") }));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null,getProgerties(location+"_price_2"),getProgerties(location+"_price_3"),getProgerties(location+"_price_4"),getProgerties(location+"_price_5"),getProgerties(location+"_price_6"),getProgerties(location+"_price_7"),getProgerties(location+"_price_8"),getProgerties(location+"_price_9"),getProgerties(location+"_price_10"),getProgerties(location+"_price_11"),getProgerties(location+"_price_12"),getProgerties(location+"_price_13"),getProgerties(location+"_price_14") }));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties(measurement+"_dfa_2"),getProgerties(measurement+"_dfa_3"),getProgerties(measurement+"_dfa_4"),getProgerties(measurement+"_dfa_5"),getProgerties(measurement+"_dfa_6"),getProgerties(measurement+"_dfa_7"),getProgerties(measurement+"_dfa_8"),getProgerties(measurement+"_dfa_9"),getProgerties(measurement+"_dfa_10"),getProgerties(measurement+"_dfa_11")}));

        jLabel8.setText(getProgerties(location+"_rent_price"));

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties(location+"_rent_1"),getProgerties(location+"_rent_2"),getProgerties(location+"_rent_3"),getProgerties(location+"_rent_4"),getProgerties(location+"_rent_5"),getProgerties(location+"_rent_6"),getProgerties(location+"_rent_7"),getProgerties(location+"_rent_8"),getProgerties(location+"_rent_9"),getProgerties(location+"_rent_10"),getProgerties(location+"_rent_11"),getProgerties(location+"_rent_12") }));

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null, getProgerties(location+"_rent_2"),getProgerties(location+"_rent_3"),getProgerties(location+"_rent_4"),getProgerties(location+"_rent_5"),getProgerties(location+"_rent_6"),getProgerties(location+"_rent_7"),getProgerties(location+"_rent_8"),getProgerties(location+"_rent_9"),getProgerties(location+"_rent_10"),getProgerties(location+"_rent_11"),getProgerties(location+"_rent_12"),getProgerties(location+"_rent_13") }));

        jCheckBox1.setText(getProgerties("buy"));
        
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit(evt);
            }
        });

        jCheckBox2.setText(getProgerties("rent"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	resetAllValue(evt);
            }
        });
        

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel6)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel5)
                                .addComponent(jLabel8))
                            .addGap(30, 30, 30)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jComboBox10, javax.swing.GroupLayout.Alignment.LEADING, 0, 96, Short.MAX_VALUE)
                                            .addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jCheckBox1))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jCheckBox2)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jComboBox7, 0, 96, Short.MAX_VALUE)
                                            .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jComboBox9, 0, 96, Short.MAX_VALUE)
                                            .addComponent(jComboBox11, 0, 96, Short.MAX_VALUE))))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(106, 106, 106)
                            .addComponent(jButton1)
                            .addGap(78, 78, 78)
                            .addComponent(jButton2))))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(getProgerties("result")));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
