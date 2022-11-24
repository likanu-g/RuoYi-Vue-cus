package com.ruoyi.web.controller.tool;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="/getString")
public class SpringDocController {

    @Operation(summary = "This SpringDoc Test One.")
    @GetMapping
    public String getClients() {
        return "Hello First SpringDoc Application!";
    }

}
