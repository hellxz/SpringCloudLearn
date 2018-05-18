package com.cnblogs.hellxz.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Hellxz
 * @Description: 字符串工具类
 * @Date : 2018/5/5 9:29
 */
public class StringUtils<T> {

    /**
     * 逗号分开的字符串转List
     * @param commaSplitString 逗号分开的字符串
     * @return List<String> 转换后的List
     */
    public static List<String> commaSplitrStringToList(String commaSplitString){
        String[] stringArray = commaSplitString.split(",");
        List<String> stringList = new ArrayList<String>();
        for(int index = 0; index<stringArray.length; index++){
            stringList.add(stringArray[index]);
        }
        return stringList;
    }

    /**
     * 使用指定符号拼接字符串
     * @param args 多个字符串参数
     * @return 拼接好的字符串
     */
    public static String mergeAllStringSplitBySymbol(String symbol, String... args){
        StringBuffer stringBuffer = new StringBuffer();
        for(int index =0; index<args.length; index++){
            stringBuffer.append(args[index]);
            stringBuffer.append(symbol);
        }
        return stringBuffer.deleteCharAt(stringBuffer.length()-1).toString();
    }

    /**
     * 使用逗号拼接字符串
     * @param args 多个字符串参数
     * @return 拼接好的字符串
     */
    public static String mergeAllStringSplitByComma(String... args){
        return mergeAllStringSplitBySymbol(",", args);
    }

    /**
     * List转字符串，使用指定符号分隔
     * @param orignList 原集合
     * @param <T> 集合内参数类型
     * @param symbol 分隔符
     * @return 转换后的字符串
     */
    public static <T> String listToStringSplitBySymbol(List<T> orignList , String symbol){
        StringBuffer stringBuffer = new StringBuffer();
        for(T t : orignList){
            stringBuffer.append(t);
            stringBuffer.append(symbol);
        }
        return stringBuffer.deleteCharAt(stringBuffer.length()-1).toString();
    }

    public static void main(String[] args) {
//        String s = mergeAllStringSplitBySymbol(".", "2", "3", "4", "5");
//        System.out.println(s);
//        List<String> stringList = commaSplitrStringToList("1,2,3,4,5");
//        System.out.println(stringList);
//        List<Object> list = new ArrayList<>();
//        list.add(new Object());list.add(new Object());
//        String s1 = listToStringSplitBySymbol(list, ",");
//        System.out.println(s1);
    }
}
