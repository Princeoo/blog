package com.princeoo.blog.dao;

import com.princeoo.blog.pojo.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    //sort排序
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}
