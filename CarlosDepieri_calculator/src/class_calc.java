import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class class_calc
{

    static final HashMap<String, Integer> prec;

    static
    {
        prec = new HashMap<>();
        prec.put("^", 3);
        prec.put("%", 2);
        prec.put("*", 2);
        prec.put("/", 2);
        prec.put("+", 1);
        prec.put("-", 1);
    }

    public static void main(String[] args)
    {

        Queue<String> InfixFila = new LinkedList<>(); 
        Scanner teclado = new Scanner(System.in);
        Double number = 0.0;
        Character x, xNext = ' ';
        String input;
        String multiDigit = "";
        do
        {
            System.out.println("Insira uma expressao abaixo: ");
            input = teclado.nextLine();
            input = input.replaceAll(" ", ""); //ignore spaces in input infix expression
            if (input.equals("sair"))
            {
                System.exit(0);
            }

            for (int i = 0; i < input.length(); i++)
            {
                x = input.charAt(i);
                if (i + 1 < input.length())
                {
                    xNext = input.charAt(i + 1);
                }

                if (x.equals('(') || x.equals(')'))
                {
                    if (x.equals('(') && xNext.equals('-'))
                    {
                    
                        System.out.println("Não Insira numeros negativos");
                        main(args);
                    } else
                    {
                        InfixFila.add(x.toString());
                    }
                } else if (!Character.isDigit(x))
                {
                    if (InfixFila.isEmpty() && x.equals('-'))
                    {
                        System.out.println("Não Insira numeros negativos");
                        main(args);
                    } else if (xNext.equals('-'))
                    {
                        System.out.println("Não Insira numeros negativos");
                        main(args);
                    } else
                    {
                        InfixFila.add(x.toString());
                    }
                } else if (Character.isDigit(x))
                {
                    if (i + 1 < input.length() && input.charAt(i + 1) == '.') //to handle decimal
                    {
                        int j = i + 1;
                        multiDigit = x.toString() + input.charAt(j); //to handle multidigit
                        while (j + 1 <= input.length() - 1 && Character.isDigit(input.charAt(j + 1)))
                        {
                            multiDigit = multiDigit + input.charAt(j + 1);
                            j++;
                        }
                        i = j;
                        InfixFila.add(multiDigit);
                        multiDigit = "";
                    } else if (i + 1 <= input.length() - 1 && Character.isDigit(input.charAt(i + 1)))
                    {
                        int j = i;
                        //multiDigit=x.toString()+input.charAt(i);
                        while (j <= input.length() - 1 && Character.isDigit(input.charAt(j)))
                        {
                            multiDigit = multiDigit + input.charAt(j);
                            j++;
                        }
                        i = j - 1;
                        InfixFila.add(multiDigit);
                        multiDigit = "";
                    } else
                    {
                        InfixFila.add(x.toString());
                    }

                }
            }

            InfixParaNPR(InfixFila);
        } while (!input.equals("sair"));
    }

  
    public static void InfixParaNPR(Queue<String> InfixFila)
    {
        Stack PilhaOpera = new Stack();
        Queue<String> FilaNPR = new LinkedList<>();
        String s;
        while (!InfixFila.isEmpty())
        {
            s = InfixFila.poll();
            try
            {
                double num = Double.parseDouble(s);
                FilaNPR.add(s);
            } catch (NumberFormatException nfe)
            {
                if (PilhaOpera.isEmpty())
                {
                    PilhaOpera.add(s);
                } else if (s.equals("("))
                {
                    PilhaOpera.add(s);
                } else if (s.equals(")"))
                {
                    while (!PilhaOpera.peek().toString().equals("("))
                    {
                        FilaNPR.add(PilhaOpera.peek().toString());
                        PilhaOpera.pop();
                    }
                    PilhaOpera.pop();
                } else
                {
                    while (!PilhaOpera.empty() && !PilhaOpera.peek().toString().equals("(") && prec.get(s) <= prec.get(PilhaOpera.peek().toString()))
                    {
                        FilaNPR.add(PilhaOpera.peek().toString());
                        PilhaOpera.pop();
                    }
                    PilhaOpera.push(s);
                }
            }
        }
        while (!PilhaOpera.empty())
        {
            FilaNPR.add(PilhaOpera.peek().toString());
            PilhaOpera.pop();
        }
        System.out.println();
        System.out.println("NPR:");
        //numbers and operators all seperated by 1 space.
        for (String val : FilaNPR)
        {
            System.out.print(val + " ");
        }
        ResultadoNPR(FilaNPR);
    }

    //method to calculate the reuslt of postfix expression.
    public static void ResultadoNPR(Queue<String> FilaNPR)
    {
        Stack<String> eval = new Stack<>(); //Standard Stack class provided by Java Framework.
        String s;
        Double n1, n2, resultado = 0.0;
        while (!FilaNPR.isEmpty())
        {
            s = FilaNPR.poll();
            try
            {
                double num = Double.parseDouble(s);
                eval.add(s);
            } catch (NumberFormatException nfe)
            {
                n1 = Double.parseDouble(eval.peek());
                eval.pop();
                n2 = Double.parseDouble(eval.peek());
                eval.pop();

                switch (s)
                {
                    case "+":
                        resultado = n2 + n1;
                        break;
                    case "-":
                        resultado = n2 - n1;
                        break;
                    case "*":
                        resultado = n2 * n1;
                        break;
                    case "/":
                        //in java, there is no exception generated when divided by zero and thus checking
                        //for 
                        if (n1 == 0)
                        {
                            System.out.println("\nERROR: Cannot Divide by zero!\n");
                            return;
                        } else
                        {
                            resultado = n2 / n1;
                            break;
                        }
                    case "%":
                        resultado = n2 % n1;
                        break;
                    case "^":
                        resultado = Math.pow(n2, n1);
                        break;

                }

                eval.push(resultado.toString());

            }

        }
        
        System.out.println("\nResultado: ");
        DecimalFormat df = new DecimalFormat("0.000");
        for (String val : eval)
        {
            System.out.println(df.format(Double.parseDouble(val)) + "\n");
        }
    }

}