package executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class Executor
{
    private static Status status = null;
    private static long COMPILATION_WAIT_TIME = 1000 * 5; // compilation wait time in milliseconds
    private static long EXECUTION_WAIT_TIME   = 1000 * 5; // execution wait time in milliseconds

    public static Status compile(Code code)
    {
        if (code.isExecutable())
        {
            setStatus(
                0,
                null,
                Status.ProcessType.COMPILATION,
                0,
                0,
                null,
                "No need to be compiled");
            return status;
        }

        List<String> compilationCommands = code.compilationCommands();
        // System.out.printf("Compiling %s...%n", compilationCommands);

        ProcessBuilder pb = new ProcessBuilder(compilationCommands);
        StringBuilder output = new StringBuilder();
        Process process = null;

        long timer;
        try
        {
            timer = System.nanoTime();
            process = pb.start();
        }
        catch (IOException e)
        {
            // could not start the process
            setStatus(
                2,
                code.getClass().getName(),
                Status.ProcessType.EXECUTION,
                0,
                0,
                code.getInputFile().toString(),
                "Error: Could not start the process");
            return status;
        }

        try
        {
            boolean timeout = holdProcess(process, COMPILATION_WAIT_TIME);
            timer = System.nanoTime() - timer;

            if (timeout)
                throw new InterruptedException("Program take too long to compile.");

            try (BufferedReader reader = process.errorReader())
            {
                String line;
                while ((line = reader.readLine()) != null) output
                    .append(line)
                    .append('\n');
            }

            setStatus(
                process.exitValue(),
                code.getClass().getName(),
                Status.ProcessType.COMPILATION,
                timer,
                0,
                code.getOutputFile().toString(),
                output.toString());
        }
        catch (IOException | InterruptedException e)
        {
            int exitValue;
            if (e instanceof InterruptedException)
            {
                output.append("Program Interrupted: take too long to compile");
                exitValue = 1;
            }
            else
            {
                e.printStackTrace();
                output.append(e.getClass().getName())
                    .append(": ")
                    .append(e.getMessage())
                    .append('\n');

                for (StackTraceElement element : e.getStackTrace())
                    output.append("\t" + element.toString() + "\n");

                exitValue = process.exitValue();
            }

            setStatus(
                exitValue,
                code.getClass().getName(),
                Status.ProcessType.COMPILATION,
                0L,
                0L,
                code.getOutputFile().toString(),
                output.toString());
        }

        if (status.exitValue == 0)
            code.markAsExecutable();

        return status;
    }

    public static Status execute(Code code, String... inputData)
        throws IllegalArgumentException
    {
        if (!code.isExecutable())
            throw new IllegalArgumentException("the code is not executable");

        BufferedWriter writer = null;
        BufferedReader reader = null;

        List<String> executionCommands = code.executionCommands();
        // System.out.printf("Executing %s...%n", executionCommands);

        StringBuilder output = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder(executionCommands);
        pb.redirectErrorStream(true);
        Process process = null;

        // for clean up purposes
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long cusedMemory = runtime.totalMemory() - runtime.freeMemory();

        long timer;
        try
        {
            timer = System.nanoTime();
            process = pb.start();
        }
        catch (IOException e)
        {
            // could not start the process
            setStatus(
                2,
                code.getClass().getName(),
                Status.ProcessType.EXECUTION,
                0,
                0,
                code.getInputFile().toString(),
                "Error: Could not start the process");
            return status;
        }

        try
        {
            // send the input data to the process input stream
            if (inputData.length > 0)
            {
                writer = process.outputWriter();
                for (String input : inputData)
                    writer.write(input + '\n');
                try
                { writer.flush(); }
                catch (IOException e){/*The stream is closed, lets just ignore it.*/}
            }

            boolean timeout = holdProcess(process, EXECUTION_WAIT_TIME);
            timer = System.nanoTime() - timer;

            if (timeout)
                throw new InterruptedException("Program take too long to execute.");

            reader = process.inputReader();
            String line;
            while ((line = reader.readLine()) != null)
                output.append(line).append('\n');

            cusedMemory = runtime.totalMemory() - runtime.freeMemory() - cusedMemory;
            setStatus(
                process.exitValue(),
                code.getClass().getName(),
                Status.ProcessType.EXECUTION,
                timer,
                cusedMemory,
                code.getOutputFile().toString(),
                output.toString());
        }
        catch (IOException | InterruptedException e)
        {
            int exitValue;
            if (e instanceof InterruptedException)
            {
                output.append("Program Interrupted: take too long to execute");
                exitValue = 1;
            }
            else
            {
                e.printStackTrace();
                output.append(e.getClass().getName())
                    .append(": ")
                    .append(e.getMessage())
                    .append('\n');

                for (StackTraceElement element : e.getStackTrace())
                    output.append("\t" + element.toString() + "\n");

                exitValue = process.exitValue();
            }

            setStatus(
                exitValue,
                code.getClass().getName(),
                Status.ProcessType.EXECUTION,
                0L,
                0L,
                code.getOutputFile().toString(),
                output.toString());
        }
        finally
        {
            try
            {
                if (writer != null) writer.close();
                if (reader != null) reader.close();
            }
            catch (IOException e){/*The stream is closed, lets just ignore it.*/}
        }

        return status;
    }

    private static boolean holdProcess(Process p, long time)
    {
        boolean timeout = false;
        long startTime = System.currentTimeMillis();

        while (p.isAlive())
        {
            if (System.currentTimeMillis() - startTime >= time)
            {
                timeout = true;
                break;
            }
        }

        if (timeout)
        {
            p.destroyForcibly();
            // wait the process to be destroyed
            while (p.isAlive());
        }

        return timeout;
    }

    private static void setStatus(int exitValue, String clsType, Status.ProcessType pt,
                    long runtime, long memory, String file, String output)
    {
        status = new Status(
            exitValue,
            clsType,
            pt,
            runtime,
            memory,
            file,
            output);
    }

    public static Status getStatus()
    {
        return status;
    }
}
