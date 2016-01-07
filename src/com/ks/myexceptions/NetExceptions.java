package com.ks.myexceptions;

import java.io.IOException;

public class NetExceptions extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NetExceptions(String exception)
	{
		super(exception);
	}

}
