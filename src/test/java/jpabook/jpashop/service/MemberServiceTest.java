package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 이게 있어야 Rollback 이 됨
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원_가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");
        
        // when
        Long saveId = memberService.join(member);

        // then
        Assert.assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);
        memberService.join(member2);
        /* 아래처럼 처리해서 fail() 까지 가지 않게 만들거나, @Test 에 expected 를 명시해주거나
        try {
            memberService.join(member2);
        } catch (IllegalStateException e) {
            return;
        }
        */

        // then
        Assert.fail("예외가 발생해야 하는데 하지 않음.");   // fail : 코드가 여기 오면 안된다는 뜻.
    }

        
}