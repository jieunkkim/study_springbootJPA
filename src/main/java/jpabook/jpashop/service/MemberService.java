package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// @AllArgsConstructor 를 해주면 생성자 빼도됨 (Lombok 에서 현재 Class 가 가지고 있는 필드로 알아서 생성자 만들어줌)
// @RequiredArgsConstructor 를 해주면 class 내 가지고 있는 final 필드만으로 생성자를 만들어줌 (위보다 나음)
@Transactional(readOnly = true)
// JPA 는 로직들이 한? transaction 안에서 돌아야 한다. (transaction 단위로 돌아야 한다는 뜻인것 같다)
// 읽기에는 가급적 readOnly = True 넣어주자. true 로 설정하면 JPA 가 성능 좀 최적화함.
// 아니면 전체를 readOnly로 설정하고, 읽기 외에 것들은 @Transactional 을 메소드 위에 붙여주자 (readOnly 기본값이 false 이므로)
public class MemberService {


    //@Autowired  // 필드 Injection (단점 : 바꿀 방법이 없음. 접근불가. 너무 폐쇄적)
    private final MemberRepository memberRepository;

    // 생성자 Injection (Service 객체 생성할 때, 파라미터로 Repository 를 넘겨줘야 하는게 명시적이어서 좋고
    // 테스트 시 mock 객체..? 등을 넘겨주기에도 편리
    // 만약 생성자가 아래 건 하나라면 @Autowired 는 빼줄수도 있다. (Spring 에서 생성자 하나면 자동으로 Autowired 함)
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 로직 추가. 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 같은 이름 있는지 찾아보기
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // 중복체크를 하고 있긴 하지만, 여러 대의 WAS 에서 동시 수행이 될 경우 (또는 MultiThread 환경일 경우) 문제가 될 수 있다.
        // 따라서, 체크 하긴 하지만 DB 에서도 member id에 unique 제약조건을 걸어주는 것이 좋다.

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
