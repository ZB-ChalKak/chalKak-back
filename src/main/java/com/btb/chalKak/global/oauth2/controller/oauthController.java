package com.btb.chalKak.global.oauth2.controller;

import com.btb.chalKak.global.oauth2.dto.SessionMember;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

//    log.info(member.toString());

    return "index";
  }
}
