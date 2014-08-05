package com.josketres;

public class Device {

	public final String id;
	public final String name;
	public final String borrowerName;
	public final DeviceStatus status;

	public Device(String id, String name, DeviceStatus status,
			String borrowerName) {
		this.id = id;
		this.name = name;
		this.status = status;
		this.borrowerName = borrowerName;
	}

}
