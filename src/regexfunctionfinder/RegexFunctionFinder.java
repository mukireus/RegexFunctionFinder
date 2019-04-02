/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexfunctionfinder;

/**
 *
 * @author Muhammet Ömer
 */
public class RegexFunctionFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Test
        ReadFile r = new ReadFile();
        r.openFile();
        r.readFile();
        r.printScreen();
        r.closeFile();    
    }
}
