package com.princeoo.blog.web;

import com.princeoo.blog.pojo.Type;
import com.princeoo.blog.service.BlogService;
import com.princeoo.blog.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model){
        List<Type> types = typeService.listTypeTop(10000);
        //没选择分类的时候传-1进来，代表从分类导航进入的
        if (id == -1){
            //id为第一个标签的id
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        //传入pageable和typeId
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);


        return "types";
    }
}
