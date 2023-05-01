package com.appost.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appost.model.DiscToApply;
import com.appost.model.Friends;
import com.appost.model.Roles;
import com.appost.model.User;
import com.appost.repository.DiscToApplyRepository;
import com.appost.repository.FriendsRepository;
import com.appost.repository.UserRepository;

@Service
public class UserManager {

  @Autowired
  UserRepository userRepository;

  @Autowired
  FriendsRepository friendsRepository;

  @Autowired
  DiscToApplyRepository discToApplyRepository;

  public void addNewUser(User user) {
    if (newIDUserAvailable(user.getId()) && usernameAvailable(user.getUsername()))
      userRepository.save(user);
  }

  public Optional<User> searchUserByID(UUID id) {
    return userRepository.findById(id);
  }

  public User searchUserByUsername(String username) {
    return userRepository.searchUserByUsername(username);
  }

  public User searchUserByEmail(String email) {
    return userRepository.searchUserByEmail(email);
  }

  public boolean newIDUserAvailable(UUID newID) {
    List<UUID> idList = /* (List<UUID>) */ userRepository.getIDList();

    if (idList.contains(newID))
      return false;
    else
      return true;
  }

  public boolean usernameAvailable(String username) {
    List<String> idList = (List<String>) userRepository.getUsernameList();

    if (idList.contains(username))
      return false;
    else
      return true;
  }

  public void updateUser(User user) {
    userRepository.save(user);
  }

  public boolean addNewUserFriendship(String usernameFriend1, String usernameFriend2) {
    ArrayList<Friends> friends = (ArrayList<Friends>) friendsRepository.gerFriendList(usernameFriend1);

    boolean friendExist = false;
    for (Friends f : friends) {
      if ((f.getUsernameFriend1().compareTo(usernameFriend1) == 0
          && f.getUsernameFriend2().compareTo(usernameFriend2) == 0)
          || (f.getUsernameFriend1().compareTo(usernameFriend2) == 0
              && f.getUsernameFriend2().compareTo(usernameFriend1) == 0)) {
        friendExist = true;
        break;
      }
    }
    if (!friendExist) {
      Friends newFriends = new Friends(usernameFriend1, usernameFriend2);
      newFriends.setId(UUID.randomUUID());
      friendsRepository.save(newFriends);
    }
    return friendExist;
  }

  public List<Friends> getFriendsList(String idUser) {
    return friendsRepository.gerFriendList(idUser);
  }

  public void addDiscToApply(String username, int percDiscToSub, String businessUser) {
    DiscToApply discToApply = new DiscToApply(username, percDiscToSub, businessUser);

    discToApply.setId(UUID.randomUUID());

    discToApplyRepository.save(discToApply);
  }

  public List<DiscToApply> getDiscToApplyList(String username)
  {
    return discToApplyRepository.getDiscToApply(username);
  }

  public DiscToApply getDiscToApply(UUID id)
  {
    return discToApplyRepository.findById(id).get();
  }

  public void deleteDiscToApply(DiscToApply discToApply)
  {
    discToApplyRepository.delete(discToApply);
  }

  public List<User> getBusinessList()
  {
    return userRepository.getBusinessList(Roles.BUSINESS.ordinal());
  }

  public List<User> gettAllUsers()
  {
    List<User> users = userRepository.findAll();
    return users;
  }

  public void deleteUser(User user)
  {
    userRepository.delete(user);
  }
}
