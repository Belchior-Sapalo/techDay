package executor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Util
{
    public static final String platform =
                    System.getProperty("os.name").toLowerCase();

    private static String TEMPDIR = "tmp";

    public enum CodeType {
        C("C", "c"),
        CPP("C++", "cpp"),
        PHP("PHP", "php"),
        JAVA("Java", "java"),
        PYTHON("Python", "py"),
        JS("JavaScript", "js");

        private String name;
        private String extension;

        CodeType(String name, String extension)
        {
            this.name = name;
            this.extension = extension;
        }

        public String getExtension()
        {
            return extension;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    public static Code codeFromString(String code, String outputPath, CodeType type)
            throws IOException
    {
        // long time = System.currentTimeMillis();
        // String filename = String.format("Code_%d.%s", time, type.getExtension());
        // // create the target directory, if needed
        // Files.createDirectories(Path.of(outputPath));

        // File file = new File(outputPath, filename);
        // BufferedWriter writer = Files.newBufferedWriter(file.toPath());

        // if (type == CodeType.JAVA)
        // {
        //     String className = "Code_" + time;
        //     writer.write(JavaCode.addClassTo(code, className));
        // }
        // else
        // {
        //     writer.write(code);
        // }
        String filename;
        long time = System.currentTimeMillis();

        if (type == CodeType.JAVA)
        {
            String[] values = JavaCode.normalise(code);
            filename = values[0] + ".java"; // new class name
            code     = values[1];           // refactored code
        }
        else
        {
            filename = String.format("Code_%d.%s", time, type.getExtension());
        }

        // create the target directory, if needed
        Files.createDirectories(Path.of(outputPath));
        File file = new File(outputPath, filename);
        BufferedWriter writer = Files.newBufferedWriter(file.toPath());

        writer.write(code);
        writer.flush();
        writer.close();

        switch (type) {
            case C:
                return new CCode(file);
            case CPP:
                return new CPPCode(file);
            case PHP:
                return new PHPCode(file);
            case JAVA:
                return new JavaCode(file);
            case PYTHON:
                return new PyCode(file);
            case JS:
                return new JSCode(file);
            default:
                throw new IllegalArgumentException("Unknown code type " + type);
        }
    }

    public static Status execute(String strCode, CodeType type, String... inputData)
    {
        Code code = null;

        try
        {
            code = codeFromString(strCode, TEMPDIR, type);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new Status(
                1,
                null,
                null,
                0L,
                0L,
                null,
                String.format("Error: could not run the program [%s]", type));
        }

        Status status = Executor.compile(code);
        if (status.exitValue != 0)
        {
            cleanUp(code);
            return status;
        }

        status = Executor.execute(code, inputData);
        cleanUp(code);
        return status;
    }

    public static void cleanUp(Code code)
    {
        // delete temporary files
        try
        {
            Files.deleteIfExists(code.getInputFile().toPath());
            Files.deleteIfExists(code.getOutputFile().toPath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
