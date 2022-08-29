package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;


public interface UserRepository extends JpaRepository<User,Long> {
////    User save(User user);
////
////    User getUserById(long id);
////
////    List<User> getUsersList();
////
////    User updateUser(User user);
////
////    void deleteUser(long id);
//    User findUserById(long id);
//
//    List<User> find();
//
//    User updateUser(User user);
//
//    @Override
//    <S extends User> S saveAndFlush(S entity);
}