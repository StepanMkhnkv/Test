import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) {
        System.out.println("Курс:");
        double toRublesRate = 0;
        try (FileReader reader = new FileReader("toRublesRate.txt")) {
            Scanner scanner = new Scanner(reader);
            toRublesRate = scanner.nextDouble();
            System.out.println("1$ = " + toRublesRate + "p");
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        double toDollarsRate = 0;
        try (FileReader reader = new FileReader("toDollarsRate.txt")) {
            Scanner scanner = new Scanner(reader);
            toDollarsRate = scanner.nextDouble();
            System.out.println("1p = $" + toDollarsRate);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
        System.out.println();
        while (true) {
            System.out.println("Введите выражение: ");
            Scanner scanner = new Scanner(System.in);
            String startingExpression = scanner.nextLine();
            while (startingExpression.contains(")")) {
                int firstClosingBracket = startingExpression.indexOf(")");
                String actualExpression = startingExpression.substring(0, firstClosingBracket);
                int lastOpeningBracket = actualExpression.lastIndexOf("(");
                actualExpression = actualExpression.substring(lastOpeningBracket + 1);
                if (!isDollarsAndRubles(actualExpression)) {
                    String beginStartingExpression = startingExpression.substring(0, lastOpeningBracket);
                    String endStartingExpression = startingExpression.substring(firstClosingBracket + 1);
                    String result;
                    if (startingExpression.length() >= 8) {
                        if (startingExpression.substring(lastOpeningBracket - 8, lastOpeningBracket).equals("toRubles")) {
                            if (calculate(actualExpression).substring(0, 1).equals("$")) {
                                result = Double.parseDouble(calculate(actualExpression).substring(1)) * toRublesRate + "p";
                            } else {
                                result = calculate(actualExpression);
                            }
                            beginStartingExpression = startingExpression.substring(0, lastOpeningBracket - 8);
                        } else if (startingExpression.substring(lastOpeningBracket - 9, lastOpeningBracket).equals("toDollars")) {
                            if (calculate(actualExpression).substring(calculate(actualExpression).length() - 1).equals("p")) {
                                result = "$" + Double.parseDouble(calculate(actualExpression).substring(0, calculate(actualExpression).length() - 1)) * toDollarsRate;
                            } else {
                                result = calculate(actualExpression);
                            }
                            beginStartingExpression = startingExpression.substring(0, lastOpeningBracket - 9);
                        } else {
                            result = calculate(actualExpression);
                        }
                        startingExpression = beginStartingExpression + result + endStartingExpression;
                    }
                } else {
                    break;
                }
            }
            double count = 100;
            if (!isDollarsAndRubles(startingExpression)) {
                if (startingExpression.contains("+") && startingExpression.contains(" ") || startingExpression.contains("-") && startingExpression.contains(" ")) {
                    if (calculate(startingExpression).substring(0, 1).equals("$")) {
                        if (Double.parseDouble(calculate(startingExpression).substring(1)) < 0) {
                            System.out.println("Ответ: -$" + Math.abs(Math.round(Double.parseDouble(calculate(startingExpression).substring(1)) * count) / count) + "\n");
                        } else {
                            System.out.println("Ответ: $" + Math.round(Double.parseDouble(calculate(startingExpression).substring(1)) * count) / count + "\n");
                        }
                    } else {
                        System.out.println("Ответ: " + Math.round(Double.parseDouble(calculate(startingExpression).substring(0, calculate(startingExpression).length() - 1)) * count) / count + "p\n");
                    }
                } else {
                    if (startingExpression.substring(0, 1).equals("$")) {
                        if (Double.parseDouble(startingExpression.substring(1)) < 0) {
                            System.out.println("Ответ: -$" + Math.abs(Math.round(Double.parseDouble(startingExpression.substring(1)) * count) / count) + "\n");
                        } else {
                            System.out.println("Ответ: $" + Math.round(Double.parseDouble(startingExpression.substring(1)) * count) / count + "\n");
                        }
                    } else {
                        System.out.println("Ответ: " + Math.round(Double.parseDouble(startingExpression.substring(0, startingExpression.length() - 1)) * count) / count + "p\n");
                    }
                }
            } else {
                System.out.println("Нельзя складывать и вычитать значения в разных валютах\n");
            }
        }
    }

    public static boolean isDollarsAndRubles(String actualExpression) {
        ArrayList<Double> dollarsArrayList = new ArrayList<>();
        ArrayList<Double> rublesArrayList = new ArrayList<>();
        String[] numbers = actualExpression.replaceAll("[^-+0-9.pр$\s]", "").split(" ");
        for (String number : numbers) {
            boolean hasDigits = false;
            for (int i = 0; i < number.length() && !hasDigits; i++) {
                if (Character.isDigit(number.charAt(i))) {
                    hasDigits = true;
                }
            }
            if (hasDigits) {
                String[] letters = number.split("");
                if (Objects.equals(letters[0], "$")) {
                    dollarsArrayList.add(Double.parseDouble(number.substring(1)));
                }
                if (Objects.equals(letters[letters.length - 1], "p") || Objects.equals(letters[letters.length - 1], "р")) {
                    rublesArrayList.add(Double.parseDouble(number.substring(0, letters.length - 1)));
                }
            }
        }
        if (!dollarsArrayList.isEmpty() && !rublesArrayList.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static String calculate(String actualExpression) {
        ArrayList<Double> dollarsArrayList = new ArrayList<>();
        ArrayList<Double> rublesArrayList = new ArrayList<>();
        String[] numbers = actualExpression.replaceAll("[^-+0-9.pр$\s]", "").split(" ");
        for (String number : numbers) {
            boolean hasDigits = false;
            for (int i = 0; i < number.length() && !hasDigits; i++) {
                if (Character.isDigit(number.charAt(i))) {
                    hasDigits = true;
                }
            }
            if (hasDigits) {
                String[] letters = number.split("");
                if (Objects.equals(letters[0], "$")) {
                    dollarsArrayList.add(Double.parseDouble(number.substring(1)));
                }
                if (Objects.equals(letters[letters.length - 1], "p") || Objects.equals(letters[letters.length - 1], "р")) {
                    rublesArrayList.add(Double.parseDouble(number.substring(0, letters.length - 1)));
                }
            }
        }
        String[] operators = actualExpression.replaceAll("[^-+0-9.pр$\s]", "").split(" ");
        ArrayList<String> operatorsArrayList = new ArrayList<>();
        for (String operator : operators) {
            if (Objects.equals(operator, "+") || Objects.equals(operator, "-")) {
                operatorsArrayList.add(operator);
            }
        }
        while (!operatorsArrayList.isEmpty()) {
            double result = 0;
            if (!dollarsArrayList.isEmpty()) {
                switch (operatorsArrayList.get(0)) {
                    case "+":
                        result = dollarsArrayList.get(0) + dollarsArrayList.get(1);
                        break;
                    case "-":
                        result = dollarsArrayList.get(0) - dollarsArrayList.get(1);
                        break;
                }
                if (dollarsArrayList.size() != 1) {
                    dollarsArrayList.remove(0);
                    dollarsArrayList.set(0, result);
                    operatorsArrayList.remove(0);
                }
            } else if (!rublesArrayList.isEmpty()) {
                switch (operatorsArrayList.get(0)) {
                    case "+":
                        result = rublesArrayList.get(0) + rublesArrayList.get(1);
                        break;
                    case "-":
                        result = rublesArrayList.get(0) - rublesArrayList.get(1);
                        break;
                }
                if (rublesArrayList.size() != 1) {
                    rublesArrayList.remove(0);
                    rublesArrayList.set(0, result);
                    operatorsArrayList.remove(0);
                }
            }
        }
        if (!dollarsArrayList.isEmpty()) {
            return "$" + dollarsArrayList.get(0);
        } else {
            return rublesArrayList.get(0) + "p";
        }
    }
}
