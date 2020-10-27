package com.optimization;
import java.math.RoundingMode;
import java.text.DecimalFormat;
public class RoundoffTest {
	public static void main(String[] args) {
        double num = 1.34567156;
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        double newnum = Double.valueOf(df.format(num));
        System.out.println(df.format(newnum));
    }
}


