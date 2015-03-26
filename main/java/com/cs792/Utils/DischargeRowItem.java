package com.cs792.Utils;

/**
 * Created by sudeelet on 3/15/2015.
 */
public class DischargeRowItem {
	private int Patient_photo;
	private String Patient_fullName;
	private String Patient_Completestatus;
	private String Patient_submitDate;
	private String patient_id;

	public DischargeRowItem(String id,int photo,String name, String status, String date) {
		this.patient_id=id;
		this.Patient_photo=photo;
		this.Patient_fullName=name;
		this.Patient_Completestatus=status;
		this.Patient_submitDate=date;
	}

	public String getPatient_id() { return this.patient_id; }
	public void setPatient_id(String id) { this.patient_id = id; }


	public int getPatient_photo() {return this.Patient_photo; }
	public void setPatient_photo(int photo) { this.Patient_photo=photo; }


	public String getName() { return this.Patient_fullName; }
	public void setName(String name) { this.Patient_fullName=name;}

	public String getStatus() { return this.Patient_Completestatus; }
	public void setStatus(String status) { this.Patient_Completestatus=status; }

	public String getDate() { return this.Patient_submitDate; }
	public void setDate(String date) { this.Patient_submitDate=date; }

}
