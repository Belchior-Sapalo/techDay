package tester;

import java.util.ArrayList;
import java.util.List;

public class UserTestSummary {
    private final String userId;
    private final List<TestResult> testResults = new ArrayList<>();
    private int totalPoints = 0;

    public UserTestSummary( String userId) {
        this.userId = userId;
    }

    public void addTestResult(TestResult result) {
        testResults.add(result);
        totalPoints += result.getPoints();
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public UserTestSummaryDetails getSummaryDetails() {
        long passedTests = testResults.stream().filter(result -> result.passed).count();
        long failedTests = testResults.stream().filter(result -> !result.passed).count();
        
        return new UserTestSummaryDetails( userId, testResults.size(), passedTests,failedTests, totalPoints, testResults);
    }
}