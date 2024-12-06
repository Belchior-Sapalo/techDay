package tester;

import executor.Util;
import java.util.List;

//Caso de teste//Usado para representar o teste do usuario.
public class TestCase {
    private final String code;
    private final Util.CodeType type;
    private final List<String> inputs;
    private final List<String> expectedOutputs;

    public TestCase(String code, Util.CodeType type, List<String> inputs, List<String> expectedOutputs) {
        this.code = code;
        this.type = type;
        this.inputs = inputs;
        this.expectedOutputs = expectedOutputs;
    }
    

    //Metodos para auxiliar...
    public String getCode() {
        return code;
    }

    public Util.CodeType getType() {
        return type;
    }

    public List<String> getInputs() {
        return inputs;
    }

    public List<String> getExpectedOutputs() {
        return expectedOutputs;
    }
    //Metodos auxiliares...
}

