package p2.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import p2.demo.dto.MemberDTO;
import p2.demo.entity.MemberEntity;
import p2.demo.entity.ProductEntity;
import p2.demo.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //회원가입 저장DTO
    public MemberEntity save(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberBir(memberDTO.getMemberBir());
        memberEntity.setMemberPhone(memberDTO.getMemberPhone());
        memberEntity.setMemberTime(LocalDateTime.now());
        return memberRepository.save(memberEntity);
    }

    //회원가입 저장Entity
    public MemberEntity save(MemberEntity memberEntity){
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

    //id로가져오기
    public MemberEntity findById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + id));
    }
    
    // 비밀번호 수정
    public void updatePassword(Long memberId, String newPassword) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        member.setMemberPassword(newPassword);
        memberRepository.save(member);
    }

    // 이메일 중복 여부 확인
    public boolean isEmailExists(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    //전체정보
    public List<MemberEntity> findAll(){
        return memberRepository.findAll();
    }


    // 일별 가입자 수 통계
    public Map<String, Long> getDailyRegistrations() {
        List<Object[]> results = memberRepository.countByRegistrationDate();
        Map<String, Long> dailyRegistrations = new HashMap<>();

        for (Object[] result : results) {
            String date = (String) result[0];
            Long count = ((Number) result[1]).longValue(); // 형 변환 처리
            dailyRegistrations.put(date, count);
        }

        return dailyRegistrations;
    }

    // 월별 가입자 수 통계
    public Map<String, Long> getMonthlyRegistrations() {
        List<Object[]> results = memberRepository.countByRegistrationMonth();
        Map<String, Long> monthlyRegistrations = new HashMap<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            Long count = ((Number) result[1]).longValue(); // 형 변환 처리
            monthlyRegistrations.put(month, count);
        }

        return monthlyRegistrations;
    }

}