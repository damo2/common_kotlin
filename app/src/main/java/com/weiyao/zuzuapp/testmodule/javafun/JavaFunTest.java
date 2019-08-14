package com.weiyao.zuzuapp.testmodule.javafun;

import android.os.Build;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import androidx.annotation.RequiresApi;

/**
 * Created by wr
 * Date: 2019/8/13  19:09
 * mail: 1902065822@qq.com
 * describe:
 */

public class JavaFunTest {
    public static void test() {
        new Thread(() -> {
            System.out.println("Hello world!");
        }).start();


        GreetingService greetService1 = message -> System.out.println("Hello " + message);

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Predicate<Integer> predicate = n -> true
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // n 如果存在则 test 方法返回 true

        System.out.println("输出所有数据:");

        // 传递参数 n
        eval(list, n -> true);

        // Predicate<Integer> predicate1 = n -> n%2 == 0
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // 如果 n%2 为 0 test 方法返回 true

        System.out.println("输出所有偶数:");
        eval(list, n -> n % 2 == 0);

        // Predicate<Integer> predicate2 = n -> n > 3
        // n 是一个参数传递到 Predicate 接口的 test 方法
        // 如果 n 大于 3 test 方法返回 true

        System.out.println("输出大于 3 的所有数字:");
        eval(list, n -> n > 3);

        ConsumerInterface<Integer, Boolean> test = n -> n < 8 && n > 4;

        System.out.println("输出大于 4  小于 8 的所有数字:");
        eval(list, test);
    }

    @FunctionalInterface
    interface GreetingService {
        void sayMessage(String message);
    }

    //    public static void eval(List<Integer> list, Function<Integer, Boolean> function) {
    //        for (Integer n : list) {
    //            if (function.apply(n)) {
    //                System.out.println(n + " ");
    //            }
    //        }
    //    }

    public static void eval(List<Integer> list, ConsumerInterface<Integer, Boolean> function) {
        for (Integer n : list) {
            if (function.accept(n)) {
                System.out.println(n + " ");
            }
        }
    }

    //自定义函数接口
    @FunctionalInterface
    public interface ConsumerInterface<T, R> {
        R accept(T t);
    }
}
