package com.dci.intellij.dbn.debugger.jdwp.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Packet {
	/* common header fields */
	private int length;
	private int id;
	private byte flags;
	/* really a 
	 * union { 
	 * 	   byte command1, command2;
	 *     short errorCode
	 * } 
	 */
	private byte errorCommand1;
	private byte errorCommand2;

	/* variable length data */
	private byte[] data;

	public final int getLength() {
		return length;
	}

	public final void setLength(int length) {
		this.length = length;
	}

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public void setFlags(byte flags) {
		this.flags = flags;
	}

	public byte getFlags() {
		return flags;
	}

	protected void setErrorCommand1(byte errorCommand1) {
		this.errorCommand1 = errorCommand1;
	}

	protected byte getErrorCommand1() {
		return this.errorCommand1;
	}

	protected byte getErrorCommand2() {
		return errorCommand2;
	}

	protected void setErrorCommand2(byte errorCommand2) {
		this.errorCommand2 = errorCommand2;
	}

	protected byte[] getData() {
		return data;
	}

	protected void setData(byte[] data) {
		this.data = data;
	}

	public static Packet read(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
		return read((DataInput) dis);
	}

	public static Packet read(DataInput di) throws IOException {
		int length = di.readInt();
		int expectedRemaining = length - 4;
		int id = di.readInt();
		expectedRemaining -= 4;
		byte flags = di.readByte();
		expectedRemaining--;
		byte errorCommand1 = di.readByte();
		expectedRemaining--;
		byte errorCommand2 = di.readByte();
		expectedRemaining--;

		byte[] data = new byte[expectedRemaining];
		di.readFully(data);

		Packet p = null;
		if ((flags & 0x80) != 0) {
			//reply
			p = new ReplyPacket();
		}
		else {
			p = new CommandPacket();
		}
		p.setId(id);
		p.setLength(length);
		p.setFlags(flags);
		p.setErrorCommand1(errorCommand1);
		p.setErrorCommand2(errorCommand2);
		p.setData(data);

		return p;
	}

	public void write(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(os));
		this.write((DataOutput) dos);
	}

	public void write(DataOutput dout) throws IOException {
		dout.writeInt(this.length);
		int expectedRemaining = this.length - 4;
		dout.writeInt(this.id);
		expectedRemaining -= 4;
		dout.writeByte(this.flags);
		expectedRemaining--;
		dout.writeByte(this.errorCommand1);
		expectedRemaining--;
		dout.writeByte(this.errorCommand2);
		expectedRemaining--;

		this.data = new byte[expectedRemaining];
		dout.write(data);
	}
	
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id: ");
		builder.append(id);
		builder.append(", length: ");
		builder.append(length);
		builder.append(", flags: ");
		builder.append(flags);
		return builder.toString();
	}
}
