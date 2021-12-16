package cn.neu.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传测试
 */
@Slf4j
@Controller
public class FormTestController {
    @GetMapping("/form_layouts")
    public String form_layouts(){
        return "form/form_layouts";
    }

    /**
     * MultipartFile  自动封装上传过来的文件
     * @param email
     * @param username
     * @param headerImage
     * @param photos
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("email") String email,
                         @RequestParam("username") String username,
                         @RequestPart("headerImage") MultipartFile headerImage,
                         @RequestPart("photos") MultipartFile[] photos) throws IOException {

        log.info("上传的信息：email={},username={},headerImage={},photos={}",
                email,username,headerImage.getSize(),photos.length);
        if (!headerImage.isEmpty()){
            //保存到服务器
            String originalFilename = headerImage.getOriginalFilename();
            headerImage.transferTo(new File("D:\\uploadFile\\"+originalFilename));
        }
        if (photos.length > 0){
            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()){
                    String name = photo.getOriginalFilename();
                    photo.transferTo(new File("D:\\uploadFile\\"+name));
                }
            }
        }
        return "main";//直接返回到main页面
    }
}
