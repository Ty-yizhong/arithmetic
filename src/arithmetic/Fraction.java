package arithmetic;

import java.math.BigDecimal;
import static java.lang.Math.min;

public class Fraction {
    public static String[] elements;

    //最大公约数
    private static Integer gcd(Integer a, Integer b) {
        if(a == 0){
            return b;
        }
        return gcd(b%a, a);
    }

    private static void getGCD(Integer a, Integer b) {
        int gcd = gcd(a,b);
        if(gcd != 0) {
            a /= gcd(a,b);
            b /= gcd(a,b);
        }
    }

    //最小公倍数
    private static Integer lcm(Integer a, Integer b) {
        if(gcd(a,b) != 0) {
            return a*b / gcd(a,b);
        }else {
            return 0;
        }
    }

    //小数改为分数
    public static String toFraction(double number) {
        int tureNum = 0;
        tureNum = (int)Math.floor(number);
        double decimal = number - tureNum;
        int cnt = 0;
        Integer all = 0;
        Integer base = 1;
        String s = (BigDecimal.valueOf(decimal).toString());
        int start = s.indexOf(".")+1;
        int len = min(s.length()-start,start+1);
        for(int i=start; i<=len; i++) {
            all = all + (Integer)(s.charAt(i)-'0')*base;
            base = base*10;
        }
        String res = null;
        Integer g = gcd(all,base);
        if(g!=0) {
            all /= g;
            base /= g;
            String frac = all + "/" + base;
            if(all==0) {
                res = ""+ tureNum;
            }else{
                res = fractionAdd(String.valueOf(tureNum), frac);
            }
        }
        return res;
    }

    //分数改成真分数
    public static String toRealFraction(String number) {
        String res = null;
        if(number.contains("/")) {
            int top = Integer.parseInt(number.substring(0, number.indexOf("/")));
            int down = Integer.parseInt(number.substring(number.indexOf("/")+1));
            if(top > down) {
                int tureNum = top/down;
                int resTop = top - tureNum*down;
                int resDown = down;
                if(resTop != 0) {
                    int g = gcd(resTop, resDown);
                    resTop /= g;
                    resDown /= g;
                    res = tureNum + "'" + resTop + "/" + resDown;
                }else{
                    res = "" + tureNum;
                }
            }else{
                int g = gcd(top,down);
                if(g != 0) {
                    top /= g;
                    down /= g;
                }
                res = top + "/" + down;
            }
            if(top==0) {
                res = "0";
            }
        }
        return res;
    }

    //将表达式中的算子出现的小数类型转换为真分数
    public static void convertToFraction(Expression e) {
        int len  = e.getLen();
        elements = new String[len];
        for(int i = 0; i<len; i++) {
            if(e.getNums()[i] instanceof Double) {
                elements[i] = toFraction((double) e.getNums()[i]);
            }else {
                elements[i] = e.getNums()[i].toString();
            }
        }
    }

