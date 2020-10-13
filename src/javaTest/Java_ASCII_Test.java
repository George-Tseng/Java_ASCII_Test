package javaTest;

import java.io.Console;
import java.util.Scanner;
import java.util.Timer;

public class Java_ASCII_Test {
	public static void main(String[] args){
		/*參數*/
		String myTime, myMacAddress, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress, inputKey, exitKey, timePara;
		/*創立Timer物件*/
		Timer tr0 = new Timer();
		/*邏輯閘*/
		boolean suc1, suc2, suc3, suc4;

		/*使用非cmd啟動本程式*/
		if(System.console() == null){
			Scanner inputScan = new Scanner(System.in);

			while(true) {
				/*初始狀態-設定檔、暫存檔均不存在*/
				if (!File_Conf.checkConfFile() && !File_Conf.checkTmpFile() && !File_Conf.checkResultFile()) {
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
						System.out.println(File_Conf.createTmpFile());
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
							/*嘗試寫入加密後的設定*/
							timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
							if(!File_Conf.writeConfFile(timePara).equals("")){
								System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
							}
							else{
								/*嘗試建立輸出檔*/
								if(!File_Conf.createResultFile().equals("")){
									System.out.println("輸出檔建立失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.createResultFile());
								}
								/*成功才執行寫入方法*/
								else{
									/*嘗試寫入驗證資訊到輸出檔*/
									if(!Get_Token.writeToken(encodeTime, encodeMacAddress).equals("")){
										System.out.println("寫入驗證時出現錯誤...，程式無法繼續執行");
									}
									/*成功*/
									else{
										System.out.print("請輸入密文(限英文):");
										inputKey = inputScan.nextLine();

										if(inputKey.equals("")){
											System.out.println("無效的輸入值，請重新輸入!");
										}
										else{
											/*顯示結果*/
											displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
										}
									}
								}
							}
						}
						else{
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					}
					else {
						System.out.println("出現錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時按正常流程關閉*/
				else if (File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*讀取設定檔中的所有紀錄*/
					if(File_Conf.readConfFile().equals("")){
						System.out.println("讀取設定檔失敗...\n錯誤訊息為：");
						System.out.println(File_Conf.readConfFile());
					}
					/*成功*/
					else{
						String allLastTime = File_Conf.readConfFile();
						/*用換行拆分資料*/
						String [] recordSpace = allLastTime.split("\n");
						/*最後一筆為上次啟動時間*/
						String lastTime = recordSpace[recordSpace.length - 1];

						//轉換回通常格式
						encodeTime = recordSpace[0];
						encodeMacAddress = recordSpace[1];
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
						String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

						System.out.println("上次啟動時間為："+decodeLastTime);

						/*紀錄啟動時的時間*/
						myTime = Get_Date.getDateNow();
						/*轉換成特殊格式*/
						timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
						/*在原有紀錄中加入新的一筆*/
						String allTime = allLastTime + "\n" + timePara;

						/*將所有紀錄追加寫入設定檔*/
						if(File_Conf.appendConfFile(allTime).equals("")){
							suc1 = true;
						}
						else{
							System.out.println(File_Conf.appendConfFile(allTime));
						}

						/*嘗試建立暫存檔*/
						if(!File_Conf.createTmpFile().equals("")){
							System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.createTmpFile());
						}
						/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
						else{
							tr0.schedule(new File_Timer(), 0, 1000);
							suc2 = true;
						}

						/*設定JVM關閉後自動刪除暫存檔*/
						if(!File_Conf.deleteTmpFile().equals("")){
							System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc3 = false;
						}

						/*全部正常才執行*/
						if(suc1 && suc2 && suc3){
							System.out.println("目前時間為："+myTime);
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else {
							System.out.println("出現錯誤...，程式無法繼續執行");
						}
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
					if(File_Conf.readConfFile().equals("")){
						System.out.println("讀取設定檔失敗...\n錯誤訊息為：");
						System.out.println(File_Conf.readConfFile());
					}
					/*成功*/
					else{
						String allLastTime = File_Conf.readConfFile();
						/*用換行拆分資料*/
						String [] recordSpace = allLastTime.split("\n");
						/*最後一筆為上次啟動時間*/
						String lastTime = recordSpace[recordSpace.length - 1];

						//轉換回通常格式
						encodeTime = recordSpace[0];
						encodeMacAddress = recordSpace[1];
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
						String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

						System.out.println("上次啟動時間為："+decodeLastTime);

						/*紀錄啟動時的時間*/
						myTime = Get_Date.getDateNow();
						/*轉換成特殊格式*/
						timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
						/*在原有紀錄中加入新的一筆*/
						String allTime = allLastTime + "\n" + timePara;

						/*將所有紀錄追加寫入設定檔*/
						if(File_Conf.appendConfFile(allTime).equals("")){
							suc1 = true;
						}
						else{
							System.out.println(File_Conf.appendConfFile(allTime));
						}

						/*嘗試立即刪除暫存檔*/
						if(!File_Conf.deleteTmpFileNow().equals("")){
							System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc2 = false;
						}

						/*嘗試建立暫存檔*/
						if(!File_Conf.createTmpFile().equals("")){
							System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.createTmpFile());
						}
						/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
						else{
							tr0.schedule(new File_Timer(), 0, 1000);
							suc3 = true;
						}

						/*設定JVM關閉後自動刪除暫存檔*/
						if(!File_Conf.deleteTmpFile().equals("")){
							System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc4 = false;
						}

						/*全部正常才執行*/
						if(suc1 && suc2 && suc3 && suc4){
							System.out.println("目前時間為："+myTime);
							System.out.print("請輸入密文(限英文):");
							inputKey = inputScan.nextLine();

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						}
						else {
							System.out.println("出現錯誤...，程式無法繼續執行");
						}
					}
				}
				/*異常*/
				else if(!File_Conf.checkConfFile() && (File_Conf.checkTmpFile() || File_Conf.checkResultFile())){
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式檔案有部分損毀或遺失...");

					/*如果輸出檔存在，就使用輸出檔進行修復*/
					if(File_Conf.checkResultFile()){
						/*讀取輸出檔*/
						if(File_Conf.readResultFile().equals("")){
							System.out.println("讀取輸出檔失敗...\n錯誤訊息為：");
							System.out.println(File_Conf.readResultFile());
						}
						/*成功*/
						else{
							String token = File_Conf.readResultFile();
							String [] tokenSpace = token.split("\n");
							String encodeTimeTmp = tokenSpace[0];
							encodeMacAddress = tokenSpace[1];
							String decodeTimeTmp = tokenSpace[3];

							/*確認時間是否一致*/
							if(!encodeTimeTmp.equals(ASCII_Translator.getMessageASCII(decodeTimeTmp))){
								System.out.println("輸出檔案內容損毀或遭更動，程式無法繼續執行...");
							}
							/*一致*/
							else{
								encodeTime = encodeTimeTmp;
								decodeTime = decodeTimeTmp;
								String lastTime = tokenSpace[tokenSpace.length - 8];
								System.out.println("上次啟動時間為："+lastTime);

								/*紀錄啟動時的時間*/
								myTime = Get_Date.getDateNow();

								/*轉換成特殊格式*/
								StringBuilder sb1 = new StringBuilder();
								sb1.append(Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, decodeTime));
								for(int j = 1; 8 * j + 2 < tokenSpace.length; j++){
									sb1.append("\n");
									sb1.append(Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, tokenSpace[8 * j + 2]));
								}
								String allOldTime = sb1.toString();
								/*轉換成特殊格式*/
								timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
								/*在原有紀錄中加入新的一筆以及部分參數*/
								String allTime = encodeTime + "\n" + encodeMacAddress + "\n" + allOldTime + "\n" + timePara;

								decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);

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
								if(File_Conf.checkTmpFile()){
									if(!File_Conf.deleteTmpFileNow().equals("")){
										System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
										System.out.println(File_Conf.deleteTmpFile());
										suc2 = false;
									}
								}

								/*嘗試建立暫存檔*/
								if(!File_Conf.createTmpFile().equals("")){
									System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.createTmpFile());
								}
								/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
								else{
									tr0.schedule(new File_Timer(), 0, 1000);
									suc3 = true;
								}

								/*設定JVM關閉後自動刪除暫存檔*/
								if(!File_Conf.deleteTmpFile().equals("")){
									System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.deleteTmpFile());
									suc4 = false;
								}

								/*全部正常才執行*/
								if(suc1 && suc2 && suc3 && suc4){
									System.out.println("目前時間為："+myTime);

									/*嘗試寫入加密後的設定*/
									timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
									if(!File_Conf.writeConfFile(timePara).equals("")){
										System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
									}
									else{
										System.out.print("請輸入密文(限英文):");
										inputKey = inputScan.nextLine();

										if(inputKey.equals("")){
											System.out.println("無效的輸入值，請重新輸入!");
										}
										else{
											/*顯示結果*/
											displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
										}
									}
								}
								else {
									System.out.println("出現錯誤...，程式無法繼續執行");
								}
							}
						}
					}
					/*不存在才使用暫存檔*/
					else{
						/*讀取暫存檔的紀錄作為上次啟動時間*/
						if(File_Conf.readTmpFile().equals("")){
							System.out.println("讀取暫存檔失敗...\n錯誤訊息為：");
							System.out.println(File_Conf.readTmpFile());
						}
						/*成功*/
						else{
							String tmpLastTime = File_Conf.readTmpFile();
							/*用換行拆分資料*/
							String [] recordSpace = tmpLastTime.split("\n");
							/*最後一筆為上次啟動時間*/
							String lastTime = recordSpace[recordSpace.length - 1];

							//轉換回通常格式
							encodeTime = recordSpace[0];
							encodeMacAddress = recordSpace[1];
							decodeTime = ASCII_Translator.getMessageChar(encodeTime);
							decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
							String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

							System.out.println("上次啟動時間為："+decodeLastTime);

							/*紀錄啟動時的時間*/
							myTime = Get_Date.getDateNow();
							/*轉換成特殊格式*/
							timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
							/*在原有紀錄中加入新的一筆*/
							String allTime = tmpLastTime + "\n" + timePara;

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
								System.out.println(File_Conf.deleteTmpFile());
								suc2 = false;
							}

							/*嘗試建立暫存檔*/
							if(!File_Conf.createTmpFile().equals("")){
								System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
								System.out.println(File_Conf.createTmpFile());
							}
							/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
							else{
								tr0.schedule(new File_Timer(), 0, 1000);
								suc3 = true;
							}

							/*設定JVM關閉後自動刪除暫存檔*/
							if(!File_Conf.deleteTmpFile().equals("")){
								System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
								System.out.println(File_Conf.deleteTmpFile());
								suc4 = false;
							}

							/*全部正常才執行*/
							if(suc1 && suc2 && suc3 && suc4){
								System.out.println("目前時間為："+myTime);

								/*嘗試寫入加密後的設定*/
								timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
								if(!File_Conf.writeConfFile(timePara).equals("")){
									System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
								}
								else{
									/*嘗試建立輸出檔*/
									if(!File_Conf.createResultFile().equals("")){
										System.out.println("輸出檔建立失敗...\n錯誤資訊為：");
										System.out.println(File_Conf.createResultFile());
									}
									/*成功才執行寫入方法*/
									else{
										/*嘗試寫入驗證資訊到輸出檔*/
										if(!Get_Token.writeToken(encodeTime, encodeMacAddress).equals("")){
											System.out.println("寫入驗證時出現錯誤...，程式無法繼續執行");
										}
										/*成功*/
										else{
											System.out.print("請輸入密文(限英文):");
											inputKey = inputScan.nextLine();

											if(inputKey.equals("")){
												System.out.println("無效的輸入值，請重新輸入!");
											}
											else{
												/*顯示結果*/
												displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
											}
										}
									}
								}
							}
							else {
								System.out.println("出現錯誤...，程式無法繼續執行");
							}
						}
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
		else if(System.console() != null){
			Console inputCon = System.console();
			Scanner consoleScan = new Scanner(inputCon.reader());

			while (true) {
				/*初始狀態-設定檔、暫存檔均不存在*/
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
						System.out.println(File_Conf.createTmpFile());
					}
					/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
					else {
						tr0.schedule(new File_Timer(), 0, 1000);
						suc2 = true;
					}

					/*設定JVM關閉後自動刪除暫存檔*/
					if (!File_Conf.deleteTmpFile().equals("")) {
						System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
						System.out.println(File_Conf.deleteTmpFile());
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
							/*嘗試寫入加密後的設定*/
							timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
							if(!File_Conf.writeConfFile(timePara).equals("")){
								System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
							}
							else{
								/*嘗試建立輸出檔*/
								if(!File_Conf.createResultFile().equals("")){
									System.out.println("輸出檔建立失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.createResultFile());
								}
								/*成功才執行寫入方法*/
								else{
									/*嘗試寫入驗證資訊到輸出檔*/
									if(Get_Token.writeToken(encodeTime, encodeMacAddress).equals("")){
										System.out.println("寫入驗證時出現錯誤...，程式無法繼續執行");
									}
									/*成功*/
									else{
										char [] inputKeySpace = inputCon.readPassword("請輸入密文(限英文):");
										inputKey = new String(inputKeySpace);

										if(inputKey.equals("")){
											System.out.println("無效的輸入值，請重新輸入!");
										}
										else{
											/*顯示結果*/
											displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
										}
									}
								}
							}
						} else {
							System.out.println("無法取得網卡資訊，請確認網路連線狀態正常後重開本程式...");
						}
					} else {
						System.out.println("出現錯誤...，程式無法繼續執行");
					}
				}
				/*上次使用時按正常流程關閉*/
				else if (File_Conf.checkConfFile() && !File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = false;
					suc3 = true;

					/*讀取設定檔中的所有紀錄*/
					if(File_Conf.readConfFile().equals("")){
						System.out.println("讀取設定檔失敗...");
					}
					/*成功*/
					else{
						String allLastTime = File_Conf.readConfFile();
						/*用換行拆分資料*/
						String[] recordSpace = allLastTime.split("\n");
						/*最後一筆為上次啟動時間*/
						String lastTime = recordSpace[recordSpace.length - 1];

						//轉換回通常格式
						encodeTime = recordSpace[0];
						encodeMacAddress = recordSpace[1];
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
						String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

						System.out.println("上次啟動時間為：" + decodeLastTime);

						/*紀錄啟動時的時間*/
						myTime = Get_Date.getDateNow();
						/*轉換成特殊格式*/
						timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
						/*在原有紀錄中加入新的一筆*/
						String allTime = allLastTime + "\n" + timePara;

						/*將所有紀錄追加寫入設定檔*/
						if (File_Conf.appendConfFile(allTime).equals("")) {
							suc1 = true;
						} else {
							System.out.println(File_Conf.appendConfFile(allTime));
						}

						/*嘗試建立暫存檔*/
						if (!File_Conf.createTmpFile().equals("")) {
							System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.createTmpFile());
						}
						/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
						else {
							tr0.schedule(new File_Timer(), 0, 1000);
							suc2 = true;
						}

						/*設定JVM關閉後自動刪除暫存檔*/
						if (!File_Conf.deleteTmpFile().equals("")) {
							System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc3 = false;
						}

						/*全部正常才執行*/
						if (suc1 && suc2 && suc3) {
							System.out.println("目前時間為：" + myTime);
							char [] inputKeySpace = inputCon.readPassword("請輸入密文(限英文):");
							inputKey = new String(inputKeySpace);

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("出現預期外的錯誤...，程式無法繼續執行");
						}
					}
				}
				/*上次使用時未依正常流程關閉*/
				else if (File_Conf.checkConfFile() && File_Conf.checkTmpFile()) {
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式似乎並未正常地被關閉...");

					/*讀取紀錄檔中的所有紀錄*/
					if(File_Conf.readConfFile().equals("")){
						System.out.println("讀取設定檔失敗...");
					}
					else{
						String allLastTime = File_Conf.readConfFile();
						/*用換行拆分資料*/
						String[] recordSpace = allLastTime.split("\n");
						/*最後一筆為上次啟動時間*/
						String lastTime = recordSpace[recordSpace.length - 1];

						//轉換回通常格式
						encodeTime = recordSpace[0];
						encodeMacAddress = recordSpace[1];
						decodeTime = ASCII_Translator.getMessageChar(encodeTime);
						decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
						String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

						System.out.println("上次啟動時間為：" + decodeLastTime);

						/*紀錄啟動時的時間*/
						myTime = Get_Date.getDateNow();
						/*轉換成特殊格式*/
						timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
						/*在原有紀錄中加入新的一筆*/
						String allTime = allLastTime + "\n" + timePara;

						/*將所有紀錄追加寫入設定檔*/
						if (File_Conf.appendConfFile(allTime).equals("")) {
							suc1 = true;
						} else {
							System.out.println(File_Conf.appendConfFile(allTime));
						}

						/*嘗試立即刪除暫存檔*/
						if (!File_Conf.deleteTmpFileNow().equals("")) {
							System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc2 = false;
						}

						/*嘗試建立暫存檔*/
						if (!File_Conf.createTmpFile().equals("")) {
							System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.createTmpFile());
						}
						/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
						else {
							tr0.schedule(new File_Timer(), 0, 1000);
							suc3 = true;
						}

						/*設定JVM關閉後自動刪除暫存檔*/
						if (!File_Conf.deleteTmpFile().equals("")) {
							System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
							System.out.println(File_Conf.deleteTmpFile());
							suc4 = false;
						}

						/*全部正常才執行*/
						if (suc1 && suc2 && suc3 && suc4) {
							System.out.println("目前時間為：" + myTime);
							char [] inputKeySpace = inputCon.readPassword("請輸入密文(限英文):");
							inputKey = new String(inputKeySpace);

							if(inputKey.equals("")){
								System.out.println("無效的輸入值，請重新輸入!");
							}
							else{
								/*顯示結果*/
								displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
							}
						} else {
							System.out.println("出現預期外的錯誤...，程式無法繼續執行");
						}
					}
				}
				/*異常*/
				else if(!File_Conf.checkConfFile() && (File_Conf.checkTmpFile() || File_Conf.checkResultFile())){
					suc1 = false;
					suc2 = true;
					suc3 = false;
					suc4 = true;

					System.out.println("程式檔案有部分損毀或遺失...");

					/*如果輸出檔存在，就使用輸出檔進行修復*/
					if(File_Conf.checkResultFile()){
						/*讀取輸出檔*/
						if(File_Conf.readResultFile().equals("")){
							System.out.println("讀取輸出檔失敗...\n錯誤訊息為：");
							System.out.println(File_Conf.readResultFile());
						}
						/*成功*/
						else{
							String token = File_Conf.readResultFile();
							String [] tokenSpace = token.split("\n");
							String encodeTimeTmp = tokenSpace[0];
							encodeMacAddress = tokenSpace[1];
							String decodeTimeTmp = tokenSpace[3];

							/*確認時間是否一致*/
							if(!encodeTimeTmp.equals(ASCII_Translator.getMessageASCII(decodeTimeTmp))){
								System.out.println("輸出檔案內容損毀或遭更動，程式無法繼續執行...");
							}
							/*一致*/
							else{
								encodeTime = encodeTimeTmp;
								decodeTime = decodeTimeTmp;
								String lastTime = tokenSpace[tokenSpace.length - 8];
								System.out.println("上次啟動時間為："+lastTime);

								/*紀錄啟動時的時間*/
								myTime = Get_Date.getDateNow();

								/*轉換成特殊格式*/
								StringBuilder sb1 = new StringBuilder();
								sb1.append(Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, decodeTime));
								for(int j = 1; 8 * j + 2 < tokenSpace.length; j++){
									sb1.append("\n");
									sb1.append(Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, tokenSpace[8 * j + 2]));
								}
								String allOldTime = sb1.toString();
								/*轉換成特殊格式*/
								timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
								/*在原有紀錄中加入新的一筆以及部分參數*/
								String allTime = encodeTime + "\n" + encodeMacAddress + "\n" + allOldTime + "\n" + timePara;

								decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);

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
								if(File_Conf.checkTmpFile()){
									if(!File_Conf.deleteTmpFileNow().equals("")){
										System.out.println("上次執行時的暫存檔清除失敗...\n錯誤資訊為：");
										System.out.println(File_Conf.deleteTmpFile());
										suc2 = false;
									}
								}

								/*嘗試建立暫存檔*/
								if(!File_Conf.createTmpFile().equals("")){
									System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.createTmpFile());
								}
								/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
								else{
									tr0.schedule(new File_Timer(), 0, 1000);
									suc3 = true;
								}

