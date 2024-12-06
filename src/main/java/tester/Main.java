package tester;

import executor.*;
import java.util.List;

public class Main {

      public static void main(String[] args) throws InterruptedException {

            // Testador com threads.
            ConcurrentTester tester = new ConcurrentTester();

            // Caso único.
            TestCase testCase = new TestCase(
                        """
                                    #include <iostream>
                                    using namespace std;
                                    int main() {
                                          int a, b;
                                          cin >> a >> b;
                                          cout << (a + b) << endl; // Soma
                                          return 0;
                                    }
                                    """,
                        Util.CodeType.CPP,
                        List.of("12,12", "1,1", "3,7", "10,5"),
                        List.of("24", "6", "6", "6"));

            // Resultado do teste único.
            UserTestSummary result = tester.testSingleUser(testCase /* Caso de teste */);

            // Exibir resultados
            System.out.println(result.getSummaryDetails());

      }

}