package com.task;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Decrypt {

	public static void main(String[] args) {
		Decrypt.Decrypt();
	
	}
public static void Decrypt() {
	String file_loc="D:\\stega\\stegImage2.jpg";
	File newImageFile=new File(file_loc);
	try {
		BufferedImage image=ImageIO.read(newImageFile);
		Pixel[] pixels=GetPixelArray(image);
		System.out.println(DecodeMessageFromPixels(pixels));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private static Pixel[] GetPixelArray(BufferedImage image) {
	int height =image.getHeight();
	int width =image.getWidth();
	Pixel[] pixels = new Pixel[height*width];
	int count=0;
	for(int x=0;x<width;x++) {
		for(int y=0;y<height;y++) {
			Color colorToAdd= new Color(image.getRGB(x, y));
			pixels[count]=new Pixel(x, y, colorToAdd);
			count++;
		}
	}
	return pixels;
}
private static String DecodeMessageFromPixels(Pixel[] pixels) {
	boolean completed=false;
	int pixelIndex=0;
	StringBuilder messageBuilder=new StringBuilder("");
	while(completed==false){
		Pixel[] pixelsToRead=new Pixel[3];
		for(int i=0;i<3;i++) {
			pixelsToRead[i]=pixels[pixelIndex];
			pixelIndex++;
	}
		messageBuilder.append(ConverPixelsToCharacter(pixelsToRead));
		if(IsEndOfMessage(pixelsToRead[2])==true) {
			completed=true;
		}
		
	}
	System.out.println("Decoded "+messageBuilder.toString());
	return messageBuilder.toString();
	
}
private static char ConverPixelsToCharacter(Pixel[] pixelsToRead) {
	ArrayList<String> binaryValues=new ArrayList<String>();
	for(int i=0;i<pixelsToRead.length;i++) {
			String[] currentBinary=TurnPixelIntegersToBinary(pixelsToRead[i]);
			binaryValues.add(currentBinary[0]);
			binaryValues.add(currentBinary[1]);
			binaryValues.add(currentBinary[2]);
	}
	return ConvertBinaryValuesToCharacter(binaryValues);
}
private static String[] TurnPixelIntegersToBinary(Pixel pixel) {
	String[] values=new String[3];
	values[0]=Integer.toBinaryString(pixel.getColor().getRed());
	values[1]=Integer.toBinaryString(pixel.getColor().getGreen());
	values[2]=Integer.toBinaryString(pixel.getColor().getBlue());
	return values;
}

private static char ConvertBinaryValuesToCharacter(ArrayList<String> binaryValues) {
StringBuilder endBinary= new StringBuilder("");
for(int i=0;i<binaryValues.size()-1;i++) {
	endBinary.append(binaryValues.get(i).charAt(binaryValues.get(i).length()-1));
}
String endBinaryString=endBinary.toString();
String nozeros=RemovePaddedZeros(endBinaryString);
int ascii=Integer.parseInt(nozeros, 2);
System.out.println(ascii);
return (char)ascii;
}

private static String RemovePaddedZeros(String endBinaryString) {
	StringBuilder builder=new StringBuilder(endBinaryString);
	int paddedzeros=0;
	for(int i=0;i<builder.length();i++) {
		if(builder.charAt(i) == '0') {
			paddedzeros++;
		}
		else {
			break;
		}
			
		}
	for(int i=0;i<paddedzeros;i++) {
		builder.deleteCharAt(0);
	}
	return builder.toString();
	}

private static boolean IsEndOfMessage(Pixel pixel) {
	if(TurnPixelIntegersToBinary(pixel)[2].endsWith("1")) {
		return false;
	}
	return true;
}
}
