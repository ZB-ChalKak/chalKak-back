package com.btb.chalKak.global.oauth2.controller;

import com.btb.chalKak.global.oauth2.dto.SessionMember;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

  @GetMapping("/login/oauth2/{code}/{registrationId}")
  public void googleLogin(@PathVariable String code, @PathVariable String registrationId) {
        log.info("google code " + code);
        log.info("google registrationId " + registrationId);
  }
}
