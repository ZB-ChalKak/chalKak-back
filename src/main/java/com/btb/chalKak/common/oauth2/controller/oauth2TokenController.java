package com.btb.chalKak.common.oauth2.controller;

import static com.btb.chalKak.common.exception.type.SuccessCode.SUCCESS_SIGN_IN;

import com.btb.chalKak.common.oauth2.service.TemporaryTokenStoreService;
import com.btb.chalKak.common.response.service.ResponseService;
import com.btb.chalKak.common.security.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class oauth2TokenController {

  private final TemporaryTokenStoreService temporaryTokenStoreService;
  private final ResponseService responseService;

  @GetMapping("/fetch-token/{email}")
  public ResponseEntity<?> fetchToken(@PathVariable("email") String email) {
    TokenDto data = temporaryTokenStoreService.retrieve(email);
    return ResponseEntity.ok(responseService.success(data, SUCCESS_SIGN_IN));
  }
}
