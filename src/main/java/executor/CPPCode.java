package executor;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class CPPCode extends Code
{
    public CPPCode(File file)
    {
        this(file, file, "g++");

        // set the name of the outputfile
        String output = file.toString();
        if (output.endsWith(".cpp"))
        {
            int i = output.length() - ".cpp".length();
            output = output.substring(0, i);
        }

        if (Util.platform.startsWith("windows"))
            setOutputFile(new File(output + ".exe"));
        else
            setOutputFile(new File("./" + output));
    }

    public CPPCode(File input, File output, String compiler)
    {
        super(input, output, compiler, null);
    }

    @Override
    public List<String> compilationCommands()
    {
        List<String> commands = new ArrayList<>(5);
        commands.add(getCompiler());
        commands.addAll(this.getCompilerFlags());

        commands.add("-o");
        commands.add(getOutputFile().toString());
        commands.add(getInputFile().toString());
        return commands;
    }

    @Override
    public List<String> executionCommands()
    {
        List<String> commands = new ArrayList<>(5);
        commands.add(getOutputFile().toString());
        commands.addAll(this.getProgramArguments());

        return commands;
    }
}
