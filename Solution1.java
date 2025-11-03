import java.util.HashMap;
import java.util.Map;

/**
 * Converts a fraction to its decimal string, wrapping repeating parts in parentheses.
 */
class Solution1 {
    public String fractionToDecimal(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (numerator == 0) {
            return "0";
        }

        long numeratorLong = numerator;
        long denominatorLong = denominator;
        StringBuilder result = new StringBuilder();
        if ((numeratorLong < 0) ^ (denominatorLong < 0)) {
            result.append('-');
        }

        numeratorLong = Math.abs(numeratorLong);
        denominatorLong = Math.abs(denominatorLong);
        long integerPart = numeratorLong / denominatorLong;
        result.append(integerPart);

        long remainder = numeratorLong % denominatorLong;
        if (remainder == 0) {
            return result.toString();
        }

        result.append('.');

        Map<Long, Integer> remainderIndexMap = new HashMap<Long, Integer>();
        StringBuilder fractionPart = new StringBuilder();
        int index = 0;
        while (remainder != 0) {
            if (remainderIndexMap.containsKey(remainder)) {
                int insertIndex = remainderIndexMap.get(remainder);
                fractionPart.insert(insertIndex, '(');
                fractionPart.append(')');
                break;
            }

            remainderIndexMap.put(remainder, index);
            remainder *= 10;
            fractionPart.append(remainder / denominatorLong);
            remainder %= denominatorLong;
            index++;
        }

        result.append(fractionPart);
        return result.toString();
    }
}
