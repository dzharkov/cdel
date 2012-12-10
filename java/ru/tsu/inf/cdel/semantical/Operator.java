package ru.tsu.inf.cdel.semantical;

public class Operator {
  public static final int DIV = 0;
  public static final int MOD = 1;
  public static final int DIVIDE = 2;
  public static final int MULTIPLY = 3;
  public static final int PLUS = 4;
  public static final int MINUS = 5;
  
  public static final int OR = 6;
  public static final int AND = 7;
  
  public static final int NOT = 8;
  public static final int UNARY_MINUS = 9;
  
  public static final int MOREOREQUAL = 10;
  public static final int LESSOREQUAL = 11;
  public static final int LESS = 12;
  public static final int MORE = 13;
  public static final int EQUALS = 14;
  public static final int NOTEQUALS = 15;
  
  private static String[] representations = new String[] {"div", "mod", "/", "*", "+", "-", "or", "and", "!", "-", ">=", "<=", "<", ">", "==", "!="};
  
  private int type;
  public Operator(int type) {
      this.type = type;
  }
  
  public boolean equals(Operator o) {
      return o.type == type;
  }
  
  public static Operator createByString(String s, boolean unary) {
      if (s.equals("-")) {
          if (unary) {
              return new Operator(UNARY_MINUS);
          }
          return new Operator(MINUS);
      }
      for (int i = 0; i < representations.length; i++) {
          if (representations[i].equalsIgnoreCase(s)) {
              return new Operator(i);
          }
      }
      
      return null;
  }

    public int getType() {
        return type;
    }
  
}
