import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Main {
    public static int[] init_a(){
        Random r = new Random();
        int[] a = new int[4];

        for (int i = 0; i < a.length; i++) {
            a[i] = r.nextInt(50)+1;
        }
        return a;
    } //랜덤 a를 4개 고름

    public static int[] init_b() {
        Random r = new Random();
        int[] b = new int[4];
        for(int i=0; i<b.length; i++) {
            b[i] = r.nextInt(50)+1;
        }
        return b;
    } //랜덤 b를 4개 고름

    public static int MSE(int a, int b ,int[] x, int[] y){
        int temp;
        int MSE =0;

        for (int i = 0; i < x.length; i++) {
            temp = y[i]-(a*x[i]+b);
            MSE += Math.pow(temp,2);
        }

        return MSE;
    } //MSE 구하기

    public static int[] selection( int[] a, int[] b, int[] x, int[] y) {
        int[] comp = new int[a.length];
        int comp_sum=0;
        double[] ratio = new double[a.length];

        for (int i = 0; i < a.length; i++) {
         comp[i] = MSE(a[i],b[i],x,y);
         comp_sum += comp[i];
        }
        for (int i = 0; i < a.length; i++) {
            comp[i] = comp_sum-comp[i];
        }
        comp_sum=0;
        for (int i = 0; i < a.length; i++) {
            comp_sum += comp[i];
        }
        // MSE가 낮을 수록 정확하므로 우열이다.
        for (int i = 0; i < ratio.length; i++) {
          if(i==0) ratio[i] = (double)(comp[i])/(double)comp_sum;
          else ratio[i] = ratio[i-1]+((double)(comp[i])/(double)comp_sum);
        }
        int[] result = new int[a.length*2];
        //result = [a, a, a, a, b, b, b, b]
        Random r = new Random();

        for (int i = 0; i < a.length; i++) {
            double p = r.nextInt();
            if(p<ratio[0]){
                result[i] = a[0];
                result[i+a.length] = b[0];
            }
            else if(p<ratio[1]){
                result[i] = a[1];
                result[i+a.length] = b[1];
            }
            else if(p<ratio[2]){
                result[i] = a[2];
                result[i+a.length] = b[2];
            }
            else {
                result[i] = a[3];
                result[i+a.length] = b[3];
            }
        }
        return result;
    }

    public static String int2String(String x) {
        return String.format("%7s", x).replace(' ', '0');
    }

    public static String[] crossover(int[] selec) {
        String[] result = new String[selec.length];
        for(int i=0; i<selec.length; i+=2) {
            String bit1 = int2String(Integer.toBinaryString(selec[i]));
            String bit2 = int2String(Integer.toBinaryString(selec[i+1]));

            result[i] = bit1.substring(0, 2) + bit2.substring(3);
            result[i+1] = bit2.substring(0, 2) + bit1.substring(3);
        }

        return result;
    }

    public static int invert(String mut) {
        Random r = new Random();
        int target = Integer.parseInt(mut, 2);
        for(int i=0; i<mut.length(); i++) {
            double p = (double)5/ (double)100; //5% 확률로 돌연변이 발생
            if(r.nextDouble() < p) {
                target = 1 << i ^ target;
            }
        }
        return target;
    }

    public static int[] mutation(String[] x) {
        int[] result = new int[x.length];
        for (int i=0; i<x.length; i++) {
            result[i] = invert(x[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        //y= ax + b
        int[] a = init_a();
        int[] b = init_b();

        //정보 : 남성-나이와 평균 몸무게
        int[] x = {7,8,9,10,11,12,13,14,15,16,17,18};        //나이대
        int[] y = {25,28,31,36,40,46,51,55,59,62,65,66};     //평균몸무게

        //int[] y = {8,9,10,11,12,13,14,15,16,17,18,19};            //test : 1x+1
        //int[] y = {20,22,24,26,28,30,32,34,36,38,40,42};          //test : 2x+6
        //int[] y = {64,73,82,91,100,109,118,127,136,145,154,163};  //test : 9x+1

        int[] result_MSE = new int[a.length];
        double min = 10000.0;
        int result_a=0;
        int result_b=0;

        for(int i=0; i<1000; i++) {
            int[] selec = selection(a,b,x,y);
            String[] cross = crossover(selec);
            int[] mut = mutation(cross);

            for (int j = 0; j <a.length ; j++) {
                a[j] = mut[j];
                b[j] = mut[j+a.length];
            }
            for(int j = 0; j <a.length; j++) {
                result_MSE[j] = MSE(a[j],b[j],x,y);
                if(min>result_MSE[j]){
                    min = result_MSE[j];
                    result_a = a[j];
                    result_b = b[j];
                }
            }
        }
        System.out.println("y = " +result_a+"x +"+ result_b);
    }
}