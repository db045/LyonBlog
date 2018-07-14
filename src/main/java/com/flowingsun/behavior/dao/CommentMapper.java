package com.flowingsun.behavior.dao;

import com.flowingsun.behavior.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    Integer insertSelective(Comment commentBean);

    Comment selectByPrimaryKey(Integer id);

    Integer selectCommentStatusByAidUid(@Param("userId") Long userId, @Param("articleId") Integer articleId);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Comment> selectAllCommentByArticleid(Integer articleId);
}