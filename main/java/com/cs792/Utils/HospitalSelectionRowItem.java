package com.cs792.Utils;

import com.cs792.CCAC.HospitalSelection;

/**
 * Created by sudeelet on 3/17/2015.
 */
public class HospitalSelectionRowItem {

	private String hospital_name;
	private int hospital_id;
	private int beds;
	private int distance;
	private String interpreter;
	private String wheelChair;

	public HospitalSelectionRowItem(){

	}

	public HospitalSelectionRowItem(int hospital_id,String hospital_name,int beds,int distance,String interpreter,String wheelChair) {
		this.hospital_id = hospital_id;
		this.hospital_name= hospital_name;
		this.beds = beds;
		this.distance = distance;
		this.interpreter = interpreter;
		this.wheelChair = wheelChair;
	}

	public String getHospital_name() { return this.hospital_name;}
	public void setHospital_name(String hospital_name) { this.hospital_name = hospital_name; }


	public int getHospital_id() { return this.hospital_id; }
	public void setHospital_id(int hospital_id) { this.hospital_id = hospital_id; }

	public int getBeds() { return this.beds; }
	public void setBeds(int beds) { this.beds = beds; }

	public int getDistance() { return this.distance; }
	public void setDistance(int distance) { this.distance = distance; }

	public String getWheelChair() { return this.wheelChair; }
	public void setWheelChair(String wheelChair) { this.wheelChair = wheelChair; }

	public String getInterpreter() { return this.interpreter; }
	public void setInterpreter(String interpreter) { this.interpreter = interpreter; }

}
