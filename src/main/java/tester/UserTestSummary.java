package tester;

import java.util.ArrayList;
import java.util.List;

public class UserTestSummary {
    private  boolean  passed= true;
    private final List<TestResult> testResults = new ArrayList<>();

    public void addTestResult(TestResult result) {
        testResults.add(result);
        if(!result.passed){
            this.passed = false;
        }
    }

    public boolean getPassed(){
        return this.passed;
    }


}