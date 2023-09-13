package com.btb.chalKak.domain.hashTag.entity;

import com.btb.chalKak.domain.post.entity.Post;
import com.btb.chalKak.common.entity.BaseTimeEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hash_tag")
public class HashTag extends BaseTimeEntity {

    @Id
    @Column(name ="hash_tag_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false, unique = true)
    private String keyword;

    @ManyToMany(mappedBy = "hashTags")
    private List<Post> posts;
}
