package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.AskDTO;
import p2.demo.entity.AskEntity;
import p2.demo.entity.MemberEntity;
import p2.demo.repository.AskRepository;
import p2.demo.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AskService {

    private final AskRepository askRepository;
    private final MemberRepository memberRepository;

    public void saveInquiry(AskDTO askDTO) {
        //member에 있는 회원인지 확인
        MemberEntity member = memberRepository.findById(askDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        AskEntity askEntity = new AskEntity();
        askEntity.setATitle(askDTO.getATitle());
        askEntity.setAContent(askDTO.getAContent());
        askEntity.setATime(LocalDateTime.now());
        askEntity.setMember(member);

        askRepository.save(askEntity);
    }
}
