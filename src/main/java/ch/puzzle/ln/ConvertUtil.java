package ch.puzzle.ln;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConvertUtil {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private final static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String bytesToHex(byte[] input) {
        char[] hexChars = new char[input.length * 2];
        for (int j = 0; j < input.length; j++) {
            int v = input[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    public static byte[] hexToBytes(String input) {
        int len = input.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                + Character.digit(input.charAt(i + 1), 16));
        }
        return data;
    }

    public static Instant unixTimestampToInstant(Long unixTimestamp) {
        return Objects.equals(unixTimestamp, 0L) ? null : Instant.ofEpochSecond(unixTimestamp);
    }

    public static String formatCurrency(String ticker, Double value) {
        return String.format("%s %s", ticker, formatNumber(value, 2));
    }

    public static String formatNumber(Double value, int numDigits) {
        return String.format("%." + numDigits + "f", value);
    }

    public static String formatTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(TIME_FORMATTER);
    }
}
