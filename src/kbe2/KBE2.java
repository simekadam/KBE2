package kbe2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

class KBE2
{
  String input;
  String token;
  String password;
  String pair;
  boolean endOfOpenText = false;
  String textToEncrypt;
  String testToDecrypt;
  String encryptedString;
  StringTokenizer st;
  int[] plaifairTable;
  char[] plaifairTabelInverted;
  HashMap pairsToEncrypt;
  String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
  static Random random = new Random();

  boolean parse() throws Exception {
    this.token = nextToken(" ");
    if (this.token.equals("end")) this.endOfOpenText = true;
    if (this.token.equals("e")) {
      this.password = nextToken(" ").trim().toUpperCase();
      this.textToEncrypt = nextToken("\n").trim().toUpperCase();
    }
    else if (this.endOfOpenText) {
      this.testToDecrypt = nextToken("").trim().toUpperCase();
      return false;
    }

    return true;
  }

  void encrypt() {
    this.encryptedString = "";
    Iterator iterator = this.pairsToEncrypt.keySet().iterator();
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
        xPositionInTableFirst = (xPositionInTableFirst + 1) % 5;
        xPositionInTableSecond = (xPositionInTableSecond + 1) % 5;
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else if (xPositionInTableFirst == xPositionInTableSecond)
      {
        yPositionInTableFirst = (yPositionInTableFirst + 1) % 5;
        yPositionInTableSecond = (yPositionInTableSecond + 1) % 5;
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableSecond * 5)]);
      }
      else {
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableSecond + yPositionInTableFirst * 5)]);
        this.encryptedString += String.valueOf(this.plaifairTabelInverted[(xPositionInTableFirst + yPositionInTableSecond * 5)]);
      }
    }
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

  void prepareStringToEncrypt()
  {
    this.pairsToEncrypt = new HashMap(25);
    String alterTextToEncrypt = this.textToEncrypt.replaceAll(" ", "").replaceAll("[.]", "");
    int increment = 0;
    int index = 0;
    if (alterTextToEncrypt.length() % 2 != 0) alterTextToEncrypt = alterTextToEncrypt + "X";
    for (char ch : alterTextToEncrypt.toCharArray())
      if (increment % 2 == 0)
      {
        this.pair = String.valueOf(ch);
        increment++;
      }
      else if (String.valueOf(ch).equals(this.pair)) {
        this.pair += getFiller();
        this.pairsToEncrypt.put(Integer.valueOf(index++), this.pair);

        increment++;
      }
      else {
        this.pair += String.valueOf(ch);
        this.pairsToEncrypt.put(Integer.valueOf(index++), this.pair);

        increment++;
      }
  }

  void encryptString()
  {
  }

  static String getFiller()
  {
    if (random.nextBoolean()) return "X";
    return "Z";
  }

  public static void main(String[] args) throws Exception
  {
    KBE2 inst = new KBE2();
    inst.input = "e radio Education is what remains after one has forgotten everything he learned in school.\n end \n LEQGI QORMO QXKRP IFLDO LUICS FARLG KRTER ANGSY SFLGX BIVPM OMBNL SCDOH LEOMQ ENRDN \n\n\n";
    inst.st = new StringTokenizer(inst.input, "");
    while (inst.parse());
    System.out.println(inst.password + "-" + inst.textToEncrypt + "-" + inst.testToDecrypt);
    inst.prepareTable(inst.password, inst.alphabet);
    inst.prepareStringToEncrypt();
    inst.encrypt();
    System.out.println(inst.encryptedString);
  }

  String nextToken(String divider)
    throws Exception
  {
    if (this.st.hasMoreTokens()) return this.st.nextToken(divider);
    return "JETOCELYVPRDELI";
  }
}