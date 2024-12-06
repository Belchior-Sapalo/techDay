package executor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class JavaCode extends Code
{
    private static final Pattern classPattern =
                Pattern.compile("public\\s+class\\s+((\\w+))");

    public JavaCode(File file)
    {
        this(file, "javac", "java");
    }

    public JavaCode(File file, String compiler, String executor)
    {
        super(file, file, compiler, executor);
        File input = getInputFile();
        String output = input.toString();

        if (output.endsWith(".java"))
        {
            int i = output.length() - ".java".length();
            output = output.substring(0, i) + ".class";
        }

        setOutputFile(new File(output));
        setExecutionFlags("-cp", input.getParent());
    }

    public String className()
    {
        String name = getOutputFile().getName();
        int i = name.length() - ".class".length();
        return name.substring(0, i);
    }

    @Override
    public List<String> executionCommands()
    {
        List<String> commands = new ArrayList<>(5);
        commands.add(this.getExecutor());
        commands.addAll(this.getExecutorFlags());
        commands.add(this.className());
        commands.addAll(this.getProgramArguments());

        return commands;
    }

    // some statics method to handle java file
    public static boolean hasMainClass(String code)
    {
        return classPattern.matcher(code).matches();
    }

    public static String[] normalise(String code)
    {
        long time = System.currentTimeMillis();
        Matcher matcher = classPattern.matcher(code);
        String altName = "JavaCode_" + time;

        if (!matcher.find())
        {
            code = addClassTo(code, altName);
        }
        else
        {
            altName = matcher.group(1) + "_" + time;
            code = matcher.replaceFirst("public class " + altName);
        }

        String[] ret = {altName, code};
        return ret;
    }

    public static String addClassTo(String code, String className)
    {
        // add a class name to a code that don't have one.
        boolean mainClassAdded = false;
        StringBuilder newCode = new StringBuilder();

        for (String line : code.split("\n"))
        {
            if (line.startsWith("import"))
            {
                newCode.append(line + "\n");
                continue;
            }

            if (!mainClassAdded)
            {
                newCode.append(String.format("public class %s {%n", className));
                mainClassAdded = true;
            }

            newCode.append(line + "\n");
        }

        newCode.append("}");
        return newCode.toString();
    }
}
