package com.optimization;

import java.io.IOException;

public class RunPython {

	public static void main(String[] args) throws IOException {
		Process process = Runtime.getRuntime().exec("python E:\\combinations.py");
		process = Runtime.getRuntime().exec("hello");
	}

}
