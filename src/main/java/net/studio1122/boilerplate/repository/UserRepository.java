package net.studio1122.boilerplate.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.studio1122.boilerplate.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public User save(User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }

    public Optional<User> findByUsername(String username) {
        User user = em.createQuery(
                "select u from User u where u.username = :username and u.isDeleted = false",
                        User.class
                )
                .setParameter("username", username)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) {
        User user = em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    public Boolean existsByUsername(String username) {
        Integer cnt = em.createQuery("select count(u) from User u where u.username = :username", Integer.class)
                .setParameter("username", username)
                .getSingleResult();
        return (cnt != 0);
    }

    public Boolean existsByNickname(String nickname) {
        Integer cnt = em.createQuery("select count(u) from User u where u.nickname = :nickname", Integer.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
        return (cnt != 0);
    }
}
