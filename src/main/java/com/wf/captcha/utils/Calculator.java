package com.wf.captcha.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculator {

    /**
     * 将中缀表达式转换为后缀表达式
     * 
     * @param infixExpressionString 中缀表达式字符串
     * @return 后缀表达式
     */
    public List<Object> convertInfixStringToPostfix(String infixExpressionString) {
        Stack<Character> operators = new Stack<>();
        List<Object> postfixExpression = new ArrayList<>();

        // 1 从左到右进行遍历
        for (int index = 0, length = infixExpressionString.length(); index < length; index++) {
            char ch = infixExpressionString.charAt(index);

            if (isOperand(ch)) {
                StringBuilder numBuilder = new StringBuilder();
                // 2 运算数,直接输出
                while (index < length) {
                    ch = infixExpressionString.charAt(index);
                    if (!isOperand(ch)) {
                        index--;
                        break;
                    }
                    numBuilder.append(ch);
                    index++;
                }
                postfixExpression.add(new BigDecimal(numBuilder.toString()));
            } else if ('(' == ch) {
                // 3 左括号,直接压入堆栈,(括号是最高优先级,无需比较)(入栈后优先级降到最低,确保其他符号正常入栈)
                operators.push(ch);
            } else if (')' == ch) {
                // 4 右括号, (意味着括号已结束) 不断弹出栈顶运算符并输出直到遇到左括号 (弹出但不输出)
                while (operators.size() > 0) {
                    Character operator = operators.pop();
                    if (operator == '(') {
                        break;
                    }
                    postfixExpression.add(operator);
                }
            } else if (isOprator(ch)) {
                // 5 运算符,将该运算符与栈顶运算符进行比较,
                while (operators.size() > 0) {
                    Character top = operators.peek();
                    int priority = compareOperatorPriority(ch, top);

                    // 5.1 如果优先级高于栈顶运算符则压入堆栈(该部分运算还不能进行),
                    if (priority > 0 || top == '(') {
                        operators.push(ch);
                        break;
                    }
                    // 5.2 如果优先级低于等于栈顶运算符则将栈顶运算符弹出并输出,然后比较新的栈顶运算符.
                    // (低于弹出意味着前面部分可以运算,先输出的一定是高优先级运算符,等于弹出是因为同等优先级,从左到右运算)
                    // 直到优先级大于栈顶运算符或者栈空,再将该运算符入栈.
                    postfixExpression.add(operators.pop());
                }

                if (operators.size() == 0) {
                    operators.push(ch);
                    continue;
                }

            }
        }

        // 6. 如果对象处理完毕,则按顺序弹出并输出栈中所有运算符.
        while (operators.size() > 0) {
            postfixExpression.add(operators.pop());
        }

        return postfixExpression;
    }

    /**
     * 比较运算符优先级
     * 
     * @param firstOperator  第一个运算符
     * @param secondOperator 第二个运算符
     * @return 运算符优先级
     */
    private int compareOperatorPriority(char firstOperator, char secondOperator) {
        switch (firstOperator) {
        case '+':
        case '-':
            return (secondOperator == '*' || secondOperator == '/' ? -1 : 0);
        case '*':
        case '/':
            return (secondOperator == '+' || secondOperator == '-' ? 1 : 0);
        }
        return 1;
    }

    /**
     * 计算根据给定的算数表达式
     * 
     * @param arithmeticExpression 算数表达式
     * @return 算数运算结果
     */
    public BigDecimal calculate(String arithmeticExpression) throws ArithmeticException {
        String infixExpressionString = arithmeticExpression.replaceAll("\\s", "");
        List<Object> postfixExpression = convertInfixStringToPostfix(infixExpressionString);
        return calculatePostfixExpression(postfixExpression);
    }

    public BigDecimal calculatePostfixExpression(List<Object> postfixExpression) throws ArithmeticException {
        Stack<BigDecimal> resultStack = new Stack<>();

        if (postfixExpression == null || postfixExpression.size() == 0) {
            return null;
        }

        // 从左到右,遇到运算符就弹出相应的运算数,运算后再把结果入栈.最终结果就是栈顶数的值.
        // (由于该运算为线性结构,具体运算时是不需要储存输出后的运算符,一般是输出一个运算符就进行一次运算,不像图中要储存输出状态.)
        for (int index = 0, length = postfixExpression.size(); index < length; index++) {
            Object item = postfixExpression.get(index);

            if (item instanceof BigDecimal) {
                resultStack.push((BigDecimal) item);
            } else {
                Character operator = (Character) item;
                BigDecimal operand2 = resultStack.pop();
                BigDecimal operand1 = resultStack.pop();
                BigDecimal result = caclulate(operand1, operand2, operator);
                resultStack.push(result);
            }
        }

        if (resultStack.size() > 0) {
            return resultStack.pop();
        }

        return null;
    }

    /**
     * 根据给定的运算符进行算术计算
     * 
     * @param operand1 第一个操作数
     * @param operand2 第二个操作数
     * @param operator 运算符
     * @return 算术计算结果
     */
    public BigDecimal caclulate(BigDecimal operand1, BigDecimal operand2, Character operator)
            throws ArithmeticException {
        BigDecimal result = BigDecimal.ZERO;
        switch (operator) {
        case '+':
            return operand1.add(operand2);
        case '-':
            return operand1.subtract(operand2);
        case '*':
            return operand1.multiply(operand2);
        case '/':
            return operand1.divide(operand2);
        }

        return result;
    }

    /**
     * 判断字符是否为运算数
     * 
     * @param ch 字符
     * @return 是否为运算数
     */
    private boolean isOperand(char ch) {
        return '0' == ch || '1' == ch || '2' == ch || '3' == ch || '4' == ch || '5' == ch || '6' == ch || '7' == ch
                || '8' == ch || '9' == ch;
    }

    /**
     * 判断字符是否为运算符
     * 
     * @param ch 字符
     * @return 是否为运算符
     */
    public boolean isOprator(char ch) {
        return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
    }

}
