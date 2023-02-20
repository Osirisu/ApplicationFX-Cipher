package Cipher.Alphabet;

public interface IAlphabet {
    public int getPositionForLiters(char liters);
    public char getLitersForPosition(int position);
    public String getNameAlphabet();
    public int getSizeAlphabet();
    public int getSizeSymbol();
    public int getStartLowerNotation();
}
