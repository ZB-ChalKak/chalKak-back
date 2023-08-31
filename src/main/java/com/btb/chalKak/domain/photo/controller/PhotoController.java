package com.btb.chalKak.domain.photo.controller;


import com.btb.chalKak.domain.photo.service.PhotoService;
import com.btb.chalKak.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
@Slf4j
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadProduct(MultipartFile[] multipartFileList){

        Post post = Post.builder()
            .content("1231")
                .build();

        log.info(multipartFileList.toString());

        return ResponseEntity.ok().body(photoService.upload(multipartFileList, post).get(0).getPhotoUrl());
    }
}
