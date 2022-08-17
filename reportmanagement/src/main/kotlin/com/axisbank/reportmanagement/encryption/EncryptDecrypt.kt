package com.axisbank.reportmanagement.encryption

import software.amazon.ion.system.IonTextWriterBuilder.UTF8
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec


class EncryptDecrypt {
    private var secretKey: SecretKey? = null
    private val random: SecureRandom
    private var encryptCipher: Cipher? = null
    private var decryptCipher: Cipher? = null
    private var ivParams: IvParameterSpec? = null

    init {
        random = SecureRandom()
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey()
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val initializationVector = ByteArray(encryptCipher!!.getBlockSize())
            random.nextBytes(initializationVector)
            ivParams = IvParameterSpec(initializationVector)
            encryptCipher!!.init(Cipher.ENCRYPT_MODE, secretKey, ivParams)
            decryptCipher!!.init(Cipher.DECRYPT_MODE, secretKey, ivParams)
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    fun encrypt(plainText: String): String? {
        val bytes = plainText.toByteArray()
        var cipherText: ByteArray? = null
        try {
            cipherText = encryptCipher!!.doFinal(bytes)
            return Base64.getEncoder().encodeToString(cipherText)
        } catch (e: IllegalBlockSizeException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    fun encryptFile(fis: FileInputStream): String? {
        val data = ByteArray(fis.available())
        fis.read(data)
        var cipherText: ByteArray? = null
        try {
            cipherText = encryptCipher!!.doFinal(data)
            return Base64.getEncoder().encodeToString(cipherText)
        } catch (e: IllegalBlockSizeException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        return null
    }

    fun decrypt(cipherText: String): String? {
        var plainText: ByteArray? = null
        try {
            plainText = decryptCipher!!.doFinal(Base64.getDecoder().decode(cipherText.toByteArray()))
            return String(plainText, UTF8)
        } catch (e: IllegalBlockSizeException) {
            // TODO Auto-generated catch block
            println(e.message)
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            println(e.message)
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            // TODO Auto-generated catch block
            println(e.message)
            e.printStackTrace()
        }
        return null
    }

    @Throws(IllegalBlockSizeException::class, BadPaddingException::class, IOException::class)
    fun decryptFile(str: String) {
        val fos =
            FileOutputStream("C:\\\\Users\\\\Welcome\\\\Desktop\\\\Green home Foods\\\\images\\\\addmealDecrypted.jpg")
        val Encrypteddata = str.toByteArray()
        var decrypteddata: ByteArray? = null
        decrypteddata = decryptCipher!!.doFinal(Base64.getDecoder().decode(Encrypteddata))
        fos.write(decrypteddata)
    }

    @Throws(IllegalBlockSizeException::class, BadPaddingException::class)
    fun decryptByteArray(cypher: ByteArray?): ByteArray? {
        var decrypteddata: ByteArray? = null
        decrypteddata = decryptCipher!!.doFinal(Base64.getDecoder().decode(cypher))
        return decrypteddata
    }
}