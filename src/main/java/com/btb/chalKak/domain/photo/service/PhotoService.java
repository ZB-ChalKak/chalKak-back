package com.btb.chalKak.domain.photo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.btb.chalKak.domain.photo.entity.Photo;
import com.btb.chalKak.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoService {

    private final static String S3Bucket = "spring-photo-bucket"; // Bucket 이름

    private final AmazonS3 amazonS3Client;

    @Transactional
    public List<Photo> upload(MultipartFile[] multipartFileList, Post post) {
        List<String> imagePathList = new ArrayList<>();
        List<Photo> photos = new ArrayList<>();

        log.info("Here Photo Service!");
        int order = 0;

        try {
            for (MultipartFile multipartFile : multipartFileList) {
                // Null and size check
                if (multipartFile == null || multipartFile.isEmpty()) {
                    continue;
                }

                String originalName = Optional.ofNullable(multipartFile.getOriginalFilename())
                        .orElse(UUID.randomUUID().toString()); // default to random UUID if null

                long size = multipartFile.getSize();

                StringBuilder sb = new StringBuilder(originalName);

                sb.append(LocalDateTime.now());

                String encodedString = Base64.getEncoder().encodeToString(sb.toString().getBytes());

                ObjectMetadata objectMetadata = new ObjectMetadata();

                objectMetadata.setContentType(Optional.ofNullable(multipartFile.getContentType())
                        .orElse("application/octet-stream")); // default to "application/octet-stream" if null
                objectMetadata.setContentLength(size);

                // Upload to S3
                amazonS3Client.putObject(
                        new PutObjectRequest(S3Bucket, encodedString,
                                multipartFile.getInputStream(),
                                objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead)
                );

                String imagePath = amazonS3Client.getUrl(S3Bucket, encodedString).toString();

                photos.add(
                        Photo.builder()
                                .url(imagePath)
                                .order(++order)
                                .post(post)
                                .name(originalName)
                                .build()
                );

                imagePathList.add(imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photos;
    }
}
