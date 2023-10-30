import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.PrintWriter;

public class parser {
    private String data;

    private parser(Builder builder) {
        this.data = builder.data;
    }

    public static class Builder {
        private String data;

        public Builder(String data) {
            this.data = data;
        }

        public parser build() {
            return new parser(this);
        }
    }

    public void parseAndProcessFile(String fileName) {
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

    public void processJsonData() {
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

    private double evaluateArithmeticExpression(String equation, JSONObject variableValues) {
        // Подстановка значений переменных в уравнение
        for (Object variable : variableValues.keySet()) {
            equation = equation.replaceAll((String) variable, String.valueOf(variableValues.getDouble((String) variable)));
        }

        // Вычисление арифметического выражения с учетом умножения и вычитания
        String[] parts = equation.split(" ");
        double total = Double.parseDouble(parts[0]);

        for (int i = 1; i < parts.length - 1; i += 2) {
            String operator = parts[i];
            double operand = Double.parseDouble(parts[i + 1]);

            if (operator.equals("*")) {
                total *= operand;
            }else if (operator.equals("/")) {
                total /= operand;
            } else if (operator.equals("+")) {
                total += operand;
            }
            else if (operator.equals("-")) {
                total -= operand;
            }
        }

        return total;
    }



    private void writeOutput(String content) {
        try (PrintWriter writer = new PrintWriter("output.txt")) {
            writer.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void findAndCountArithmeticOperations() {
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


    private String readFile(String fileName) {
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
