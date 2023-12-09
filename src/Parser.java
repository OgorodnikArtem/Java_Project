import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static String data;

    private Parser(Builder builder) {
        this.data = builder.data;
    }

    public static class Builder {
        private String data;

        public Builder(String data) {
            this.data = data;
        }

        public Parser build() {
            return new Parser(this);
        }
    }


    public static class Results {
        private int expressionNumber;
        private String expression;
        private double result;

        public Results(int expressionNumber, String expression, double result) {
            this.expressionNumber = expressionNumber;
            this.expression = expression;
            this.result = result;
        }

        public int getExpressionNumber() {
            return expressionNumber;
        }

        public String getExpression() {
            return expression;
        }

        public double getResult() {
            return result;
        }
    }


    public static String parseAndProcessFile(String fileName, String outputFileName) {
        if (fileName.endsWith(".json")) {
            data = readFile(fileName);
            processJsonData(outputFileName);
        } else if (fileName.endsWith(".txt")) {
            data = readFile(fileName);
            processTxtData(outputFileName);
        } else {
            System.err.println("Неподдерживаемый формат файла");
        }
        return data;
    }


    private static void processTxtData(String outputFileName) {
        if (data.isEmpty()) {
            System.err.println("Нет данных для обработки.");
            return;
        }
        String regex = "(\\d+\\s*[+\\-*/]\\s*\\d+)";
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);

            List<Results> resultsList = new ArrayList<>();
            int expressionNumber = 1;

            while (matcher.find()) {
                String match = matcher.group();
                double result = evaluateExpression(match);
                Results resultObject = new Results(expressionNumber++, match, result);
                resultsList.add(resultObject);
            }

            Results[] result_ = resultsList.toArray(new Results[0]);

            int l_ = 0 ;


            for (Results result : resultsList) {
                l_++;

            }

            writeTxtOutput(result_, outputFileName , l_);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double evaluateExpression(String expression) {
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        String[] parts = expression.split("[+\\-*/]");
        for (String part : parts) {
            operands.push(Double.parseDouble(part.trim()));
        }

        String operatorsStr = expression.replaceAll("[0-9\\s]+", "");
        for (char operator : operatorsStr.toCharArray()) {
            while (!operators.isEmpty() && hasHigherPrecedence(operator, operators.peek())) {
                double num2 = operands.pop();
                double num1 = operands.pop();
                char op = operators.pop();
                double result = performOperation(num1, num2, op);
                operands.push(result);
            }
            operators.push(operator);
        }

        while (!operators.isEmpty()) {
            double num2 = operands.pop();
            double num1 = operands.pop();
            char op = operators.pop();
            double result = performOperation(num1, num2, op);
            operands.push(result);
        }

        return operands.pop();
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        return (op2 == '*' || op2 == '/') && (op1 == '+' || op1 == '-');
    }

    private static double performOperation(double num1, double num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }


    private static void processJsonData(String outputFileName) {
        if (data.isEmpty()) {
            System.err.println("Нет данных для обработки.");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray tasks = jsonObject.getJSONArray("tasks");


            Results[] results_m = new Results[tasks.length()];

            StringBuilder results = new StringBuilder();

            int n_ = tasks.length();

            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                int taskNumber = task.getInt("task_number");
                String equation = task.getString("equation");
                JSONObject variableValues = task.getJSONObject("variable_values");

                double result = evaluateArithmeticExpression(equation, variableValues);


            results_m[i] = new Results(taskNumber,equation,result);

            }

            if(outputFileName.endsWith(".json")) {
                writeJsonOutput(results_m, outputFileName , n_);
            }else if(outputFileName.endsWith(".txt")){
                writeTxtOutput(results_m , outputFileName , n_);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void writeJsonOutput(Results[] results_m, String outputFileName , int n_) {
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            StringBuilder results = new StringBuilder();
            results.append("{\n  \"tasks\": [\n");

            for (int i = 0; i < n_; i++) {
                results.append("    {\n")
                        .append("      \"task_number\": ").append(results_m[i].getExpressionNumber()).append(",\n")
                        .append("      \"equation\": \"").append(results_m[i].getExpression()).append("\",\n")
                        .append("      \"result\": ").append(results_m[i].getResult()).append("\n")
                        .append("    }");

                if (i < n_ - 1) {
                    results.append(",");
                }

                results.append("\n");

            }

            results.append("  ]\n}");

            try (PrintWriter writer_ = new PrintWriter(outputFileName)) {
                writer_.println(results);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTxtOutput(Results[] results_m, String outputFileName , int n_) {
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            StringBuilder results = new StringBuilder();

            for (int i = 0; i < n_; i++) {
                results.append("  \"number\": \"").append(results_m[i].getExpressionNumber()).append("\n")
                        .append("  \"equation\": \"").append(results_m[i].getExpression()).append("\n")
                        .append("  \"result\": ").append(results_m[i].getResult()).append("\n");
            }

            try (PrintWriter writer_ = new PrintWriter(outputFileName)) {
                writer_.println(results);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
        e.printStackTrace();
    }

    }



    private static double evaluateArithmeticExpression(String equation, JSONObject variableValues) {
        for (Object variable : variableValues.keySet()) {
            equation = equation.replaceAll((String) variable, String.valueOf(variableValues.getDouble((String) variable)));
        }

        String[] parts = equation.split(" ");
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String part : parts) {
            if (part.matches("[+\\-*/]")) {
                while (!operators.isEmpty() && hasPrecedence(part, operators.peek())) {
                    double operand2 = values.pop();
                    double operand1 = values.pop();
                    values.push(applyOperator(operators.pop(), operand1, operand2));
                }
                operators.push(part);
            } else {
                values.push(Double.parseDouble(part));
            }
        }

        while (!operators.isEmpty()) {
            double operand2 = values.pop();
            double operand1 = values.pop();
            values.push(applyOperator(operators.pop(), operand1, operand2));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(String operator1, String operator2) {
        return (operator2.equals("*") || operator2.equals("/")) &&
                (operator1.equals("+") || operator1.equals("-"));
    }

    private static double applyOperator(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Неверный оператор: " + operator);
        }
    }

    private static String readFile(String fileName) {
        StringBuilder fileData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileData.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }
}