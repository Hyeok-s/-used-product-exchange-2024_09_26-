package p2.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
public class AskDTO {
    private Long id;
    private String aTitle;
    private String aContent;
    private String aReturn;
    private LocalDateTime aTime;
    private LocalDateTime rTime;
    private Long memberId;
}
