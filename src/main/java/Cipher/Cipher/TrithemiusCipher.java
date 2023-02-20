package Cipher.Cipher;

import Cipher.Alphabet.IAlphabet;

public class TrithemiusCipher implements Cipher{
    private final String startText;
    private final IAlphabet alphabet;

    @Override
    public String encryption() {
        return encryptionOrDecryption(ENCRYPTION);
    }
    @Override
    public String decryption() {
        return encryptionOrDecryption(DECRYPTION);
    }

    private String encryptionOrDecryption(int type){
        StringBuilder encryption = new StringBuilder();
        int sizeAlphabet = alphabet.getSizeSymbol();

        for (int i = 0; i < startText.length(); ++i){
            char ch = startText.charAt(i);
            int shift = (int) (2*Math.pow(i,2) + 5*Math.pow(i,1) + 3);

            char positionCipher;
            try {
                int positionCh = alphabet.getPositionForLiters(ch);

                int temp;
                if (type == ENCRYPTION)
                    temp = positionCh + shift;
                else
                    temp = positionCh - shift;

                while (temp < 0)
                    temp += sizeAlphabet;

                int formula = temp % sizeAlphabet;
                positionCipher = alphabet.getLitersForPosition(formula);
            }catch (Exception e){
                positionCipher = ch;
            }

            encryption.append(positionCipher);
        }
        return encryption.toString();
    }

    public TrithemiusCipher(String startText, IAlphabet alphabet){
        this.startText = startText.toLowerCase();
        this.alphabet = alphabet;
    }
}
