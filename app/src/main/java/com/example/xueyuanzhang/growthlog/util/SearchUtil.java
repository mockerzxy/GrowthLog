package com.example.xueyuanzhang.growthlog.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyuanzhang on 16/7/13.
 */
public class SearchUtil {
    public static final int Max_text_length = 10;
    public static final int Max_diary_length = 50;
    public static final int Max_diary_count = 6;
    private String diary[] = new String[Max_diary_count];//»’÷æƒ⁄»›
    private String result[] = new String[Max_diary_count];//»’÷æ≤È’“Ω·π˚
    private double score[] = new double[Max_diary_count];//»’÷æ∆•≈‰∂»
    private int position[][] = new int[Max_diary_count][Max_diary_length];
    private int match[] = new int[Max_diary_count];

    private int[][] vector_diary = new int[Max_diary_count][Max_text_length];//»’º«Œƒ±æ◊÷∑˚◊÷∆µ
    private String text;//ƒø±ÍŒƒ±æ
    private int vector_text[] = new int[Max_text_length];//ƒø±ÍŒƒ±æ◊÷∑˚◊÷∆µ
    private int[][][] coord_diary = new int[Max_diary_count][Max_text_length][Max_diary_length];//500»’º«◊Ó¥Û≥§∂»,10◊Ó¥Û∆•≈‰◊÷∑˚¥Æ≥§∂»
    private int[][] arise = new int[Max_diary_count][Max_diary_length];
    private int diary_count = 0;
    private List<String> coord = new ArrayList<String>();
    private int[] diary_path = new int[Max_diary_count];
    private double rank[][] = new double[Max_diary_count][3];

    public String[] getDiary() {
        return diary;
    }

    public void setDiary(String diary[]) {
        this.diary = diary;
        this.diary_count = diary.length;
    }

    public void addDiary(String diary) {
        this.diary[diary_count] = diary;
        diary_count++;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void participle() {
        int[] count = new int[80000];
        int max = 0;
        for (int i = 0; i < text.length(); i++) {
            int cou = ++count[(int) text.charAt(i)];
            if (cou >= max) {
                max = cou;
            }
        }
        for (int i = 0; i < text.length(); i++) {
            vector_text[i] = count[(int) text.charAt(i)];
        }
        for (int i = 0; i < text.length(); i++) {
            char t = text.charAt(i);
            for (int m = 0; m < diary.length; m++) {
                for (int n = 0; n < diary[m].length(); n++) {
                    if (diary[m].charAt(n) == t) {
                        coord_diary[m][i][n] = 1;
                        vector_diary[m][i]++;
                        match[m]++;
                        arise[m][n] = i + 1;
                    }
                }
            }
        }
    }

    public int findpath(int m, String path, int index) {
        for (int i = index; i < diary[m].length(); i++) {
            if (arise[m][i] != 0) {
                if (path.length() == 0) {
                    path = path + ";位置" + i + ":" + Integer.toString(arise[m][i]);
                    index = i + 1;
                } else {
                    if (Integer.parseInt(path.substring(path.length() - 1)) < arise[m][i]) {
                        path = path + ";位置" + i + ":" + Integer.toString(arise[m][i]);
                    }
                }
            }
        }
        path = path + "(" + m + ")";
        coord.add(path);
        path = "";
        return index;
    }

    public void critical_path() {
        for (int i = 0; i < getDiary().length; i++) {
            String path = "";
            int index = 0;
            if (match[i] != 0) {
                while (index != diary[i].length()) {
                    index = findpath(i, path, index);
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            diary_path[i] = -1;
        }
        for (int i = 0; i < coord.size(); i++) {
            int n_diary = Integer.parseInt(coord.get(i).substring(coord.get(i).length() - 2, coord.get(i).length() - 1));
            if (diary_path[n_diary] == -1) {
                diary_path[n_diary] = i;
            } else {
                int o_diary = diary_path[n_diary];
                if (coord.get(o_diary).length() < coord.get(i).length()) {
                    diary_path[n_diary] = i;
                }
            }
        }
        for (int i = 0; i < diary.length; i++) {
            if (match[i] != 0) {
                result[i] = coord.get(diary_path[i]);
            }
        }
    }

    public double svm(int n) {
        double svm;
        double s = 0;
        for (int i = 0; i < text.length(); i++) {
            s = s + vector_text[i] * vector_diary[n][i];
        }
        double vector1 = 0;
        double vector2 = 0;
        for (int i = 0; i < text.length(); i++) {
            vector1 = vector1 + vector_text[i] * vector_text[i];
        }
        for (int i = 0; i < text.length(); i++) {
            vector2 = vector2 + vector_diary[n][i] * vector_diary[n][i];
        }
        vector1 = Math.sqrt(vector1);
        vector2 = Math.sqrt(vector2);
        svm = s / (vector1 * vector2);
        return svm;
    }

    public void sort() {
        for (int i = 0; i < diary.length; i++) {
            if (match[i] != 0) {
                int site = diary_path[i];
                rank[i][0] = (coord.get(site).length() - 3) / 6;
                int x1, x2;
                String s = coord.get(site);
                x1 = coord.get(site).indexOf(":");
                x2 = coord.get(site).lastIndexOf(":");
                int y1, y2;
                y1 = coord.get(site).indexOf("置");
                y2 = coord.get(site).lastIndexOf("置");
                rank[i][1] = Integer.parseInt(coord.get(site).substring(y2 + 1, x2)) - Integer.parseInt(coord.get(site).substring(y1 + 1, x1));
                rank[i][2] = svm(Integer.parseInt(coord.get(site).substring(coord.get(site).length() - 2, coord.get(site).length() - 1)));
                score[i] = rank[i][0] * 500 - rank[i][1] + rank[i][2];
                int s1 = 0;
                int s2;
                int s3;
                while (s1 != -1) {
                    s1 = s.indexOf("置");
                    s2 = s.indexOf(":");
                    s3 = s2 + 2;
                    if (s1 != -1) {
                        int p = Integer.parseInt(s.substring(s1 + 1, s2));
                        position[i][p] = 1;
                        s = s.substring(s3);
                    }
                }
            }
        }
    }

    public double[] retrieval(String text, String[] diary) {
        this.text = text;
        this.diary = diary;
        participle();
        critical_path();
        sort();
        return score;
    }


}



