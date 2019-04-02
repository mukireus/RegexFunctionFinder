/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexfunctionfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *
 * @author Muhammet Ömer    
 */
public class ReadFile {
    // Dosya okuma için gerekli olan veri tipleri tanımlandı.
    private Scanner scanner;
    protected String file;
    
    // Sayaçlar tanımlandı.
    private int operatorCounter = 0;
    private int functionCounter = 0;
    private int parameterCounter= 0;
    
    // Bulunan fonksiyon ve parametleri tutacak List'ler tanımlandı.
    private final List<String> functions;
    private final List<String> parameters;
    private final List<String> parameterMain;
    
    // Kurucu fonksiyon
    public ReadFile() {
        this.functions = new ArrayList<>();
        this.parameters = new ArrayList<>();
        this.parameterMain = new ArrayList<>();
    }
    
    // Dosyayı açan fonksiyon
    public void openFile(){
        try{
            scanner = new Scanner(new File("Program.c"));
            scanner.useDelimiter("[\n]");
        }
        catch(FileNotFoundException e){
            System.out.println("Dosya Bulunamadi");
        }
    }
    
    // Dosyayı okuyan fonksiyon
    public void readFile(){
        while(scanner.hasNext()){
            file = scanner.next();
            //System.out.printf("%s\n",file);
            findOperator(file);
            findFunction(file);
            findParameter(file);
        }
    }
    
    // Dosya okuması tamamlandıktan sonra dosyayı kapatan fonksiyon
    public void closeFile(){
        scanner.close();
    }
    
