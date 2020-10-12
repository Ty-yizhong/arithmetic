package arithmetic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Generate {
    private static final char[] OPERATIONS = {'+', '-', 'x', '÷'};

    //生成括号概率, 范围为 0~1, 数值越大生成括号概率越大
    private double parenthesisFactor = 0.6;

    //生成分数概率, 范围为 0~1, 数字越大生成分数概率越大
    private double fractionFactor = 0.2;

    //参与运算的数值的个数
    private int maxLen = 4;

    //各个算数值的最大值(不包括)
    private int numberBound;

    //生成表达式的数量
    private int amount;

    //生成表达式
    public Set<Expression> produce() {
        Set<Expression> expressions = new HashSet<>();
        int count = 0;
        while (count < amount) {
            Expression e = new Expression(ThreadLocalRandom.current().nextInt(2, maxLen + 1));
            for (int i = 0; i < e.getLen(); i++) {
                // 生成运算数
                e.getNums()[i] = getRandomNumber();
                if (i < e.getLen() - 1) {
                    // 生成运算符号
                    e.getOps()[i] = getRandomOperation();
                }
            }
            // 生成括号
            polish(e, 0, e.getLen() - 1);
            // 计算答案
            String answer = Calculation.count(e.ZTing());
            // 小数转换为分数
            Fraction.convertToFraction(e);

            if (answer == null || answer.contains("-")) {
                e.setCorrect(false);
            } else {
                e.setUltimateAnswer(answer);
            }
            if (e.isCorrect() && !expressions.contains(e)) {
                expressions.add(e);
                count++;
            }
        }
        return expressions;
    }

    //生成随机运算符
    private char getRandomOperation() {
        return OPERATIONS[new Random().nextInt(OPERATIONS.length)];
    }

    //生成随机数字
    private Number getRandomNumber() {
        if (new Random().nextDouble() < fractionFactor) {
            double d = ThreadLocalRandom.current().nextDouble(0, numberBound);
            d = BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (d == (int) d) {
                return (int) d;
            } else {
                return d;
            }
        } else {
            return new Random().nextInt(numberBound);
        }
    }

    //生成括号
    private void polish(Expression e, int start, int end) {
        if (end > start && new Random().nextDouble() < parenthesisFactor) {
            int middle = ThreadLocalRandom.current().nextInt(start, end);
            // 避免在最外层套括号
            if (!(start == 0 && end == e.getLen() - 1)) {
                if (new Random().nextDouble() < parenthesisFactor) {
                    e.getParenthesis()[start][0]++;
                    e.getParenthesis()[end][1]++;
                }
            }
            polish(e, start, middle);
            polish(e, middle + 1, end);
        }
    }

    public double getParenthesisFactor() {
        return parenthesisFactor;
    }

    public void setParenthesisFactor(double parenthesisFactor) {
        this.parenthesisFactor = parenthesisFactor;
    }

    public double getFractionFactor() {
        return fractionFactor;
    }

    public void setFractionFactor(double fractionFactor) {
        this.fractionFactor = fractionFactor;
    }

    public int getMaxLen() {
        return maxLen;
    }

    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    public int getNumberBound() {
        return numberBound;
    }

    public void setNumberBound(int numberBound) {
        this.numberBound = numberBound;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
