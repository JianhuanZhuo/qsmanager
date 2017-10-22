package cn.keepfight.utils;

import cn.keepfight.qsmanager.Mapper.TestOrderMapper;
import cn.keepfight.qsmanager.QSApp;
import cn.keepfight.qsmanager.dao.OperatorDao;
import cn.keepfight.qsmanager.model.OrderSelection;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class UtilTest {
    @Test
    public void main() throws Exception {
        for (int i = 0; i < 10; i++) {
            test((i+1)*10);
        }
    }

    static void test(int num){
        int[] as = randomArr(num);
        long x = System.nanoTime();
        quickSort(as, 0,as.length-1);
        long y = System.nanoTime();
        System.out.println("run "+ num +" with time spend:" +(y-x));
    }

    public static int[] randomArr(int length){
        int[] as = new int[length];
        for(int i = 0; i < as.length; i++) {
            as[i] = (int)(Math.random()*length);
        }
        return as;
    }

    public static void quickSort(int arr[],int l,int r){
        int q;
        if(l<r){
            q=partition(arr,l,r);
            quickSort(arr,l,q-1);
            quickSort(arr,q+1,r);
        }
    }
    public static int partition(int []arr,int l,int r){
        int x=arr[l];
        int i=l;
        for(int j=l+1;j<=r;j++){
            if(arr[j]<=x){
                i++;
                swap(arr,i,j);
            }

        }
        swap(arr,i,l);
        return i;
    }
    public static void swap(int [] arr,int m,int n){
        int temp=arr[m];
        arr[m]=arr[n];
        arr[n]=temp;
    }
}