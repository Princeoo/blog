package com.princeoo.blog.web;

import com.princeoo.blog.pojo.Tag;
import com.princeoo.blog.service.BlogService;
import com.princeoo.blog.service.TagService;
import com.princeoo.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Tag> tags = tagService.listTagTop(10000);
        //没选择分类的时候传-1进来，代表从分类导航进入的
        if (id == -1){
            //id为第一个标签的id
            id = tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        //传入pageable和tagId
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);


        return "tags";
    }
}
