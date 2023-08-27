 package com.btb.chalKak.domain.follow.entity;

import com.btb.chalKak.domain.member.entity.Member;
import com.btb.chalKak.domain.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

 @Getter
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 @Entity
 @EntityListeners(AuditingEntityListener.class)
 @Table(name = "follow")
 public class Follow {

     @Id
     @Column(name ="follow_id", nullable = false)
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "member_id")
     private Member following;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "member_id")
     private Member follower;

     @CreatedDate
     @Column(name ="followed_at")
     private LocalDateTime followedAt;
 }

