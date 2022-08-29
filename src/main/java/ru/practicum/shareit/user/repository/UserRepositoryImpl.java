package ru.practicum.shareit.user.repository;

public class UserRepositoryImpl  {
//
//    private final Map<Long, User> users = new HashMap<>();
//    private long count = 1;
//
//    @Override
//    public User save(User user) {
//        checkEmailExists(user);
//        user.setId(counter());
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public User getUserById(long id) {
//        if (users.containsKey(id)) {
//            return users.get(id);
//        } else {
//            throw new NotFoundException("User with this id:" + id + " doesn't exist");
//        }
//    }
//
//    @Override
//    public List<User> getUsersList() {
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public User updateUser(User user) {
//        checkEmailExists(user);
//        if (user.getName() == null) {
//            user.setName(users.get(user.getId()).getName());
//        } else if (user.getEmail() == null) {
//            user.setEmail(users.get(user.getId()).getEmail());
//        }
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public void deleteUser(long id) {
//        getUserById(id);
//        users.remove(id);
//    }
//
//    private void checkEmailExists(User user) {
//        for (User u : users.values()) {
//            if (u.getEmail().equals(user.getEmail())) {
//                throw new EmailExistsException("Email already exists");
//            }
//        }
//    }
//
//    private long counter() {
//        return count++;
//    }
}