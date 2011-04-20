package bezpecnost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

class KBE2
{
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
  HashMap pairsToEncrypt;
  HashMap pairsToDecrypt;
  String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
  static Random random = new Random();
  String output;
  static StringBuilder outputBuilder = new StringBuilder();
  boolean parse() throws Exception {
    this.token = nextToken(" ");
    if (this.token.equals("end")) {
        System.out.println(outputBuilder);
        return false;
        }

    if (this.token.equals("e")) {
      this.password = nextToken(" ").trim().toUpperCase();
      this.textToEncrypt = nextToken("\n").trim().toUpperCase();
      this.prepareTable(this.password, this.alphabet);
      this.prepareStringToEncrypt();
      this.encrypt();
      outputBuilder.append(this.returnEncryptedText());
      outputBuilder.append("\n");

    }
    else if (this.token.equals("d")) {
      this.password = nextToken(" ").trim().toUpperCase();
      this.textToDecrypt = nextToken("\n").trim().toUpperCase();
      this.prepareTable(this.password, this.alphabet);
      this.prepareStringToDecrypt();
      this.decrypt();
      outputBuilder.append(this.returnDecryptedText());
      outputBuilder.append("\n");
    }

    return true;
  }

  void encrypt() {
    this.encryptedString = "";
    Iterator iterator = new TreeMap(this.pairsToEncrypt).keySet().iterator();
    while (iterator.hasNext()) {
      int key = ((Integer)iterator.next()).intValue();
      String tmp = (String)this.pairsToEncrypt.get(Integer.valueOf(key));
      char first = tmp.charAt(0);
      char second = tmp.charAt(1);
      int xPositionInTableFirst = this.plaifairTable[(first % 'A')] % 5;
      int yPositionInTableFirst = this.plaifairTable[(first % 'A')] / 5;
      int xPositionInTableSecond = this.plaifairTable[(second % 'A')] % 5;
      int yPositionInTableSecond = this.plaifairTable[(second % 'A')] / 5;

      if (yPositionInTableFirst == yPositionInTableSecond)
      {

        xPositionInTableFirst = (xPositionInTableFirst+1) % 5;
        xPositionInTableSecond = (xPositionInTableSecond+1) % 5;
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else if (xPositionInTableFirst == xPositionInTableSecond)
      {

        yPositionInTableFirst = (yPositionInTableFirst+1) % 5;
        yPositionInTableSecond = (yPositionInTableSecond+1) % 5;
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else {

        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableSecond * 5)]);
      }
    }
  }
  /*
   *
   *  ===============================
   *  DODELAT TO TADY  ZACATEK
   * ================================
   *
   *
   */
  void decrypt(){
      this.decryptedString = "";
    Iterator iterator = new TreeMap(this.pairsToEncrypt).keySet().iterator();
    while (iterator.hasNext()) {
      int key = ((Integer)iterator.next()).intValue();
      String tmp = (String)this.pairsToDecrypt.get(Integer.valueOf(key));
      char first = tmp.charAt(0);
      char second = tmp.charAt(1);
      int xPositionInTableFirst = this.plaifairTable[(first % 'A')] % 5;
      int yPositionInTableFirst = this.plaifairTable[(first % 'A')] / 5;
      int xPositionInTableSecond = this.plaifairTable[(second % 'A')] % 5;
      int yPositionInTableSecond = this.plaifairTable[(second % 'A')] / 5;

      if (yPositionInTableFirst == yPositionInTableSecond)
      {
        xPositionInTableFirst = (xPositionInTableFirst + 4) % 5;
        xPositionInTableSecond = (xPositionInTableSecond + 4) % 5;
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else if (xPositionInTableFirst == xPositionInTableSecond)
      {
        yPositionInTableFirst = (yPositionInTableFirst + 4) % 5;
        yPositionInTableSecond = (yPositionInTableSecond + 4) % 5;
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else {
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableFirst * 5)]);
        this.decryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableSecond * 5)]);
      }
    }
  }
