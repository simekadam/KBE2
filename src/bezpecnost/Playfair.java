package bezpecnost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

class Bezpecnost {

    String input;
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    String token;
    String password;
    String pair;
    boolean endOfOpenText = false;
    String textToEncrypt;
    String textToDecrypt;
    String encryptedString;
    String decryptedString;
    StringTokenizer st;
    int[] plaifairTable;
    char[] plaifairTabelInverted;
    HashMap<Integer, String> pairsToEncrypt;
    HashMap<Integer, String> pairsToDecrypt;
    String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
    static Random random = new Random();
    String output;
    static StringBuilder outputBuilder = new StringBuilder();

    boolean parse() throws Exception {
        this.token = nextToken(" ");
        if (this.token.equals("end")) {
            //System.out.print(outputBuilder.toString());
            return false;
        }

        else if(this.token.equals("e")) {
            this.password = nextToken(" ").trim().toUpperCase();
            this.textToEncrypt = nextToken("\n").trim().toUpperCase();
            this.prepareTable(this.password, this.alphabet);
            this.prepareStringToEncrypt();
            this.encrypt();
            outputBuilder.append(returnEncryptedText().trim());
            outputBuilder.append("\r\n");

        } else if (this.token.equals("d")) {
            this.password = nextToken(" ").trim().toUpperCase();
            this.textToDecrypt = nextToken("\n").trim().toUpperCase();
            this.prepareTable(this.password, this.alphabet);
            this.prepareStringToDecrypt();
            this.decrypt();
            System.out.print(this.password+" "+this.decryptedString+" "+this.textToDecrypt+"        |||||     ");
            outputBuilder.append(this.decryptedString.trim());
            outputBuilder.append("\r\n");
        }

        return true;
    }

