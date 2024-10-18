package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //회원가입 저장
    public MemberEntity save(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberBir(memberDTO.getMemberBir());
        memberEntity.setMemberPhone(memberDTO.getMemberPhone());
        return memberRepository.save(memberEntity);
    }

    //로그인 정보 확인
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

    //id가져오기
    public MemberEntity findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + id));
    }
    
    // 비밀번호 수정
    public void updatePassword(Long memberId, String newPassword) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        member.setMemberPassword(newPassword); // 암호화 로직 추가 가능
        memberRepository.save(member);
    }

}