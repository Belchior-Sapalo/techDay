package tester;

//Representa o teste individual de cada TestCase..
class TestResult {
    private final String input;
    private final String expectedOutput;
    private final String actualOutput;
    final boolean passed;
    private final long executionTime;
    private final long usedMemory;
    private final int points;

    public TestResult(String input, String expectedOutput, String actualOutput, boolean passed, long executionTime,
            long usedMemory) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.passed = passed;
        this.executionTime = executionTime;
        this.usedMemory = usedMemory;
        // Calcula os pontos ao criar classe.
        this.points = calculatePoints();
    }

    private int calculatePoints() {
        // Pontos base para sucesso ou falha
        int basePoints = passed ? 5 : 0;

        // Pontos com base no tempo de execução (quanto menor o tempo, maior o bônus)
        int timeBonus = passed ? calculateInvertedBonus(executionTime, 10_000_000L) : 0; // Define intervalo de 100ms

        // Pontos com base no uso de memória (quanto menor a memória, maior o bônus)
        int memoryBonus = passed ? calculateInvertedBonus(usedMemory, 100_000) : 0; // Define intervalo de 1MB
        // Soma total dos pontos
        int totalPoints = (basePoints + timeBonus + memoryBonus) >= 0 ? basePoints + timeBonus + memoryBonus : 0;
        return totalPoints;
    }

    // Método auxiliar para calcular bônus inverso (quanto menor o valor, maior o
    // bônus)
    private int calculateInvertedBonus(long value, long interval) {
        if (value <= 0)
            return 3; // Pontuação máxima para valores muito baixos ou zero
        int n = (int) (value / interval); // Determina qual intervalo o valor se encaixa
        return Math.max(3 - n, 0); // Pontuação decrescente com base no intervalo (mínimo de 0)
    }

    public int getPoints() {
        return points;
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
                "Input: %s | Expected: %s | Actual: %s | Passed: %s | Time: %dms | Memory: %d bytes | Points: %d",
                input.replace("\n", ", "), expectedOutput, actualOutput, passed, executionTime / 1_000_000, usedMemory,
                points);
    }
}