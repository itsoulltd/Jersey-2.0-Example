package com.it.soul.lab.service.models;

import java.util.ArrayList;
import java.util.List;

public class PassengerList {
	
	public PassengerList() {
		super();
	}

	private List<Passenger> passengerList = new ArrayList<Passenger>();
	 
    public List<Passenger> getPassengerList() {
        return passengerList;
    }
 
    public void setPassengerList(List<Passenger> passengerList) {
    	if (passengerList == null) {return;}
        this.passengerList = passengerList;
    }
    
    public PassengerList add(Passenger pass) {
    	if (pass == null || passengerList.contains(pass) == true) {return this;}
    	passengerList.add(pass);
    	return this;
    }
}
