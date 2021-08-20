package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository // @SpringBootApplication 가 돌 때 Component Scan 을 수행. @Repository 안에 @Component Scan 내장되어 있음.
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);   // find(타입, PK)
    }

    public List<Member> findAll(){
        // JPQL 임. SQL과 다른점은 from 절에 Entity 객체를 사용
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        // JPQL 이고 :name 은 파라미 바인딩 하는거임
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
