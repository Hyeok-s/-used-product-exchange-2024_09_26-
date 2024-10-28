package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import p2.demo.dto.AskDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.service.AskService;

@Controller
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;

    @GetMapping("/ask")
    public String showInquiryForm(Model model, HttpSession session) {
        MemberDTO loggedInUser = (MemberDTO) session.getAttribute("loggedInUser");
        if(loggedInUser == null){
            return "redirect:/error/unauthorized";
        }
        model.addAttribute("askDTO", new AskDTO());
        return "ask";
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
