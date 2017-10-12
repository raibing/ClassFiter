package kang.classfiter;


/**
 * Created by kang on 2017/9/25.
 */

public class CalculateUnit {

    public static float[] AcclAndGryp(float[] acc, float[] gryp) {

        return new float[]{0, 0, 0};
    }

    public static float[] AcclAndMang(float[] acc, float[] gryp) {

        return new float[]{0, 0, 0};
    }

    public static float Gravity2(float[] g) {
        if (g.length != 3) return -1;

        return (g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
    }

    public static float Gravity1(float[] g) {
        if (g.length != 3) return -1;

        return (float) Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
    }

    public static float Avg(float[] group) {
        float sum = 0;
        int l = group.length;
        for (int i = 0; i < l; i++)
            sum += group[i];
        return sum / l;
    }

    public static float Variance(float[] group) {
        float avg = Avg(group);
        float s = 0f, l = group.length;
        for (int i = 0; i < l; i++)
            s += ((group[i] - avg) * (group[i] - avg));
        return s / l;
    }
}
