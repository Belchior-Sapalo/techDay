package executor;

import java.io.File;

public class JSCode extends Code
{
    public JSCode(File file)
    {
        this(file, "node");
    }

    public JSCode(File file, String executor)
    {
        super(file, file, null, executor);
        markAsExecutable();
    }
}
