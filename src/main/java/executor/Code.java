package executor;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

public class Code
{
    private String compiler; // compiler command/program
    private String executor; // executor command/program/"execution notation"
    private File inputFile;
    private File outputFile;
    private boolean executable; // mark that a code can be executed
    private final List<String> compilerFlags;
    private final List<String> executorFlags;
    private final List<String> programArguments; // args to add when executing the program

    public Code(File input, File output, String compiler, String executor)
    {
        if (input == null || output == null)
            throw new IllegalArgumentException("inputFile can not be null");

        this.inputFile  = input;
        this.outputFile = output;
        this.compiler   = compiler;
        this.executor   = executor;
        this.executable = false;

        compilerFlags    = new ArrayList<String>();
        executorFlags    = new ArrayList<String>();
        programArguments = new ArrayList<String>();
    }

    public void markAsExecutable()
    {
        this.executable = true;
    }

    public void setInputFile(File file)
    {
        this.inputFile = file;
    }

    public void setOutputFile(File file)
    {
        this.outputFile = file;
    }

    public void setCompilerFlags(String ...flags)
    {
        for (String flag : flags)
            this.compilerFlags.add(flag);
    }

    public void setExecutionFlags(String ...flags)
    {
        for (String flag : flags)
            this.executorFlags.add(flag);
    }

    public void setProgramArguments(String ...values)
    {
        for (String value : values)
            this.programArguments.add(value);
    }

    public void setCompiler(String compiler)
    {
        this.compiler = compiler;
    }

    public void setExecutor(String executor)
    {
        this.executor = executor;
    }

    public File getInputFile()
    {
        return this.inputFile;
    }

    public File getOutputFile()
    {
        return this.outputFile;
    }

    protected List<String> getCompilerFlags()
    {
        return this.compilerFlags;
    }

    protected List<String> getExecutorFlags()
    {
        return this.executorFlags;
    }

    protected List<String> getProgramArguments()
    {
        return this.programArguments;
    }

    public boolean isExecutable()
    {
        return this.executable;
    }

    public String getCompiler()
    {
        return this.compiler;
    }

    public String getExecutor()
    {
        return this.executor;
    }

    public List<String> compilationCommands()
    {
        List<String> commands = new ArrayList<>(3);
        commands.add(this.compiler);
        commands.addAll(this.getCompilerFlags());
        commands.add(this.inputFile.toString());

        return commands;
    }

    public List<String> executionCommands()
    {
        List<String> commands = new ArrayList<>(4);
        commands.add(this.executor);
        commands.addAll(this.getExecutorFlags());
        commands.add(this.outputFile.toString());
        commands.addAll(this.getProgramArguments());

        return commands;
    }

    public void clean()
    {
        clean(false);
    }

    public void clean(boolean skipInput)
    {
    // delete temporary files
        try
        {
            if (!skipInput)
                Files.deleteIfExists(this.getInputFile().toPath());
            Files.deleteIfExists(this.getOutputFile().toPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder();
        if (this.compiler != null)
            out.append(String.format("Compiler Commands: %s%n", compilationCommands()));
        if (this.executor != null)
            out.append(String.format("Execution Commands: %s", executionCommands()));

        return out.toString();
    }
}