    //比较分数大小
    public static String fractionCompare(String pre, String next) {
        String ans = null;
        if(!pre.contains("/") && !next.contains("/")) {
            Integer prex = Integer.parseInt(pre);
            Integer nextx = Integer.parseInt(next);
            if(prex.equals(nextx)){
                ans = "=";
            }else if(prex>nextx){
                ans = ">";
            }else if(prex<nextx){
                ans = "<";
            }
        }else if(pre.contains("/") && next.contains("/")) {
            Integer resTop = null;
            Integer resDown = null;
            Integer preTop = Integer.parseInt(pre.substring(0, pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/") + 1));
            Integer nextTop = Integer.parseInt(next.substring(0, next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/") + 1));
            resDown = lcm(preDown, nextDown);
            nextTop *= (resDown / nextDown);
            preTop *= (resDown / preDown);
            if(preTop > nextTop) {
                ans = ">";
            }else if(preTop < nextTop){
                ans = "<";
            }else if(preTop.equals(nextTop)){
                ans = "=";
            }
        }else if(!pre.contains("/") && next.contains("/")) {
            Integer nextTop = Integer.parseInt(next.substring(0, next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/") + 1));
            Integer preTop = Integer.parseInt(pre)*nextDown;
            if(preTop > nextTop){
                ans = ">";
            }else if(preTop < nextTop) {
                ans = "<";
            }else{
                ans = "=";
            }
        }else if(pre.contains("/") && !next.contains("/")) {
            Integer preTop = Integer.parseInt(pre.substring(0, pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/") + 1));
            Integer nextTop = Integer.parseInt(next)*preDown;
            if(preTop > nextTop){
                ans = ">";
            }else if(preTop < nextTop){
                ans = "<";
            }else{
                ans = "=";
            }
        }
        return ans;
    }

    //分数加法
    public static String fractionAdd(String pre, String next) {
        Integer resTop = null;
        Integer resDown = null;
        if(pre.contains("/") && next.contains("/")) {
            Integer preTop = Integer.parseInt(pre.substring(0, pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/") + 1));
            Integer nextTop = Integer.parseInt(next.substring(0, next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/") + 1));
            resDown = lcm(preDown, nextDown);
            preTop *= (resDown / preDown);
            nextTop *= (resDown / nextDown);
            resTop = preTop + nextTop;
        }else if(!pre.contains("/") && !next.contains("/")){
            return String.valueOf(Integer.parseInt(pre)+Integer.parseInt(next));
        }else if(pre.contains("/") && !next.contains("/")){
            Integer preTop = Integer.parseInt(pre.substring(0,pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/")+1));
            resDown = preDown;
            resTop = preTop + Integer.parseInt(next)*preDown;
            getGCD(resTop,resDown);
        }else if(!pre.contains("/") && next.contains("/")){
            Integer nextTop = Integer.parseInt(next.substring(0,next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/")+1));
            resDown = nextDown;
            resTop = nextTop + Integer.parseInt(pre)*nextDown;
            getGCD(resTop, resDown);
        }
        if(resTop == null || resDown == null) {
            return "0";
        }else {
            return resTop + "/" + resDown;
        }
    }

    //分数减法
    public static String fractionSubstract(String pre, String next) {
        Integer resTop = 0;
        Integer resDown = 0;
        if(pre.contains("/") && next.contains("/")) {
            Integer preTop = Integer.parseInt(pre.substring(0, pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/") + 1));
            Integer nextTop = Integer.parseInt(next.substring(0, next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/") + 1));
            resDown = lcm(preDown, nextDown);
            preTop *= (resDown / preDown);
            nextTop *= (resDown / nextDown);
            resTop = preTop - nextTop;
            getGCD(resTop,resDown);
        }else if(!pre.contains("/") && !next.contains("/")){
            Integer res = Integer.parseInt(pre)-Integer.parseInt(next);
            return "" + res;
        }else if(pre.contains("/") && !next.contains("/")){
            Integer preTop = Integer.parseInt(pre.substring(0,pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/")+1));
            resDown = preDown;
            resTop = preTop - Integer.parseInt(next)*preDown;
            getGCD(resTop,resDown);
        }else if(!pre.contains("/") && next.contains("/")){
            Integer nextTop = Integer.parseInt(next.substring(0,next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/")+1));
            resDown = nextDown;
            resTop = nextTop - Integer.parseInt(pre)*nextDown;
            getGCD(resTop,resDown);
        }
        if(resTop == null || resDown == null) {
            return "0";
        }else {
            return resTop + "/" + resDown;
        }
    }

    //模拟分数乘法
    public static String fractionMultiply(String pre, String next) {
        Integer resTop = 0;
        Integer resDown = 0;

        if(pre.contains("/") && next.contains("/")) {
            Integer preTop = Integer.parseInt(pre.substring(0,pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/")+1));
            Integer nextTop = Integer.parseInt(next.substring(0,next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/")+1));

            resTop = preTop*nextTop;
            resDown = preDown*nextDown;
            getGCD(resTop,resDown);
        }else if(!pre.contains("/") && !next.contains("/")){
            Integer res = Integer.parseInt(pre)*Integer.parseInt(next);
            return "" + res;
        }else if(!pre.contains("/") && next.contains("/")) {
            Integer nextTop = Integer.parseInt(next.substring(0,next.indexOf("/")));
            Integer nextDown = Integer.parseInt(next.substring(next.indexOf("/")+1));
            resTop = nextTop*Integer.parseInt(pre);
            resDown = nextDown;
            getGCD(resTop,resDown);
        }else if(pre.contains("/") && !next.contains("/")){
            Integer preTop = Integer.parseInt(pre.substring(0,pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/")+1));
            resTop = preTop*Integer.parseInt(next);
            resDown = preDown;
            getGCD(resTop,resDown);
        }
        if(resTop == null || resDown == null) {
            return "0";
        }else {
            return resTop + "/" + resDown;}
    }

    //分数除法
    public static String fractionDivide(String pre, String next) {
        Integer resTop = null;
        Integer resDown = null;
        if(pre.contains("/") && next.contains("/")) {
            Integer nextTop = Integer.parseInt(next.substring(next.indexOf("/")+1));
            Integer nextDown = Integer.parseInt(next.substring(0,next.indexOf("/")));
            next = nextTop + "/" + nextDown;
            return fractionMultiply(pre,next);
        }else if(!pre.contains("/") && !next.contains("/")) {
            resTop = Integer.parseInt(pre);
            resDown = Integer.parseInt(next);
            getGCD(resTop,resDown);
        }else if(!pre.contains("/") && next.contains("/")) {
            Integer nextTop = Integer.parseInt(next.substring(next.indexOf("/")+1));
            Integer nextDown = Integer.parseInt(next.substring(0,next.indexOf("/")));
            next = nextTop + "/" + nextDown;
            return fractionMultiply(pre,next);
        }else if(pre.contains("/") && !next.contains("/")){
            Integer preTop = Integer.parseInt(pre.substring(0,pre.indexOf("/")));
            Integer preDown = Integer.parseInt(pre.substring(pre.indexOf("/")+1));
            resDown = preDown*Integer.parseInt(next);
            resTop = preTop;
            getGCD(resTop,resDown);
        }
        if(resTop == 0|| resDown == null) {
            return null;
        } else {
            return resTop + "/" + resDown;}
    }
}