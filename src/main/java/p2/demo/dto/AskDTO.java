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
    private LocalDateTime aTime;
    private Long memberId;
    private boolean answers;
}
