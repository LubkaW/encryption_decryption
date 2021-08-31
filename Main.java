package com.company;

import java.util.*;
import java.io.*;



public class Main {

    public static void main(String[] args) {

        int key;
        String mode;
        String data;
        String inputDataPath = "";
        String outputDataPath;
        String alg;
        File output = null;
        File input = null;
        Scanner sc = new Scanner(System.in);
        String answer = "";
        EncrypterDecrypter encrypterDecrypter = new EncrypterDecrypter();

        key = Arrays.asList(args).contains("-key") ? Integer.parseInt(args[Arrays.asList(args).indexOf("-key")+1]) : 0;
        mode = Arrays.asList(args).contains("-mode") ? args[Arrays.asList(args).indexOf("-mode")+1] : "enc";
        data = Arrays.asList(args).contains("-data") ? args[Arrays.asList(args).indexOf("-data")+1] : "";
        alg = Arrays.asList(args).contains("-alg") ? args[Arrays.asList(args).indexOf("-alg")+1] : "";

        if(data.equals("")){
        inputDataPath = Arrays.asList(args).contains("-in") ? args[Arrays.asList(args).indexOf("-in")+1] : "";
        }
        outputDataPath = Arrays.asList(args).contains("-out") ? args[Arrays.asList(args).indexOf("-out")+1] : "";

        if(!outputDataPath.equals(""))  output = new File(outputDataPath);
        if(!inputDataPath.equals("")) input = new File(inputDataPath);



        if(data.equals("") ) {
            if (!inputDataPath.equals("")) {
                data = readFile(input);
            } else {
                data = sc.nextLine();
            }
        }

                switch(mode){

            case "enc":
                if(alg.equals("unicode")){
                    encrypterDecrypter.setStrategy(new EncryptByUnicode());
                }else{
                    encrypterDecrypter.setStrategy(new EncryptByShift());
                }
                break;

            case "dec":
                if(alg.equals("unicode")){
                    encrypterDecrypter.setStrategy(new DecryptByUnicode());
                }else{
                    encrypterDecrypter.setStrategy(new DecryptByShift());
                }
                break;
        }

        answer = encrypterDecrypter.doTask(data,key);


        if(outputDataPath.equals("")){
            System.out.println(answer);
        }else{
            writeToFile(output,answer);
        }


    }







    public static void writeToFile(File file,String answer){
            try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
                br.write(answer);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

    }

    public static String readFile(File file){
            String readedMessage = "";
            try(BufferedReader bt = new BufferedReader(new FileReader(file))){
                readedMessage = bt.readLine();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
    return readedMessage;
    }




}


//Strategy interface!
interface EncodeDecodeStrategy {
    StringBuilder builder = new StringBuilder();
    String doTask(String message, int key);
}

// Encrypt message by Shifting CLASS!
class EncryptByShift implements EncodeDecodeStrategy {

    @Override
    public String doTask(String message, int key){
        builder.append(message);
        for(int i = 0; i<builder.length();i++){
            char c = builder.charAt(i);
            if(c > 64 && c<91){
                if(c+key>91){
                    builder.setCharAt(i,(char)(c+key-90+64));
                }else{
                    builder.setCharAt(i,(char)(c+key));
                }
            }else{
                if(c>96 && c<123) {
                    if(c+key>123){
                        builder.setCharAt(i,(char)(c+key-122+96));
                    }else{
                        builder.setCharAt(i,(char)(c+key));
                    }
                }
            }



        }
        return builder.toString();
    }
}

// Decrypt message by shifting CLASS!
class DecryptByShift implements EncodeDecodeStrategy {

    @Override
    public String doTask(String message, int key){
        builder.append(message);
        for(int i = 0; i<builder.length();i++){
            char c = builder.charAt(i);
            if(c > 64 && c<91){
                if(c-key<65){
                    builder.setCharAt(i,(char)(c-key+90-64));
                }else{
                    builder.setCharAt(i,(char)(c-key));
                }
            }else{
                if(c>96 && c<123) {
                    if(c-key<97){
                        builder.setCharAt(i,(char)(c-key+122-96));
                    }else{
                        builder.setCharAt(i,(char)(c-key));
                    }
                }
            }



        }
        return builder.toString();


    }
}

//Encrypt by UnicodeChange CLASS!
class EncryptByUnicode implements EncodeDecodeStrategy {

    @Override
    public String doTask(String message, int key) {
        builder.append(message);
        for(int i = 0; i< message.length();i++){
            builder.setCharAt(i,(char)(builder.charAt(i)+key));
        }

        return builder.toString();
    }

}

//Decrypt by unicode Class
class DecryptByUnicode implements  EncodeDecodeStrategy{

    @Override
    public String doTask(String message, int key) {
        builder.append(message);
        for(int i = 0; i< message.length();i++){
            builder.setCharAt(i,(char)(builder.charAt(i)-key));
        }

        return builder.toString();
    }
}

class EncrypterDecrypter{

    private EncodeDecodeStrategy strategy;

    public void setStrategy(EncodeDecodeStrategy strategy){
        this.strategy = strategy;
    }

    public String doTask(String message, int key){
        return this.strategy.doTask(message,key);
    }

}