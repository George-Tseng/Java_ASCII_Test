package javaTest;

import java.util.Scanner;

public class Java_ASCII_Test {
	public static void main(String[] args) {

		Scanner inputScan = new Scanner(System.in);
		
		String myTime = Get_Date.getDateNow();
		System.out.println("現在時間為："+myTime);
		String myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
		System.out.println("網卡位址為："+myMacAddress);

		String encodeTime = ASCII_Translator.getMessageASCII(myTime);
		String encodeMacAddress = ASCII_Translator.getMessageASCII(myMacAddress);
		System.out.println("轉換後時間資訊為："+encodeTime);
		System.out.println("轉換後網卡資訊為："+encodeMacAddress);

		String decodeTime = ASCII_Translator.getMessageChar(encodeTime);
		String decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
		System.out.println("轉換回時間資訊為："+decodeTime);
		System.out.println("轉換回網卡資訊為："+decodeMacAddress);

		while(true) {
			System.out.println("請輸入密文(限英文):");
			String inputKey = inputScan.nextLine();

			String result = Get_Key.getEncodeKey(encodeTime, encodeMacAddress, inputKey);
			System.out.println("轉換結果為：" + result);

			String resultMid = Get_Key.getEncodeKeyMid(encodeTime, encodeMacAddress, inputKey);
			System.out.println("轉換結果為：" + resultMid);

			String recoveryKey = Get_Key.getDecodeKey(encodeTime, encodeMacAddress, result);
			System.out.println("還原結果為：" + recoveryKey);

			String recoveryKeyMid = Get_Key.getDecodeKeyMid(encodeTime, encodeMacAddress, resultMid);
			System.out.println("還原結果為：" + recoveryKeyMid);
			
			System.out.println("\n如要結束程式，請輸入「Exit」:");
			String exitKey = inputScan.nextLine();
			
			if(exitKey.toUpperCase().equals("EXIT")) {
				System.out.println("程式已結束...");
				break;
			}
		}
		
		inputScan.close();
	}
}
