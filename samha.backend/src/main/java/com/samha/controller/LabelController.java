package com.samha.controller;

import com.samha.commons.UseCaseFacade;
import com.samha.controller.common.BaseController;
import com.samha.domain.Label;
import com.samha.domain.log.LabelAud;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/label")
public class LabelController extends BaseController<Label, LabelAud, Long> {
    public LabelController(UseCaseFacade facade) {
        super(Label.class, LabelAud.class, facade);
    }
}
