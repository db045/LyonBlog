package com.flowingsun.behavior.service;

import com.flowingsun.article.entity.Article;
import com.flowingsun.article.vo.CategoryArticleQuery;
import com.flowingsun.behavior.entity.Comment;
import com.flowingsun.behavior.entity.Picture;
import com.flowingsun.behavior.entity.Thank;
import com.flowingsun.behavior.vo.PictureQuery;
import com.flowingsun.user.entity.User;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.Date;

public interface BehaviorService {

    String setComment(Comment commentBean, HttpServletRequest request);

    String setThank(Thank thankBean,HttpServletRequest request);

    boolean saveUserImage(Picture picture);

    String imageUpload(MultipartHttpServletRequest request);

    PictureQuery getUserImages(PictureQuery pictureQuery) throws ParseException;

    Article getUserArticleBehavior(Article article,Long userId);

    CategoryArticleQuery getUserCategoryArticleBehavior(CategoryArticleQuery categoryArticleQuery, Long userId);
}
