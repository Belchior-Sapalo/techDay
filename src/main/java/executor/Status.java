package executor;

public class Status
{
    public enum ProcessType { COMPILATION, EXECUTION };
    public final int exitValue;
    public final String classType;
    public final ProcessType processType;
    public final String file;
    public final long executionTime;
    public final long usedMemory;
    public final String output;

    public Status(int exitValue, String classType, ProcessType processType,
                  long etime, long memory, String file, String output)
    {
        this.exitValue     = exitValue;
        this.classType     = classType;
        this.processType   = processType;
        this.executionTime = etime;
        this.usedMemory    = memory;
        this.file          = file;
        this.output        = output;
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        out.append("Exit Value: ").append(exitValue).append('\n');
        out.append("Class Type: ").append(classType).append('\n');
        out.append("Process Type: ").append(processType).append('\n');
        out.append("Runtime: ").append(executionTime).append('\n');
        out.append("Used Memory: ").append(usedMemory).append('\n');
        out.append("file: ").append(file).append('\n');
        out.append("output: ").append(output);
        return out.toString();
    }
}