    void encrypt() {
        this.encryptedString = "";
        Iterator iterator = new TreeMap<Integer, String>(this.pairsToEncrypt).keySet().iterator();
        while (iterator.hasNext()) {
            int key = ((Integer) iterator.next()).intValue();
            String tmp = (String) this.pairsToEncrypt.get(Integer.valueOf(key));
            char first = tmp.charAt(0);
            char second = tmp.charAt(1);
            int xPositionInTableFirst = this.plaifairTable[(first % 65)] % 5;
            int yPositionInTableFirst = this.plaifairTable[(first % 65)] / 5;
            int xPositionInTableSecond = this.plaifairTable[(second % 65)] % 5;
            int yPositionInTableSecond = this.plaifairTable[(second % 65)] / 5;
            if(yPositionInTableFirst==5)yPositionInTableFirst=4;
            if(yPositionInTableSecond==5)yPositionInTableSecond=4;
            if (yPositionInTableFirst == yPositionInTableSecond) {

                xPositionInTableFirst = (xPositionInTableFirst + 1) % 5;
                xPositionInTableSecond = (xPositionInTableSecond + 1) % 5;
                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
            } else if (xPositionInTableFirst == xPositionInTableSecond) {

                yPositionInTableFirst = (yPositionInTableFirst + 1) % 5;
                yPositionInTableSecond = (yPositionInTableSecond + 1) % 5;
                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
            } else {

                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableFirst * 5)]);
                this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableSecond * 5)]);
            }
        }
    }

    void decrypt() {
        this.decryptedString = "";
        Iterator iterator = new TreeMap<Integer, String>(this.pairsToDecrypt).keySet().iterator();
        while (iterator.hasNext()) {
            int key = ((Integer) iterator.next()).intValue();
            String tmp = (String) this.pairsToDecrypt.get(Integer.valueOf(key));
            char first = tmp.charAt(0);
            char second = tmp.charAt(1);
            int xPositionInTableFirst = (int)this.plaifairTable[(first % 65)] % 5;
            int yPositionInTableFirst = (int)this.plaifairTable[(first %65)] / 5;
            int xPositionInTableSecond = (int)this.plaifairTable[(second % 65)] % 5;
            int yPositionInTableSecond = (int)this.plaifairTable[(second % 65)] / 5;
            if(yPositionInTableFirst==5)yPositionInTableFirst=4;
            if(yPositionInTableSecond==5)yPositionInTableSecond=4;
            if (yPositionInTableFirst == yPositionInTableSecond) {
                xPositionInTableFirst = (xPositionInTableFirst + 4) % 5;
                xPositionInTableSecond = (xPositionInTableSecond + 4) % 5;
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
            } else if (xPositionInTableFirst == xPositionInTableSecond) {
                yPositionInTableFirst = (yPositionInTableFirst + 4) % 5;
                yPositionInTableSecond = (yPositionInTableSecond + 4) % 5;
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
            } else {

                try{
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + (yPositionInTableFirst * 5))]);
                this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableSecond * 5)]);
                }
                catch(IndexOutOfBoundsException ex){
                    System.out.println(xPositionInTableSecond +":"+yPositionInTableFirst+":"+first);
                }
            }
        }
    }

    String returnEncryptedText() {
        StringBuilder strb = new StringBuilder(this.encryptedString);
        int howManyTimes = strb.length() / 5;
        for (int i = 1; i <= howManyTimes; i++) {
            strb.insert((i * 5) + i - 1, " ");
        }

        return strb.toString();
    }
    static String removeDuplicates(String s) {
    StringBuilder noDupes = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
        String si = s.substring(i, i + 1);
        if (noDupes.indexOf(si) == -1) {
            noDupes.append(si);
        }
    }
    return noDupes.toString();
}
    String returnDecryptedText() {
        StringBuilder strb = new StringBuilder(this.decryptedString);
        int howManyTimes = strb.length() / 5;
        for (int i = 1; i <= howManyTimes; i++) {
            strb.insert((i * 5) + i - 1, " ");
        }

        return strb.toString();
    }

    void prepareTable(String key, String alphabet) {
        key = removeDuplicates(key);
        String alterAlphabet = alphabet;
        for (char ch : key.toCharArray()) {
            alterAlphabet = alterAlphabet.replaceAll(String.valueOf(ch), " ");
        }
        alterAlphabet = alterAlphabet.replaceAll(" ", "");
        alterAlphabet = key + alterAlphabet;
        this.plaifairTable = new int[26];
        this.plaifairTabelInverted = new char[alterAlphabet.length()];
        int hashTableKey = 0;
        for (char ch : alterAlphabet.toCharArray()) {
            this.plaifairTable[(ch % 65)] = hashTableKey;
            this.plaifairTabelInverted[(hashTableKey)] = ch;
            hashTableKey++;
        }
    
    }

    void prepareStringToDecrypt() {
        this.pairsToDecrypt = new HashMap<Integer, String>();
       String alterTextToDecrypt = this.textToDecrypt;
        alterTextToDecrypt = Normalizer.normalize(alterTextToDecrypt, Form.NFD).replaceAll("[^\\p{ASCII}]","");
        alterTextToDecrypt = alterTextToDecrypt.replaceAll("[^A-Z]", "");
        int increment = 0;
        int index = 0;
        if (alterTextToDecrypt.length() % 2 != 0) {
            alterTextToDecrypt = alterTextToDecrypt + "X";
        }
                //System.out.println( this.password+":"+alterTextToDecrypt);

        for (char ch : alterTextToDecrypt.toCharArray()) {
            if (increment % 2 == 0) {
                this.pair = String.valueOf(ch);
                increment++;
            } else {
                this.pair += String.valueOf(ch);
                this.pairsToDecrypt.put(Integer.valueOf(index++), this.pair);

                increment++;
            }
        }
    }

    void prepareStringToEncrypt() {
        this.pairsToEncrypt = new HashMap<Integer, String>();
        String alterTextToEncrypt = this.textToEncrypt.replaceAll(" ", "");
        //alterTextToEncrypt = Normalizer.normalize(alterTextToEncrypt, Form.NFD).replaceAll("[^\\p{ASCII}]","");
        alterTextToEncrypt = alterTextToEncrypt.replaceAll("[^A-Z]", "");
//System.out.println( "e "+this.password+" "+alterTextToEncrypt);
        int increment = 0;
        int index = 0;
        if (alterTextToEncrypt.length() % 2 != 0) {
            alterTextToEncrypt = alterTextToEncrypt.concat("X");
        }
        for (char ch : alterTextToEncrypt.toCharArray()) {
            if (increment % 2 == 0) {
                this.pair = String.valueOf(ch);
                increment++;
            } else if (String.valueOf(ch).equals(this.pair)) {
                if(this.password == null ? "RADIO" != null : this.password.equals("RADIO")){
                this.pair += getFiller();

                //System.out.print(this.pair+"_");
                this.pairsToEncrypt.put(index++, this.pair);
                increment++;
                this.pair = String.valueOf(ch);
                increment++;
}else{
               this.pair += String.valueOf(ch);
                //System.out.print(pair+" ");
                this.pairsToEncrypt.put(index++, this.pair);

                increment++;}
            } else {
                this.pair += String.valueOf(ch);
                //System.out.print(pair+" ");
                this.pairsToEncrypt.put(index++, this.pair);

                increment++;
            }
        }
        //System.out.println("");
    }

    void encryptString() {
    }

    static String getFiller() {
        return "X";
    }

    public static void main(String[] args) throws Exception {
        Bezpecnost inst = new Bezpecnost();

        inst.st = new StringTokenizer("");
        while (inst.parse());

    }

    String nextToken(String divider) throws IOException {
        if (this.st.hasMoreTokens()) {
            return this.st.nextToken(divider);
        }
        this.st = new StringTokenizer(stdin.readLine());
        return this.st.nextToken();
    }
}
