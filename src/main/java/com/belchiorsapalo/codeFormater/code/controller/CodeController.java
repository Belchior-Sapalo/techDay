package com.belchiorsapalo.codeFormater.code.controller;
import com.belchiorsapalo.codeFormater.code.DTO.CodeDTO;
import com.belchiorsapalo.codeFormater.code.DTO.SubmitResponseDTO;
import com.belchiorsapalo.codeFormater.code.DTO.TestResponseDTO;
import com.belchiorsapalo.codeFormater.code.services.CodeService;
import com.belchiorsapalo.codeFormater.exceptions.InvalidCodeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/code")
public class CodeController {

    private CodeService codeService;

    @Autowired
    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("/process")
    public ResponseEntity<SubmitResponseDTO> processCode(@RequestBody CodeDTO codeDTO) throws InvalidCodeException, InterruptedException{
        return ResponseEntity.ok().body(codeService.processCode(codeDTO));
    }

    @PostMapping("/test")
    public ResponseEntity<TestResponseDTO> testCode(@RequestBody CodeDTO codeDTO){
        return ResponseEntity.ok().body(codeService.testCode(codeDTO));
    }
}
