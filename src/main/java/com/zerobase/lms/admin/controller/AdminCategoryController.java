package com.zerobase.lms.admin.controller;

import com.zerobase.lms.admin.dto.CategoryDto;
import com.zerobase.lms.admin.dto.MemberDto;
import com.zerobase.lms.admin.model.CategoryInput;
import com.zerobase.lms.admin.model.MemberInput;
import com.zerobase.lms.admin.model.MemberParam;
import com.zerobase.lms.admin.service.CategoryService;
import com.zerobase.lms.member.service.MemberService;
import com.zerobase.lms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/category/list.do")
    public String list(Model model, MemberParam parameter) {

        List<CategoryDto> list = categoryService.list();
        model.addAttribute("list", list);

        return "admin/category/list";
    }

    @PostMapping("/admin/category/add.do")
    public String add(Model model, CategoryInput parameter) {

        boolean result = categoryService.add(parameter.getCategoryName());

        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/admin/category/delete.do")
    public String del(Model model, CategoryInput parameter) {

        boolean result = categoryService.del(parameter.getId());

        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/admin/category/update.do")
    public String update(Model model, CategoryInput parameter) {

        boolean result = categoryService.update(parameter);

        return "redirect:/admin/category/list.do";
    }


}
