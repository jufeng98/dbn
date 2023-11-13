package com.dbn.debugger.jdwp.process;

public class CommandPacket extends Packet {

	public CommandPacket() {
		super();
	}

	public byte getCommandSet() {
		return getErrorCommand1();
	}

	public byte getCommand() {
		return getErrorCommand2();
	}

	public void setCommandSet(byte commandSet) {
		setErrorCommand1(commandSet);
	}

	public void setCommand(byte command2) {
		setErrorCommand2(command2);
	}


	public String toString() {
		StringBuilder builder = new StringBuilder(super.toString());
		
		builder.append(", commandset: ");
		builder.append(getCommandSet());
		builder.append(", command: ");
		builder.append(getCommand());
		builder.append(", payload size: ");
		builder.append(getData().length);
		return builder.toString();
	}
}
