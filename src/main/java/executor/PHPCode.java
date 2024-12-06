package executor;
import java.io.File;

public class PHPCode extends Code
{
    public PHPCode(File file)
    {
        this(file, "php");
    }

    public PHPCode(File file, String executor)
    {
        super(file, file, null, executor);
        markAsExecutable();
    }
}
