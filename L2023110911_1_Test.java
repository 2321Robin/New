import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试用例设计的总体原则：采用等价类划分结合边界值分析，覆盖有限小数、循环小数、符号变化、零值以及异常输入等场景。
 */
public class L2023110911_1_Test {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface CaseDescription {
        String value();
    }

    public static void main(String[] args) {
        L2023110911_1_Test testInstance = new L2023110911_1_Test();
        List<String> failures = new ArrayList<>();
        int executed = 0;

        for (Method method : L2023110911_1_Test.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(CaseDescription.class)) {
                executed++;
                try {
                    method.invoke(testInstance);
                } catch (InvocationTargetException ex) {
                    Throwable cause = ex.getCause();
                    failures.add(method.getName() + " failed: " + (cause != null ? cause.getMessage() : ex.getMessage()));
                } catch (IllegalAccessException ex) {
                    failures.add(method.getName() + " failed: " + ex.getMessage());
                }
            }
        }

        if (failures.isEmpty()) {
            System.out.println("All " + executed + " tests passed.");
        } else {
            System.err.println(executed + " tests executed with " + failures.size() + " failure(s):");
            for (String message : failures) {
                System.err.println(message);
            }
            System.exit(1);
        }
    }

    private void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " | expected: " + expected + ", actual: " + actual);
        }
    }

    private void assertThrows(Class<? extends Throwable> expected, Runnable action, String message) {
        try {
            action.run();
        } catch (Throwable throwable) {
            if (expected.isInstance(throwable)) {
                return;
            }
            throw new AssertionError(message + " | expected exception: " + expected.getSimpleName() + ", actual: " + throwable.getClass().getSimpleName());
        }
        throw new AssertionError(message + " | expected exception: " + expected.getSimpleName() + " but none thrown");
    }

    /**
     * 测试目的：验证有限小数场景；用例：1/2 -> 0.5。
     */
    @CaseDescription("Terminating decimal when numerator < denominator")
    public void testTerminatingPositiveFraction() {
        Solution1 solution = new Solution1();
        assertEquals("0.5", solution.fractionToDecimal(1, 2), "1/2 should produce 0.5");
    }

    /**
     * 测试目的：验证结果为整数的情况；用例：2/1 -> 2。
     */
    @CaseDescription("Whole number without fractional part")
    public void testWholeNumberResult() {
        Solution1 solution = new Solution1();
        assertEquals("2", solution.fractionToDecimal(2, 1), "2/1 should produce 2");
    }

    /**
     * 测试目的：验证循环小数的处理；用例：2/3 -> 0.(6)。
     */
    @CaseDescription("Repeating decimal with single digit cycle")
    public void testRepeatingDecimal() {
        Solution1 solution = new Solution1();
        assertEquals("0.(6)", solution.fractionToDecimal(2, 3), "2/3 should produce 0.(6)");
    }

    /**
     * 测试目的：验证负分数的符号处理；用例：-25/4 -> -6.25。
     */
    @CaseDescription("Negative fraction resulting in finite decimal")
    public void testNegativeFraction() {
        Solution1 solution = new Solution1();
        assertEquals("-6.25", solution.fractionToDecimal(-25, 4), "-25/4 should produce -6.25");
    }

    /**
     * 测试目的：验证分子为零的边界情况；用例：0/5 -> 0。
     */
    @CaseDescription("Zero numerator should return zero")
    public void testZeroNumerator() {
        Solution1 solution = new Solution1();
        assertEquals("0", solution.fractionToDecimal(0, 5), "0/x should produce 0");
    }

    /**
     * 测试目的：验证分母为零时抛出异常；用例：1/0 -> ArithmeticException。
     */
    @CaseDescription("Division by zero must raise ArithmeticException")
    public void testDivisionByZero() {
        Solution1 solution = new Solution1();
        assertThrows(ArithmeticException.class, () -> solution.fractionToDecimal(1, 0), "1/0 should throw");
    }

    /**
     * 测试目的：验证整型溢出边界的处理；用例：Integer.MIN_VALUE / -1 -> 2147483648。
     */
    @CaseDescription("Handles INT_MIN to avoid overflow when abs is taken")
    public void testIntegerMinBoundary() {
        Solution1 solution = new Solution1();
        assertEquals("2147483648", solution.fractionToDecimal(Integer.MIN_VALUE, -1), "INT_MIN/-1 should produce positive 2147483648");
    }
}