/*
   *
   *  ===============================
   *  DODELAT TO TADY KONEC
   * ================================
   *
   *
   */
  String returnEncryptedText(){
      StringBuilder strb = new StringBuilder(this.encryptedString);
      int howManyTimes = strb.length()/5;
      for(int i = 1;i<=howManyTimes;i++){
           strb.insert((i*5)+i-1, " ");
      }

      return strb.toString();
  }

String returnDecryptedText(){
      StringBuilder strb = new StringBuilder(this.decryptedString);
      int howManyTimes = strb.length()/5;
      for(int i = 1;i<=howManyTimes;i++){
           strb.insert((i*5)+i-1, " ");
      }

      return strb.toString();
  }
  void prepareTable(String key, String alphabet) {
    String alterAlphabet = alphabet;
    for (char ch : key.toCharArray()) {
      alterAlphabet = alterAlphabet.replaceAll(String.valueOf(ch), " ");
    }
    alterAlphabet = alterAlphabet.replaceAll(" ", "");
    alterAlphabet = key + alterAlphabet;
    this.plaifairTable = new int[26];
    this.plaifairTabelInverted = new char[25];
    int hashTableKey = 0;
    for (char ch : alterAlphabet.toCharArray()) {
      this.plaifairTable[(ch % 'A')] = hashTableKey;
      this.plaifairTabelInverted[(hashTableKey++)] = ch;
    }
  }


  void prepareStringToDecrypt(){
      this.pairsToDecrypt = new HashMap();
      String alterTextToDecrypt = this.textToDecrypt.replaceAll(" ", "");
      int increment = 0;
    int index = 0;
    if (alterTextToDecrypt.length() % 2 != 0) alterTextToDecrypt = alterTextToDecrypt + "X";
    for (char ch : alterTextToDecrypt.toCharArray())
      if (increment % 2 == 0)
      {
        this.pair = String.valueOf(ch);
        increment++;
      }
      else {
        this.pair += String.valueOf(ch);
        this.pairsToDecrypt.put(Integer.valueOf(index++), this.pair);

        increment++;
      }
  }

  void prepareStringToEncrypt()
  {
    this.pairsToEncrypt = new HashMap();
    String alterTextToEncrypt = this.textToEncrypt.replaceAll(" ", "").replaceAll("[.]", "");
    int increment = 0;
    int index = 0;
    if (alterTextToEncrypt.length() % 2 != 0) alterTextToEncrypt = alterTextToEncrypt.concat("X");
    for (char ch : alterTextToEncrypt.toCharArray()){
      if (increment % 2 == 0)
      {
        this.pair = String.valueOf(ch);
        increment++;
      }
      else if (String.valueOf(ch).equals(this.pair)) {
        this.pair += getFiller();
        this.pairsToEncrypt.put(index++, this.pair);
        increment++;
        this.pair = String.valueOf(ch);
        increment++;
      }
      else {
        this.pair += String.valueOf(ch);
        this.pairsToEncrypt.put(index++, this.pair);

        increment++;
      }}
  }

  void encryptString()
  {
  }

  static String getFiller()
  {    
    return "X";
  }

  public static void main(String[] args) throws Exception
  {
    KBE2 inst = new KBE2();

    inst.input = "e radio Education is what remains after one has forgotten everything he learned in school.\n end \n LEQGI QORMO QXKRP IFLDO LUICS FARLG KRTER ANGSY SFLGX BIVPM OMBNL SCDOH LEOMQ ENRDN \n\n\n";
    inst.st = new StringTokenizer(inst.input, "");
    while (inst.parse());

  }

  String nextToken(String divider) throws IOException
  {
    if (this.st.hasMoreTokens()) return this.st.nextToken(divider);
    this.st = new StringTokenizer (stdin.readLine());
    return this.st.nextToken();
  }
}