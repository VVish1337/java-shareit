package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private long count=1;

    @Override
    public User save(User user) {
        for(User u:users.values()){
            if(u.getEmail().equals(user.getEmail())){
                throw new IllegalStateException("this email alredy exists");
            }
        }
        user.setId(counter());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        if(users.containsKey(id)){
            return users.get(id);
        }else {
            throw new IllegalStateException("User with this id:"+id+" doesn't exist");
        }
    }

    @Override
    public List<User> getUsersList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        for(User u:users.values()){
            if(u.getEmail().equals(user.getEmail())){
                throw new IllegalStateException("this email alredy exists");
            }
        }
        System.out.println(user);
        if(user.getName()==null){
            user.setName(users.get(user.getId()).getName());
        }else if(user.getEmail()==null){
            user.setEmail(users.get(user.getId()).getEmail());
        }
        users.put(user.getId(),user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        getUserById(id);
        users.remove(id);
    }

    private long counter(){
        return count++;
    }
}