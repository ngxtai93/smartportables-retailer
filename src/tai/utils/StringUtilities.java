package tai.utils;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum StringUtilities {
    INSTANCE;

    private StringUtilities() {

    }

    public String generateRandomString(int size) {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public long generateRandomNumber(int digit) {
        // max = 10 digits
        long range = (long) Math.pow(10,digit);
        return ThreadLocalRandom.current().nextLong(range);
    }
    


    public boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public String filter(String input) {
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for(int i=0; i<input.length(); i++) {
          c = input.charAt(i);
          if (c == '<') {
            filtered.append("&lt;");
          } else if (c == '>') {
            filtered.append("&gt;");
          } else if (c == '"') {
            filtered.append("&quot;");
          } else if (c == '&') {
            filtered.append("&amp;");
          } else {
            filtered.append(c);
          }
        }
        return(filtered.toString());
      }
}