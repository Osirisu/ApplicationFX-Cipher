package Cipher.Alphabet;

import java.util.HashMap;

public class Alphabet implements IAlphabet{
    private final int startAlphabetLower;
    private int countSpecSymbol;
    protected final Character[] symbol;
    protected final int countAllSymbol;
    protected String nameAlphabet;
    protected int sizeAlphabet;
    protected HashMap<Integer, Character> litersForPosition = new HashMap<>();
    protected HashMap<Character, Integer> positionForLiters = new HashMap<>();

    public Alphabet(LanguageCollection language){
        this.nameAlphabet = language.name();
        this.sizeAlphabet = language.sizeAlphabet();
        this.startAlphabetLower = language.startAlphabetLower();

        this.symbol = new Character[]
                {
                        ' ',',','.','!','?',
                        '0','1','2','3','4',
                        '5','6','7','8','9'
                };

        setLiters();
        setPosition();
        insertSpecSymbol();

        this.countAllSymbol = this.sizeAlphabet + this.countSpecSymbol;
    }

    protected void setLiters(){
        for (int i = 0; i < sizeAlphabet; ++i){
            litersForPosition.put(i, (char)(startAlphabetLower +i));
        }
    };
    protected void setPosition(){
        for (int i = 0; i < sizeAlphabet; ++i){
            positionForLiters.put((char)(startAlphabetLower +i), i);
        }
    };

    private void insertSpecSymbol(){
        int countSymbol = sizeAlphabet - 1;

        for (Character character : this.symbol) {
            int index = ++countSymbol;

            litersForPosition.put(index, character);
            positionForLiters.put(character, index);
        }

        this.countSpecSymbol = countSymbol - this.sizeAlphabet + 1;
    }

    @Override
    public int getPositionForLiters(char liters) {
        return positionForLiters.get(liters);
    }
    @Override
    public char getLitersForPosition(int position) {
        return litersForPosition.get(position);
    }
    @Override
    public String getNameAlphabet() {
        return nameAlphabet;
    }
    @Override
    public int getSizeAlphabet() {
        return sizeAlphabet;
    }
    @Override
    public int getSizeSymbol() {
        return countAllSymbol;
    }
    @Override
    public int getStartLowerNotation() {
        return startAlphabetLower;
    }
}
