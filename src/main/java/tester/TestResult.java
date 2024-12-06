package tester;

//Representa o teste individual de cada TestCase..
class TestResult {
    private final String input;
    private final String expectedOutput;
    private final String actualOutput;
    final boolean passed;
    private final long executionTime;
    private final long usedMemory;

    public TestResult(String input, String expectedOutput, String actualOutput, boolean passed, long executionTime,
            long usedMemory) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.passed = passed;
        this.executionTime = executionTime;
        this.usedMemory = usedMemory;
    }

    public String getInput() {
        return input;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public String getActualOutput() {
        return actualOutput;
    }

    public boolean isPassed() {
        return passed;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public long getUsedMemory() {
        return usedMemory;
    }

    @Override
    public String toString() {
        return String.format(
                "Input: %s | Expected: %s | Actual: %s | Passed: %s | Time: %dms | Memory: %d bytes",
                input.replace("\n", ", "), expectedOutput, actualOutput, passed, executionTime / 1_000_000, usedMemory);
    }
}