package com.sbsc.convertee.tools;

import java.math.BigDecimal;
import java.math.MathContext;

public class StringCalculationParser {

    /**
     * For more info please check evalPrecise(String), as they're almost the exact same.
     * The only difference is the Number type we work with: double in eval() and
     * BigDecimal in evalPrecise()
     * @param str - Calculation to solve
     * @return Double result
     */
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);

                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    /**
     * Iterates through a String that contains a calculation
     * Currently it can handle following statements:
     *      Basics: + , - , * , /
     *  Extended Functions (only to double accuracy due to Javas Math class not working with BigDecimal):
     *      sqrt, sin, cos, tan, ^
     *  Decimal Numbers are separated by . and Brackets () are also usable
     * @param str String Calculation
     * @return BigDecimal result
     */
    public static BigDecimal evalPrecise(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = ( ++pos < str.length() ) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            /**
             * Starts off calculation
             * @return BigDecimal Result
             */
            BigDecimal parse() {
                nextChar();
                BigDecimal x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            BigDecimal parseExpression() {
                BigDecimal x = parseTerm();

                for (;;) {
                    if      (eat('+')) x = x.add(parseTerm()); // addition
                    else if (eat('-')) x = x.subtract(parseTerm()); // subtraction
                    else return x;
                }
            }

            BigDecimal parseTerm() {
                BigDecimal x = parseFactor();

                for (;;) {
                    if      (eat('*')) x = x.multiply(parseFactor() , MathContext.DECIMAL128); // multiplication
                    else if (eat('/')) x = x.divide(parseFactor(), MathContext.DECIMAL128); // division
                    else return x;
                }
            }

            BigDecimal parseFactor() {

                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return parseFactor().negate(); // unary minus

                BigDecimal x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = new BigDecimal(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
                    else if (func.equals("sin")) x = BigDecimal.valueOf(Math.sin(Math.toRadians(x.doubleValue())));
                    else if (func.equals("cos")) x = BigDecimal.valueOf(Math.cos(Math.toRadians(x.doubleValue())));
                    else if (func.equals("tan")) x = BigDecimal.valueOf(Math.tan(Math.toRadians(x.doubleValue())));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = x.pow( parseFactor().intValue()); // exponentiation

                return x;
            }
        }.parse();
    }
}
