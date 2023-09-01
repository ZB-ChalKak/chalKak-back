package com.btb.chalKak.domain.comment.repository;


import com.btb.chalKak.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByPostId (Long postId);

    List<Comment> findCommentByPostId (Long postId);
    @Query("DELETE FROM Comment c WHERE c.id = ?1")
    int deleteCommentById (Long commentId);

    Page<Comment> findByPostIdOrderByCreatedAtDesc (Long postId, Pageable pageable);

}