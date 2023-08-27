package com.btb.chalKak.common.oauth2.controller;

import com.btb.chalKak.common.oauth2.dto.SessionMember;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class oauthController {

  private final HttpSession httpSession;

  @GetMapping("/")
  public String index(Model model) {

    SessionMember member = (SessionMember) httpSession.getAttribute("member");

    if(member != null){
      model.addAttribute("memberName", member.getName());
    }

    return "index";
  }

}
