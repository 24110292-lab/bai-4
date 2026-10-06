package vn.iotstar.service;

import java.util.Locale;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.model.User;

public class UserService {
    private final EntityManagerFactory entityManagerFactory;

    public UserService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public User register(String username, String password, String fullname, String phone) {
        String normalizedUsername = normalizeUsername(username);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Long count = entityManager.createQuery(
                    "select count(u) from User u where u.username = :username", Long.class)
                    .setParameter("username", normalizedUsername)
                    .getSingleResult();
            if (count > 0) {
                throw new DuplicateUsernameException();
            }

            User user = new User();
            user.setUsername(normalizedUsername);
            user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setFullname(fullname.trim());
            user.setPhone(phone.trim());
            entityManager.persist(user);
            transaction.commit();
            return user;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    public User authenticate(String username, String password) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            User user = findByUsername(entityManager, normalizeUsername(username));
            if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
                return null;
            }
            return user;
        } finally {
            entityManager.close();
        }
    }

    public User findById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(User.class, id);
        } finally {
            entityManager.close();
        }
    }

    public User updateProfile(Long id, String fullname, String phone, String imageFile) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user == null) {
                throw new IllegalStateException("The authenticated user no longer exists.");
            }
            user.setFullname(fullname.trim());
            user.setPhone(phone.trim());
            if (imageFile != null) {
                user.setImageFile(imageFile);
            }
            transaction.commit();
            return user;
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    private User findByUsername(EntityManager entityManager, String username) {
        return entityManager.createQuery(
                "select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }

    public static final class DuplicateUsernameException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
