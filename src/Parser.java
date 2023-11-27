import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
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

    public static void parseAndProcessFile(String fileName) {
        if (fileName.endsWith(".json")) {
            data = readFile(fileName);
            processJsonData();
        } else if (fileName.endsWith(".txt")) {
            data = readFile(fileName);
            findAndCountArithmeticOperations();
        } else {
            System.err.println("Неподдерживаемый формат файла");
        }
    }

    public static void processJsonData() {
        if (data.isEmpty()) {
            System.err.println("Нет данных для обработки.");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray tasks = jsonObject.getJSONArray("tasks");

            StringBuilder results = new StringBuilder();

            for (int i = 0; i < tasks.length(); i++) {
                JSONObject task = tasks.getJSONObject(i);
                String equation = task.getString("equation");
                JSONObject variableValues = task.getJSONObject("variable_values");

                double result = evaluateArithmeticExpression(equation, variableValues);
                results.append("Результат для уравнения: ").append(equation).append(" = ").append(result).append("\n");
            }

            writeOutput(results.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double evaluateArithmeticExpression(String equation, JSONObject variableValues) {
        // Подстановка значений переменных в уравнение
        for (Object variable : variableValues.keySet()) {
            equation = equation.replaceAll((String) variable, String.valueOf(variableValues.getDouble((String) variable)));
        }

        // Вычисление арифметического выражения с учетом приоритета знаков
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

    private static void writeOutput(String content) {
        try (PrintWriter writer = new PrintWriter("output.txt")) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void findAndCountArithmeticOperations() {
        String regex = "(\\d+\\s*[+\\-*/]\\s*\\d+)";
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);

            int count = 0;
            StringBuilder results = new StringBuilder();

            while (matcher.find()) {
                String match = matcher.group();
                results.append(match).append(" = ");
                String[] parts = match.split("[+\\-*/]");
                double num1 = Double.parseDouble(parts[0].trim());
                double num2 = Double.parseDouble(parts[1].trim());
                double result = 0.0;

                if (match.contains("+")) {
                    result = num1 + num2;
                } else if (match.contains("-")) {
                    result = num1 - num2;
                } else if (match.contains("*")) {
                    result = num1 * num2;
                } else if (match.contains("/")) {
                    result = num1 / num2;
                }

                results.append(result).append("\n");
                count++;
            }

            writeOutput(results.toString());

        } catch (Exception e) {
            e.printStackTrace();
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