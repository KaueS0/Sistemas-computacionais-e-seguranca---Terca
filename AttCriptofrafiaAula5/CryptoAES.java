// Classe "CryptoAES.java"
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.imageio.IIOException;
public class CryptoAES
{
    private byte[] textoCifrado;
    private byte[] textoDecifrado;
    public CryptoAES()
    {
        textoCifrado = null;
        textoDecifrado = null;
    }
    public void geraChave(File fSim)
       throws IIOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException, IOException
    {
        //Gera uma chave simetra de 128 bits:
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey sk = kg.generateKey();
        //Grava a chave simetrica em formato serializado
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fSim));
        oos.writeObject(sk);
        oos.close();
    }
    public void geraCifra(byte[] texto, File fSim)
       throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException, ClassNotFoundException
       {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fSim));
        SecretKey iSim = (SecretKey) ois.readObject();
        byte[] chave = iSim.getEncoded();
        ois.close();
        Cipher aescf = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);
        aescf.init (Cipher.ENCRYPT_MODE, new SecretKeySpec (chave, "AES"), ivspec);
        textoCifrado = aescf.doFinal (texto);
       }
       public byte[] getTextoCifrado() throws Exception
       {
        return textoCifrado;
       }
       public void geraDecifra(byte[] texto, File fSim)
       throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException, ClassNotFoundException
       {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fSim));
        SecretKeySpec iSim = (SecretKeySpec) ois.readObject();
        ois.close();
        Cipher aescf = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivspec = new IvParameterSpec(new byte[16]);
        aescf.init (Cipher.DECRYPT_MODE, iSim, ivspec);
        textoDecifrado = aescf.doFinal (texto);
       }

       public byte[] getTextoDecifrado() throws Exception
       {
        return textoDecifrado;
       }
}