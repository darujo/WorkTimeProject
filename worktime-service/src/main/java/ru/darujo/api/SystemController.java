package ru.darujo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.darujo.TestAndShutdownController;


@RestController()
@RequestMapping("/v1/worktime")
public class SystemController extends TestAndShutdownController {

}
