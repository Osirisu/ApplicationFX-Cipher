package Cipher.Cipher;

//import javax.security.auth.kerberos.EncryptionKey;

public interface Cipher {
    public static int ENCRYPTION = 0;
    public static int DECRYPTION = 1;

    public String encryption();
    public String decryption();
}
