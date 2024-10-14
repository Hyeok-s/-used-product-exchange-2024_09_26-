package p2.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import p2.demo.dto.AskDTO;
import p2.demo.service.AskService;

@Controller
@RequiredArgsConstructor
public class AskController {

    private final AskService askService;

    @GetMapping("/ask")
    public String showInquiryForm(Model model) {
        model.addAttribute("askDTO", new AskDTO());
        return "ask";
    }

    @PostMapping("/ask")
    public String submitInquiry(@ModelAttribute AskDTO askDTO,
                                HttpSession session) {
        askService.saveInquiry(askDTO);
        return "redirect:/inquiry";
    }
}
