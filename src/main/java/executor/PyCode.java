package executor;
import java.io.File;

public class PyCode extends Code
{
    public PyCode(File file)
    {
        this(file, "python3");

        // python is called diferently on windows
        if (Util.platform.startsWith("windows"))
            setExecutor("python");
    }

    public PyCode(File file, String executor)
    {
        super(file, file, null, executor);
        markAsExecutable();
    }
}
