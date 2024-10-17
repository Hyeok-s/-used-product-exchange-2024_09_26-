package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDTO {
    private Long id;
    private String aReturn;
    private LocalDateTime rTime;
    private Long askId;
}
