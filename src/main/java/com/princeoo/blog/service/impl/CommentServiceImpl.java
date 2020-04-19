package com.princeoo.blog.service.impl;

import com.princeoo.blog.dao.CommentRepository;
import com.princeoo.blog.pojo.Comment;
import com.princeoo.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        //只拿到顶级数据，放到eachComment方法中
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        //用一个新的List<Comment>存放数据，防止原有的数据发生变化
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //combineChildren用于合并评论的各层子代到一个第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        //拿到的是commentsView，还是父评论
        for (Comment comment : comments) {
            //父评论的第一子代
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //这是一个循环的方法，目的是找出所有子代放到一起，最终返回一个tempReplys集合。
                recursively(reply1);
            }
            //把所有的子代放到tempReplys容器中，作为comment父评论单独的一个子代集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {//(comment)里面是第一子代
        tempReplys.add(comment);//将第一子代放容器中
        //只要子代大于0，就不停的找子代的子代。
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);//不停遍历，把取到的子代放到容器中
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
