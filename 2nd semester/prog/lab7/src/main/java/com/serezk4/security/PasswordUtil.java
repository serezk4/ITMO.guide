package com.serezk4.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for handling password-related operations.
 *
 * <p>The {@code PasswordUtil} class provides methods for securely hashing passwords using
 * the SHA-224 hashing algorithm. It is designed to ensure that passwords are stored in
 * a non-reversible format to enhance security.</p>
 *
 * <p>This class cannot be instantiated. Use its static methods directly.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * String hashedPassword = PasswordUtil.hashPassword("mySecurePassword");
 * System.out.println(hashedPassword);
 * </pre>
 *
 * <p>Thread Safety:</p>
 * <p>The class is thread-safe as it uses local variables for hashing operations.</p>
 *
 * @author serezk4
 * @version 1.0
 * @since 1.0
 */
public final class PasswordUtil {

    /**
     * Private constructor to prevent instantiation of the utility class.
     *
     * <p>This ensures that the class cannot be instantiated, as it is intended
     * to be used solely through its static methods.</p>
     */
    private PasswordUtil() {
    }

    /**
     * Hashes the provided password using the SHA-224 algorithm.
     *
     * <p>The method converts the input password into a fixed-length hash using the
     * SHA-224 hashing algorithm. The resulting hash is returned as a hexadecimal
     * string.</p>
     *
     * <p>Failure Handling:</p>
     * <ul>
     *     <li>Throws a {@link RuntimeException} if the SHA-224 algorithm is not available.</li>
     * </ul>
     *
     * @param password the plain-text password to hash.
     * @return the hashed password as a hexadecimal string.
     * @throws RuntimeException if the hashing algorithm is not available.
     * @see MessageDigest
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");

            byte[] messageDigest = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) sb.append(String.format("%02x", b));

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
