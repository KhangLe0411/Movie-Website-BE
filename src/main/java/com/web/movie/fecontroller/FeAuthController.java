package com.web.movie.fecontroller;

import com.web.movie.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Slf4j
public class FeAuthController {

    @GetMapping("/dang-ky")
    public String getRegisterPage() {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/auth/register";
    }
    @GetMapping("/dang-nhap")
    public String getLoginPage() {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }
        return "web/auth/login";
    }
}
