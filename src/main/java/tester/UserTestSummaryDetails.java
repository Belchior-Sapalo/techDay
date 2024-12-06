package tester;

import java.util.List;

//Usamos apenas para quando quisermos exibir mais detalhes sobre os teste.
class UserTestSummaryDetails {
    private final String userId;
    private final long totalTests;
    private final long passedTests;
    private final long failedTests;
    private final List<TestResult> testResults;

    public UserTestSummaryDetails( String userId, long totalTests, long passedTests,long failedTests, int totalPoints, List<TestResult> testResults) {
        this.userId = userId;
        this.totalTests = totalTests;
        this.passedTests = passedTests;
        this.testResults = testResults;
        this.failedTests = failedTests;
    }

    public String getUserId() {
        return userId;
    }

    public long getTotalTests() {
        return totalTests;
    }

    public long getPassedTests() {
        return passedTests;
    }
    
    public long getFailedTests() {
        return failedTests;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    @Override
    public String toString() {
        return String.format("(ID: %s) \n Total Tests: %d \n Passed: %d \n Test Details: %s",
                 userId, totalTests, passedTests, testResults);
    }
}