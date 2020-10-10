package javaTest;

import java.io.Console;
import java.text.ParseException;
import java.util.Scanner;

public class Java_ASCII_Test {
	public static void main(String[] args) throws ParseException {

		String myTime, myMacAddress, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress, inputKey, exitKey;

		if(System.console() == null){
			Scanner inputScan = new Scanner(System.in);

			myTime = Get_Date.getDateNow();
			System.out.println("現在時間為："+myTime);
			myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
			System.out.println("網卡位址為："+myMacAddress);

			encodeTime = ASCII_Translator.getMessageASCII(myTime);
			encodeMacAddress = ASCII_Translator.getMessageASCII(myMacAddress);
			decodeTime = ASCII_Translator.getMessageChar(encodeTime);
			decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);

			while(true) {
				System.out.print("請輸入密文(限英文):");

				inputKey = inputScan.nextLine();

				String result = Get_Key.getEncodeKey(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法一轉換結果為：" + result);

				String resultMid = Get_Key.getEncodeKeyMid(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法二轉換結果為：" + resultMid);

				String resultLong = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法三轉換結果為：" + resultLong);

				String recoveryKey = Get_Key.getDecodeKey(decodeTime, decodeMacAddress, result);
				System.out.println("方法一還原結果為：" + recoveryKey);

				String recoveryKeyMid = Get_Key.getDecodeKeyMid(decodeTime, decodeMacAddress, resultMid);
				System.out.println("方法二還原結果為：" + recoveryKeyMid);

				String recoveryKeyLong = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, resultLong);
				System.out.println("方法三還原結果為：" + recoveryKeyLong);

				System.out.println("\n如要結束程式，請輸入「Exit」:");
				exitKey = inputScan.nextLine();
				if(exitKey.toUpperCase().equals("EXIT")) {
					System.out.println("程式已結束...");
					break;
				}
			}
			inputScan.close();
		}
		else{
			Console inputCon = System.console();
			Scanner consoleScan = new Scanner(inputCon.reader());

			myTime = Get_Date.getDateNow();
			System.out.println("現在時間為："+myTime);
			myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
			System.out.println("網卡位址為："+myMacAddress);

			encodeTime = ASCII_Translator.getMessageASCII(myTime);
			encodeMacAddress = ASCII_Translator.getMessageASCII(myMacAddress);
			decodeTime = ASCII_Translator.getMessageChar(encodeTime);
			decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);

			while(true){
				char [] inputKeyChar = inputCon.readPassword("請輸入密文(限英文):");
				inputKey = new String(inputKeyChar);

				String result = Get_Key.getEncodeKey(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法一轉換結果為：" + result);

				String resultMid = Get_Key.getEncodeKeyMid(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法二轉換結果為：" + resultMid);

				String resultLong = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, inputKey);
				System.out.println("方法三轉換結果為：" + resultLong);

				String recoveryKey = Get_Key.getDecodeKey(decodeTime, decodeMacAddress, result);
				System.out.println("方法一還原結果為：" + recoveryKey);

				String recoveryKeyMid = Get_Key.getDecodeKeyMid(decodeTime, decodeMacAddress, resultMid);
				System.out.println("方法二還原結果為：" + recoveryKeyMid);

				String recoveryKeyLong = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, resultLong);
				System.out.println("方法三還原結果為：" + recoveryKeyLong);

				System.out.println("\n如要結束程式，請輸入「Exit」:");
				exitKey = consoleScan.nextLine();
				if(exitKey.toUpperCase().equals("EXIT")) {
					System.out.println("程式已結束...");
					break;
				}
			}
			consoleScan.close();
		}
	}
}
