package kang.classfiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kang on 2017/9/25.
 */

public class Jdugment {
    public static final int Running=1;
    public static final int Walking=2;
    public static final int Jumping=3;
    public static final int NoResult=0;
    public static final int Still=4;
    private final float Gravity=9.83f;
    private List mylist;
    private List jdlist;
    private int count;
    private float avg,variance,sum,smallvariance,twosum,smallavg,smallsum,stwosum;
   private boolean Up=false;
    public Jdugment(){
        count=1;
        sum=0;
        twosum=0;
        smallsum=0;
        avg=-1f;
        smallavg=-1f;
        variance=-1f;
        smallvariance=-1f;
        jdlist=new ArrayList();
        mylist=new ArrayList();
        mylist.add(9.85f);

    }

    public int simpleJd(float[] gravity){

        return 0;
    }
    public  int selfJd(final float g){

       // boolean seedup=false;
        add(g);

                        if(g>16){
                            //overwright
                            return Jumping;
                        }else if(g<3){
                            //lossweight
                            return Jumping;
                        }
                        avg();
                        setVariance();
                        if(smallvariance<0.4f)
                            return Still;
                       /* if(variance<smallvariance+0.25){
                            //加速
                            seedup=true;
                        }else if(variance +0.25>smallvariance){
                            //减速
                            seedup=false;
                        }*/
                        if(existPeak()){
                        if(smallvariance<2.25f&&smallvariance>1f){

                            return Walking;


                        }else if(smallvariance<25f&&smallvariance>9f){

                            return Running;


                        }}



        return NoResult;

    }
    private void add(float data){

        mylist.add(data);
        count=mylist.size();
        sum+=data;
        smallsum+=data;

        if((float)mylist.get(count-2)<data){
            if(Up)jdlist.add(0);
            else jdlist.add(1);
            Up=true;
        }else {
            if(!Up)jdlist.add(0);
            else jdlist.add(1);
            Up=false;
        }
        if(count>11)jdlist.remove(0);

        if(count>=21)
            sum-=(float)mylist.get(count-21);
        if(count>=11)
            smallsum-=(float)mylist.get(count-11);

    }
    private void avg(){
        if(count>20){
            avg=sum/20;
        }else {
            float arr=0;
            for(int i=0;i<count;i++)
                arr+=(float)mylist.get(i);
            avg=arr/count;
        }
        if(count<10){
            smallavg=avg;
        }else{
            smallavg=smallsum/10;
        }


    }
    private void setVariance(){
        float temp=0,temp2=0,tem1=0,tem2=0;
        if(count<=20) {
            for (int i = 0; i <count; i++) {
                tem1 = (float) mylist.get(i) - avg;
                temp += (tem1 * tem1);
            }
            twosum=temp;
            variance=temp/count;
        }else {
            tem1 = (float) mylist.get(count-21) - avg;
            tem2=(float) mylist.get(count-1) - avg;
            twosum=twosum-tem1*tem1+tem2*tem2;
            variance=twosum/20;
        }
        temp=0;
        if (count>10) {

            tem1 = (float) mylist.get(count-11) - avg;

            stwosum=stwosum-tem1*tem1+tem2*tem2;
            smallvariance=stwosum/10;
        }else smallvariance=variance;

    }
    private boolean existPeak(){
        return jdlist.contains(1);
    }
}
