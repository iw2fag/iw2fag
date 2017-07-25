package com.iw2fag.test;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 7/25/2017
 * Time: 3:30 PM
 */
public class StackTest {
    public static void main(String[] args){
        Stack<String> stack = new Stack<String>();
        stack.push("1");
        stack.push("2");
        stack.push("3");

        stack.pop();
        System.out.println(stack.get(0));
    }
}
