package com.zerobase.lms.admin.controller;

import com.zerobase.lms.admin.dto.MemberDto;
import com.zerobase.lms.admin.model.MemberParam;
import com.zerobase.lms.admin.model.MemberInput;
import com.zerobase.lms.course.controller.BaseController;
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
public class AdminMemberController extends BaseController {

    private final MemberService memberService;

    // 유저 목록
    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam parameter) {

        parameter.init();

        List<MemberDto> members = memberService.list(parameter);

        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }
    // 유저 상세정보
    @GetMapping("/admin/member/detail.do")
    public String detail(Model model, MemberParam parameter) {

        parameter.init();

        MemberDto member = memberService.detail(parameter.getUserId());
        model.addAttribute("member", member);

        return "admin/member/detail";
    }

    // admin 유저의 상태정보 업데이트
    @PostMapping("/admin/member/status.do")
    public String status(Model model, MemberInput parameter) {
        boolean result = memberService.updateStatus(parameter.getUserId(), parameter.getUserStatus());

        return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
    }

    // admin 비밀번호 초기화
    @PostMapping("/admin/member/password.do")
    public String password(Model model, MemberInput parameter) {
        boolean result = memberService.updatePassword(parameter.getUserId(), parameter.getPassword());

        return "redirect:/admin/member/detail.do?userId=" + parameter.getUserId();
    }
}
