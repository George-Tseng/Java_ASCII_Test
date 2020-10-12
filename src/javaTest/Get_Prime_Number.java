package javaTest;

public class Get_Prime_Number {
    protected static int getMaxPrimeNumI(int input) {
        /*參數*/
        int inputNum, result = 0;

        try{
            inputNum = input;
            /*最小質數為2*/
            if(inputNum >= 2){
                for(int i = 2; i <= inputNum; i++){
                    /*boolean的設定要放入第一層才能不斷再生*/
                    boolean rightNumber = true;
                    /*因數最大值時為平方根*/
                    int stopNum = (int)Math.ceil(Math.sqrt(i));
                    /*2以外，只有奇數有可能是質數*/
                    if(i % 2 == 1){
                        for(int j = 2; j <= stopNum; j++){
                            if(i % j == 0){
                                rightNumber = false;
                                break;
                            }
                        }
                        /*質數時的行為*/
                        if(rightNumber){
                            result = i;
                        }
                    }
                    else if(i == 2){
                        result = i;
                    }
                }
            }
        } catch(NumberFormatException NFE){
            result = 0;
        }
        return result;
    }
}
