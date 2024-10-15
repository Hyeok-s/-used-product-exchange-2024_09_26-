package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.AskDTO;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.AskEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.AnswerRepository;
import p2.demo.repository.AskRepository;
import p2.demo.repository.MemberRepository;
import p2.demo.entity.AnswerEntity;

import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AskService {

    private final AskRepository askRepository;
    private final MemberRepository memberRepository;
    private final AnswerRepository answerRepository;

    //질문저장
    public void saveAsk(AskDTO askDTO, MemberDTO loggedInUser) {
        AskEntity askEntity = new AskEntity();
        askEntity.setATitle(askDTO.getATitle());
        askEntity.setAContent(askDTO.getAContent());
        askEntity.setATime(LocalDateTime.now());

        // 세션에서 가져온 사용자 정보로 member_id 설정
        MemberEntity memberEntity = memberRepository.findById(loggedInUser.getId()).orElseThrow(() ->
                new IllegalStateException("사용자를 찾을 수 없습니다."));
        askEntity.setMember(memberEntity);  // 외래키 설정

        askRepository.save(askEntity);
    }

    // 모든 문의를 가져오는 메서드
    public List<AskEntity> getAllAsks() {
        return askRepository.findAll();
    }

    // 특정 문의를 ID로 조회하는 메서드
    public AskEntity getAskById(Long id) {
        return askRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ask ID: " + id));
    }

    //답장저장
    public void submitAnswer(Long id, String aReturn) {
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAReturn(aReturn);
        answerEntity.setRTime(LocalDateTime.now());  // 답변 시간 설정

        AskEntity askEntity = askRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        answerEntity.setAsk(askEntity);

        answerRepository.save(answerEntity);
    }

    //질문 상태변경
    public void setAskState(Long id){
        AskEntity ask = askRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));;
        ask.setAState("P");  // 상태 변경
        askRepository.save(ask);
    }
}
