package com.mysite.sbb.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)    // 글자수 200 바이트 제한 (200 글자가 아님!!)
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
