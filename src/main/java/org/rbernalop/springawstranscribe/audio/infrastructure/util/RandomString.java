package org.rbernalop.springawstranscribe.audio.infrastructure.util;

public class RandomString {

        public static String make() {
            int length = 10;
            String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder randomString = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int randomIndex = (int) (Math.random() * chars.length());
                randomString.append(chars.charAt(randomIndex));
            }
            return randomString.toString();
        }
}
