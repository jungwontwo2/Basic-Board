package Tanguri.BasicBoard.repository;

import Tanguri.BasicBoard.domain.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepository {

    private final EntityManager em;

    @Transactional
    public void save(User user){
        if(user.getId()==null){
            em.persist(user);
        }
        else {
            em.merge(user);
        }
    }

    public User findOne(Long id){
        return em.find(User.class, id);
    }

    public User findByNickname(String nickname){
        try{
            return em.createQuery("select m from User m where m.nickname = :nickname",User.class)
                    .setParameter("nickname",nickname)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public Optional<User> findByLoginId(String loginId){
        return findAll().stream().filter(m->m.getLoginId().equals(loginId)).findAny();
    }

    public List<User> findAll(){
        return em.createQuery("select m from User m",User.class).getResultList();
    }
}
