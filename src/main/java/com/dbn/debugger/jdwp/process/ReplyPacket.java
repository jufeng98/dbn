package com.dbn.debugger.jdwp.process;

public class ReplyPacket extends Packet {

	public short getErrorCode() {
		return (short) (getErrorCommand1() <<  8 + getErrorCommand2());
	}


	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		
		builder.append(", errorCode: ");
		builder.append(getErrorCode());
		builder.append(", payload size: ");
		builder.append(getData().length);
		return builder.toString();
	}

}
