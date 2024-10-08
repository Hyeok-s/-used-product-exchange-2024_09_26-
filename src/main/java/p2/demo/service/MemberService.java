package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberAge(memberDTO.getMemberAge());
        memberEntity.setMemberAddress(memberDTO.getMemberAddress());
        memberRepository.save(memberEntity);
    }

    public MemberDTO login(String email, String password) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(email);

        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            // 비밀번호 검증
            if (memberEntity.getMemberPassword().equals(password)) {
                return MemberDTO.toMemberDTO(memberEntity);
            }
        }
        return null;
    }

}