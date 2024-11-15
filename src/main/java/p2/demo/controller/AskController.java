package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import p2.demo.dto.AnswerDTO;
import p2.demo.dto.AskDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.AnswerEntity;
import p2.demo.entity.AskEntity;
import p2.demo.service.AskService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;

    @GetMapping("/ask")
    public String showInquiryForm(@RequestParam(defaultValue = "0") int page, Model model, HttpSession session) {
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/demo/errorMessage/1";
        }

        Pageable pageable = PageRequest.of(page, 4);  // 한 페이지에 4개씩
        Page<AskEntity> askPage = askService.findByMemberId(loggedInUser.getId(), pageable);

        model.addAttribute("askPage", askPage);
        model.addAttribute("askDTO", new AskDTO());
        return "ask";
    }

    @GetMapping("/admin/answer/{askId}")
    @ResponseBody
    public AnswerEntity getAnswer(@PathVariable Long askId) {
        AnswerEntity answer = askService.getAnswerByAskId(askId);
        return answer;
    }

    @PostMapping("/ask/submit")
    public String submitAsk(@ModelAttribute("askDTO") AskDTO askDTO, HttpSession session, Model model) {
        try {
            MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");
            if (loggedInUser != null) {
                askService.saveAsk(askDTO, loggedInUser);
                return "redirect:/";
            } else {
                model.addAttribute("errorMessage", "로그인이 필요합니다.");
                return "redirect:/demo/signup";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/ask";
        }
    }
}
