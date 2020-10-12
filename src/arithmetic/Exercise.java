package arithmetic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Exercise {

    //生成问题和答案文本
    public static void generateFile(List<Expression> expressions) {

        StringBuilder question = new StringBuilder();

        //将表达式中的假分数化成真分数
        for (int i = 0; i < expressions.size(); i++) {
            Expression e = expressions.get(i);
            for(int j=0;j<e.getLen();j++) {
                if(e.getNumsToString()[j].contains("/")) {
                    String toTrue = Fraction.toRealFraction(e.getNumsToString()[j]);
                    if(toTrue!=null) {
                        e.getNumsToString()[j] = toTrue;
                    }
                }
            }
            question.append("" + (i + 1) + ". " + e + "=" + "\r\n");
        }

        // 生成答案
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            Expression e = expressions.get(i);
            if(e.getUltimateAnswer().contains("/")) {
                String AnswerToTrue = Fraction.toRealFraction(e.getUltimateAnswer());
                if(AnswerToTrue!=null) {
                    e.setUltimateAnswer(AnswerToTrue);
                }
            }
            answer.append("" + (i + 1) + ". " + e.getUltimateAnswer() + "\r\n");
        }

        try {
            // 问题写入文件
            File file = new File(System.getProperty("user.dir") + "/" + "Exercises" + ".txt");
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.append(question);

            // 答案写入文件
            file = new File(System.getProperty("user.dir") + "/" + "Answer" + ".txt");
            ps = new PrintStream(new FileOutputStream(file));
            ps.append(answer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //批改用户输入的答案
    public static void checkAnswers(String exerciseFile, String userAnswerPath) {
        List<String> Correct = new ArrayList<>();
        List<String> False = new ArrayList<>();
        File excersice = new File(exerciseFile);
        String answer_timestamp = exerciseFile.substring(exerciseFile.lastIndexOf("_") + 1, exerciseFile.lastIndexOf("."));
        File answer = new File(System.getProperty("user.dir") + "/" + "Answer_" + answer_timestamp + ".txt");

        try {
            FileReader readUser = new FileReader(new File(userAnswerPath));
            FileReader readAnswer = new FileReader(answer);
            BufferedReader bufferUser = new BufferedReader(readUser);
            BufferedReader bufferAnswer = new BufferedReader(readAnswer);
            String sAnswer = null;
            String sUser = null;
            while ((sAnswer = bufferAnswer.readLine()) != null && (sUser = bufferUser.readLine()) != null) {
                if (sAnswer.equals(sUser)) {
                    Correct.add(sAnswer.substring(0, sAnswer.lastIndexOf(".")));
                } else {
                    False.add(sAnswer.substring(0, sAnswer.lastIndexOf(".")));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File Grade = new File(System.getProperty("user.dir") + "/" + "Grade" + ".txt");
        StringBuilder sGrade = new StringBuilder("Correct: ");
        sGrade.append(Correct.size() + "(");
        for (int i = 0; i < Correct.size(); i++) {
            sGrade.append(Correct.get(i) + ",");
        }
        sGrade.append(")" + "\n");
        sGrade.append("Wrong: ");
        sGrade.append(False.size() + "(");

        for (int i = 0; i < False.size(); i++) {
            sGrade.append(False.get(i) + ",");
        }
        sGrade.append(")" + "\n");

        String toGrade = sGrade.toString();
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(Grade));
            ps.append(toGrade);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
