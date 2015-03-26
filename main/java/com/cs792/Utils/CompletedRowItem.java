package com.cs792.Utils;

/**
 * Created by sudeelet on 3/18/2015.
 */
public class CompletedRowItem {

	private int Patient_photo;
	private String Patient_fullName;
	private String Patient_Completestatus;
	private String Patient_assignedTo;
	private String Patient_completeDate;
	private String patient_id;

	public CompletedRowItem(String id,int photo,String name, String status, String assignedTo, String date) {
		this.patient_id=id;
		this.Patient_photo=photo;
		this.Patient_fullName=name;
		this.Patient_Completestatus=status;
		this.Patient_assignedTo = assignedTo;
		this.Patient_completeDate=date;
	}

	public String getPatient_id() { return this.patient_id; }
	public void setPatient_id(String id) { this.patient_id = id; }


	public int getPatient_photo() {return this.Patient_photo; }
	public void setPatient_photo(int photo) { this.Patient_photo=photo; }

	public String getAssignedTo() { return  this.Patient_assignedTo; }
	public void setAssignedTo(String complete) { this.Patient_assignedTo = complete; }

	public String getName() { return this.Patient_fullName; }
	public void setName(String name) { this.Patient_fullName=name;}

	public String getStatus() { return this.Patient_Completestatus; }
	public void setStatus(String status) { this.Patient_Completestatus=status; }

	public String getDate() { return this.Patient_completeDate; }
	public void setDate(String date) { this.Patient_completeDate=date; }

}
