package com.btb.chalKak.domain.member.dto.request;

import com.btb.chalKak.domain.member.type.Gender;
import java.util.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpMemberRequest {

    @NotBlank(message = "이메일이 누락되었습니다.")
    @Email(message = "이메일 주소 형식이 잘못되었습니다.")
    private String email;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?!.*\\s).+$"
            , message = "비밀번호는 영어와 숫자를 혼용해야 하며 공백은 사용할 수 없습니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자 이상 최대 16글자 이하로 작성해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임이 누락되었습니다.")
    private String nickname;

    @NotBlank(message = "성별이 누락되었습니다.")
    private Gender gender;

    @NotNull(message = "키가 누락되었습니다.")
    private Double height;
    @NotNull(message = "체중이 누락되었습니다.")
    private Double weight;

    @NotNull(message = "키 프라이버시 설정이 누락되었습니다.")
    private boolean privacyHeight;
    @NotNull(message = "체중 프라이버시 설정이 누락되었습니다.")
    private boolean privacyWeight;

    @NotNull(message = "키워드가 누락되었습니다.")
    private List<Long> styleTags;
}
