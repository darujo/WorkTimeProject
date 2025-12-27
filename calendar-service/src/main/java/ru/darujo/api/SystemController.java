package ru.darujo.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.TestAndShutdownController;


@RestController()
@RequestMapping("/v1")
@Log4j2
public class SystemController extends TestAndShutdownController {
}