								/*設定JVM關閉後自動刪除暫存檔*/
								if(!File_Conf.deleteTmpFile().equals("")){
									System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
									System.out.println(File_Conf.deleteTmpFile());
									suc4 = false;
								}

								/*全部正常才執行*/
								if(suc1 && suc2 && suc3 && suc4){
									System.out.println("目前時間為："+myTime);

									/*嘗試寫入加密後的設定*/
									timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
									if(!File_Conf.writeConfFile(timePara).equals("")){
										System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
									}
									else{
										char [] inputKeySpace = inputCon.readPassword("請輸入密文(限英文):");
										inputKey = new String(inputKeySpace);

										if(inputKey.equals("")){
											System.out.println("無效的輸入值，請重新輸入!");
										}
										else{
											/*顯示結果*/
											displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
										}
									}
								}
								else {
									System.out.println("出現錯誤...，程式無法繼續執行");
								}
							}
						}
					}
					/*不存在才使用暫存檔*/
					else{
						/*讀取暫存檔的紀錄作為上次啟動時間*/
						if(File_Conf.readTmpFile().equals("")){
							System.out.println("讀取暫存檔失敗...");
						}
						/*成功*/
						else {
							String tmpLastTime = File_Conf.readTmpFile();
							/*用換行拆分資料*/
							String[] recordSpace = tmpLastTime.split("\n");
							/*最後一筆為上次啟動時間*/
							String lastTime = recordSpace[recordSpace.length - 1];

							//轉換回通常格式
							encodeTime = recordSpace[0];
							encodeMacAddress = recordSpace[1];
							decodeTime = ASCII_Translator.getMessageChar(encodeTime);
							decodeMacAddress = ASCII_Translator.getMessageChar(encodeMacAddress);
							String decodeLastTime = Get_Key.getDecodeKeyLong(decodeTime, decodeMacAddress, lastTime);

							System.out.println("上次啟動時間為：" + decodeLastTime);

							/*紀錄啟動時的時間*/
							myTime = Get_Date.getDateNow();
							/*轉換成特殊格式*/
							timePara = Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
							/*在原有紀錄中加入新的一筆*/
							String allTime = tmpLastTime + "\n" + timePara;

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
								System.out.println(File_Conf.deleteTmpFile());
								suc2 = false;
							}

							/*嘗試建立暫存檔*/
							if(!File_Conf.createTmpFile().equals("")){
								System.out.println("暫存檔建立失敗...\n錯誤資訊為：");
								System.out.println(File_Conf.createTmpFile());
							}
							/*成功才執行寫入方法，立即執行第一次，之後每過一秒執行一次*/
							else{
								tr0.schedule(new File_Timer(), 0, 1000);
								suc3 = true;
							}

							/*設定JVM關閉後自動刪除暫存檔*/
							if (!File_Conf.deleteTmpFile().equals("")) {
								System.out.println("暫存檔排定清除失敗...\n錯誤資訊為：");
								System.out.println(File_Conf.deleteTmpFile());
								suc4 = false;
							}

							/*全部正常才執行*/
							if (suc1 && suc2 && suc3 && suc4) {
								System.out.println("目前時間為：" + myTime);

								/*嘗試寫入加密後的設定*/
								timePara = encodeTime+"\n"+encodeMacAddress+"\n"+Get_Key.getEncodeKeyLong(encodeTime, encodeMacAddress, myTime);
								if(!File_Conf.writeConfFile(timePara).equals("")){
									System.out.println("寫入設定時出現錯誤...，程式無法繼續執行");
								}
								else{
									/*嘗試建立輸出檔*/
									if(!File_Conf.createResultFile().equals("")){
										System.out.println("輸出檔建立失敗...\n錯誤資訊為：");
										System.out.println(File_Conf.createResultFile());
									}
									/*成功才執行寫入方法*/
									else{
										/*嘗試寫入驗證資訊到輸出檔*/
										if(!Get_Token.writeToken(encodeTime, encodeMacAddress).equals("")){
											System.out.println("寫入驗證時出現錯誤...，程式無法繼續執行");
										}
										/*成功*/
										else{
											char [] inputKeySpace = inputCon.readPassword("請輸入密文(限英文):");
											inputKey = new String(inputKeySpace);

											if(inputKey.equals("")){
												System.out.println("無效的輸入值，請重新輸入!");
											}
											else{
												/*顯示結果*/
												displayResult(myTime, inputKey, encodeTime, encodeMacAddress, decodeTime, decodeMacAddress);
											}
										}
									}
								}
							} else {
								System.out.println("出現預期外的錯誤...，程式無法繼續執行");
							}
						}
					}
				}

				System.out.print("\n如要結束程式，請輸入「Exit」:");
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

	/**
	 *
	 * @param myTime 當下時間(String)
	 * @param inputKey 使用者輸入的資訊(String)
	 * @param encodeTime 初步加密後的時戳(String)
	 * @param encodeMacAddress 初步加密後的網卡資訊(String)
	 * @param decodeTime 解密後的時戳(String)
	 * @param decodeMacAddress 解密後的網卡資訊(String)
	 *
	 */
	/*顯示結果*/
	private static void displayResult(String myTime, String inputKey, String encodeTime, String encodeMacAddress, String decodeTime, String decodeMacAddress){
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

		String totalResult = myTime + "\n" + inputKey + "\n" + result + "\n" + recoveryKey + "\n" + resultMid + "\n" + recoveryKeyMid + "\n" + resultLong + "\n" + recoveryKeyLong;

		/*確認輸出檔是否存在*/
		if(!File_Conf.checkResultFile()){
			/*嘗試建立輸出檔*/
			if(!File_Conf.createResultFile().equals("")){
				System.out.println("輸出檔建立失敗...\n錯誤資訊為：");
				System.out.println(File_Conf.createResultFile());
			}
			else{
				/*嘗試寫入輸出檔*/
				if(!File_Conf.writeResultFile(totalResult).equals("")){
					System.out.println("輸出檔寫入失敗...\n錯誤資訊為：");
					System.out.println(File_Conf.writeResultFile(totalResult));
				}
				else{
					System.out.println("輸出檔案成功!");
				}
			}
		}
		/*原有的輸出檔有內容*/
		else if(!File_Conf.readResultFile().equals("")){
			/*嘗試寫入輸出檔*/
			String oldResult = File_Conf.readResultFile();
			String newResult = oldResult + "\n" + totalResult;
			if(!File_Conf.writeResultFile(newResult).equals("")){
				System.out.println("輸出檔寫入失敗...\n錯誤資訊為：");
				System.out.println(File_Conf.writeResultFile(newResult));
			}
			else{
				System.out.println("輸出檔案成功!");
			}
		}
		/*原有的輸出檔無內容視為異常*/
		else{
			System.out.println("輸出檔案失敗!檔案出現異常...");
		}
	}
}
