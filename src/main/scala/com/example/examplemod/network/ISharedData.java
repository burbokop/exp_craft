package com.example.examplemod.network;


import io.netty.buffer.ByteBuf;

public interface ISharedData {
	public void read(ByteBuf buf);
	public void write(ByteBuf buf);		
}
