package com.btb.chalKak.domain.photo.service;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import java.time.LocalDateTime;
import java.util.Base64;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.btb.chalKak.domain.photo.entity.Photo;
import com.btb.chalKak.domain.photo.repository.PhotoRepository;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoService {

  private final static String S3Bucket = "spring-photo-bucket"; // Bucket 이름

  private final AmazonS3Client amazonS3Client;
  private final PhotoRepository photoRepository;


  public List<String> upload(MultipartFile[] multipartFileList, Object instance) throws Exception {
    List<String> imagePathList = new ArrayList<>();
//        Long[] orders = form.getOrder();

    for (int i = 0; i < multipartFileList.length; i++) {
      MultipartFile multipartFile = multipartFileList[i];
      String originalName = multipartFile.getOriginalFilename(); // file name
      long size = multipartFile.getSize(); // file size


      StringBuilder sb = new StringBuilder(originalName);
      sb.append(LocalDateTime.now());

      String encodedString = Base64.getEncoder().encodeToString(sb.toString().getBytes());

      ObjectMetadata objectMetaData = new ObjectMetadata();
      objectMetaData.setContentType(multipartFile.getContentType());
      objectMetaData.setContentLength(size);

      // Upload to S3
      amazonS3Client.putObject(
          new PutObjectRequest(S3Bucket, encodedString, multipartFile.getInputStream(),
              objectMetaData)
              .withCannedAcl(CannedAccessControlList.PublicRead)
      );

      String imagePath = amazonS3Client.getUrl(S3Bucket, encodedString)
          .toString(); // Get the accessible URL

      if (instance instanceof Member) {

        photoRepository.save(Photo.builder()
            .photoUrl(imagePath)
            .member((Member) instance)
            .photoName(originalName)
            .build());

      }

      if (instance instanceof Post) {

        photoRepository.save(Photo.builder()
            .photoUrl(imagePath)
            .post((Post) instance)
            .photoName(originalName)
            .build());
      }

      imagePathList.add(imagePath);
    }
    return imagePathList;
  }
}
