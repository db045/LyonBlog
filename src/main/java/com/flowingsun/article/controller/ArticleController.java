package com.flowingsun.article.controller;

import com.alibaba.fastjson.JSONObject;
import com.flowingsun.article.entity.Article;
import com.flowingsun.article.entity.ArticleTag;
import com.flowingsun.article.entity.Category;
import com.flowingsun.article.entity.RegularRecommend;
import com.flowingsun.article.service.ArticleService;
import com.flowingsun.article.vo.CategoryArticleQuery;

import com.flowingsun.article.vo.TagArticleQuery;
import com.flowingsun.behavior.service.BehaviorService;
import com.flowingsun.user.entity.User;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private BehaviorService behaviorService;

    @RequestMapping("/category")
    public String categoryArticle(@RequestParam("cId") Integer cId,@RequestParam(value="pageNum",required=false,defaultValue = "1")Integer pageNum, @RequestParam(value="pageSize",required=false,defaultValue = "10")Integer pageSize,Model model){
        CategoryArticleQuery queryBean = new CategoryArticleQuery();
        queryBean.setPageSize(pageSize);
        queryBean.setPageNum(pageNum);
        queryBean.setcId(cId);
        List<Category> categorys = articleService.getCategory();
        CategoryArticleQuery categoryArticleQuery = articleService.getCategoryArticles(cId,queryBean);
        model.addAttribute("categorys",categorys);
        Long userId = (Long)SecurityUtils.getSubject().getSession().getAttribute("userId");
        List<ArticleTag> allTags = articleService.selectAllTag();
        model.addAttribute("allTags",allTags);
        if(userId!=null){
            CategoryArticleQuery result = behaviorService.getUserCategoryArticleBehavior(categoryArticleQuery,userId);
            model.addAttribute("pageQueryBean",result);
        }else{
            model.addAttribute("pageQueryBean",categoryArticleQuery);
        }
        return "/article/categoryArticle";
    }

    @RequestMapping("/single")
    public String singleArticle(@RequestParam("articleId") Integer articleId,Model model){
        List<Category> categorys = articleService.getCategory();
        model.addAttribute("categorys",categorys);
        if(articleService.checkArticleExist(articleId)){
            Article article = articleService.getArticle(articleId);
            List<ArticleTag> allTags = articleService.selectAllTag();
            model.addAttribute("allTags",allTags);
            if(article!=null){
                RegularRecommend regularRecommend = articleService.getRegularRecommendArticle(articleId);
                if(regularRecommend!=null){article.setRegularRecommend(regularRecommend);}
                Long userId = (Long)SecurityUtils.getSubject().getSession().getAttribute("userId");
                if(userId!=null){
                    Article result = behaviorService.getUserArticleBehavior(article,userId);
                    model.addAttribute("article",result);
                }else{model.addAttribute("article",article);}
            }
        }
        return "/article/singleArticle";
    }

    @RequiresPermissions("article:changeCategory")
    @RequestMapping("/changeCategory")
    @ResponseBody
    public String changeArticleCategory(@RequestBody Category articles){
        return articleService.changeArticleCategory(articles);
    }

    @RequestMapping("/tag")
    public String tagArticle(@RequestParam("tagId") Integer tagId, @RequestParam(value="pageNum",required=false,defaultValue = "1")Integer pageNum, @RequestParam(value="pageSize",required=false,defaultValue = "10")Integer pageSize,Model model){
        TagArticleQuery queryBean = new TagArticleQuery();
        queryBean.setPageSize(pageSize);
        queryBean.setPageNum(pageNum);
        queryBean.setTagid(tagId);
        TagArticleQuery tagArticleQuery = articleService.getTagArticles(queryBean);
        List<Category> categorys = articleService.getCategory();
        List<ArticleTag> allTags = articleService.selectAllTag();
        model.addAttribute("allTags",allTags);
        model.addAttribute("categorys",categorys);
        model.addAttribute("pageQueryBean",tagArticleQuery);
        return "/article/tagArticle";
    }

    //@RequiresPermissions("admin:home")
    @RequestMapping("/uploadBlogFile")
    public void uploadArticleImage(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "editormd-image-file", required = false)MultipartFile attach){
        try {
            request.setCharacterEncoding( "UTF-8" );
            response.setHeader( "Content-Type" , "text/html" );
            //String rootPath = "/Users/zhaoluyang/JavaProject/LyonBlog/src/main/webapp/static/uploadBlogFile/image/";
            String rootPath = request.getSession().getServletContext().getRealPath("/static/uploadBlogFile/image/");
            System.out.println("\nrootPath:"+rootPath);

            /**
             * 文件路径不存在则需要创建文件路径
             */
            File filePath=new File(rootPath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            //最终文件名
            File realFile=new File(rootPath+File.separator+attach.getOriginalFilename());
            FileUtils.copyInputStreamToFile(attach.getInputStream(), realFile);
            //下面response返回的json格式是editor.md所限制的，规范输出就OK
            response.getWriter().write( "{\"success\": 1, \"message\":\"上传成功\",\"url\":\"/static/uploadBlogFile/image/" + attach.getOriginalFilename() + "\"}" );
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write( "{\"success\":0}" +e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


}
