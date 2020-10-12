package arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Expression {
    private Number[] nums;
    private String[] finalNumber;
    private String[] numsToString;
    private char[] ops;
    private int len;
    private int[][] parenthesis;
    private boolean isCorrect;
    private double answer;
    private String finalAnswer;

    public Expression(int len) {
        this.len = len;
        this.nums = new Number[len];
        this.numsToString = new String[len];
        this.ops = new char[len - 1];
        this.parenthesis = new int[len][2];
        this.finalNumber = new String[len];
        this.isCorrect = true;
        this.finalAnswer = new String();
    }

    public Number[] getNums() {
        return nums;
    }

    public void setNums(Number[] nums) {
        this.nums = nums;
    }

    public char[] getOps() {
        return ops;
    }

    public void setOps(char[] ops) {
        this.ops = ops;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int[][] getParenthesis() {
        return parenthesis;
    }

    public void setParenthesis(int[][] parenthesis) {
        this.parenthesis = parenthesis;
    }

    public String[] getUltimateNumber() {
        return finalNumber;
    }

    public void setUltimateNumber(String[] ultimateNumber) {
        this.finalNumber = ultimateNumber;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public double getAnswer() {
        return answer;
    }

    public void setAnswer(double answer) {
        if (!Double.isNaN(answer) && !Double.isInfinite(answer)) {
            this.answer = BigDecimal.valueOf(answer).setScale(4, RoundingMode.HALF_UP).doubleValue();
        }
    }

    public String getUltimateAnswer() {
        return finalAnswer;
    }

    public void setUltimateAnswer(String ultimateAnswer) {
        this.finalAnswer = ultimateAnswer;
    }

    public String[] getNumsToString() {
        return numsToString;
    }

    public void setNumsToString(String[] numsToString) {
        this.numsToString = numsToString;
    }

    public List<String> ZTing() {
        List<String> ting = new LinkedList<>();
        Fraction.convertToFraction(this);
        setNumsToString(Fraction.elements);
        for (int i = 0; i < len; i++) {
            int left = parenthesis[i][0];
            int right = parenthesis[i][1];
            while (left > 0) {
                ting.add("(");
                left--;
            }
            ting.add(numsToString[i] + "");
            while (right > 0) {
                ting.add(")");
                right--;
            }
            if (i < len - 1) {
                ting.add(ops[i] + "");
            }
        }
        return ting;
    }

    public String printOrigin() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int left = parenthesis[i][0];
            int right = parenthesis[i][1];
            while (left > 0) {
                s.append("(");
                left--;
            }
            s.append(nums[i]).append(" ");
            while (right > 0) {
                s = new StringBuilder(s.toString().trim());
                s.append(") ");
                right--;
            }
            if (i < len - 1) {
                s.append(ops[i]).append(" ");
            }
        }
        return s.toString().trim();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < len; i++) {
            while (parenthesis[i][0] > 0) {
                s.append("(");
                parenthesis[i][0]--;
            }
            s.append(numsToString[i]).append(" ");
            while (parenthesis[i][1] > 0) {
                s = new StringBuilder(s.toString().trim());
                s.append(") ");
                parenthesis[i][1]--;
            }
            if (i < len - 1) {
                s.append(ops[i]).append(" ");
            }
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        Number[] comThat;
        if(len!=that.len) {
            return false;
        }
        Number[] thatnums = new Number[that.len];
        Number[] thisnums = new Number[len];

        for(int i=0;i<that.len;i++) {
            thatnums[i] = that.nums[i].doubleValue();
        }
        Arrays.sort(thatnums);
        for(int i=0;i<len;i++) {
            thisnums[i] = nums[i].doubleValue();
        }
        Arrays.sort(thisnums);

        return len == that.len &&
                isCorrect == that.isCorrect &&
                Double.compare(that.answer, answer) == 0 &&
                Arrays.equals(thisnums,thatnums)&&
                Objects.equals(finalAnswer, that.finalAnswer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(len, isCorrect, answer, finalAnswer);
        result = 31 * result + Arrays.hashCode(nums);
        result = 31 * result + Arrays.hashCode(finalNumber);
        result = 31 * result + Arrays.hashCode(ops);
        return result;
    }
}