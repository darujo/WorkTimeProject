package ru.darujo.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

@RestController()
@RequestMapping("/v1/update")
public class UpdateController {


    @PostMapping("update")
    public String updateFile(@RequestHeader String username,
                             @RequestPart("file") MultipartFile multipartFile) {
        try (FileOutputStream fout = new FileOutputStream(multipartFile.getOriginalFilename())) {
            fout.write(multipartFile.getBytes());
            return "Success!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed!";
        }
    }


}