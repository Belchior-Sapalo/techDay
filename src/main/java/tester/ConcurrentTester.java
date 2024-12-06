package tester;

import executor.Status;
import executor.Util;
import java.util.*;
import java.util.concurrent.*;

public class ConcurrentTester {
    private final ExecutorService executor;

    public ConcurrentTester() {
        this.executor = Executors.newCachedThreadPool();
    }

    public Map<String, UserTestSummary> testAllUsers(Map<String, List<TestCase>> userTestCases)
            throws InterruptedException {
        Map<String, Future<UserTestSummary>> futureResults = new HashMap<>();

        for (Map.Entry<String, List<TestCase>> entry : userTestCases.entrySet()) {
            String userName = entry.getKey();
            List<TestCase> testCases = entry.getValue();
            futureResults.put(userName, executor.submit(() -> testUser(userName, testCases)));
        }

        Map<String, UserTestSummary> results = new HashMap<>();
        for (Map.Entry<String, Future<UserTestSummary>> entry : futureResults.entrySet()) {
            try {
                results.put(entry.getKey(), entry.getValue().get());
            } catch (ExecutionException e) {
                System.err.println("Error testing user " + entry.getKey() + ": " + e.getMessage());
            }
        }

        executor.shutdown();
        return results;
    }

    public UserTestSummary testSingleUser(TestCase userTestCase)
            throws InterruptedException {

        Future<UserTestSummary> futureResults;
        futureResults = (executor.submit(() -> testUser("", List.of(userTestCase))));

        UserTestSummary result = new UserTestSummary("");
            try {
                result = futureResults.get();
            } catch (ExecutionException e) {
                System.err.println("Error testing user ");
            }
        

        executor.shutdown();
        return result;
    }

    private UserTestSummary testUser(String userId, List<TestCase> testCases) {
        UserTestSummary summary = new UserTestSummary(userId);
        for (TestCase testCase : testCases) {
            List<String> inputs = testCase.getInputs();
            List<String> expectedOutputs = testCase.getExpectedOutputs();

            for (int i = 0; i < inputs.size(); i++) {
                String input = inputs.get(i);
                String expectedOutput = expectedOutputs.get(i);

                Status status = Util.execute(testCase.getCode(), testCase.getType(), input.replace(",", "\n"));// Substituimos
                                                                                                               // as
                                                                                                               // virgulas
                                                                                                               // por
                                                                                                               // paragrafo.
                boolean passed = expectedOutput.equals(status.output.trim());
                summary.addTestResult(new TestResult(
                        input,
                        expectedOutput,
                        status.output.trim(),
                        passed,
                        status.executionTime,
                        status.usedMemory));
            }
        }
        return summary;
    }

    public List<UserTestSummary> generateRanking(Map<String, UserTestSummary> userSummaries) {
        List<UserTestSummary> ranking = new ArrayList<>(userSummaries.values());
        ranking.sort((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints())); // Descending order
        return ranking;
    }
}
