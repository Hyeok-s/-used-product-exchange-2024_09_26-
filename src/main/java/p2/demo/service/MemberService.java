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
    public void save(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberAge(memberDTO.getMemberAge());
        memberEntity.setMemberAddress(memberDTO.getMemberAddress());
        memberRepository.save(memberEntity);
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
    //정보 수정
    public void updateMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = memberRepository.findById(memberDTO.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberAge(memberDTO.getMemberAge());
        memberEntity.setMemberPhone(memberDTO.getMemberPhone());
        memberEntity.setMemberAddress(memberDTO.getMemberAddress());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberRepository.save(memberEntity);  // 변경 사항을 DB에 저장
    }

}