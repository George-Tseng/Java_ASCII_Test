package javaTest;

import java.io.Console;
import java.util.Scanner;
import java.util.Timer;

public class Java_ASCII_Test {
	public static void main(String[] args){
		/*參數*/
		String myTime, myMacAddress, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress, inputKey, exitKey;
		/*創立Timer物件*/
		Timer tr0 = new Timer();
		/*邏輯閘*/
		boolean suc1, suc2, suc3, suc4;

		/*使用非cmd啟動本程式*/
		if(System.console() == null){
			Scanner inputScan = new Scanner(System.in);

			while(true) {
				/*初始狀態-設定檔及暫存檔均不存在*/
				if (!File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();

					System.out.println("這似乎是本程式第一次被啟動，將開始執行初始化作業");

					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*嘗試建立設定檔*/
					if(!File_Conf.createConfFile().equals("")){
						System.out.println("設定檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法*/
					else{
						if(File_Conf.writeConfFile(myTime).equals("")){
							suc1 = true;
						}
						else{
							System.out.println(File_Conf.writeConfFile(myTime));
						}
					}

					/*嘗試建立暫存檔*/
					if(!File_Conf.createTmpFile().equals("")){
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else{
						tr0.schedule(new File_Timer(), 0, 1000);
						suc2 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if(!File_Conf.deleteTmpFile().equals("")){
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc3 = false;
					}

					/*全部正常才執行*/
					if(suc1 && suc2 && suc3){
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(myTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);

						System.out.println("目前時間為："+myTime);
						System.out.println("網卡位址為："+myMacAddress);

						/*網卡資訊存在才執行*/
						if(!myMacAddress.equals("NotFound")){
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else{
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					}
					else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時按正常流程關閉*/
				else if (File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*讀取設定檔中的所有紀錄*/
					String allLastTime = File_Conf.readConfFile();
					/*用換行拆分資料*/
					String [] recordSpace = allLastTime.split("\n");
					/*最後一筆為上次啟動時間*/
					String lastTime = recordSpace[recordSpace.length - 1];
					/*第一筆為首次啟動時間*/
					String firstTime = recordSpace[0];

					System.out.println("上次啟動時間為："+lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = allLastTime + "\n" + myTime;

					/*將所有紀錄重新寫入設定檔*/
					if(File_Conf.writeConfFile(allTime).equals("")){
						suc1 = true;
					}
					else{
						System.out.println(File_Conf.writeConfFile(allTime));
					}

					/*嘗試建立暫存檔*/
					if(!File_Conf.createTmpFile().equals("")){
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else{
						tr0.schedule(new File_Timer(), 0, 1000);
						suc2 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if(!File_Conf.deleteTmpFile().equals("")){
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc3 = false;
					}

					/*全部正常才執行*/
					if(suc1 && suc2 && suc3){
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(firstTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為："+myTime);

						/*網卡資訊存在才執行*/
						if(!myMacAddress.equals("NotFound")){
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else{
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					}
					else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時未依正常流程關閉*/
				else if (File_Conf.checkConfFile() && File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式似乎並未正常地被關閉...");

					/*讀取設定檔中的所有紀錄*/
					String allLastTime = File_Conf.readConfFile();
					/*用換行拆分資料*/
					String [] recordSpace = allLastTime.split("\n");
					/*最後一筆為上次啟動時間*/
					String lastTime = recordSpace[recordSpace.length - 1];
					/*第一筆為首次啟動時間*/
					String firstTime = recordSpace[0];

					System.out.println("上次啟動時間為："+lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = allLastTime + "\n" + myTime;

					/*將所有紀錄重新寫入設定檔*/
					if(File_Conf.writeConfFile(allTime).equals("")){
						suc1 = true;
					}
					else{
						System.out.println(File_Conf.writeConfFile(allTime));
					}

					/*嘗試立即刪除暫存檔*/
					if(!File_Conf.deleteTmpFileNow().equals("")){
						System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc2 = false;
					}

					/*嘗試建立暫存檔*/
					if(!File_Conf.createTmpFile().equals("")){
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else{
						tr0.schedule(new File_Timer(), 0, 1000);
						suc3 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if(!File_Conf.deleteTmpFile().equals("")){
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc4 = false;
					}

					/*全部正常才執行*/
					if(suc1 && suc2 && suc3 && suc4){
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(firstTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為："+myTime);

						/*網卡資訊存在才執行*/
						if(!myMacAddress.equals("NotFound")){
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else{
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					}
					else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*異常*/
				else {
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式檔案有部分損毀或遺失...");

					/*讀取暫存檔的紀錄作為上次啟動時間*/
					String lastTime = File_Conf.readTmpFile();
					System.out.println("最後紀錄時間為："+lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = lastTime + "\n" + myTime;

					/*嘗試建立設定檔*/
					if(!File_Conf.createConfFile().equals("")){
						System.out.println("設定檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法*/
					else{
						if(File_Conf.writeConfFile(allTime).equals("")){
							suc1 = true;
						}
						else{
							System.out.println(File_Conf.writeConfFile(allTime));
						}
					}

					/*嘗試立刻刪除暫存檔*/
					if(!File_Conf.deleteTmpFileNow().equals("")){
						System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc2 = false;
					}

					/*嘗試建立暫存檔*/
					if(!File_Conf.createTmpFile().equals("")){
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else{
						tr0.schedule(new File_Timer(), 0, 1000);
						suc3 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if(!File_Conf.deleteTmpFile().equals("")){
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc4 = false;
					}

					/*全部正常才執行*/
					if(suc1 && suc2 && suc3 && suc4){
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(lastTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為："+myTime);

						/*網卡資訊存在才執行*/
						if(!myMacAddress.equals("NotFound")){
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else{
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					}
					else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}

				System.out.print("\n如要結束程式，請輸入「Exit」:");
				exitKey = inputScan.nextLine();
				if(exitKey.toUpperCase().equals("EXIT")) {
					System.out.println("程式已結束...");
					break;
				}
			}

			/*關閉Scanner*/
			inputScan.close();
			/*中止Timer*/
			tr0.cancel();
		}
		/*使用cmd啟動本程式*/
		else if(System.console() != null) {
			Console inputCon = System.console();
			Scanner consoleScan = new Scanner(inputCon.reader());

			while (true) {
				/*初始狀態-設定檔及暫存檔均不存在*/
				if (!File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();

					System.out.println("這似乎是本程式第一次被啟動，將開始執行初始化作業");

					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*嘗試建立設定檔*/
					if (!File_Conf.createConfFile().equals("")) {
						System.out.println("設定檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法*/
					else {
						if (File_Conf.writeConfFile(myTime).equals("")) {
							suc1 = true;
						} else {
							System.out.println(File_Conf.writeConfFile(myTime));
						}
					}

					/*嘗試建立暫存檔*/
					if (!File_Conf.createTmpFile().equals("")) {
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else {
						tr0.schedule(new File_Timer(), 0, 1000);
						suc2 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if (!File_Conf.deleteTmpFile().equals("")) {
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc3 = false;
					}

					/*全部正常才執行*/
					if (suc1 && suc2 && suc3) {
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(myTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);

						System.out.println("目前時間為：" + myTime);
						System.out.println("網卡位址為：" + myMacAddress);

						/*網卡資訊存在才執行*/
						if (!myMacAddress.equals("NotFound")) {
							System.out.print("請輸入密文(限英文):");
							inputKey = consoleScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					} else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時按正常流程關閉*/
				else if (File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*讀取設定檔中的所有紀錄*/
					String allLastTime = File_Conf.readConfFile();
					/*用換行拆分資料*/
					String[] recordSpace = allLastTime.split("\n");
					/*最後一筆為上次啟動時間*/
					String lastTime = recordSpace[recordSpace.length - 1];
					/*第一筆為首次啟動時間*/
					String firstTime = recordSpace[0];

					System.out.println("上次啟動時間為：" + lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = allLastTime + "\n" + myTime;

					/*將所有紀錄重新寫入設定檔*/
					if (File_Conf.writeConfFile(allTime).equals("")) {
						suc1 = true;
					} else {
						System.out.println(File_Conf.writeConfFile(allTime));
					}

					/*嘗試建立暫存檔*/
					if (!File_Conf.createTmpFile().equals("")) {
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else {
						tr0.schedule(new File_Timer(), 0, 1000);
						suc2 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if (!File_Conf.deleteTmpFile().equals("")) {
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc3 = false;
					}

					/*全部正常才執行*/
					if (suc1 && suc2 && suc3) {
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(firstTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為：" + myTime);

						/*網卡資訊存在才執行*/
						if (!myMacAddress.equals("NotFound")) {
							System.out.print("請輸入密文(限英文):");
							inputKey = consoleScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					} else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時未依正常流程關閉*/
				else if (File_Conf.checkConfFile() && File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式似乎並未正常地被關閉...");

					/*讀取設定檔中的所有紀錄*/
					String allLastTime = File_Conf.readConfFile();
					/*用換行拆分資料*/
					String[] recordSpace = allLastTime.split("\n");
					/*最後一筆為上次啟動時間*/
					String lastTime = recordSpace[recordSpace.length - 1];
					/*第一筆為首次啟動時間*/
					String firstTime = recordSpace[0];

					System.out.println("上次啟動時間為：" + lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = allLastTime + "\n" + myTime;

					/*將所有紀錄重新寫入設定檔*/
					if (File_Conf.writeConfFile(allTime).equals("")) {
						suc1 = true;
					} else {
						System.out.println(File_Conf.writeConfFile(allTime));
					}

					/*嘗試立即刪除暫存檔*/
					if (!File_Conf.deleteTmpFileNow().equals("")) {
						System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc2 = false;
					}

					/*嘗試建立暫存檔*/
					if (!File_Conf.createTmpFile().equals("")) {
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else {
						tr0.schedule(new File_Timer(), 0, 1000);
						suc3 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if (!File_Conf.deleteTmpFile().equals("")) {
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc4 = false;
					}

					/*全部正常才執行*/
					if (suc1 && suc2 && suc3 && suc4) {
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(firstTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為：" + myTime);

						/*網卡資訊存在才執行*/
						if (!myMacAddress.equals("NotFound")) {
							System.out.print("請輸入密文(限英文):");
							inputKey = consoleScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					} else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}
				/*異常*/
				else {
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式檔案有部分損毀或遺失...");

					/*讀取暫存檔的紀錄作為上次啟動時間*/
					String lastTime = File_Conf.readTmpFile();
					System.out.println("最後紀錄時間為：" + lastTime);

					/*紀錄啟動時的時間*/
					myTime = Get_Date.getDateNow();
					/*在原有紀錄中加入新的一筆*/
					String allTime = lastTime + "\n" + myTime;

					/*嘗試建立設定檔*/
					if (!File_Conf.createConfFile().equals("")) {
						System.out.println("設定檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法*/
					else {
						if (File_Conf.writeConfFile(allTime).equals("")) {
							suc1 = true;
						} else {
							System.out.println(File_Conf.writeConfFile(allTime));
						}
					}

					/*嘗試立刻刪除暫存檔*/
					if (!File_Conf.deleteTmpFileNow().equals("")) {
						System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc2 = false;
					}

					/*嘗試建立暫存檔*/
					if (!File_Conf.createTmpFile().equals("")) {
						System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else {
						tr0.schedule(new File_Timer(), 0, 1000);
						suc3 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if (!File_Conf.deleteTmpFile().equals("")) {
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.createConfFile());
						suc4 = false;
					}

					/*全部正常才執行*/
					if (suc1 && suc2 && suc3 && suc4) {
						/*參數賦值*/
						encodeTime = ASCII_Translator.getMessageASCII(lastTime);
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						myMacAddress = Get_Mac_Address.getMacAddress().equals("") ? "NotFound" : Get_Mac_Address.getMacAddress();
						encodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageASCII(myMacAddress);
						decodeMacAddress = (myMacAddress.equals("NotFound")) ? null : ASCII_Translator.getMessageChar(encodeMacAddress);
						System.out.println("目前時間為：" + myTime);

						/*網卡資訊存在才執行*/
						if (!myMacAddress.equals("NotFound")) {
							System.out.print("請輸入密文(限英文):");
							inputKey = consoleScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					} else {
						System.out.println("出現預期外的錯誤...，程式無法繼續執行");
					}
				}

				System.out.println("\n如要結束程式，請輸入「Exit」:");
				exitKey = consoleScan.nextLine();
				if (exitKey.toUpperCase().equals("EXIT")) {
					System.out.println("程式已正常結束...");
					break;
				}
			}
			/*關閉Scanner*/
			consoleScan.close();
			/*中止Timer*/
			tr0.cancel();
		}
	}

	/*顯示結果*/
	private static void displayResult(String inputKey, String encodeTime, String encodeMacAddress, String decodeTime, String decodeMacAddress){
		String resultTmp = Get_Key.getEncodeKey(encodeTime, encodeMacAddress, inputKey);
		String result = (!resultTmp.equals("")) ? resultTmp : "執行失敗";
		System.out.println("方法一轉換結果為：" + result);

		String resultMidTmp = Get_Key.getEncodeKeyMid(encodeTime, encodeMacAddress, inputKey);
		String resultMid = (!resultMidTmp.equals("")) ? resultMidTmp : "執行失敗";
		System.out.println("方法二轉換結果為：" + resultMid);

		String resultLongTmp = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, inputKey);
		String resultLong = (!resultLongTmp.equals("")) ? resultLongTmp : "執行失敗";
		System.out.println("方法三轉換結果為：" + resultLong);

		String recoveryKeyTmp = Get_Key.getDecodeKey(decodeTime, decodeMacAddress, result);
		String recoveryKey = (!recoveryKeyTmp.equals("")) ? recoveryKeyTmp : "執行失敗";
		System.out.println("方法一還原結果為：" + recoveryKey);

		String recoveryKeyMidTmp = Get_Key.getDecodeKeyMid(decodeTime, decodeMacAddress, resultMid);
		String recoveryKeyMid = (!recoveryKeyMidTmp.equals("")) ? recoveryKeyMidTmp : "執行失敗";
		System.out.println("方法二還原結果為：" + recoveryKeyMid);

		String recoveryKeyLongTmp = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, resultLong);
		String recoveryKeyLong = (!recoveryKeyLongTmp.equals("")) ? recoveryKeyLongTmp : "執行失敗";
		System.out.println("方法三還原結果為：" + recoveryKeyLong);
	}
}