    // Operatörleri bulan fonksiyon (Parametre herhangi gönderilen bir dosyadır.)
    public void findOperator(String file){
        // Tüm operatörleri bulur ve sayaçta bir arttırır.
        String operatorPattern = "['+''-''*''/''=''<''>''<=''>=''&''|''^''!''\\-?']";
        Pattern pattern = Pattern.compile(operatorPattern);
        Matcher matcher = pattern.matcher(file);
        while (matcher.find()) {
            operatorCounter++;
        }
        
        // String içerisine yazılan operatörleri sayaçtan çıkarır.
        String doubleQuotesPatternString = "\".*\"";
        Pattern doubleQuotesPattern = Pattern.compile(doubleQuotesPatternString);
        Matcher doubleQuotesMatcher = doubleQuotesPattern.matcher(file);
        while (doubleQuotesMatcher.find()) {
            String printOperator = doubleQuotesMatcher.group();
            Pattern patternPrintOperator = Pattern.compile(operatorPattern);
            Matcher matcherPrintOperator = patternPrintOperator.matcher(printOperator);
            while (matcherPrintOperator.find()) {
                operatorCounter--;
            }
        }
        
        // İki operatör olup tek işlem yapan (örneğin ++ "Arttırma" operatörü) sayaçtan çıkarır.
        String doubleOperatorPatternString = "\\+\\+|--|\\+\\+|&&|\\+\\+|==|\\||\\\\instanceof";
        Pattern doubleOperatorPattern = Pattern.compile(doubleOperatorPatternString);
        Matcher doubleOperatorMatcher = doubleOperatorPattern.matcher(file);
        while(doubleOperatorMatcher.find()){
            operatorCounter--;
        }
        
        // Yorum satırlarında yazılan operatörleri sayaçtan çıkarır.
        String commentPatternString = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|(//.*)";
        Pattern commentPattern = Pattern.compile(commentPatternString);
        Matcher commentMatcher = commentPattern.matcher(file);
        while (commentMatcher.find()) {
            String printOperator = commentMatcher.group();
            Pattern patternPrintOperator = Pattern.compile(operatorPattern);
            Matcher matcherPrintOperator = patternPrintOperator.matcher(printOperator);
            while (matcherPrintOperator.find()) {
                operatorCounter--;
            }
        }
    }
    
    // Fonksiyonları bulan fonksiyon (Parametre herhangi gönderilen bir dosyadır.)
    public void findFunction(String file){
        
        // Tüm fonksiyonları bulur, Liste atar ve sayaçta bir arttırır.
        String functionPattern = "\\w+ +\\w+ *\\([^\\)]*\\) *\\{";
        Pattern pattern = Pattern.compile(functionPattern);
        Matcher matcher = pattern.matcher(file);
        while (matcher.find()) {
            String function = matcher.group();
            String[] splitedFunction = function.split(" ");
            String splitedFun = splitedFunction[1];
            String[] splitedParameter = splitedFun.split("\\(");
            String spliteEnd = splitedParameter[0];
            //Eğer main ise ayrı tutulmaktadır.
            if(spliteEnd.equals("main")){
                // 
            }else
                functions.add(spliteEnd);
            functionCounter++;
        }
    }
    
    // Parametreleri bulan fonksiyon (Parametre herhangi gönderilen bir dosyadır.)
    public void findParameter(String file){
        /* Tüm fonksiyonları bulur. [Fonksiyon içerisinde yazılan new classları bulmaması için +
         (Örneğin scanner = new Scanner(new File("Program.c"));]  */
        String functionPattern = "\\w+ +\\w+ *\\([^\\)]*\\) *\\{";
        Pattern pattern = Pattern.compile(functionPattern);
        Matcher matcher = pattern.matcher(file);
        while (matcher.find()) {
            String function = matcher.group();
            // Fonksiyonlardan parametreleri ayırır.
            String[] splitedFunction = function.split(" ");
            String splitedFun = splitedFunction[1];
            String[] splitedParameter = splitedFun.split("\\(");
            String spliteEnd = splitedParameter[0];
            // Eğer parametresi yok ise boş atama yapar. 
            String emptyParameter = "\\([^\\)]*\\)";
            Pattern emptyPattern = Pattern.compile(emptyParameter);
            Matcher emptyMatcher = emptyPattern.matcher(function);
            while(emptyMatcher.find())
            {
                String test = emptyMatcher.group();
                if(test.equals("()"))
                {
                    parameters.add(" ");
                }
            }
            if(spliteEnd.equals("main")){
                // Eğer fonksiyon main ise ayrı bir listeye atıyor. 
                String parameterMainPatternString = "(\\((?:\\s*[^\\s,]+\\s+[^\\s,]+\\s*,)*\\s*[^\\s,]+\\s+[^\\s,]+\\s*\\))";
                Pattern parameterMainPattern = Pattern.compile(parameterMainPatternString);
                Matcher parameterMainMatcher = parameterMainPattern.matcher(function);
                while (parameterMainMatcher.find()) {
                    String parametre = parameterMainMatcher.group();
                    String[] splitedParameterMain = parametre.split(",");
                    int uzunluk = splitedParameterMain.length;
                    parameterCounter += uzunluk;
                    parameterMain.add(parametre);
                }
            }else{
                // Değilse parametre listesine atıyor.
                String parameterPatternString = "(\\((?:\\s*[^\\s,]+\\s+[^\\s,]+\\s*,)*\\s*[^\\s,]+\\s+[^\\s,]+\\s*\\))";
                Pattern parameterPattern = Pattern.compile(parameterPatternString);
                Matcher parameterMatcher = parameterPattern.matcher(function);
                while (parameterMatcher.find()) {
                    String parametre = parameterMatcher.group();
                    String[] splitedParamete = parametre.split(",");
                    int uzunluk = splitedParamete.length;
                    parameterCounter += uzunluk;
                    //System.out.println("test "+parametre);
                    parameters.add(parametre);
                }
            }
        }
    }
   
    // Yazdırma işlemini yapan fonksiyon
     public void printScreen(){
        System.out.println("Toplam Operatör Sayısı: " + operatorCounter);
        System.out.println("Toplam Fonksiyon Sayısı: " + functionCounter);
        System.out.println("Toplam Parametre Sayısı: " + parameterCounter);
        System.out.println("Fonksiyon İsimleri: ");
        if(parameters.isEmpty())
        {
            for (int i = 0; i < functions.size(); i++){
                System.out.println(functions.get(i)+ " - Parametreler: ");
            }
        }
        else{
            for (int i = 0; i < functions.size(); i++){
                System.out.println(functions.get(i)+ " - Parametreler: " + parameters.get(i));
            }
        }
        if(parameterMain.isEmpty())
        {
            System.out.println("main - Parametreler: ");
        }else
            System.out.println("main - Parametreler: " + parameterMain.get(0));
     }
}
