package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.MatrizCurricular;
import com.samha.domain.log.MatrizCurricularAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/matrizCurricular")
public class MatrizCurricularController extends BaseController<MatrizCurricular, MatrizCurricularAud, Long> {
    public MatrizCurricularController(UseCaseFacade facade) {
        super(MatrizCurricular.class, MatrizCurricularAud.class, facade);
    }
}
