package executor;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class CCode extends Code
{
    public CCode(File file)
    {
        this(file, file, "gcc");

        // remove the extension for outputfile
        String output = file.toString();
        if (output.endsWith(".c"))
        {
            int i = output.length() - ".c".length();
            output = output.substring(0, i);
        }

        if (Util.platform.startsWith("windows"))
            setOutputFile(new File(output + ".exe"));
        else
            setOutputFile(new File("./" + output));
    }

    public CCode(File input, File output, String compiler)
    {
        super(input, output, compiler, null);
    }

    @Override
    public List<String> compilationCommands()
    {
        List<String> commands = new ArrayList<>(5);
        commands.add(this.getCompiler());
        commands.addAll(this.getCompilerFlags());

        commands.add("-o");
        commands.add(this.getOutputFile().toString());
        commands.add(this.getInputFile().toString());
        return commands;
    }

    @Override
    public List<String> executionCommands()
    {
        List<String> commands = new ArrayList<>(5);
        commands.add(this.getOutputFile().toString());
        commands.addAll(this.getExecutorFlags());

        return commands;
    }
}
